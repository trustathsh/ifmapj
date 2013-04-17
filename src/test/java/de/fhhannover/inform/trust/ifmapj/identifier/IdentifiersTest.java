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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import util.DomHelpers;

import de.fhhannover.inform.trust.ifmapj.exception.MarshalException;
import de.fhhannover.inform.trust.ifmapj.exception.UnmarshalException;

/**
 * Test functionality provided by the {@link Identifiers} class.
 * 
 * @author aw
 *
 */
public class IdentifiersTest {

	@Test
	public void testQueryDefaultHandlers() throws MarshalException {
		
		assertNotNull(Identifiers.getHandlerFor(Identifiers.createAr("AR100")));
		assertNotNull(Identifiers.getHandlerFor(Identifiers.createDev("DEV100")));
		assertNotNull(Identifiers.getHandlerFor(Identifiers.createIdentity(
				IdentityType.userName, "USER100")));
		assertNotNull(Identifiers.getHandlerFor(Identifiers.createIp4("192.168.0.1")));
		assertNotNull(Identifiers.getHandlerFor(Identifiers.createMac("aa:bb:cc:dd:ee:ff")));
		assertNotNull(Identifiers.getHandlerFor(Identifiers.createExtendedIdentity(
				"<ns:network administrative-domain=\"\" type=\"IPv4\" " +
				"address=\"192.168.0.0\" netmask=\"255.255.255.0\" " +
				"xmlns:ns=\"http://trust.inform.fh-hannover.de/NETWORK-IDENTITY\" />")));
	}
	
	@Test
	public void testQueryUnknownHandler() {
		assertNull(Identifiers.getHandlerFor(new Identifier() { /* anonymous */}));
	}

	@Test(expected=NullPointerException.class)
	public void testRegisterNullHandler() {
		Identifiers.registerIdentifierHandler(null);
	}

	@Test(expected=RuntimeException.class)
	public void testRegisterDefaultHandlerTwice() {
		Identifiers.registerIdentifierHandler(new AccessRequestHandler());
	}

	@Test(expected=RuntimeException.class)
	public void testRegisterCustomHandlerTwice() {
		
		class CustomIdent implements Identifier { };
		class CustomIdentHandler implements IdentifierHandler<CustomIdent> {
			@Override
			public Element toElement(Identifier i, Document doc)
					throws MarshalException {
				return DomHelpers.createNonNsElement(doc, "customident");
			}

			@Override
			public CustomIdent fromElement(Element el)
					throws UnmarshalException {
				return null;
			}

			@Override
			public Class<CustomIdent> handles() {
				return CustomIdent.class;
			}
		}	
		
		Identifiers.registerIdentifierHandler(new CustomIdentHandler());
		Identifiers.registerIdentifierHandler(new CustomIdentHandler());
	}

	
	@Test
	public void testRegisterGoodHandler() {
		
		class CustomIdent implements Identifier {
			
		};
		
		class CustomIdentHandler implements IdentifierHandler<CustomIdent> {

			@Override
			public Element toElement(Identifier i, Document doc)
					throws MarshalException {
				return DomHelpers.createNonNsElement(doc, "customident");
			}

			@Override
			public CustomIdent fromElement(Element el)
					throws UnmarshalException {
				return null;
			}

			@Override
			public Class<CustomIdent> handles() {
				return CustomIdent.class;
			}
		}
		
		Identifiers.registerIdentifierHandler(new CustomIdentHandler());
		
		assertNotNull(Identifiers.getHandlerFor(new CustomIdent()));
	}
	
	
	@Test(expected=NullPointerException.class)
	public void testRegisterHandlesNullHandler() {
		
		class CustomIdent implements Identifier {
			
		};
		
		class CustomIdentHandler implements IdentifierHandler<CustomIdent> {

			@Override
			public Element toElement(Identifier i, Document doc)
					throws MarshalException {
				return null;
			}

			@Override
			public CustomIdent fromElement(Element el)
					throws UnmarshalException {
				return null;
			}

			@Override
			public Class<CustomIdent> handles() {
				//return CustomIdent.class;
				return null;
			}
			
		}
	
		// will throw NullPointers, because handles returns null
		Identifiers.registerIdentifierHandler(new CustomIdentHandler());
	}
	
	@Test
	public void testToElementNewHandler() {
		
		Document doc = DomHelpers.newDocumentBuilder().newDocument();
		
		class CustomIdent implements Identifier {
			
		};
		
		class CustomIdentHandler implements IdentifierHandler<CustomIdent> {

			@Override
			public Element toElement(Identifier i, Document doc)
					throws MarshalException {
				return DomHelpers.createNonNsElement(doc, "customident");
			}

			@Override
			public CustomIdent fromElement(Element el)
					throws UnmarshalException {
				return null;
			}

			@Override
			public Class<CustomIdent> handles() {
				return CustomIdent.class;
			}
			
		}
		
		Identifiers.registerIdentifierHandler(new CustomIdentHandler());
		
		Element element = Identifiers.tryToElement(new CustomIdent(), doc);
		
		assertEquals("customident", element.getLocalName());
	}
	
	@Test
	public void testFromElementNewHandler() throws UnmarshalException {
		
		Document doc = DomHelpers.newDocumentBuilder().newDocument();
		Element el = DomHelpers.createNonNsElement(doc, "customident");
		doc.appendChild(el);
		
		class CustomIdent implements Identifier {
			
		};
		
		class CustomIdentHandler implements IdentifierHandler<CustomIdent> {

			@Override
			public Element toElement(Identifier i, Document doc)
					throws MarshalException {
				return DomHelpers.createNonNsElement(doc, "customident");
			}

			@Override
			public CustomIdent fromElement(Element el)
					throws UnmarshalException {
				if (el.getLocalName().equals("customident"))
					return new CustomIdent();
				
				return null;
			}

			@Override
			public Class<CustomIdent> handles() {
				return CustomIdent.class;
			}
			
		}
		
		Identifiers.registerIdentifierHandler(new CustomIdentHandler());
		
		Identifier i = Identifiers.tryFromElement(el);
	
		assertNotNull(i);
		assertEquals(CustomIdent.class, i.getClass());
	}
	
	@Test
	public void testTryFromElementReturnsNull() throws UnmarshalException {
		
		Document doc = DomHelpers.newDocumentBuilder().newDocument();
		Element el = DomHelpers.createNonNsElement(doc, "customident2");
		doc.appendChild(el);
		
		Identifier i = Identifiers.tryFromElement(el);
		assertNull(i);
	}

	@Test(expected=UnmarshalException.class)
	public void testFromElementThrows() throws UnmarshalException {
		
		Document doc = DomHelpers.newDocumentBuilder().newDocument();
		Element el = DomHelpers.createNonNsElement(doc, "customident2");
		doc.appendChild(el);
		
		Identifier i = Identifiers.fromElement(el);
		assertNull(i);
	}
	
	@Test
	public void testTryToElementReturnsNull() {
		
		Document doc = DomHelpers.newDocumentBuilder().newDocument();
		class CustomIdent implements Identifier { };
		
		
		Element el = Identifiers.tryToElement(new CustomIdent(), doc);
		assertNull(el);
	}
	
	@Test(expected=MarshalException.class)
	public void testToElementThrows() throws MarshalException {
		
		Document doc = DomHelpers.newDocumentBuilder().newDocument();
		class CustomIdent implements Identifier { };
		
		Element el = Identifiers.toElement(new CustomIdent(), doc);
		assertNull(el);
	}
}
