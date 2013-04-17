package de.fhhannover.inform.trust.ifmapj;

/*
 * #%L
 * ====================================================
 *   _____                _     ____  _____ _   _ _   _
 *  |_   _|_ __ _   _ ___| |_  / __ \|  ___| | | | | | |
 *    | | | '__| | | / __| __|/ / _` | |_  | |_| | |_| |
 *    | | | |  | |_| \__ \ |_| | (_| |  _| |  _  |  _  |
 *    |_| |_|   \__,_|___/\__|\ \__,_|_|   |_| |_|_| |_|
 *                             \____/
 * 
 * =====================================================
 * 
 * Fachhochschule Hannover 
 * (University of Applied Sciences and Arts, Hannover)
 * Faculty IV, Dept. of Computer Science
 * Ricklinger Stadtweg 118, 30459 Hannover, Germany
 * 
 * Email: trust@f4-i.fh-hannover.de
 * Website: http://trust.inform.fh-hannover.de/
 * 
 * This file is part of IfmapJ, version 0.1.5, implemented by the Trust@FHH 
 * research group at the Fachhochschule Hannover.
 * 
 * IfmapJ is a lightweight, platform-independent, easy-to-use IF-MAP client
 * library for Java. IF-MAP is an XML based protocol for sharing data across
 * arbitrary components, specified by the Trusted Computing Group. IfmapJ is
 * maintained by the Trust@FHH group at the Fachhochschule Hannover. IfmapJ 
 * was developed within the ESUKOM research project.
 * %%
 * Copyright (C) 2010 - 2013 Trust@FHH
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

import java.io.IOException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import de.fhhannover.inform.trust.ifmapj.channel.SSRC;
import de.fhhannover.inform.trust.ifmapj.channel.SsrcImpl;
import de.fhhannover.inform.trust.ifmapj.exception.InitializationException;
import de.fhhannover.inform.trust.ifmapj.identifier.Device;
import de.fhhannover.inform.trust.ifmapj.identifier.Identifier;
import de.fhhannover.inform.trust.ifmapj.identifier.IdentifierFactory;
import de.fhhannover.inform.trust.ifmapj.identifier.Identifiers;
import de.fhhannover.inform.trust.ifmapj.messages.RequestFactory;
import de.fhhannover.inform.trust.ifmapj.messages.Requests;
import de.fhhannover.inform.trust.ifmapj.metadata.StandardIfmapMetadataFactory;
import de.fhhannover.inform.trust.ifmapj.metadata.StandardIfmapMetadataFactoryImpl;
import de.fhhannover.inform.trust.ifmapj21.ClockSkewDetector;


/**
 * Entry class to do IF-MAP 2.0 communication using IfmapJ.
 * 
 * @author aw
 *
 */
@SuppressWarnings("deprecation")
public class IfmapJ {
	
	/**
	 * Create a new {@link SSRC} object to operate on the given url
	 * using basic authentication.
	 * 
	 * @param url the URL to connect to
	 * @param user basic authentication user
	 * @param pass basic authentication password
	 * @param tms TrustManager instances to initialize the {@link SSLContext} with.
	 * @return a new {@link SSRC} that uses basic authentication
	 * @throws IOException 
	 */
	public static SSRC createSSRC(String url, String user, String pass, TrustManager[] tms)
			throws InitializationException {
		return new SsrcImpl(url, user, pass, tms);
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
	 * @param url the URL to connect to
	 * @param kms TrustManager instances to initialize the {@link SSLContext} with.
	 * @param tms KeyManager instances to initialize the {@link SSLContext} with.
	 * @return a new {@link SSRC} that uses certificate-based authentication
	 * @throws IOException 
	 */
	public static SSRC createSSRC(String url, KeyManager[] kms, TrustManager[] tms)
			throws InitializationException {
		return new SsrcImpl(url, kms, tms);
	}
	
	/**
	 * Create a new {@link RequestFactory} object to create IF-MAP requests
	 * to be used with the methods provided by the {@link SSRC} class.
	 * 
	 * @return a new {@link RequestFactory} that is used to create IF-MAP requests
	 * @deprecated
	 * Use {@link Requests} directly.
	 */
	public static RequestFactory createRequestFactory() {
		return Requests.getRequestFactory();
	}
	
	/**
	 * Create a {@link IdentifierFactory} object to create different types
	 * of {@link Identifier} implementations.
	 * 
	 * @return a new {@link IdentifierFactory} that is used to create IF-MAP identifiers
	 * @deprecated
	 * Use {@link Identifiers} directly.
	 */
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
	 * Create a {@link ClockSkewDetector} instance which can be used to
	 * synchronize the time with the MAPS.
	 * 
	 * @param ssrc the {@link SSRC} instance to be used for synchronization
	 * @param dev the {@link Device} used for time synchronization
	 * @return
	 */
	public static ClockSkewDetector createClockSkewDetector(SSRC ssrc, Device dev) {
		return ClockSkewDetector.newInstance(ssrc, dev);
	}
}
