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
package de.hshannover.f4.trust.ifmapj.config;

import de.hshannover.f4.trust.ifmapj.channel.SSRC;

/**
 * Configuration for certificate based SSRC authentication.
 */
public class CertAuthConfig {

	public final String url;
	public final String keyStorePath;
	public final String keyStorePassword;
	public final String trustStorePath;
	public final String trustStorePassword;

	public final boolean threadSafe;

	/**
	 * Timeout for the initial connection attempt in milliseconds.
	 */
	public final int initialConnectionTimeout;

	/**
	 * Create a new {@link CertAuthConfig} object with the given configuration
	 * parameter.
	 *
	 * @param url the URL to connect to
	 * @param keyStorePath path to keyStore file
	 * @param keyStorePassword password for keyStore
	 * @param trustStorePath path to trustStore
	 * @param trustStorePassword password for trustStore
	 * @param threadSafe true if the SSRC should be thread safe
	 */
	public CertAuthConfig(
			String url,
			String keyStorePath,
			String keyStorePassword,
			String trustStorePath,
			String trustStorePassword,
			boolean threadSafe,
			int initialConnectionTimeout) {
		super();
		this.url = url;
		this.keyStorePath = keyStorePath;
		this.keyStorePassword = keyStorePassword;
		this.trustStorePath = trustStorePath;
		this.trustStorePassword = trustStorePassword;
		this.threadSafe = threadSafe;
		this.initialConnectionTimeout = initialConnectionTimeout;
	}

	/**
	 * Create a {@link CertAuthConfig} for a non thread safe {@link SSRC}
	 * with initial connection timeout of 120 seconds.
	 *
	 * @param url the URL to connect to
	 * @param username basic authentication user
	 * @param password basic authentication password
	 * @param trustStorePath path to trustStore
	 * @param trustStorePassword password for trustStore
	 */
	public CertAuthConfig(
			String url,
			String keyStorePath,
			String keyStorePassword,
			String trustStorePath,
			String trustStorePassword) {
		this(url, keyStorePath, keyStorePassword, trustStorePath, trustStorePassword, false, 120*1000);
	}
}
