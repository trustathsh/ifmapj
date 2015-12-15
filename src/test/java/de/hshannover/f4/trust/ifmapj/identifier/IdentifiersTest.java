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
 * This file is part of ifmapj, version 2.3.1, implemented by the Trust@HsH
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.hshannover.f4.trust.ifmapj.exception.MarshalException;
import de.hshannover.f4.trust.ifmapj.exception.UnmarshalException;
import util.DomHelpers;

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
				"<ns:network administrative-domain=\"\" type=\"IPv4\" "
				+ "address=\"192.168.0.0\" netmask=\"255.255.255.0\" "
				+ "xmlns:ns=\"http://trust.inform.fh-hannover.de/NETWORK-IDENTITY\" />")));
	}

	@Test
	public void testQueryUnknownHandler() {
		assertNull(Identifiers.getHandlerFor(new Identifier() { /* anonymous */ }));
	}

	@Test(expected = NullPointerException.class)
	public void testRegisterNullHandler() {
		Identifiers.registerIdentifierHandler(null);
	}

	@Test(expected = RuntimeException.class)
	public void testRegisterDefaultHandlerTwice() {
		Identifiers.registerIdentifierHandler(new AccessRequestHandler());
	}

	@Test(expected = RuntimeException.class)
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


	@Test(expected = NullPointerException.class)
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

		class CustomIdentToElement implements Identifier {

		};

		class CustomIdentHandlerToElement implements IdentifierHandler<CustomIdentToElement> {

			@Override
			public Element toElement(Identifier i, Document doc)
					throws MarshalException {
				return DomHelpers.createNonNsElement(doc, "customidenttoelement");
			}

			@Override
			public CustomIdentToElement fromElement(Element el)
					throws UnmarshalException {
				return null;
			}

			@Override
			public Class<CustomIdentToElement> handles() {
				return CustomIdentToElement.class;
			}

		}

		Identifiers.registerIdentifierHandler(new CustomIdentHandlerToElement());

		Element element = Identifiers.tryToElement(new CustomIdentToElement(), doc);

		assertEquals("customidenttoelement", element.getLocalName());
	}

	@Test
	public void testFromElementNewHandler() throws UnmarshalException {

		Document doc = DomHelpers.newDocumentBuilder().newDocument();
		Element el = DomHelpers.createNonNsElement(doc, "customidentfromElement");
		doc.appendChild(el);

		class CustomIdentFromElement implements Identifier {

		};

		class CustomIdentHandlerFromElement implements IdentifierHandler<CustomIdentFromElement> {

			@Override
			public Element toElement(Identifier i, Document doc)
					throws MarshalException {
				return DomHelpers.createNonNsElement(doc, "customidentfromElement");
			}

			@Override
			public CustomIdentFromElement fromElement(Element el)
					throws UnmarshalException {
				if (el.getLocalName().equals("customidentfromElement")) {
					return new CustomIdentFromElement();
				}

				return null;
			}

			@Override
			public Class<CustomIdentFromElement> handles() {
				return CustomIdentFromElement.class;
			}

		}

		Identifiers.registerIdentifierHandler(new CustomIdentHandlerFromElement());

		Identifier i = Identifiers.tryFromElement(el);

		assertNotNull(i);
		assertEquals(CustomIdentFromElement.class, i.getClass());
	}

	@Test
	public void testTryFromElementReturnsNull() throws UnmarshalException {

		Document doc = DomHelpers.newDocumentBuilder().newDocument();
		Element el = DomHelpers.createNonNsElement(doc, "customident2");
		doc.appendChild(el);

		Identifier i = Identifiers.tryFromElement(el);
		assertNull(i);
	}

	@Test(expected = UnmarshalException.class)
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

	@Test(expected = MarshalException.class)
	public void testToElementThrows() throws MarshalException {

		Document doc = DomHelpers.newDocumentBuilder().newDocument();
		class CustomIdent implements Identifier { };

		Element el = Identifiers.toElement(new CustomIdent(), doc);
		assertNull(el);
	}

	@Test
	public void testEqualsContractForMac() {
		Identifier mac1 = Identifiers.createMac("ee:ee:ee:ee:ee:ee");
		Identifier mac2 = Identifiers.createMac("ee:ee:ee:ee:ee:ee");
		Identifier mac3 = Identifiers.createMac("ee:ee:ee:ee:ee:ee");
		Identifier macX = Identifiers.createMac("11:11:11:11:11:11");
		// reflexive
		assertTrue(mac1.equals(mac1));
		// symmetric
		assertTrue(mac1.equals(mac3) && mac3.equals(mac1));
		// transitive
		assertTrue(mac1.equals(mac3) && mac3.equals(mac2) && mac1.equals(mac2));
		// negative
		assertFalse(mac1.equals(macX));
		assertFalse(macX.equals(mac1));
		// hashcode
		assertTrue(mac1.hashCode() == mac3.hashCode());
		assertFalse(mac1.hashCode() == macX.hashCode());
	}

	@Test
	public void testEqualsContractForIpv4() {
		Identifier ipv41 = Identifiers.createIp4("10.1.1.1");
		Identifier ipv42 = Identifiers.createIp4("10.1.1.1");
		Identifier ipv43 = Identifiers.createIp4("10.1.1.1");
		Identifier ipv4X = Identifiers.createIp4("8.8.8.8");
		// reflexive
		assertTrue(ipv41.equals(ipv41));
		// symmetric
		assertTrue(ipv41.equals(ipv43) && ipv43.equals(ipv41));
		// transitive
		assertTrue(ipv41.equals(ipv43) && ipv43.equals(ipv42) && ipv41.equals(ipv42));
		// negative
		assertFalse(ipv41.equals(ipv4X));
		assertFalse(ipv4X.equals(ipv41));
		// hashcode
		assertTrue(ipv41.hashCode() == ipv43.hashCode());
		assertFalse(ipv41.hashCode() == ipv4X.hashCode());
	}

	@Test
	public void testEqualsContractForIpv6() {
		Identifier ipv61 = Identifiers.createIp6("fe80::a64e:31ff:feb5:17c");
		Identifier ipv62 = Identifiers.createIp6("fe80::a64e:31ff:feb5:17c");
		Identifier ipv63 = Identifiers.createIp6("fe80::a64e:31ff:feb5:17c");
		Identifier ipv6X = Identifiers.createIp6("fe80::a64e:eeee:eeee:eeee");
		// reflexive
		assertTrue(ipv61.equals(ipv61));
		// symmetric
		assertTrue(ipv61.equals(ipv63) && ipv63.equals(ipv61));
		// transitive
		assertTrue(ipv61.equals(ipv63) && ipv63.equals(ipv62) && ipv61.equals(ipv62));
		// negative
		assertFalse(ipv61.equals(ipv6X));
		assertFalse(ipv6X.equals(ipv61));
		// hashcode
		assertTrue(ipv61.hashCode() == ipv63.hashCode());
		assertFalse(ipv61.hashCode() == ipv6X.hashCode());
	}

	@Test
	public void testEqualsContractForDev() {
		Identifier dev1 = Identifiers.createDev("pdp");
		Identifier dev2 = Identifiers.createDev("pdp");
		Identifier dev3 = Identifiers.createDev("pdp");
		Identifier devX = Identifiers.createDev("pdp-42");
		// reflexive
		assertTrue(dev1.equals(dev1));
		// symmetric
		assertTrue(dev1.equals(dev3) && dev3.equals(dev1));
		// transitive
		assertTrue(dev1.equals(dev3) && dev3.equals(dev2) && dev1.equals(dev2));
		// negative
		assertFalse(dev1.equals(devX));
		assertFalse(devX.equals(dev1));
		// hashcode
		assertTrue(dev1.hashCode() == dev3.hashCode());
		assertFalse(dev1.hashCode() == devX.hashCode());
	}

	@Test
	public void testEqualsContractForAccessRequest() {
		Identifier ar1 = Identifiers.createAr("111:33");
		Identifier ar2 = Identifiers.createAr("111:33");
		Identifier ar3 = Identifiers.createAr("111:33");
		Identifier arX = Identifiers.createAr("42");
		// reflexive
		assertTrue(ar1.equals(ar1));
		// symmetric
		assertTrue(ar1.equals(ar3) && ar3.equals(ar1));
		// transitive
		assertTrue(ar1.equals(ar3) && ar3.equals(ar2) && ar1.equals(ar2));
		// negative
		assertFalse(ar1.equals(arX));
		assertFalse(arX.equals(ar1));
		// hashcode
		assertTrue(ar1.hashCode() == ar3.hashCode());
		assertFalse(ar1.hashCode() == arX.hashCode());
	}

	@Test
	public void testEqualsContractForIdentity() {
		Identifier id1 = Identifiers.createIdentity(IdentityType.userName, "john.doe");
		Identifier id2 = Identifiers.createIdentity(IdentityType.userName, "john.doe");
		Identifier id3 = Identifiers.createIdentity(IdentityType.userName, "john.doe");
		Identifier idX = Identifiers.createIdentity(IdentityType.userName, "peter.pan");
		// reflexive
		assertTrue(id1.equals(id1));
		// symmetric
		assertTrue(id1.equals(id3) && id3.equals(id1));
		// transitive
		assertTrue(id1.equals(id3) && id3.equals(id2) && id1.equals(id2));
		// negative
		assertFalse(id1.equals(idX));
		assertFalse(idX.equals(id1));
		// hashcode
		assertTrue(id1.hashCode() == id3.hashCode());
		assertFalse(id1.hashCode() == idX.hashCode());
	}

	public void testUnEqualOfDifferentIdentifierTypes() {
		Identifier mac = Identifiers.createMac("ee:ee:ee:ee:ee:ee");
		Identifier ipv4 = Identifiers.createIp4("10.1.1.1");
		Identifier ipv6 = Identifiers.createIp6("fe80::a64e:31ff:feb5:17c");
		Identifier dev = Identifiers.createDev("pdp");
		Identifier ar = Identifiers.createAr("111:33");
		Identifier id = Identifiers.createIdentity(IdentityType.userName, "john.doe");

		assertFalse(mac.equals(ipv4));
		assertFalse(mac.equals(ipv6));
		assertFalse(mac.equals(dev));
		assertFalse(mac.equals(ar));
		assertFalse(mac.equals(id));

		assertFalse(ipv4.equals(mac));
		assertFalse(ipv4.equals(ipv6));
		assertFalse(ipv4.equals(dev));
		assertFalse(ipv4.equals(ar));
		assertFalse(ipv4.equals(id));

		assertFalse(ipv6.equals(ipv4));
		assertFalse(ipv6.equals(mac));
		assertFalse(ipv6.equals(dev));
		assertFalse(ipv6.equals(ar));
		assertFalse(ipv6.equals(id));

		assertFalse(dev.equals(ipv4));
		assertFalse(dev.equals(ipv6));
		assertFalse(dev.equals(mac));
		assertFalse(dev.equals(ar));
		assertFalse(dev.equals(id));

		assertFalse(ar.equals(ipv4));
		assertFalse(ar.equals(ipv6));
		assertFalse(ar.equals(dev));
		assertFalse(ar.equals(mac));
		assertFalse(ar.equals(id));

		assertFalse(id.equals(ipv4));
		assertFalse(id.equals(ipv6));
		assertFalse(id.equals(dev));
		assertFalse(id.equals(ar));
		assertFalse(id.equals(mac));
	}

}
