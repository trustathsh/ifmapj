/*
 * #%L
 * =====================================================
 *   _____                _     ____  _   _       _   _
 *  |_   _|_ __ _   _ ___| |_  / __ \| | | | ___ | | | |
 *    | | | '__| | | / __| __|/ / _` | |_| |/ __|| |_| |
 *    | | | |  | |_| \__ \ |_| | (_| |  _  |\__ \|  _  |
 *    |_| |_|   \__,_|___/\__|\ \__,_|_| |_||___/|_| |_|
 *                             \____/
 * 
 * =====================================================
 * 
 * Hochschule Hannover
 * (University of Applied Sciences and Arts, Hannover)
 * Faculty IV, Dept. of Computer Science
 * Ricklinger Stadtweg 118, 30459 Hannover, Germany
 * 
 * Email: trust@f4-i.fh-hannover.de
 * Website: http://trust.f4.hs-hannover.de/
 * 
 * This file is part of ifmapj, version 2.1.0, implemented by the Trust@HsH
 * research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2010 - 2014 Trust@HsH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package de.hshannover.f4.trust.ifmapj.channel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import util.Base64;
import de.hshannover.f4.trust.ifmapj.exception.CommunicationException;
import de.hshannover.f4.trust.ifmapj.exception.InitializationException;

/**
 * Template-method pattern for {@link CommunicationHandler} implementations.
 *
 * The method {@link #doRequest(InputStream)} is used as template, all methods
 * defined abstract in this class need to be implemented by a subclass.
 *
 * @author aw
 */
abstract class AbstractCommunicationHandler implements CommunicationHandler {

	private final SSLSocketFactory mSocketFactory;
	private SSLSocket mSocket;
	private final String mAuthHeaderValue;
	private final boolean mBasicAuth;
	private final String mPath;
	private final URL mUrl;
	private final int mPort;
	private boolean mGzip;
	private HostnameVerifier mHostnameVerifier;
	private final int mInitialConnectionTimeout;

	AbstractCommunicationHandler(String url, String user, String pass,
			SSLSocketFactory sslSocketFactory,
			HostnameVerifier verifier, int initialConnectionTimeout) throws InitializationException {

		if (url == null) {
			throw new NullPointerException("url is null");
		}

		if (sslSocketFactory == null) {
			throw new NullPointerException("sslSocketFactory is null");
		}

		if (verifier == null) {
			throw new NullPointerException("verifier is null");
		}

		try {
			mUrl = new URL(url);
		} catch (MalformedURLException e) {
			throw new InitializationException("Malformed URL ["
					+ e.getMessage() + "]");
		}
		// fix up the path in case non is given
		mPath = mUrl.getPath().length() == 0 ? "/" : mUrl.getPath();

		// set the port to an appropriate value
		mPort = mUrl.getPort() == -1 ? mUrl.getDefaultPort() : mUrl.getPort();

		// check whether we use basicauth
		mBasicAuth = !(user == null || pass == null);

		// if we do, prepare the Authorization header value
		if (mBasicAuth) {
			mAuthHeaderValue = "Basic " + Base64.encodeToString((user + ":" + pass).getBytes(), false);
		} else {
			mAuthHeaderValue = null;
		}

		mSocketFactory = sslSocketFactory;

		mHostnameVerifier = verifier;
		mInitialConnectionTimeout = initialConnectionTimeout;
	}

	/*
	 * Template method.
	 *
	 * (non-Javadoc)
	 * @see de.fhhannover.inform.trust.ifmapj.channel.CommunicationHandler#doRequest(java.io.InputStream)
	 */
	@Override
	public final InputStream doRequest(InputStream httpBody) throws CommunicationException {
		InputStream reply = null;

		try {

			getSocket();

			prepareCommunication();

			createPostRequest(getPath());

			writeHostLine();

			writeContentTypeHeaders();

			if (usesGzip()) {
				httpBody = compressInputStream(httpBody);
				writeGzipHeaders();
			}

			if (usesBasicAuth()) {
				writeAuthHeader();
			}

			writeContentLengthHeader(httpBody.available());

			finishHeaders();

			reply = doActualRequest(httpBody);

			if (replyIsGzipped()) {
				reply = new GZIPInputStream(reply);
			}

			return reply;

		} catch (IOException e) {
			throw new CommunicationException(e.getMessage());
		}
	}

	private void writeHostLine() throws IOException {
		addHeader("Host", getUrl().getHost() + ":" + getPort());
	}

	private void writeContentTypeHeaders() throws IOException {
		addHeader("Accept-Type", "application/soap+xml");
		addHeader("Content-Type", "application/soap+xml");
	}

	private void writeGzipHeaders() throws IOException {
		addHeader("Accept-Encoding", "gzip");
		addHeader("Content-Encoding", "gzip");

	}

	private void writeAuthHeader() throws IOException {
		addHeader("Authorization", getAuthHeaderValue());
	}

	private void writeContentLengthHeader(int length) throws IOException {
		addHeader("Content-Length", "" + length);
	}

	@Override
	public void setGzip(boolean gzip) {
		mGzip = gzip;
	}

	@Override
	public boolean usesGzip() {
		return mGzip;
	}

	protected SSLSocket getSocket() throws IOException {
		if (mSocket == null) {
			try {
				closeTcpConnection();
			} catch (CommunicationException e) {
				throw new IOException(e.getMessage());
			}
			mSocket = getNewSocket();
		}
		return mSocket;
	}

	/**
	 * Before we allocate a new socket, try to delete the old one.
	 * closeTcpConnection() needs to be a nop for non-opened connections.
	 *
	 * @return
	 * @throws IOException
	 */
	private SSLSocket getNewSocket() throws IOException {
		String host = getUrl().getHost();
		int port = getPort();
		Socket socketConnection = new Socket();
		InetSocketAddress mapServerAddress = new InetSocketAddress(host, port);
		socketConnection.connect(mapServerAddress, mInitialConnectionTimeout);
		SSLSocket ret = (SSLSocket) mSocketFactory.createSocket(socketConnection, host, port, true);

		ret.setTcpNoDelay(true);
		ret.setWantClientAuth(true);

		// Check if all is good... Will throw IOException if we don't
		// like the other end...
		ret.getSession().getPeerCertificates();

		if (!mHostnameVerifier.verify(mUrl.getHost(), ret.getSession())) {
			throw new IOException("Hostname Verification failed! "
					+ "Did you set ifmapj.communication.verifypeerhost?");
		}

		return ret;
	}

	private String getAuthHeaderValue() {
		return mAuthHeaderValue;
	}

	private boolean usesBasicAuth() {
		return mBasicAuth;
	}

	private String getPath() {
		return mPath;
	}

	protected URL getUrl() {
		return mUrl;
	}

	protected int getPort() {
		return mPort;
	}

	/**
	 * Subclasses should set up sockets and stuff here.
	 *
	 * @throws IOException
	 */
	protected abstract void prepareCommunication() throws IOException;

	/**
	 * Subclasses should create the POST requests or already write something
	 * to the server.
	 *
	 * @param path
	 * @throws IOException
	 */
	protected abstract void createPostRequest(String path) throws IOException;

	/**
	 * Subclasses should add headers or send them to the other server
	 * already.
	 *
	 * @param key
	 * @param value
	 * @throws IOException
	 */
	protected abstract void addHeader(String key, String value) throws IOException;

	/**
	 * Indicates that now headers will follow anymore
	 *
	 * @throws IOException
	 */
	protected abstract void finishHeaders() throws IOException;


	/**
	 * Do the actual httpRequest. Return a {@link InputStream} instance
	 * representing the content of the HTTP body.
	 *
	 * @param in
	 * @return
	 */
	protected abstract InputStream doActualRequest(InputStream in) throws
			IOException, CommunicationException;

	/**
	 * Returns whether the last doActualRequest() invocation lead returned
	 * a gzip compressed stream.
	 *
	 * @return
	 * @throws IOException
	 */
	protected abstract boolean replyIsGzipped() throws IOException;


	/**
	 * Do whatever has to be done for cleaning up a TCP connection.
	 */
	protected abstract void closeTcpConnectionImpl() throws IOException;

	/* (non-Javadoc)
	 * @see de.fhhannover.inform.trust.ifmapj.channel.CommunicationHandler#closeTcpConnection()
	 */
	@Override
	public final void closeTcpConnection() throws CommunicationException {
		IOException tmpException = null;
		try {
			closeTcpConnectionImpl();
		} catch (IOException e) {
			tmpException = e;
		} finally {
			try {

				if (mSocket != null) {
					mSocket.close();
				}

			} catch (IOException e) {
				tmpException = e;
			} finally {
				mSocket = null;
			}
		}

		if (tmpException != null) {
			throw new CommunicationException(tmpException);
		}
	}

	/**
	 * Use gzip to compress the input stream.
	 * We do copying here, but I'm not sure whether there is a way around it anyway,
	 * because we need to know the number of bytes to send out, before we can
	 * start sending them. (no chunked encoding)
	 *
	 * @param is
	 * @return
	 * @throws IOException if something goes wrong while working on
	 *	 the {@link InputStream} instances.
	 */
	private InputStream compressInputStream(InputStream is) throws IOException {
		int next = -1;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		OutputStream os = new GZIPOutputStream(baos);

		while ((next = is.read()) >= 0) {
			os.write(next);
		}

		baos.close();
		os.close();
		is.close();
		return new ByteArrayInputStream(baos.toByteArray());
	}
}
