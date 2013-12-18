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
 * Website: http://trust.f4.hs-hannover.de
 * 
 * This file is part of ifmapj, version 1.0.0, implemented by the Trust@HsH
 * research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2010 - 2013 Trust@HsH
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
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

import de.hshannover.f4.trust.ifmapj.exception.CommunicationException;
import de.hshannover.f4.trust.ifmapj.exception.InitializationException;
import de.hshannover.f4.trust.ifmapj.log.IfmapJLog;

/**
 * Attempt to create a {@link CommunicationHandler} implementation to be
 * independent of any extra library... Meaning making usage only of standard
 * JAVA classes.
 *
 * @author aw
 */
class JavaCommunicationHandler extends AbstractCommunicationHandler {

	private OutputStream mOutputStream;
	private InputStream mInputStream;
	private DataOutput mDataOutputStream;
	private DataInput mDataInputStream;
	private Map<String, String> mReceivedHeaders;

	private byte[] mBuffer;

	private class StatusLine {
		public int statusCode;
		public String reason;
	}

	JavaCommunicationHandler(String url, String user, String pass,
			SSLSocketFactory sslSocketFactory, HostnameVerifier verifier)
			throws InitializationException {
		super(url, user, pass, sslSocketFactory, verifier);

		// some random initial buffer
		mBuffer = new byte[1024];
		mReceivedHeaders = new HashMap<String, String>(10);
	}

	@Override
	public InputStream doActualRequest(InputStream is) throws IOException, CommunicationException {
		StatusLine statusLine = null;
		int retLength = -1;

		sendHttpBody(is);
		statusLine = readStatusLine();
		checkStatusLine(statusLine);
		receiveHeaders();

		if (responseIsChunked())
			retLength = readChunkedBody();
		else if (responseContainsContentLength())
			retLength = readContinuousBody();
		else
			throw new CommunicationException("Could not determine length of body");

		return new ByteArrayInputStream(mBuffer, 0, retLength);
	}

	private int readChunkedBody() throws CommunicationException, IOException {
		String chunkLengthLine = null, tmpLine = null;
		int curOffset = 0;
		int curChunkLength = -1;

		while ((chunkLengthLine = mDataInputStream.readLine()) != null) {

			if (chunkLengthLine.length() == 0)
				throw new CommunicationException("Unexpected empty chunk length");

			curChunkLength = parseChunkLength(chunkLengthLine);

			// Was the last chunk reached?
			if (curChunkLength == 0)
				break;

			reallocateBuffer(mBuffer.length + 2 * curChunkLength);

			readStreamIntoBuffer(mInputStream, curOffset, curChunkLength);
			curOffset += curChunkLength;

			// Read the CR LF sequence at the end of the chunk body
			tmpLine = mDataInputStream.readLine();

			if (tmpLine == null || tmpLine.length() > 0)
				throw new CommunicationException("Unexpected chunk ending: " +
						tmpLine);

		}

		// After the last chunk, there might be some trailers, we ignore them
		// for now, but we need to read them anyway.
		while ((tmpLine = mDataInputStream.readLine()) != null)
			if (tmpLine.length() == 0)
				break;

		return curOffset;
	}


	private int parseChunkLength(String chunkLengthLine) throws CommunicationException {
		String lengthStrElements[] = chunkLengthLine.split(" ");
		if (lengthStrElements.length < 1)
			throw new CommunicationException("No chunk length included: " + chunkLengthLine);

		try {
			return Integer.parseInt(lengthStrElements[0], 16);
		} catch (NumberFormatException e) {
			throw new CommunicationException("Could not parse chunk length");
		}
	}

	private boolean responseContainsContentLength() {
		return findHeaderValue("Content-Length") != null;
	}

	private boolean responseIsChunked() {
		String teHeader = findHeaderValue("Transfer-Encoding");
		// FIXME: We only look out for chunked, but
		// there could be something else as well
		return (teHeader != null && teHeader.contains("chunked"));
	}

	@Override
	protected void prepareCommunication() throws IOException {

		if (mOutputStream != null && mInputStream != null)
			return;

		mOutputStream = getSocket().getOutputStream();
		mInputStream = getSocket().getInputStream();
		mDataOutputStream =  new DataOutputStream(mOutputStream);
		mDataInputStream = new DataInputStream(mInputStream);
	}

	@Override
	protected void createPostRequest(String path) throws IOException {
		mReceivedHeaders.clear();
		writeLine("POST " + path + " HTTP/1.1");
	}

	@Override
	protected void addHeader(String key, String value) throws IOException {
		writeHeaderLine(key, value);
	}

	@Override
	protected void finishHeaders() throws IOException {
		writeHeaderEnding();
	}

	@Override
	protected boolean replyIsGzipped() throws IOException {
		String encodingHdr = findHeaderValue("Content-Encoding");
		return (encodingHdr != null && encodingHdr.contains("gzip"));
	}

	private void sendHttpBody(InputStream is) throws IOException {
		int length = is.available();
		allocateBuffer(length);
		readStreamIntoBuffer(is, length);
		sendBufferContents(length);
	}

	private void writeHeaderLine(String key, String value) throws IOException {
		writeLine(key + ": " + value);
	}

	private void writeHeaderEnding() throws IOException {
		writeLine("");
		mOutputStream.flush();
	}

	private void writeLine(String line) throws IOException {
		mDataOutputStream.write((line + "\r\n").getBytes());
	}

	protected void closeTcpConnectionImpl() throws IOException {
		IOException tmp = null;

		try {
			if (mInputStream != null) mInputStream.close();
		} catch (IOException e) {
			if (tmp == null) tmp = e;
		} finally {
			mInputStream = null;
		}

		try {
			if (mOutputStream != null) mOutputStream.close();
		} catch (IOException e) {
			if (tmp != null) tmp = e;
		} finally {
			mOutputStream = null;
		}

		mDataOutputStream = null;
		mDataInputStream = null;

		if (tmp != null)
			throw tmp;
	}

	private int getContentLength() throws CommunicationException {

		String lengthHdr = findHeaderValue("Content-Length");

		if (lengthHdr == null)
			throw new CommunicationException("No Content-Length header found");

		return parseContentLengthHeader(lengthHdr);
	}

	private int parseContentLengthHeader(String lengthHdr) throws CommunicationException {
		try {
			return Integer.parseInt(lengthHdr);
		} catch (NumberFormatException e) {
			throw new CommunicationException("Content-Length invalid: "
					+ "\"" + lengthHdr + "\"");
		}
	}

	private String findHeaderValue(String hdrField) {
		for (String hdr : mReceivedHeaders.keySet())
			if (hdr.equalsIgnoreCase(hdrField))
				return mReceivedHeaders.get(hdr);

		return null;
	}


	private void receiveHeaders() throws CommunicationException, IOException  {

		String line = null;

		mReceivedHeaders.clear();

		while ((line = mDataInputStream.readLine()) != null && line.length() > 0) {
			String fields[] = line.split(":", 2);
			if (fields.length < 2)
				throw new CommunicationException("Invalid Header Received: "  + line);

			// trim a bit
			fields[0] = fields[0].replaceAll("^\\s+", "");
			fields[0] = fields[0].replaceAll("\\s+$", "");
			fields[1] = fields[1].replaceAll("^\\s+", "");
			fields[1] = fields[1].replaceAll("\\s+$", "");

			mReceivedHeaders.put(fields[0], fields[1]);
		}

		if (line == null)
			throw new CommunicationException("Unexpected EOF reached");
	}

	private StatusLine readStatusLine() throws CommunicationException, IOException {
		StatusLine ret = new StatusLine();
		String line = mDataInputStream.readLine();
		if (line == null)
			throw new CommunicationException("No status line received");

		String fields[] = line.split(" ", 3);
		if (fields.length < 2)
			throw new CommunicationException("Bad status line received");
		String proto = fields[0];

		if (!proto.equals("HTTP/1.1"))
			throw new  CommunicationException("Communication not HTTP/1.1");

		try {
			ret.statusCode = Integer.parseInt(fields[1]);
		} catch (NumberFormatException e) {
			throw new CommunicationException("Bad status code received");
		}

		if (fields.length == 3)
			ret.reason = fields[2];

		return ret;
	}

	private int readContinuousBody() throws IOException, CommunicationException {
		int length = getContentLength();
		allocateBuffer(length);
		readStreamIntoBuffer(mInputStream, length);
		return length;
	}

	private void checkStatusLine(StatusLine status) throws CommunicationException {
		if (status.statusCode != 200) {
			IfmapJLog.warn("HTTP Status Code: "
					+ status.statusCode + " "
					+ status.reason);
			throw new CommunicationException("HTTP Status Code: "
					+ status.statusCode + " "
					+ status.reason);
		}
	}

	private void sendBufferContents(int length) throws IOException {
		mOutputStream.write(mBuffer, 0, length);
		mOutputStream.flush();
	}

	/**
	 * Helper to read length bytes into {@link JavaCommunicationHandler#mBuffer}.
	 *
	 * @param is
	 * @param length
	 * @throws IOException
	 */
	private void readStreamIntoBuffer(InputStream is, int length) throws IOException {
		readStreamIntoBuffer(is, 0, length);
	}

	private void readStreamIntoBuffer(InputStream is, int off, int length)
			throws IOException {
		int read = 0;
		int ret;

		while (read < length) {
			ret = is.read(mBuffer, read + off, length - read);
			if (ret == -1)
				throw new IOException("Stream exception");
			read += ret;
		}
	}

	/**
	 * Check if our current buffer is too small, and if yes, allocate some
	 * more
	 *
	 * @param length
	 */
	private void allocateBuffer(int newLength) {
		allocateBuffer(newLength, false);
	}

	private void reallocateBuffer(int newLength) {
		allocateBuffer(newLength, true);
	}

	private void allocateBuffer(int newLength, boolean copy) {
		byte tmp[];
		if (newLength > mBuffer.length) {
			tmp = mBuffer;
			mBuffer = new byte[newLength];

			if (copy)
				System.arraycopy(tmp, 0, mBuffer, 0, tmp.length);
		}
	}
}
