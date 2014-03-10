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
 * This file is part of ifmapj, version 1.0.1, implemented by the Trust@HsH
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

import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.auth.x500.X500Principal;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import util.DomHelpers;
import de.hshannover.f4.trust.ifmapj.binding.IfmapStrings;
import de.hshannover.f4.trust.ifmapj.exception.CommunicationException;
import de.hshannover.f4.trust.ifmapj.exception.IfmapErrorResult;
import de.hshannover.f4.trust.ifmapj.exception.IfmapException;
import de.hshannover.f4.trust.ifmapj.exception.InitializationException;
import de.hshannover.f4.trust.ifmapj.exception.MarshalException;
import de.hshannover.f4.trust.ifmapj.exception.UnmarshalException;
import de.hshannover.f4.trust.ifmapj.log.IfmapJLog;
import de.hshannover.f4.trust.ifmapj.messages.Request;
import de.hshannover.f4.trust.ifmapj.messages.RequestHandler;
import de.hshannover.f4.trust.ifmapj.messages.Requests;
import de.hshannover.f4.trust.ifmapj.messages.Result;

/**
 * Encapsulate functionality needed by both {@link SSRC} and {@link ARC}
 * implementations.
 *
 * @author aw
 *
 */
abstract class AbstractChannel implements IfmapChannel {

	public static final String VERIFY_PEER_CERT_PROPERTY = "ifmapj.communication.verifypeercert";
	public static final String VERIFY_PEER_HOST_PROPERTY = "ifmapj.communication.verifypeerhost";

	/**
	 * represents the handler used for communication to be used
	 */
	private final CommunicationHandler mCommunicationHandler;

	/**
	 * Represents the url in string form
	 */
	private final String mUrlStr;

	/**
	 * user to be used for basic authentication, null for certificate-based
	 */
	private final String mUser;

	/**
	 * password to be used for basic authentication, null for certificate-based
	 */
	private final String mPassword;

	/**
	 * whether this channel uses basic authentication or certificate-based
	 * authentication
	 */
	private final boolean mBasicAuth;

	/**
	 * {@link DocumentBuilder} to create {@link Document} instances for requests.
	 */
	private final DocumentBuilder mDocumentBuilder;

	/**
	 * Construct a {@link AbstractChannel} object in the most general form.
	 *
	 * @param url
	 * @param user
	 * @param pass
	 * @param kms the keystore managers to be used
	 * @param tms the truststore managers to be used
	 * @throws InitializationException
	 */
	private AbstractChannel(String url, String user, String pass, KeyManager[] kms, TrustManager[] tms)
			throws InitializationException  {

		if (url == null) {
			throw new InitializationException("URL not allowed to be null");
		}

		// check only trustmanager
		if (tms == null) {
			throw new InitializationException("keystore and truststore need "
					+ "to be set");
		}

		// If basic authentication  is used, make sure user and pass are set
		if (user != null && pass == null || user == null && pass != null) {
			throw new InitializationException("One basic auth parameter is null");
		}


		mUrlStr = url;
		mUser = user;
		mPassword = pass;
		mBasicAuth = !(mUser == null && mPassword == null);

		mDocumentBuilder = DomHelpers.newDocumentBuilder();
		SSLSocketFactory sslSocketFactory = initSslSocketFactory(kms, tms);
		HostnameVerifier verifier = initHostnameVerifier();
		mCommunicationHandler = CommunicationHandlerFactory.newHandler(
				url, user, pass, sslSocketFactory, verifier);
	}

	/**
	 * Used for basic-authentication
	 *
	 * @param url
	 * @param user
	 * @param pass
	 * @param tms
	 * @throws InitializationException
	 */
	AbstractChannel(String url, String user, String pass, TrustManager[] tms) throws InitializationException {
		this(url, user, pass, null, tms);
	}

	/**
	 * Used for cert-based authentication
	 * @param url
	 * @param kms
	 * @param tms
	 * @throws InitializationException
	 */
	AbstractChannel(String url, KeyManager[] kms, TrustManager[] tms)
			throws InitializationException {
		this(url, null, null, kms, tms);
	}

	@Override
	public final Result genericRequestWithSessionId(Request req) throws IfmapErrorResult, IfmapException {
		if (getSessionId() == null) {
			throw new IfmapException("no session-id", "session-id not set for channel");
		}
		req.setSessionId(getSessionId());
		return genericRequest(req);
	}

	@Override
	public final Result genericRequest(Request req) throws IfmapErrorResult, IfmapException {
		Document docReq;
		Document docRes;
		Element elBody;
		Element content;
		RequestHandler<? extends Request> reqhandler = Requests.getHandlerFor(req);

		if (reqhandler == null) {
			throw new MarshalException("No handler for " + req.getClass());
		}


		docReq = mDocumentBuilder.newDocument();
		elBody = addSoapEnvelopeBody(docReq);
		content = reqhandler.toElement(req, docReq);
		elBody.appendChild(content);

		docRes = parseDocument(doHttpRequest(DomHelpers.toInputStream(docReq)));

		return reqhandler.fromElement(findResponseElement(docRes));
	}

	/**
	 * Build the soap:Envelope and soap:Body things and return the {@link Element}
	 * for the soap:Body.
	 */
	private Element addSoapEnvelopeBody(Document doc) {
		Element env = doc.createElementNS(IfmapStrings.SOAP_ENV_NS_URI,
				IfmapStrings.SOAP_PREFIXED_ENV_EL_NAME);
		Element body = doc.createElementNS(IfmapStrings.SOAP_ENV_NS_URI,
				IfmapStrings.SOAP_PREFIXED_BODY_EL_NAME);
		doc.appendChild(env);
		env.appendChild(body);

		return body;
	}

	private Document parseDocument(InputStream is) throws UnmarshalException {
		try {
			return mDocumentBuilder.parse(is);
		} catch (Exception e) { // catchall, not nice but we don't do anything special
			IfmapJLog.error("Parsing Exception occurred [" + e.getMessage() + "]");
			throw new UnmarshalException(e.getMessage());
		}
	}

	private Element findResponseElement(Document doc) throws UnmarshalException {
		Element env;
		Element body;
		Element response;
		env = DomHelpers.findElementInChildren(doc, IfmapStrings.SOAP_ENV_EL_NAME,
				IfmapStrings.SOAP_ENV_NS_URI);

		if (env == null) {
			throw new UnmarshalException("Could not find SOAP Envelope");
		}

		body = DomHelpers.findElementInChildren(env, IfmapStrings.SOAP_BODY_EL_NAME,
				IfmapStrings.SOAP_ENV_NS_URI);

		if (body == null) {
			throw new UnmarshalException("Could not find SOAP Body");
		}

		response = DomHelpers.findElementInChildren(body,
				IfmapStrings.RESPONSE_EL_NAME, IfmapStrings.BASE_NS_URI);

		if (response == null) {
			throw new UnmarshalException("Could not find IF-MAP response element");
		}

		return response;
	}

	private InputStream doHttpRequest(InputStream is) throws CommunicationException {
		return mCommunicationHandler.doRequest(is);
	}

	public String getUrl() {
		return mUrlStr;
	}

	public String getUser() {
		return mUser;
	}

	public String getPassword() {
		return mPassword;
	}

	@Override
	public void setGzip(boolean useGzip) {
		mCommunicationHandler.setGzip(useGzip);
	}

	@Override
	public boolean usesGzip() {
		return mCommunicationHandler.usesGzip();
	}

	/**
	 * @return whether the channel uses basic authentication
	 */
	public boolean isBasicAuth() {
		return mBasicAuth;
	}

	@Override
	public void closeTcpConnection() throws CommunicationException {
		mCommunicationHandler.closeTcpConnection();
	}

	/**
	 * get a {@link SSLSocketFactory} based on the {@link KeyManager} and
	 * {@link TrustManager} objects we got.
	 *
	 * @throws InitializationException
	 */
	private SSLSocketFactory initSslSocketFactory(KeyManager[] kms, TrustManager[] tms)
			throws InitializationException {

		SSLContext ctx = null;

		String verify = System.getProperty(VERIFY_PEER_CERT_PROPERTY);


		// If VERIFY_PEER_PROPERTY is set to false, we don't want verification
		// of the other sides certificate
		if (verify != null &&  verify.equals("false")) {
			tms = getTrustAllKeystore();
		} else if (verify == null || verify != null && verify.equals("true")) {
			// use the given tms
		} else {
			throw new InitializationException("Bad value for "
					+ VERIFY_PEER_CERT_PROPERTY +  " property. Expected: true|false");
		}

		if (!isBasicAuth() && kms == null) {
			throw new InitializationException("certificate-based auth needs a KeyManager");
		}


		try {
			ctx = SSLContext.getInstance("TLS");
			ctx.init(kms, tms, new SecureRandom());
		} catch (Exception e) {
			/* catch all */
			IfmapJLog.error("Could not initialize SSLSocketFactory ["
								  + e.getMessage() + "]");
			throw new InitializationException(e);
		}
		return ctx.getSocketFactory();
	}

	private TrustManager[] getTrustAllKeystore() {
		return new TrustManager[] {new TrustAllManager() };
	}

	private HostnameVerifier initHostnameVerifier() {
		String verify = System.getProperty(VERIFY_PEER_HOST_PROPERTY);

		if (verify != null && verify.equals("true")) {
			return new X509CommonNameHostnameVerifier();
		} else { /* verify is not set or to something else than true */
			return new AllOkHostnameVerifier();
		}
	}


	// http://exampledepot.com/egs/javax.net.ssl/TrustAll.html :)
	private class TrustAllManager implements X509TrustManager {

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			// NOTHING
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			// NOTHING
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	};

	private class AllOkHostnameVerifier implements HostnameVerifier {

		@Override
		public boolean verify(String arg0, SSLSession arg1) {
			return true;
		}
	}

	/**
	 * Simple class to extract the common name from a X509Certificate
	 * and compare all IP address received from hosts.txt/DNS/...
	 * for the common name with all IP addresses received for the
	 * given hostname.
	 *
	 * TODO: is this really what we want?
	 *
	 * @return true if one pair of IP addresses matches...
	 */
	private class X509CommonNameHostnameVerifier implements HostnameVerifier {

		@Override
		public boolean verify(String hostname, SSLSession session) {
			X509Certificate cert0 = null;
			X500Principal principal = null;
			InetAddress[] hostIps;
			InetAddress[] certIps;
			String name = null;
			String certCn = null;
			Certificate[] certs;
			int idx;

			try {
				certs = session.getPeerCertificates();
			} catch (SSLPeerUnverifiedException e) {
				return false;
			}

			if (certs.length == 0) {
				return false;
			}

			if (!(certs[0] instanceof X509Certificate)) {
				return false;
			}

			cert0 = (X509Certificate) certs[0];
			principal = cert0.getSubjectX500Principal();

			if (principal == null) {
				return false;
			}

			name = principal.getName();

			// assuming it always does
			if (!name.startsWith("CN=")) {
				return false;
			}

			idx = name.indexOf(',');

			if (idx < 0 || idx < 3) {
				return false;
			}

			certCn = name.substring(3, idx);

			try {
				hostIps = InetAddress.getAllByName(hostname);
				certIps = InetAddress.getAllByName(certCn);
			} catch (UnknownHostException e) {
				return false;
			}

			for (InetAddress hostIp : hostIps) {
				for (InetAddress certIp : certIps) {
					if (hostIp.equals(certIp)) {
						return true;
					}
				}
			}

			return false;
		}
	}
}
