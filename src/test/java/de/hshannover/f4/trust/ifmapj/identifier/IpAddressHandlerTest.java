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

import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.hshannover.f4.trust.ifmapj.exception.MarshalException;
import de.hshannover.f4.trust.ifmapj.exception.UnmarshalException;
import util.DomHelpers;

public class IpAddressHandlerTest {

	private static DocumentBuilder sDocBuilder = DomHelpers.newDocumentBuilder();
	private static IpAddressHandler sIpAddressHandler = new IpAddressHandler();

	@Test(expected = MarshalException.class)
	public void testToElementWithNullValue() throws MarshalException {
		Document doc = sDocBuilder.newDocument();
		IpAddress ip = Identifiers.createIp4(null);
		Element res = sIpAddressHandler.toElement(ip, doc);
		assertNull(res);
	}

	@Test(expected = MarshalException.class)
	public void testToElementWithEmptyValue() throws MarshalException {
		Document doc = sDocBuilder.newDocument();
		IpAddress ip = Identifiers.createIp4("");
		Element res = sIpAddressHandler.toElement(ip, doc);
		assertNull(res);
	}

	@Test
	public void testToElementNullTypeIpv4() throws MarshalException {
		Document doc = sDocBuilder.newDocument();

		IpAddress ip = Identifiers.createIp(null, "192.168.0.1", "");

		Element res = sIpAddressHandler.toElement(ip, doc);
		assertNotNull(res);
		assertEquals("ip-address", res.getLocalName());
		assertNull(res.getPrefix());
		assertNull(res.getNamespaceURI());

		assertNotNull(res.getAttributeNode("value"));
		assertEquals("192.168.0.1", res.getAttribute("value"));

		assertNotNull(res.getAttributeNode("type"));
		assertEquals("IPv4", res.getAttribute("type"));

		assertNull(res.getAttributeNode("administrative-domain"));

		assertEquals(0, res.getChildNodes().getLength());
		assertEquals(2, res.getAttributes().getLength());
	}

	/**
	 * This shouldn't show the administrative-domain
	 * @throws MarshalException
	 */
	@Test
	public void testToElementWithEmptyAd() throws MarshalException {
		Document doc = sDocBuilder.newDocument();
		IpAddress ip = Identifiers.createIp4("192.168.0.1", "");

		Element res = sIpAddressHandler.toElement(ip, doc);
		assertNotNull(res);
		assertEquals("ip-address", res.getLocalName());
		assertNull(res.getPrefix());
		assertNull(res.getNamespaceURI());

		assertNotNull(res.getAttributeNode("value"));
		assertEquals("192.168.0.1", res.getAttribute("value"));

		// default is IPv4
		assertNotNull(res.getAttributeNode("type"));
		assertEquals("IPv4", res.getAttribute("type"));

		assertNull(res.getAttributeNode("administrative-domain"));

		assertEquals(0, res.getChildNodes().getLength());
		assertEquals(2, res.getAttributes().getLength());
	}


	@Test
	public void testToElementWithValueIpv46() throws MarshalException {
		Document doc = sDocBuilder.newDocument();
		IpAddress ip = Identifiers.createIp(IpAddressType.IPv4, "192.168.0.1", null);

		Element res = sIpAddressHandler.toElement(ip, doc);
		assertNotNull(res);
		assertEquals("ip-address", res.getLocalName());
		assertNull(res.getPrefix());
		assertNull(res.getNamespaceURI());

		assertNotNull(res.getAttributeNode("value"));
		assertEquals("192.168.0.1", res.getAttribute("value"));

		assertNotNull(res.getAttributeNode("type"));
		assertEquals("IPv4", res.getAttribute("type"));

		assertNull(res.getAttributeNode("administrative-domain"));

		assertEquals(0, res.getChildNodes().getLength());
		assertEquals(2, res.getAttributes().getLength());

		ip = Identifiers.createIp(IpAddressType.IPv6, "192.168.0.1", null);
		// for simplicity we leave a IPv4 value, it's not checked...

		res = sIpAddressHandler.toElement(ip, doc);
		assertNotNull(res);
		assertEquals("ip-address", res.getLocalName());
		assertNull(res.getPrefix());
		assertNull(res.getNamespaceURI());

		assertNotNull(res.getAttributeNode("value"));
		assertEquals("192.168.0.1", res.getAttribute("value"));

		assertNotNull(res.getAttributeNode("type"));
		assertEquals("IPv6", res.getAttribute("type"));

		assertNull(res.getAttributeNode("administrative-domain"));

		assertEquals(0, res.getChildNodes().getLength());
		assertEquals(2, res.getAttributes().getLength());
	}

	@Test
	public void testToElementWithValueAndAdIpv46() throws MarshalException {
		Document doc = sDocBuilder.newDocument();
		IpAddress ip = Identifiers.createIp(IpAddressType.IPv4, "192.168.0.1",
				"mydomain");

		Element res = sIpAddressHandler.toElement(ip, doc);
		assertNotNull(res);
		assertEquals("ip-address", res.getLocalName());
		assertNull(res.getPrefix());
		assertNull(res.getNamespaceURI());

		assertNotNull(res.getAttributeNode("value"));
		assertEquals("192.168.0.1", res.getAttribute("value"));

		assertNotNull(res.getAttributeNode("type"));
		assertEquals("IPv4", res.getAttribute("type"));

		assertNotNull(res.getAttributeNode("administrative-domain"));
		assertEquals("mydomain", res.getAttribute("administrative-domain"));

		assertEquals(0, res.getChildNodes().getLength());
		assertEquals(3, res.getAttributes().getLength());

		// for simplicity we leave a IPv4 value, it's not checked...
		ip = Identifiers.createIp(IpAddressType.IPv6, "192.168.0.1",
				"mydomain");

		res = sIpAddressHandler.toElement(ip, doc);
		assertNotNull(res);
		assertEquals("ip-address", res.getLocalName());
		assertNull(res.getPrefix());
		assertNull(res.getNamespaceURI());

		assertNotNull(res.getAttributeNode("value"));
		assertEquals("192.168.0.1", res.getAttribute("value"));

		assertNotNull(res.getAttributeNode("type"));
		assertEquals("IPv6", res.getAttribute("type"));

		assertNotNull(res.getAttributeNode("administrative-domain"));
		assertEquals("mydomain", res.getAttribute("administrative-domain"));

		assertEquals(0, res.getChildNodes().getLength());
		assertEquals(3, res.getAttributes().getLength());
	}

	@Test
	public void testFromElementWithValueIpv46() throws UnmarshalException {
		Document doc = sDocBuilder.newDocument();
		Element xmlIp = doc.createElementNS(null, "ip-address");

		xmlIp.setAttribute("value", "192.168.0.1");
		xmlIp.setAttribute("type", "IPv4");

		IpAddress ip = sIpAddressHandler.fromElement(xmlIp);
		assertNotNull(ip);
		assertNotNull(ip.getValue());
		assertEquals("192.168.0.1", ip.getValue());
		assertEquals(IpAddressType.IPv4, ip.getType());

		assertNull(ip.getAdministrativeDomain());

		// again, leave the value IPv4, it's not checked
		xmlIp.setAttribute("type", "IPv6");
		ip = sIpAddressHandler.fromElement(xmlIp);
		assertNotNull(ip);
		assertNotNull(ip.getValue());
		assertEquals("192.168.0.1", ip.getValue());
		assertEquals(IpAddressType.IPv6, ip.getType());
		assertNull(ip.getAdministrativeDomain());

		// try no type at all, should default to IPv4
		xmlIp.removeAttribute("type");
		ip = sIpAddressHandler.fromElement(xmlIp);
		assertNotNull(ip);
		assertNotNull(ip.getValue());
		assertEquals("192.168.0.1", ip.getValue());
		assertEquals(IpAddressType.IPv4, ip.getType());
		assertNull(ip.getAdministrativeDomain());
	}

	@Test(expected = MarshalException.class)
	@Ignore // We don't have checks for this
	public void testToElementWithInvalidValueIpv4() throws MarshalException {
		Document doc = sDocBuilder.newDocument();
		IpAddress ip = Identifiers.createIp4("333.168.0.1", "");
		assertNotNull(ip);
		//assertNull(ip.getValue());

		Element res = sIpAddressHandler.toElement(ip, doc);
		assertNotNull(res);
	}

	@Test(expected = MarshalException.class)
	@Ignore // We don't have checks for this
	public void testToElementWithInvalidValueIpv6() throws MarshalException {
		Document doc = sDocBuilder.newDocument();
		IpAddress ip = Identifiers.createIp6("XX80::0202:B3YY:ZZ1E:8329", "");
		assertNotNull(ip);
		assertNull(ip.getValue());

		Element res = sIpAddressHandler.toElement(ip, doc);
		assertNull(res);
	}

	@Test
	@Ignore // We don't do canocalization in (in favor of clients)
	public void testToElementWithCanonicalizationIpv4() throws MarshalException {
		Document doc = sDocBuilder.newDocument();
		IpAddress ip = Identifiers.createIp4("192.168.000.001", "");
		assertNotNull(ip);
		assertNotNull(ip.getValue());
		assertEquals(ip.getValue(), "192.168.0.1");

		Element res = sIpAddressHandler.toElement(ip, doc);
		assertNotNull(res);
		assertEquals("ip-address", res.getLocalName());
		assertNull(res.getPrefix());
		assertNull(res.getNamespaceURI());

		assertNotNull(res.getAttributeNode("value"));
		assertEquals("192.168.0.1", res.getAttribute("value"));

		assertNotNull(res.getAttributeNode("type"));
		assertEquals("IPv4", res.getAttribute("type"));

		assertNull(res.getAttributeNode("administrative-domain"));

		assertEquals(0, res.getChildNodes().getLength());
		assertEquals(2, res.getAttributes().getLength());
	}

	@Test
	@Ignore // We don't do canocilaziation (in favor of clients)
	public void testToElementWithCanonicalizationIpv6() throws MarshalException {
		Document doc = sDocBuilder.newDocument();
		IpAddress ip = Identifiers.createIp6("FE80::0202:B3FF:FE1E:8329");
		assertNotNull(ip);
		assertNotNull(ip.getValue());
		assertEquals(ip.getValue(), "fe80:0:0:0:202:b3ff:fe1e:8329");

		Element res = sIpAddressHandler.toElement(ip, doc);
		assertNotNull(res);
		assertEquals("ip-address", res.getLocalName());
		assertNull(res.getPrefix());
		assertNull(res.getNamespaceURI());

		assertNotNull(res.getAttributeNode("value"));
		assertEquals("fe80:0:0:0:202:b3ff:fe1e:8329", res.getAttribute("value"));

		assertNotNull(res.getAttributeNode("type"));
		assertEquals("IPv6", res.getAttribute("type"));

		assertNull(res.getAttributeNode("administrative-domain"));

		assertEquals(0, res.getChildNodes().getLength());
		assertEquals(2, res.getAttributes().getLength());
	}

	@Test(expected = UnmarshalException.class)
	public void testFromElementWithBadType() throws UnmarshalException {
		Document doc = sDocBuilder.newDocument();
		Element xmlIp = doc.createElementNS(null, "ip-address");
		xmlIp.setAttribute("value", "192.168.0.1");
		xmlIp.setAttribute("type", "IPxx");
		IpAddress ip = sIpAddressHandler.fromElement(xmlIp);
		assertNull(ip);
	}

	@Test
	public void testFromElementWithValueAndAdIpv46() throws UnmarshalException {
		Document doc = sDocBuilder.newDocument();
		Element xmlIp = doc.createElementNS(null, "ip-address");

		xmlIp.setAttribute("value", "192.168.0.1");
		xmlIp.setAttribute("type", "IPv4");
		xmlIp.setAttribute("administrative-domain", "mydomain");

		IpAddress ip = sIpAddressHandler.fromElement(xmlIp);
		assertNotNull(ip);
		assertNotNull(ip.getValue());
		assertEquals("192.168.0.1", ip.getValue());
		assertEquals(IpAddressType.IPv4, ip.getType());
		assertEquals("mydomain", ip.getAdministrativeDomain());

		// again, leave the value IPv4, it's not checked
		xmlIp.setAttribute("type", "IPv6");
		ip = sIpAddressHandler.fromElement(xmlIp);
		assertNotNull(ip);
		assertNotNull(ip.getValue());
		assertEquals("192.168.0.1", ip.getValue());
		assertEquals(IpAddressType.IPv6, ip.getType());
		assertEquals("mydomain", ip.getAdministrativeDomain());

		// try no type at all, should default to IPv4
		xmlIp.removeAttribute("type");
		ip = sIpAddressHandler.fromElement(xmlIp);
		assertNotNull(ip);
		assertNotNull(ip.getValue());
		assertEquals("192.168.0.1", ip.getValue());
		assertEquals(IpAddressType.IPv4, ip.getType());
		assertEquals("mydomain", ip.getAdministrativeDomain());
	}

	@Test(expected = UnmarshalException.class)
	public void testFromElementNullValue() throws UnmarshalException {
		Document doc = sDocBuilder.newDocument();
		Element xmlIp = doc.createElementNS(null, "ip-address");
		IpAddress ip = sIpAddressHandler.fromElement(xmlIp);
		assertNull(ip);
	}

	@Test(expected = UnmarshalException.class)
	public void testFromElementEmptyValue() throws UnmarshalException {
		Document doc = sDocBuilder.newDocument();
		Element xmlIp = doc.createElementNS(null, "ip-address");
		xmlIp.setAttribute("value", "");
		IpAddress ip = sIpAddressHandler.fromElement(xmlIp);
		assertNull(ip);
	}

	@Test
	public void testNotResponsible() throws UnmarshalException {
		Document doc = sDocBuilder.newDocument();
		Element xmlIp = doc.createElementNS(null, "access-request");

		IpAddress ip = sIpAddressHandler.fromElement(xmlIp);
		assertNull(ip);
	}

	@Test(expected = MarshalException.class)
	public void testToElementWrongIdentifierType() throws UnmarshalException, MarshalException {
		Document doc = sDocBuilder.newDocument();
		Element ret = sIpAddressHandler.toElement(Identifiers.createDev("DEV100"), doc);
		assertNull(ret);
	}
}
