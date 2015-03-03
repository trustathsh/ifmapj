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
 * This file is part of ifmapj, version 2.2.0, implemented by the Trust@HsH
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import de.hshannover.f4.trust.ifmapj.exception.InitializationException;

/**
 * To create a {@link SSRC} one needs to have {@link KeyManager} and
 * {@link TrustManager} instances. This is a helper class to make it
 * easy to get those.
 *
 * @author aw
 *
 */
public abstract class IfmapJHelper {

	/**
	 * Creates {@link KeyManager} instances based on the javax.net.ssl.keyStore
	 * and javax.net.ssl.keyStorePassword environment variables.
	 *
	 * @return an array of {@link KeyManager} instances.
	 * @throws InitializationException
	 */
	public static KeyManager[] getKeyManagers() throws InitializationException {
		String file = System.getProperty("javax.net.ssl.keyStore");
		String pass = System.getProperty("javax.net.ssl.keyStorePassword");

		if (file == null || pass == null) {
			throw new InitializationException("javax.net.ssl.keyStore / "
					+ "javax.net.ssl.keyStorePassword not set");
		}

		return getKeyManagers(file, pass);
	}

	/**
	 * Creates {@link KeyManager} instances based on the given parameters.
	 *
	 * @param keyStoreFile path to keyStore file
	 * @param pass password for keyStore
	 * @return an array of {@link KeyManager} instances
	 * @throws InitializationException
	 */
	public static KeyManager[] getKeyManagers(String keyStoreFile, String pass)
			throws InitializationException {
		return getKeyManagers(getFileAsInputStream(keyStoreFile), pass);
	}

	/**
	 * Creates {@link KeyManager} instances based on the given parameters.
	 *
	 * @param keyStoreIs {@link InputStream} representing contents of a keyStore
	 * @param pass password for the keyStore
	 * @return an array of {@link KeyManager} instances
	 * @throws InitializationException
	 */
	public static KeyManager[] getKeyManagers(InputStream keyStoreIs, String pass)
	throws InitializationException {
		if (keyStoreIs == null || pass == null) {
			throw new NullPointerException("parameters null?");
		}

		try {
			String defaultAlgo = KeyManagerFactory.getDefaultAlgorithm();
			KeyManagerFactory kmf = KeyManagerFactory.getInstance(defaultAlgo);
			KeyStore ks = loadKeyStore(keyStoreIs, pass);
			kmf.init(ks, pass.toCharArray());
			return kmf.getKeyManagers();
		} catch (Exception e) {
			throw new InitializationException(e.getMessage());
		}
	}

	/**
	 * Creates {@link TrustManager} instances based on the javax.net.ssl.trustStore
	 * and javax.net.ssl.trustStorePassword environment variables.
	 *
	 * @return an array of {@link TrustManager} instances
	 * @throws InitializationException
	 */
	public static TrustManager[] getTrustManagers() throws InitializationException {
		String file = System.getProperty("javax.net.ssl.trustStore");
		String pass = System.getProperty("javax.net.ssl.trustStorePassword");

		if (file == null || pass == null) {
			throw new InitializationException("javax.net.ssl.trustStore / "
					+ "javax.net.ssl.trustStorePassword not set");
		}

		return getTrustManagers(file, pass);
	}

	/**
	 * Creates {@link TrustManager} instances based on the given parameters.
	 *
	 * @param trustStorePath path to trustStore
	 * @param pass password for trustStore
	 * @return an array of {@link TrustManager} instances
	 * @throws InitializationException
	 */
	public static TrustManager[] getTrustManagers(String trustStorePath, String pass)
			throws InitializationException {
			return getTrustManagers(getFileAsInputStream(trustStorePath), pass);
	}

	/**
	 * Creates {@link TrustManager} instances based on the given parameters.
	 *
	 * @param trustStoreIs {@link InputStream} representing contents of a trustStore
	 * @param pass password for the trustStore
	 * @return an array of {@link TrustManager} instances
	 * @throws InitializationException
	 */
	public static TrustManager[] getTrustManagers(InputStream trustStoreIs, String pass)
			throws InitializationException {
		if (trustStoreIs == null || pass == null) {
			throw new NullPointerException("parameters null?");
		}

		try {
			String defaultAlgo = TrustManagerFactory.getDefaultAlgorithm();
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(defaultAlgo);
			KeyStore ks = loadKeyStore(trustStoreIs, pass);
			tmf.init(ks);
			return tmf.getTrustManagers();
		} catch (Exception e) {
			throw new InitializationException(e.getMessage());
		}
	}

	/**
	 * Helper to load a {@link KeyStore} instance.
	 *
	 * @param is {@link InputStream} representing contents of a keyStore
	 * @param pass the password of the keyStore
	 * @return an instance of the new loaded {@link KeyStore}
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws IOException
	 * @throws KeyStoreException
	 */
	private static KeyStore loadKeyStore(InputStream is, String pass)
			throws InitializationException, KeyStoreException,
			NoSuchAlgorithmException, CertificateException, IOException {
			KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
			ks.load(is, pass.toCharArray());
			return ks;
	}

	/**
	 * Helper to open a usual File to get the content as {@link InputStream}
	 *
	 * @param file the name of the file
	 * @return an {@link InputStream} that points to the file
	 * @throws InitializationException
	 */
	private static InputStream getFileAsInputStream(String file) throws InitializationException {
		if (file == null) {
			throw new NullPointerException("file is null");
		}
		try {
			return new FileInputStream(new File(file));
		} catch (FileNotFoundException e) {
			throw new InitializationException("File not found: " + file);
		}
	}
}
