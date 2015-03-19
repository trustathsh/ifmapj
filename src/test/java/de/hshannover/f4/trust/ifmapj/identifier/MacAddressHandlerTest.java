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
 * This file is part of ifmapj, version 2.2.2, implemented by the Trust@HsH
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

import javax.xml.parsers.DocumentBuilder;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.hshannover.f4.trust.ifmapj.exception.MarshalException;
import de.hshannover.f4.trust.ifmapj.exception.UnmarshalException;
import util.DomHelpers;

public class MacAddressHandlerTest {

	private static DocumentBuilder sDocBuilder = DomHelpers.newDocumentBuilder();
	private static MacAddressHandler sMacAddressHandler = new MacAddressHandler();

	@Test(expected = MarshalException.class)
	public void testToElementWithNullName() throws MarshalException {
		Document doc = sDocBuilder.newDocument();
		MacAddress mac = Identifiers.createMac(null);

		Element res = sMacAddressHandler.toElement(mac, doc);
		assertNull(res);
	}

	@Test(expected = MarshalException.class)
	public void testToElementWithNoName() throws MarshalException {
		Document doc = sDocBuilder.newDocument();
		MacAddress mac = Identifiers.createMac("");

		Element res = sMacAddressHandler.toElement(mac, doc);
		assertNull(res);
	}

	/**
	 * This shouldn't show the administrative-domain
	 * @throws MarshalException
	 */
	@Test
	public void testToElementWithEmptyAd() throws MarshalException {
		Document doc = sDocBuilder.newDocument();
		MacAddress mac = Identifiers.createMac("aa:bb:cc:dd:ee:ff", "");

		Element res = sMacAddressHandler.toElement(mac, doc);
		assertNotNull(res);
		assertEquals("mac-address", res.getLocalName());
		assertNull(res.getPrefix());
		assertNull(res.getNamespaceURI());
		assertNotNull(res.getAttributeNode("value"));
		assertEquals("aa:bb:cc:dd:ee:ff", res.getAttribute("value"));

		assertNull(res.getAttributeNode("administrative-domain"));

		assertEquals(0, res.getChildNodes().getLength());
		assertEquals(1, res.getAttributes().getLength());
	}

	@Test
	public void testToElementFromByteWithEmptyAd() throws MarshalException {
		Document doc = sDocBuilder.newDocument();
		byte[] b = new byte[6];
		b[0] = (byte) 0xaa;	b[1] = (byte) 0xbb;
		b[2] = (byte) 0xcc;	b[3] = (byte) 0xdd;
		b[4] = (byte) 0xee;	b[5] = (byte) 0xff;
		MacAddress mac = Identifiers.createMacFromByte(b, "");

		Element res = sMacAddressHandler.toElement(mac, doc);
		assertNotNull(res);
		assertEquals("mac-address", res.getLocalName());
		assertNull(res.getPrefix());
		assertNull(res.getNamespaceURI());
		assertNotNull(res.getAttributeNode("value"));
		assertEquals("aa:bb:cc:dd:ee:ff", res.getAttribute("value"));

		assertNull(res.getAttributeNode("administrative-domain"));

		assertEquals(0, res.getChildNodes().getLength());
		assertEquals(1, res.getAttributes().getLength());
	}

	@Test
	public void testToElementWithName() throws MarshalException {
		Document doc = sDocBuilder.newDocument();
		MacAddress mac = Identifiers.createMac("aa:bb:cc:dd:ee:ff", null);

		Element res = sMacAddressHandler.toElement(mac, doc);
		assertNotNull(res);
		assertEquals("mac-address", res.getLocalName());
		assertNull(res.getPrefix());
		assertNull(res.getNamespaceURI());
		assertNotNull(res.getAttributeNode("value"));
		assertEquals("aa:bb:cc:dd:ee:ff", res.getAttribute("value"));

		assertNull(res.getAttributeNode("administrative-domain"));

		assertEquals(0, res.getChildNodes().getLength());
		assertEquals(1, res.getAttributes().getLength());
	}

	@Test
	public void testToElementFromByteWithName() throws MarshalException {
		Document doc = sDocBuilder.newDocument();
		byte[] b = new byte[6];
		b[0] = (byte) 0xaa;	b[1] = (byte) 0xbb;
		b[2] = (byte) 0xcc;	b[3] = (byte) 0xdd;
		b[4] = (byte) 0xee;	b[5] = (byte) 0xff;
		MacAddress mac = Identifiers.createMacFromByte(b, null);

		Element res = sMacAddressHandler.toElement(mac, doc);
		assertNotNull(res);
		assertEquals("mac-address", res.getLocalName());
		assertNull(res.getPrefix());
		assertNull(res.getNamespaceURI());
		assertNotNull(res.getAttributeNode("value"));
		assertEquals("aa:bb:cc:dd:ee:ff", res.getAttribute("value"));

		assertNull(res.getAttributeNode("administrative-domain"));

		assertEquals(0, res.getChildNodes().getLength());
		assertEquals(1, res.getAttributes().getLength());
	}

	@Test
	public void testToElementFromByteWithNameAndAd() throws MarshalException {
		Document doc = sDocBuilder.newDocument();
		byte[] b = new byte[6];
		b[0] = (byte) 0xaa;	b[1] = (byte) 0xbb;
		b[2] = (byte) 0xcc;	b[3] = (byte) 0xdd;
		b[4] = (byte) 0xee;	b[5] = (byte) 0xff;
		MacAddress mac = Identifiers.createMacFromByte(b, "mydomain");

		Element res = sMacAddressHandler.toElement(mac, doc);
		assertNotNull(res);
		assertEquals("mac-address", res.getLocalName());
		assertNull(res.getPrefix());
		assertNull(res.getNamespaceURI());
		assertNotNull(res.getAttributeNode("value"));
		assertEquals("aa:bb:cc:dd:ee:ff", res.getAttribute("value"));

		assertNotNull(res.getAttributeNode("administrative-domain"));
		assertEquals("mydomain", res.getAttribute("administrative-domain"));

		assertEquals(0, res.getChildNodes().getLength());
		assertEquals(2, res.getAttributes().getLength());
	}

	@Test
	public void testFromElementWithName() throws UnmarshalException {
		Document doc = sDocBuilder.newDocument();
		Element xmlMac = doc.createElementNS(null, "mac-address");
		xmlMac.setAttribute("value", "aa:bb:cc:dd:ee:ff");

		MacAddress mac = sMacAddressHandler.fromElement(xmlMac);
		assertNotNull(mac);
		assertNotNull(mac.getValue());
		assertEquals("aa:bb:cc:dd:ee:ff", mac.getValue());

		assertNull(mac.getAdministrativeDomain());
	}

	@Test
	public void testFromElementWithNameAndAd() throws UnmarshalException {
		Document doc = sDocBuilder.newDocument();
		Element xmlMac = doc.createElementNS(null, "mac-address");
		xmlMac.setAttribute("value", "aa:bb:cc:dd:ee:ff");
		xmlMac.setAttribute("administrative-domain", "mydomain");

		MacAddress mac = sMacAddressHandler.fromElement(xmlMac);
		assertEquals("aa:bb:cc:dd:ee:ff", mac.getValue());
		assertEquals("mydomain", mac.getAdministrativeDomain());
	}

	@Test(expected = UnmarshalException.class)
	public void testFromElementWithNullName() throws UnmarshalException {
		Document doc = sDocBuilder.newDocument();
		Element xmlMac = doc.createElementNS(null, "mac-address");
		MacAddress mac = sMacAddressHandler.fromElement(xmlMac);
		assertNull(mac);
	}

	@Test(expected = UnmarshalException.class)
	public void testFromElementWithEmptyName() throws UnmarshalException {
		Document doc = sDocBuilder.newDocument();
		Element xmlMac = doc.createElementNS(null, "mac-address");
		xmlMac.setAttribute("value", "");
		MacAddress mac = sMacAddressHandler.fromElement(xmlMac);
		assertNull(mac);
	}

	@Test
	public void testNotResponsible() throws UnmarshalException {
		Document doc = sDocBuilder.newDocument();
		Element xmlMac = doc.createElementNS(null, "ip-address");

		MacAddress mac = sMacAddressHandler.fromElement(xmlMac);
		assertNull(mac);
	}

	@Test(expected = MarshalException.class)
	public void testToElementWrongIdentifierType() throws UnmarshalException, MarshalException {
		Document doc = sDocBuilder.newDocument();
		Element ret = sMacAddressHandler.toElement(Identifiers.createDev("DEV100"), doc);
		assertNull(ret);
	}
}
