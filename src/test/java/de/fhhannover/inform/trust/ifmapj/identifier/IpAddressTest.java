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
 * Trivial tests for {@link IpAddress} class.
 * 
 * @author ibente
 *
 */
public class IpAddressTest {

	public String ad = "de.fhhannover.inform.trust";
	public String value = "127.0.0.1";

	@Test
	public void testIpAddress() {
		IpAddress ip = Identifiers.createIp(null, null, null);

		assertNotNull(ip);
		assertNull(ip.getAdministrativeDomain());
		assertNull(ip.getValue());
		assertNotNull(ip.getType());
		assertEquals(IpAddressType.IPv4, ip.getType());

		ip = Identifiers.createIp(IpAddressType.IPv6, value, ad);

		assertEquals(ad, ip.getAdministrativeDomain());
		assertEquals(value, ip.getValue());
		assertEquals(IpAddressType.IPv6, ip.getType());
	}

	@Test
	public void testIpAddressToString() {
		IpAddress ip = Identifiers.createIp(null, null, null);

		assertEquals("ip{null, IPv4}", ip.toString());

		ip = Identifiers.createIp(null, null, "abc");
		assertEquals("ip{null, IPv4, abc}", ip.toString());

		ip = Identifiers.createIp(IpAddressType.IPv6, null, "abc");
		assertEquals("ip{null, IPv6, abc}", ip.toString());

		ip = Identifiers.createIp(IpAddressType.IPv6,
				"i'll never remember how it looks like",
				"abc");
		assertEquals("ip{i'll never remember how it looks like, IPv6, abc}",
				ip.toString());

		/* this actually tests the super class */
		ip = Identifiers.createIp6("i'll never remember how it looks like");
		assertEquals("ip{i'll never remember how it looks like, IPv6}",
				ip.toString());

		ip = Identifiers.createIp4("192.168.0.1");
		assertEquals("ip{192.168.0.1, IPv4}", ip.toString());
	}
}
