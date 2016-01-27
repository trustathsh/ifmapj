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
 * This file is part of ifmapj, version 2.3.1, implemented by the Trust@HsH
 * research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2010 - 2015 Trust@HsH
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
package de.hshannover.f4.trust.ifmapj;

import java.io.IOException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import de.hshannover.f4.trust.ifmapj.channel.SSRC;
import de.hshannover.f4.trust.ifmapj.channel.SsrcImpl;
import de.hshannover.f4.trust.ifmapj.channel.ThreadSafeSsrc;
import de.hshannover.f4.trust.ifmapj.config.BasicAuthConfig;
import de.hshannover.f4.trust.ifmapj.config.CertAuthConfig;
import de.hshannover.f4.trust.ifmapj.exception.InitializationException;
import de.hshannover.f4.trust.ifmapj.identifier.Device;
import de.hshannover.f4.trust.ifmapj.identifier.Identifier;
import de.hshannover.f4.trust.ifmapj.identifier.IdentifierFactory;
import de.hshannover.f4.trust.ifmapj.identifier.Identifiers;
import de.hshannover.f4.trust.ifmapj.messages.RequestFactory;
import de.hshannover.f4.trust.ifmapj.messages.Requests;
import de.hshannover.f4.trust.ifmapj.metadata.ContentAuthorizationMetadataFactory;
import de.hshannover.f4.trust.ifmapj.metadata.ContentAuthorizationMetadataFactoryImpl;
import de.hshannover.f4.trust.ifmapj.metadata.IcsSecurityMetadataFactory;
import de.hshannover.f4.trust.ifmapj.metadata.IcsSecurityMetadataFactoryImpl;
import de.hshannover.f4.trust.ifmapj.metadata.StandardIfmapMetadataFactory;
import de.hshannover.f4.trust.ifmapj.metadata.StandardIfmapMetadataFactoryImpl;
import de.hshannover.f4.trust.ifmapj.metadata.VendorSpecificMetadataFactory;
import de.hshannover.f4.trust.ifmapj.metadata.VendorSpecificMetadataFactoryImpl;
import de.hshannover.f4.trust.ifmapj21.ClockSkewDetector;

/**
 * Entry class to do IF-MAP 2.0 communication using IfmapJ.
 *
 * @author aw
 *
 */
@SuppressWarnings("deprecation")
public final class IfmapJ {

	private IfmapJ() {
	}

	/**
	 * Create a new {@link SSRC} object to operate on the given url
	 * using basic authentication.
	 *
	 * @param url
	 *            the URL to connect to
	 * @param user
	 *            basic authentication user
	 * @param pass
	 *            basic authentication password
	 * @param tms
	 *            TrustManager instances to initialize the {@link SSLContext} with.
	 * @return a new {@link SSRC} that uses basic authentication
	 * @throws IOException
	 * @deprecated use createSsrc(BasicAuthConfig) instead
	 */
	@Deprecated
	public static SSRC createSsrc(String url, String user, String pass, TrustManager[] tms)
			throws InitializationException {
		return new SsrcImpl(url, user, pass, tms, 120 * 1000);
	}

	/**
	 * Create a new {@link SSRC} object to operate on the given configuration
	 * using basic authentication.
	 *
	 * @param config
	 *            the configuration parameter for the new SSRC
	 * @return a new {@link SSRC} that uses basic authentication
	 * @throws InitializationException
	 *             if the SSRC initialization fails
	 */
	public static SSRC createSsrc(BasicAuthConfig config)
			throws InitializationException {
		TrustManager[] trustManagers = IfmapJHelper.getTrustManagers(
				config.trustStorePath,
				config.trustStorePassword);
		SSRC ssrc = new SsrcImpl(
				config.url, config.username, config.password, trustManagers, config.initialConnectionTimeout);

		if (config.threadSafe) {
			return new ThreadSafeSsrc(ssrc);
		} else {
			return ssrc;
		}
	}

	/**
	 * Create a new {@link SSRC} object to operate on the given URL
	 * using certificate-based authentication.
	 *
	 * The keystore and truststore to be used have to be set using
	 * the {@link System#setProperty(String, String)} method using
	 * the keys javax.net.ssl.keyStore, and javax.net.ssl.trustStore
	 * respectively.
	 *
	 * @param url
	 *            the URL to connect to
	 * @param kms
	 *            TrustManager instances to initialize the {@link SSLContext} with.
	 * @param tms
	 *            KeyManager instances to initialize the {@link SSLContext} with.
	 * @return a new {@link SSRC} that uses certificate-based authentication
	 * @throws IOException
	 * @deprecated use createSsrc(CertAuthConfig) instead
	 */
	@Deprecated
	public static SSRC createSsrc(String url, KeyManager[] kms, TrustManager[] tms)
			throws InitializationException {
		return new SsrcImpl(url, kms, tms, 120 * 1000);
	}

	/**
	 * Create a new {@link SSRC} object to operate on the given configuration
	 * using certificate based authentication.
	 *
	 * @param config
	 *            the configuration parameter for the new SSRC
	 * @return a new {@link SSRC} that uses certificate based authentication
	 * @throws InitializationException
	 *             if the SSRC initialization fails
	 */
	public static SSRC createSsrc(CertAuthConfig config)
			throws InitializationException {
		TrustManager[] trustManagers = IfmapJHelper.getTrustManagers(
				config.trustStorePath,
				config.trustStorePassword);
		KeyManager[] keyManagers = IfmapJHelper.getKeyManagers(
				config.keyStorePath,
				config.keyStorePassword);
		SSRC ssrc = new SsrcImpl(config.url, keyManagers, trustManagers, config.initialConnectionTimeout);

		if (config.threadSafe) {
			return new ThreadSafeSsrc(ssrc);
		} else {
			return ssrc;
		}
	}

	/**
	 * Create a new {@link RequestFactory} object to create IF-MAP requests
	 * to be used with the methods provided by the {@link SSRC} class.
	 *
	 * @return a new {@link RequestFactory} that is used to create IF-MAP requests
	 * @deprecated
	 *             Use {@link Requests} directly.
	 */
	@Deprecated
	public static RequestFactory createRequestFactory() {
		return Requests.getRequestFactory();
	}

	/**
	 * Create a {@link IdentifierFactory} object to create different types
	 * of {@link Identifier} implementations.
	 *
	 * @return a new {@link IdentifierFactory} that is used to create IF-MAP identifiers
	 * @deprecated
	 *             Use {@link Identifiers} directly.
	 */
	@Deprecated
	public static IdentifierFactory createIdentifierFactory() {
		return Identifiers.getIdentifierFactory();
	}

	/**
	 * Create a new {@link StandardIfmapMetadataFactory} object to create
	 * standard metadata defined by IF-MAP 2.0.
	 *
	 * @return a new {@link StandardIfmapMetadataFactory} to create metadata
	 */
	public static StandardIfmapMetadataFactory createStandardMetadataFactory() {
		return new StandardIfmapMetadataFactoryImpl();
	}

	/**
	 * Create a new {@link IcsSecurityMetadataFactory} object
	 * to create the IF-MAP Metadata for ICS Security
	 *
	 * @return a new {@link IcsSecurityMetadataFactory} to create metadata
	 */
	public static IcsSecurityMetadataFactory createIcsSecurityMetadataFactory() {
		return new IcsSecurityMetadataFactoryImpl();
	}

	/**
	 * Create a new {@link ContentAuthorizationMetadataFactory} object
	 * to create the IF-MAP Metadata for Content Authorization
	 *
	 * @return a new {@link ContentAuthorizationMetadataFactory} to create metadata
	 */
	public static ContentAuthorizationMetadataFactory createContentAuthorizationMetadataFactory() {
		return new ContentAuthorizationMetadataFactoryImpl();
	}

	/**
	 * Create a {@link ClockSkewDetector} instance which can be used to
	 * synchronize the time with the MAPS.
	 *
	 * @param ssrc
	 *            the {@link SSRC} instance to be used for synchronization
	 * @param dev
	 *            the {@link Device} used for time synchronization
	 * @return
	 */
	public static ClockSkewDetector createClockSkewDetector(SSRC ssrc, Device dev) {
		return ClockSkewDetector.newInstance(ssrc, dev);
	}

	/**
	 * Create a {@link VendorSpecificMetadataFactory} instance which can be used
	 * to create vendor specific metadata documents.
	 *
	 * @return a new {@link VendorSpecificMetadataFactory}
	 */
	public static VendorSpecificMetadataFactory createVendorSpecificMetadataFactory() {
		return new VendorSpecificMetadataFactoryImpl();
	}
}
