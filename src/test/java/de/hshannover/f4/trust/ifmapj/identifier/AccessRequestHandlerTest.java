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
 * This file is part of ifmapj, version 2.0.0, implemented by the Trust@HsH
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

import util.DomHelpers;
import de.hshannover.f4.trust.ifmapj.exception.MarshalException;
import de.hshannover.f4.trust.ifmapj.exception.UnmarshalException;

public class AccessRequestHandlerTest {

	private static DocumentBuilder sDocBuilder = DomHelpers.newDocumentBuilder();
	private static AccessRequestHandler sAccessRequestHandler = new AccessRequestHandler();

	@Test(expected = MarshalException.class)
	public void testToElementWithNullName() throws MarshalException {
		Document doc = sDocBuilder.newDocument();
		AccessRequest ar = Identifiers.createAr(null);
		// throws
		Element res = sAccessRequestHandler.toElement(ar, doc);
		assertNotNull(res);
	}

	@Test(expected = MarshalException.class)
	public void testToElementWithEmptyName() throws MarshalException {
		Document doc = sDocBuilder.newDocument();
		AccessRequest ar = Identifiers.createAr("");
		// throws
		Element res = sAccessRequestHandler.toElement(ar, doc);
		assertNotNull(res);
	}

	/**
	 * This shouldn't show the administrative-domain
	 * @throws MarshalException
	 */
	@Test
	public void testToElementWithEmptyAd() throws MarshalException {
		Document doc = sDocBuilder.newDocument();
		AccessRequest ar = Identifiers.createAr("AR100", "");

		Element res = sAccessRequestHandler.toElement(ar, doc);
		assertNotNull(res);
		assertEquals("access-request", res.getLocalName());
		assertNull(res.getPrefix());
		assertNull(res.getNamespaceURI());
		assertNotNull(res.getAttributeNode("name"));
		assertEquals("AR100", res.getAttribute("name"));

		assertNull(res.getAttributeNode("administrative-domain"));

		assertEquals(0, res.getChildNodes().getLength());
		assertEquals(1, res.getAttributes().getLength());
	}

	@Test
	public void testToElementWithNullAd() throws MarshalException {
		Document doc = sDocBuilder.newDocument();
		AccessRequest ar = Identifiers.createAr("AR100", null);

		Element res = sAccessRequestHandler.toElement(ar, doc);
		assertNotNull(res);
		assertEquals("access-request", res.getLocalName());
		assertNull(res.getPrefix());
		assertNull(res.getNamespaceURI());
		assertNotNull(res.getAttributeNode("name"));
		assertEquals("AR100", res.getAttribute("name"));

		assertNull(res.getAttributeNode("administrative-domain"));

		assertEquals(0, res.getChildNodes().getLength());
		assertEquals(1, res.getAttributes().getLength());
	}

	@Test
	public void testToElementWithName() throws MarshalException {
		Document doc = sDocBuilder.newDocument();
		AccessRequest ar = Identifiers.createAr("AR100");

		Element res = sAccessRequestHandler.toElement(ar, doc);
		assertNotNull(res);
		assertEquals("access-request", res.getLocalName());
		assertNull(res.getPrefix());
		assertNull(res.getNamespaceURI());
		assertNotNull(res.getAttributeNode("name"));
		assertEquals("AR100", res.getAttribute("name"));

		assertNull(res.getAttributeNode("administrative-domain"));

		assertEquals(0, res.getChildNodes().getLength());
		assertEquals(1, res.getAttributes().getLength());
	}

	@Test
	public void testToElementWithNameAndAd() throws MarshalException {
		Document doc = sDocBuilder.newDocument();
		AccessRequest ar = Identifiers.createAr("AR100", "mydomain");

		Element res = sAccessRequestHandler.toElement(ar, doc);
		assertNotNull(res);
		assertEquals("access-request", res.getLocalName());
		assertNull(res.getPrefix());
		assertNull(res.getNamespaceURI());
		assertNotNull(res.getAttributeNode("name"));
		assertEquals("AR100", res.getAttribute("name"));

		assertNotNull(res.getAttributeNode("administrative-domain"));
		assertEquals("mydomain", res.getAttribute("administrative-domain"));

		assertEquals(0, res.getChildNodes().getLength());
		assertEquals(2, res.getAttributes().getLength());
	}

	@Test
	public void testFromElementWithName() throws UnmarshalException {
		Document doc = sDocBuilder.newDocument();
		Element xmlAr = doc.createElementNS(null, "access-request");
		xmlAr.setAttribute("name", "AR100");

		AccessRequest ar = sAccessRequestHandler.fromElement(xmlAr);
		assertNotNull(ar);
		assertNotNull(ar.getName());
		assertEquals("AR100", ar.getName());

		assertNull(ar.getAdministrativeDomain());
	}

	@Test
	public void testFromElementWithNameAndAd() throws UnmarshalException {
		Document doc = sDocBuilder.newDocument();
		Element xmlAr = doc.createElementNS(null, "access-request");
		xmlAr.setAttribute("name", "AR100");
		xmlAr.setAttribute("administrative-domain", "mydomain");

		AccessRequest ar = sAccessRequestHandler.fromElement(xmlAr);
		assertEquals("AR100", ar.getName());
		assertEquals("mydomain", ar.getAdministrativeDomain());
	}

	@Test(expected = UnmarshalException.class)
	public void testFromElementWithNullName() throws UnmarshalException {
		Document doc = sDocBuilder.newDocument();
		Element xmlAr = doc.createElementNS(null, "access-request");

		// throws
		AccessRequest ar = sAccessRequestHandler.fromElement(xmlAr);
		assertNull(ar);
	}

	@Test(expected = UnmarshalException.class)
	public void testFromElementWithEmptyName() throws UnmarshalException {
		Document doc = sDocBuilder.newDocument();
		Element xmlAr = doc.createElementNS(null, "access-request");
		xmlAr.setAttribute("name", "");

		// throws
		AccessRequest ar = sAccessRequestHandler.fromElement(xmlAr);
		assertNull(ar);
	}

	@Test
	public void testNotResponsible() throws UnmarshalException {
		Document doc = sDocBuilder.newDocument();
		Element xmlAr = doc.createElementNS(null, "ip-address");

		AccessRequest ar = sAccessRequestHandler.fromElement(xmlAr);
		assertNull(ar);
	}

	@Test(expected = MarshalException.class)
	public void testToElementWrongIdentifierType() throws UnmarshalException, MarshalException {
		Document doc = sDocBuilder.newDocument();
		Element ret = sAccessRequestHandler.toElement(Identifiers.createIp4("192.168.0.1"), doc);
		assertNull(ret);
	}
}
