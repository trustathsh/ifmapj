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
 * This file is part of ifmapj, version 2.2.1, implemented by the Trust@HsH
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
package de.hshannover.f4.trust.ifmapj.identifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Trivial tests for {@link MacAddress} class.
 *
 * @author ibente
 *
 */
public class MacAddressTest {

	private String mAd = "de.fhhannover.inform.trust";
	private String mMac = "00:11:22:AA:BB:CC";
//	public String mMac_canonicalized = "00:11:22:aa:bb:cc";

	@Test
	public void testMacAddress() {
		MacAddress m = Identifiers.createMac(null);
		assertNotNull(m);
		assertNull(m.getAdministrativeDomain());
		assertNull(m.getValue());
		m = Identifiers.createMac(mMac, mAd);
		assertEquals(mAd, m.getAdministrativeDomain());
//		ifmapj does not do this
//		assertEquals(mMac_canonicalized, m.getValue());
	}

	@Test
	public void testMacAddressToString() {
		MacAddress mac = Identifiers.createMac(null);

		assertEquals("mac{null}", mac.toString());

		mac = Identifiers.createMac("abc");
		assertEquals("mac{abc}", mac.toString());

		mac = Identifiers.createMac(null, "abc");
		assertEquals("mac{null, abc}", mac.toString());

		mac = Identifiers.createMac("abc", "abc");
		assertEquals("mac{abc, abc}", mac.toString());

		mac = Identifiers.createMac(mMac.replaceAll(":", "-"), mAd);
//		ifmapj does not do this
//		assertEquals(mMac_canonicalized, mac.getValue());
	}
}
