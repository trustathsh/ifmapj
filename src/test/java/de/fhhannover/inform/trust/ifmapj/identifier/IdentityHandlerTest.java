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

import javax.xml.parsers.DocumentBuilder;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.fhhannover.inform.trust.ifmapj.exception.MarshalException;
import de.fhhannover.inform.trust.ifmapj.exception.UnmarshalException;

import util.DomHelpers;

public class IdentityHandlerTest {
	
	private static DocumentBuilder sDocBuilder = DomHelpers.newDocumentBuilder();
	private static IdentityHandler sIdentityHandler = new IdentityHandler();
	
	@Test(expected=MarshalException.class)
	public void testToElementWithNullName() throws MarshalException {
		Document doc = sDocBuilder.newDocument();
		Identity id = Identifiers.createIdentity(IdentityType.userName, null);
		Element res = sIdentityHandler.toElement(id, doc);
		assertNull(res);
	}

	@Test(expected=MarshalException.class)
	public void testToElementWithEmptyName() throws MarshalException {
		Document doc = sDocBuilder.newDocument();
		Identity id = Identifiers.createIdentity(IdentityType.userName, "");
		Element res = sIdentityHandler.toElement(id, doc);
		assertNull(res);
	}
	
	@Test(expected=MarshalException.class)
	public void testToElementNullType() throws MarshalException {
		Document doc = sDocBuilder.newDocument();
		Identity id = Identifiers.createIdentity(null, "username");
		Element res = sIdentityHandler.toElement(id, doc);
		assertNull(res);
	}

	/**
	 * This shouldn't show the administrative-domain
	 * @throws MarshalException 
	 */
	@Test
	public void testToElementWithEmptyAd() throws MarshalException {
		Document doc = sDocBuilder.newDocument();
		Identity id = Identifiers.createIdentity(IdentityType.userName, "USER100", "");
		
		Element res = sIdentityHandler.toElement(id, doc);
		assertNotNull(res);
		assertEquals("identity", res.getLocalName());
		assertNull(res.getPrefix());
		assertNull(res.getNamespaceURI());
		
		assertNotNull(res.getAttributeNode("name"));
		assertEquals("USER100", res.getAttribute("name"));

		assertNotNull(res.getAttributeNode("type"));
		assertEquals("username", res.getAttribute("type"));
	
		assertNull(res.getAttributeNode("administrative-domain"));
		
		assertEquals(0, res.getChildNodes().getLength());
		assertEquals(2, res.getAttributes().getLength());
	}
	
	@Test
	public void testToElementWithAd() throws MarshalException {
		Document doc = sDocBuilder.newDocument();
		Identity id = Identifiers.createIdentity(IdentityType.userName, "USER100", "mydomain");
		
		Element res = sIdentityHandler.toElement(id, doc);
		assertNotNull(res);
		assertEquals("identity", res.getLocalName());
		assertNull(res.getPrefix());
		assertNull(res.getNamespaceURI());
		
		assertNotNull(res.getAttributeNode("name"));
		assertEquals("USER100", res.getAttribute("name"));

		assertNotNull(res.getAttributeNode("type"));
		assertEquals("username", res.getAttribute("type"));
	
		assertNotNull(res.getAttributeNode("administrative-domain"));
		assertEquals("mydomain", res.getAttribute("administrative-domain"));
		
		assertEquals(0, res.getChildNodes().getLength());
		assertEquals(3, res.getAttributes().getLength());
	}
	
	@Test
	public void testToElementOtherType() throws MarshalException {
		Document doc = sDocBuilder.newDocument();
		Identity id = Identifiers.createIdentity(IdentityType.other,
				"SOMETHING_WEIRD", null, "OTHER_TYPE_DEF");
		
		Element res = sIdentityHandler.toElement(id, doc);
		assertNotNull(res);
		assertEquals("identity", res.getLocalName());
		assertNull(res.getPrefix());
		assertNull(res.getNamespaceURI());
		
		assertNotNull(res.getAttributeNode("name"));
		assertEquals("SOMETHING_WEIRD", res.getAttribute("name"));

		assertNotNull(res.getAttributeNode("type"));
		assertEquals("other", res.getAttribute("type"));
	
		assertNotNull(res.getAttributeNode("other-type-definition"));
		assertEquals("OTHER_TYPE_DEF", res.getAttribute("other-type-definition"));
		
		assertEquals(0, res.getChildNodes().getLength());
		assertEquals(3, res.getAttributes().getLength());
	}
	
	
	@Test
	public void testToElementOtherTypeWithAd() throws MarshalException {
		Document doc = sDocBuilder.newDocument();
		Identity id = Identifiers.createOtherIdentity("SOMETHING_WEIRD", "mydomain", "OTHER_TYPE_DEF");
		
		Element res = sIdentityHandler.toElement(id, doc);
		assertNotNull(res);
		assertEquals("identity", res.getLocalName());
		assertNull(res.getPrefix());
		assertNull(res.getNamespaceURI());
		
		assertNotNull(res.getAttributeNode("name"));
		assertEquals("SOMETHING_WEIRD", res.getAttribute("name"));

		assertNotNull(res.getAttributeNode("type"));
		assertEquals("other", res.getAttribute("type"));
	
		assertNotNull(res.getAttributeNode("other-type-definition"));
		assertEquals("OTHER_TYPE_DEF", res.getAttribute("other-type-definition"));

		assertNotNull(res.getAttributeNode("administrative-domain"));
		assertEquals("mydomain", res.getAttribute("administrative-domain"));
		
		assertEquals(0, res.getChildNodes().getLength());
		assertEquals(4, res.getAttributes().getLength());
	}
		
	@Test(expected=MarshalException.class)
	public void testToElementWithNullOtherTypeDef() throws MarshalException {
		Document doc = sDocBuilder.newDocument();
		Identity id = Identifiers.createIdentity(IdentityType.other, "USER100", null, null);
		Element res = sIdentityHandler.toElement(id, doc);
		assertNull(res);
	}

	@Test(expected=MarshalException.class)
	public void testToElementWithEmptyOtherTypeDef() throws MarshalException {
		Document doc = sDocBuilder.newDocument();
		Identity id = Identifiers.createIdentity(IdentityType.other, "USER100", null, "");
		Element res = sIdentityHandler.toElement(id, doc);
		assertNull(res);
	}

	@Test(expected=MarshalException.class)
	/**
	 * With not type=other we enforce other-type-definition to be not set!
	 */
	public void testToElementNotOtherTypeWithOtherTypeDef() throws MarshalException {
		Document doc = sDocBuilder.newDocument();
		Identity id = Identifiers.createIdentity(IdentityType.userName,
				"USER100", null, "OTHER_TYPE_DEF");
		
		Element res = sIdentityHandler.toElement(id, doc);
		assertNull(res);
	}

	/**
	 * Allow non-null empty other-type-definition.
	 */
	@Test
	public void testToElementNotOtherTypeWithEmptyOtherTypeDef() throws MarshalException {
		Document doc = sDocBuilder.newDocument();
		Identity id = Identifiers.createIdentity(IdentityType.userName,
				"USER100", null, "");
		
		Element res = sIdentityHandler.toElement(id, doc);
		assertNotNull(res);
	}


	@Test
	public void testFromElementWithName() throws UnmarshalException {
		Document doc = sDocBuilder.newDocument();
		Element xmlId = doc.createElementNS(null, "identity");
		
		xmlId.setAttribute("name", "USER100");
		xmlId.setAttribute("type", "username");
		
		Identity id = sIdentityHandler.fromElement(xmlId);
		assertNotNull(id);
		assertEquals("USER100", id.getName());
		assertEquals(IdentityType.userName, id.getType());
		
		assertNull(id.getAdministrativeDomain());
	}
	
	
	@Test
	public void testFromElementWithNameAndAd() throws UnmarshalException {
		Document doc = sDocBuilder.newDocument();
		Element xmlId = doc.createElementNS(null, "identity");
		
		xmlId.setAttribute("name", "USER100");
		xmlId.setAttribute("type", "username");
		xmlId.setAttribute("administrative-domain", "mydomain");
		
		Identity id = sIdentityHandler.fromElement(xmlId);
		assertNotNull(id);
		assertEquals("USER100", id.getName());
		assertEquals(IdentityType.userName, id.getType());
		assertEquals("mydomain", id.getAdministrativeDomain());
	}
	
	@Test(expected=UnmarshalException.class)
	public void testFromElementWithBadType() throws UnmarshalException {
		Document doc = sDocBuilder.newDocument();
		Element xmlId = doc.createElementNS(null, "identity");
		
		xmlId.setAttribute("name", "USER100");
		xmlId.setAttribute("type", "IP");
		
		Identity id = sIdentityHandler.fromElement(xmlId);
		assertNull(id);
	}
	
	@Test(expected=UnmarshalException.class)
	public void testFromElementWithNullName() throws UnmarshalException {
		Document doc = sDocBuilder.newDocument();
		Element xmlId = doc.createElementNS(null, "identity");
		xmlId.setAttribute("type", "username");
		
		Identity id = sIdentityHandler.fromElement(xmlId);
		assertNull(id);
	}

	@Test(expected=UnmarshalException.class)
	public void testFromElementWithEmptyName() throws UnmarshalException {
		Document doc = sDocBuilder.newDocument();
		Element xmlId = doc.createElementNS(null, "identity");
		xmlId.setAttribute("type", "username");
		xmlId.setAttribute("name", "");
		
		Identity id = sIdentityHandler.fromElement(xmlId);
		assertNull(id);
	}
	
	@Test(expected=UnmarshalException.class)
	public void testFromElementWithNullType() throws UnmarshalException {
		Document doc = sDocBuilder.newDocument();
		Element xmlId = doc.createElementNS(null, "identity");
		xmlId.setAttribute("name", "USER100");
		
		Identity id = sIdentityHandler.fromElement(xmlId);
		assertNull(id);
	}

	@Test(expected=UnmarshalException.class)
	public void testFromElementWithEmptyType() throws UnmarshalException {
		Document doc = sDocBuilder.newDocument();
		Element xmlId = doc.createElementNS(null, "identity");
		xmlId.setAttribute("name", "USER100");
		xmlId.setAttribute("type", "");
		
		Identity id = sIdentityHandler.fromElement(xmlId);
		assertNull(id);
	}

	@Test(expected=UnmarshalException.class)
	public void testFromElementWithOtherTypeNullOtherTypeDef() throws UnmarshalException {
		Document doc = sDocBuilder.newDocument();
		Element xmlId = doc.createElementNS(null, "identity");
		
		xmlId.setAttribute("name", "OTHER_NAME");
		xmlId.setAttribute("type", "other");
		
		Identity id = sIdentityHandler.fromElement(xmlId);
		assertNull(id);
	}

	@Test(expected=UnmarshalException.class)
	public void testFromElementWithOtherTypeEmptyOtherTypeDef() throws UnmarshalException {
		Document doc = sDocBuilder.newDocument();
		Element xmlId = doc.createElementNS(null, "identity");
		
		xmlId.setAttribute("name", "OTHER_NAME");
		xmlId.setAttribute("type", "other");
		xmlId.setAttribute("other-type-definition", "");
		
		Identity id = sIdentityHandler.fromElement(xmlId);
		assertNull(id);
	}

	@Test(expected=UnmarshalException.class)
	public void testFromElementWithOtherTypeDefNotOtherType() throws UnmarshalException {
		Document doc = sDocBuilder.newDocument();
		Element xmlId = doc.createElementNS(null, "identity");
		
		xmlId.setAttribute("name", "USER100");
		xmlId.setAttribute("type", "username");
		xmlId.setAttribute("other-type-definition", "OTHER_TYPE_DEF");
		
		Identity id = sIdentityHandler.fromElement(xmlId);
		assertNull(id);
	}

	@Test(expected=UnmarshalException.class)
	public void testFromElementWithOtherTypeDefAttrNotOtherType() throws UnmarshalException {
		Document doc = sDocBuilder.newDocument();
		Element xmlId = doc.createElementNS(null, "identity");
		
		xmlId.setAttribute("name", "USER100");
		xmlId.setAttribute("type", "username");
		xmlId.setAttribute("other-type-definition", "");
		
		Identity id = sIdentityHandler.fromElement(xmlId);
		assertNull(id);
	}

	@Test
	public void testNotResponsible() throws UnmarshalException {
		Document doc = sDocBuilder.newDocument();
		Element xmlId = doc.createElementNS(null, "access-request");
		
		Identity id = sIdentityHandler.fromElement(xmlId);
		assertNull(id);
	}
	
	@Test(expected=MarshalException.class)
	public void testToElementWrongIdentifierType() throws UnmarshalException, MarshalException {
		Document doc = sDocBuilder.newDocument();
		Element ret = sIdentityHandler.toElement(Identifiers.createDev("DEV"), doc);
		assertNull(ret);
	}
}
