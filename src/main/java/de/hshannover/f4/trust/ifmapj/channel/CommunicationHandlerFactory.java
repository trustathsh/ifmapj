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
package de.hshannover.f4.trust.ifmapj.channel;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

import de.hshannover.f4.trust.ifmapj.exception.InitializationException;

public abstract class CommunicationHandlerFactory {

	public static final String HANDLER_PROPERTY = "ifmapj.communication.handler";

	/**
	 * Create a {@link CommunicationHandler} instance.
	 *
	 * The {@link CommunicationHandler} implementation returned by this method
	 * depends on the system property ifmapj.communication.handler. If it is
	 * set to <b>java</b> an instance of {@link JavaCommunicationHandler} is
	 * returned, if set to <b>apache</b> instead, {@link ApacheCoreCommunicationHandler}
	 * is used. If it is not set, either one is returned, preferring
	 * {@link ApacheCoreCommunicationHandler}.
	 *
	 * @param url The url to connect to.
	 * @param user for basic auth, if user != null -> pass != null
	 * @param pass for basic auth, if pass != null -> user != null
	 * @param sslSocketFactory the {@link SSLSocketFactory} to be used
	 * @param verifier
	 * @param initialConnectionTimeout the initial connection timeout in milliseconds
	 * @return the new {@link CommunicationHandler}
	 * @throws InitializationException
	 */
	public static CommunicationHandler newHandler(String url, String user,
			String pass, SSLSocketFactory sslSocketFactory, HostnameVerifier verifier, int initialConnectionTimeout)
			throws InitializationException {

		String handler = System.getProperty(HANDLER_PROPERTY);

		if (handler != null) {
			return newHandlerPreference(handler, url, user, pass, sslSocketFactory, verifier, initialConnectionTimeout);
		} else {
			return newHandlerAuto(url, user, pass, sslSocketFactory, verifier, initialConnectionTimeout);
		}
	}


	/**
	 * Helper to return an instance of the {@link ApacheCoreCommunicationHandler}.
	 * If the {@link ApacheCoreCommunicationHandler} cannot be initialized, return
	 * a {@link JavaCommunicationHandler} instance.
	 *
	 * @param url
	 * @param user
	 * @param pass
	 * @param sslSocketFactory
	 * @param verifier
	 * @param initialConnectionTimeout the initial connection timeout in milliseconds
	 * @return
	 * @throws InitializationException
	 */
	private static CommunicationHandler newHandlerAuto(String url, String user,
			String pass, SSLSocketFactory sslSocketFactory, HostnameVerifier verifier, int initialConnectionTimeout)
					throws InitializationException {
		try {
			return new ApacheCoreCommunicationHandler(url, user, pass,
					sslSocketFactory, verifier, initialConnectionTimeout);
		} catch (NoClassDefFoundError e) {
			return new JavaCommunicationHandler(url, user, pass,
					sslSocketFactory, verifier, initialConnectionTimeout);
		}
	}

	/**
	 * Helper to return the handler which is indicated by handlerProp
	 *
	 * @param handlerProp
	 * @param url
	 * @param user
	 * @param pass
	 * @param sslSocketFactory
	 * @param verifier
	 * @param initialConnectionTimeout the initial connection timeout in milliseconds
	 * @return
	 * @throws InitializationException
	 */
	private static CommunicationHandler newHandlerPreference(String handlerProp,
			String url, String user, String pass,
			SSLSocketFactory sslSocketFactory, HostnameVerifier verifier, int initialConnectionTimeout)
					throws InitializationException {
		if (handlerProp == null) {
			throw new NullPointerException();
		}

		if (handlerProp.equals("java")) {
			return new JavaCommunicationHandler(url, user, pass,
					sslSocketFactory, verifier, initialConnectionTimeout);
		} else if (handlerProp.equals("apache")) {
			try {
				return new ApacheCoreCommunicationHandler(url, user, pass,
						sslSocketFactory, verifier, initialConnectionTimeout);
			} catch (NoClassDefFoundError e) {
				throw new InitializationException("Could not initialize ApacheCommunicationHandler");
			}
		} else {
			throw new InitializationException("Invalid " + HANDLER_PROPERTY + " value");
		}
	}
}
