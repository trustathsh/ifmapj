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
package de.hshannover.f4.trust.ifmapj.identifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.xml.parsers.DocumentBuilder;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import util.DomHelpers;
import de.hshannover.f4.trust.ifmapj.exception.MarshalException;
import de.hshannover.f4.trust.ifmapj.exception.UnmarshalException;

public class DeviceHandlerTest {

	private static DocumentBuilder sDocBuilder = DomHelpers.newDocumentBuilder();
	private static DeviceHandler sDevHandler = new DeviceHandler();

	@Test(expected = MarshalException.class)
	public void testToElementWithNullName() throws MarshalException {
		Document doc = sDocBuilder.newDocument();
		Device dev = Identifiers.createDev(null);

		Element res = sDevHandler.toElement(dev, doc);
		assertNotNull(res);
	}

	@Test(expected = MarshalException.class)
	public void testToElementWithEmptyName() throws MarshalException {
		Document doc = sDocBuilder.newDocument();
		Device dev = Identifiers.createDev("");

		Element res = sDevHandler.toElement(dev, doc);
		assertNotNull(res);
	}

	@Test
	public void testToElementWithName() throws MarshalException {
		Document doc = sDocBuilder.newDocument();
		Device dev = Identifiers.createDev("DEV100");

		Element xmlName = null;
		Element res = sDevHandler.toElement(dev, doc);
		assertNotNull(res);
		assertEquals("device", res.getLocalName());
		assertNull(res.getPrefix());
		assertNull(res.getNamespaceURI());
		assertEquals(1, res.getChildNodes().getLength());
		xmlName = (Element) res.getFirstChild();
		assertNotNull(xmlName);
		assertEquals("name", xmlName.getLocalName());
		assertEquals("DEV100", xmlName.getTextContent());

		assertNull(res.getAttributeNode("administrative-domain"));

		assertEquals(1, res.getChildNodes().getLength());
		assertEquals(0, res.getAttributes().getLength());
		assertEquals(1, xmlName.getChildNodes().getLength());
		assertEquals(0, xmlName.getAttributes().getLength());
		assertEquals(Node.TEXT_NODE, xmlName.getFirstChild().getNodeType());
	}

	@Test
	public void testFromElementWithName() throws UnmarshalException {
		Document doc = sDocBuilder.newDocument();
		Element xmlDev = doc.createElementNS(null, "device");
		Element xmlName = doc.createElementNS(null, "name");
		xmlDev.appendChild(xmlName);
		xmlName.setTextContent("DEV100");

		Device dev = sDevHandler.fromElement(xmlDev);
		assertNotNull(dev);
		assertNotNull(dev.getName());
		assertEquals("DEV100", dev.getName());
	}

	@Test(expected = UnmarshalException.class)
	public void testFromElementWithNoName() throws UnmarshalException {
		Document doc = sDocBuilder.newDocument();
		Element xmlDev = doc.createElementNS(null, "device");
		Device dev = sDevHandler.fromElement(xmlDev);
		assertNull(dev);
	}

	@Test(expected = UnmarshalException.class)
	public void testFromElementWithEmptyName() throws UnmarshalException {
		Document doc = sDocBuilder.newDocument();
		Element xmlDev = doc.createElementNS(null, "device");
		Element xmlName = doc.createElementNS(null, "name");
		xmlName.setTextContent("");
		xmlDev.appendChild(xmlName);
		Device dev = sDevHandler.fromElement(xmlDev);
		assertNull(dev);
	}

	@Test(expected = UnmarshalException.class)
	public void testFromElementWithBadElement() throws UnmarshalException {
		Document doc = sDocBuilder.newDocument();
		Element xmlDev = doc.createElementNS(null, "device");
		Element xmlName = doc.createElementNS(null, "bad_name_element");
		xmlDev.appendChild(xmlName);
		Device dev = sDevHandler.fromElement(xmlDev);
		assertNull(dev);
	}

	@Test
	public void testNotResponsible() throws UnmarshalException {
		Document doc = sDocBuilder.newDocument();
		Element xmlAr = doc.createElementNS(null, "ip-address");
		Device dev = sDevHandler.fromElement(xmlAr);
		assertNull(dev);
	}

	@Test(expected = MarshalException.class)
	public void testToElementWrongIdentifierType() throws UnmarshalException, MarshalException {
		Document doc = sDocBuilder.newDocument();
		Element ret = sDevHandler.toElement(Identifiers.createAr("ABC"), doc);
		assertNull(ret);
	}
}
