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
 * This file is part of ifmapj, version 2.0.1, implemented by the Trust@HsH
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
package de.hshannover.f4.trust.ifmapj;

import javax.net.ssl.KeyManager;
import javax.net.ssl.TrustManager;

import junit.framework.TestCase;

import org.junit.Test;

import de.hshannover.f4.trust.ifmapj.exception.InitializationException;

public class IfmapJHelperTest extends TestCase {

	@Test
	public void testGetKeyManagersOk() {
		// set necessary system properties
		System.setProperty("javax.net.ssl.keyStore", Config.KEY_STORE_PATH);
		System.setProperty("javax.net.ssl.keyStorePassword", Config.KEY_STORE_PASSWORD);
		try {
			KeyManager[] km = IfmapJHelper.getKeyManagers();
			assertEquals(km.length, 1);
		} catch (InitializationException e) {
			assertFalse(e.getMessage(), true);
		}
	}

	@Test
	public void testGetKeyManagersFail() {
		System.setProperty("javax.net.ssl.keyStore", Config.KEY_STORE_PATH);
		System.setProperty("javax.net.ssl.keyStorePassword", Config.KEY_STORE_PASSWORD);

		System.clearProperty("javax.net.ssl.keyStore");
		try {
			IfmapJHelper.getKeyManagers();
			assertFalse("getKeyManagers did not fail but was expected to do so", true);
		} catch (InitializationException e) {
			assertTrue(true);
		}

		System.setProperty("javax.net.ssl.keyStore", Config.KEY_STORE_PATH);
		System.clearProperty("javax.net.ssl.keyStorePassword");
		try {
			IfmapJHelper.getKeyManagers();
			assertFalse("getKeyManagers did not fail but was expected to do so", true);
		} catch (InitializationException e) {
			assertTrue(true);
		}

		System.clearProperty("javax.net.ssl.keyStore");
		try {
			IfmapJHelper.getKeyManagers();
			assertFalse("getKeyManagers did not fail but was expected to do so", true);
		} catch (InitializationException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetTrustManagersOk() {
		// set necessary system properties
		System.setProperty("javax.net.ssl.trustStore", Config.TRUST_STORE_PATH);
		System.setProperty("javax.net.ssl.trustStorePassword", Config.TRUST_STORE_PASSWORD);
		try {
			TrustManager[] tm = IfmapJHelper.getTrustManagers();
			assertEquals(tm.length, 1);
		} catch (InitializationException e) {
			e.printStackTrace();
			assertFalse(e.getMessage(), true);
		}
	}

	@Test
	public void testGetTrustManagersFail() {
		System.setProperty("javax.net.ssl.trustStore", Config.TRUST_STORE_PATH);
		System.setProperty("javax.net.ssl.trustStorePassword", Config.TRUST_STORE_PASSWORD);

		System.clearProperty("javax.net.ssl.trustStore");
		try {
			IfmapJHelper.getTrustManagers();
			assertFalse("getTrustManagers did not fail but was expected to do so", true);
		} catch (InitializationException e) {
			assertTrue(true);
		}

		System.setProperty("javax.net.ssl.trustStore", Config.TRUST_STORE_PATH);
		System.clearProperty("javax.net.ssl.trustStorePassword");
		try {
			IfmapJHelper.getTrustManagers();
			assertFalse("getTrustManagers did not fail but was expected to do so", true);
		} catch (InitializationException e) {
			assertTrue(true);
		}

		System.clearProperty("javax.net.ssl.trustStore");
		try {
			IfmapJHelper.getTrustManagers();
			assertFalse("getTrustManagers did not fail but was expected to do so", true);
		} catch (InitializationException e) {
			assertTrue(true);
		}
	}

}
