package de.fhhannover.inform.trust.ifmapj.identifier;

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
