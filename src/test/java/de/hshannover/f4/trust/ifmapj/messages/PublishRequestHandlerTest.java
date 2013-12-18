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
 * Website: http://trust.f4.hs-hannover.de
 * 
 * This file is part of ifmapj, version 1.0.0, implemented by the Trust@HsH
 * research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2010 - 2013 Trust@HsH
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
package de.hshannover.f4.trust.ifmapj.messages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.xml.parsers.DocumentBuilder;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import util.DomHelpers;
import de.hshannover.f4.trust.ifmapj.exception.IfmapErrorResult;
import de.hshannover.f4.trust.ifmapj.exception.MarshalException;
import de.hshannover.f4.trust.ifmapj.exception.UnmarshalException;
import de.hshannover.f4.trust.ifmapj.identifier.Identifier;
import de.hshannover.f4.trust.ifmapj.identifier.IpAddressType;
import de.hshannover.f4.trust.ifmapj.messages.MetadataLifetime;
import de.hshannover.f4.trust.ifmapj.messages.PublishElement;
import de.hshannover.f4.trust.ifmapj.messages.PublishRequest;
import de.hshannover.f4.trust.ifmapj.messages.PublishRequestHandler;
import de.hshannover.f4.trust.ifmapj.messages.PublishRequestImpl;
import de.hshannover.f4.trust.ifmapj.messages.PublishUpdate;
import de.hshannover.f4.trust.ifmapj.messages.Request;
import de.hshannover.f4.trust.ifmapj.messages.RequestHandler;
import de.hshannover.f4.trust.ifmapj.messages.Result;
import de.hshannover.f4.trust.ifmapj.messages.SubscribeRequestImpl;

public class PublishRequestHandlerTest {

	private static RequestHandler<? extends Request> sHandler = makeHandler();
	private static DocumentBuilder sDocBuilder = DomHelpers.newDocumentBuilder();
	private static final String REQ_EL_NAME = "publish";
	private static final String RES_EL_NAME = "publishReceived";
	private static final String IFMAP_URI = "http://www.trustedcomputinggroup.org/2010/IFMAP/2";

	private static PublishRequest makeRequest() {
		return new PublishRequestImpl();
	}

	private static PublishRequest makeSimpleRequest() {
		PublishRequest ret = makeRequest();
		PublishElement update = TestHelpers.publishUpdate(
				TestHelpers.ip("192.168.0.1", IpAddressType.IPv4, null),
				null,
				TestHelpers.simpleMetadata(sDocBuilder, "simpleMetadata", "singleValue"));
		ret.addPublishElement(update);
		return ret;
	}

	private static RequestHandler<? extends Request> makeHandler() {
		return new PublishRequestHandler();
	}

	@Test
	public void testToElementGood() throws MarshalException {
		Request req = makeSimpleRequest();
		req.setSessionId("1234");
		Document doc = sDocBuilder.newDocument();
		Element ret = sHandler.toElement(req, doc);
		assertNotNull(ret);
		assertEquals(REQ_EL_NAME, ret.getLocalName());
		assertEquals(IFMAP_URI, ret.getNamespaceURI());
		assertEquals(1, ret.getAttributes().getLength());
		assertEquals("1234", ret.getAttribute("session-id"));
		assertEquals(1, ret.getChildNodes().getLength());
		Element updateEl = (Element) ret.getChildNodes().item(0);
		assertEquals("update", updateEl.getLocalName());
		assertNull(updateEl.getNamespaceURI());
		assertNotNull(updateEl.getAttributeNode("lifetime"));
		assertEquals("session", updateEl.getAttribute("lifetime"));
		assertEquals(2, updateEl.getChildNodes().getLength());
		assertEquals(1, updateEl.getElementsByTagNameNS(null, "metadata").getLength());
		Element md = (Element) updateEl.getElementsByTagNameNS(null, "metadata").item(0);
		assertEquals(1, md.getChildNodes().getLength());
		Element mdEl = (Element) md.getChildNodes().item(0);
		assertEquals("simpleMetadata", mdEl.getLocalName());
		assertEquals(1, updateEl.getElementsByTagNameNS(null, "ip-address").getLength());
	}

	@Test(expected=MarshalException.class)
	public void testToElementNullSessionId() throws MarshalException {
		Request req = makeSimpleRequest();
		Document doc = sDocBuilder.newDocument();
		Element ret = sHandler.toElement(req, doc);
		assertNull(ret);
	}

	@Test(expected=MarshalException.class)
	public void testToElementEmptySessionId() throws MarshalException {
		Request req = makeSimpleRequest();
		req.setSessionId("");
		Document doc = sDocBuilder.newDocument();
		Element ret = sHandler.toElement(req, doc);
		assertNull(ret);
	}

	@Test(expected=MarshalException.class)
	public void testToElementWrongType() throws MarshalException {
		Request req = new SubscribeRequestImpl();
		req.setSessionId("1234");
		Document doc = sDocBuilder.newDocument();
		Element ret = sHandler.toElement(req, doc);
		assertNull(ret);
	}

	@Test(expected=MarshalException.class)
	public void testToElementNoPublishElements() throws MarshalException {
		Request req = makeRequest();
		req.setSessionId("1234");
		Document doc = sDocBuilder.newDocument();
		Element ret = sHandler.toElement(req, doc);
		assertNull(ret);
	}

	@Test(expected=MarshalException.class)
	public void testToElementUpdateNoMetadata() throws MarshalException {
		PublishRequest req = makeRequest();
		PublishElement pe = TestHelpers.publishUpdate(
				TestHelpers.mac("aa:bb:cc:dd:ee:ff", null), null);

		req.addPublishElement(pe);

		req.setSessionId("1234");
		Document doc = sDocBuilder.newDocument();
		Element ret = sHandler.toElement(req, doc);
		assertNull(ret);
	}

	@Test(expected=MarshalException.class)
	public void testToElementNotifyMetadata() throws MarshalException {
		PublishRequest req = makeRequest();
		PublishElement pe = TestHelpers.publishNotify(
				TestHelpers.mac("aa:bb:cc:dd:ee:ff", null), null);

		req.addPublishElement(pe);

		req.setSessionId("1234");
		Document doc = sDocBuilder.newDocument();
		Element ret = sHandler.toElement(req, doc);
		assertNull(ret);
	}

	@Test
	public void testFromElementGood() throws UnmarshalException, IfmapErrorResult {
		Document doc = sDocBuilder.newDocument();
		Element response = doc.createElementNS(IFMAP_URI, "ifmap:response");
		Element result = doc.createElementNS(null, RES_EL_NAME);
		response.appendChild(result);
		Result res = sHandler.fromElement(response);
		assertNull(res);	// This handler returns null on success
	}

	@Test(expected=IfmapErrorResult.class)
	public void testFromElementWithErrorResult() throws IfmapErrorResult, UnmarshalException {
		Document doc = sDocBuilder.newDocument();
		Element response = doc.createElementNS(IFMAP_URI, "ifmap:response");
		Element result = doc.createElementNS(null, "errorResult");
		result.setAttribute("errorCode", "SystemError");
		response.appendChild(result);
		Result res = sHandler.fromElement(response);
		assertNull(res);
	}

	@Test(expected=UnmarshalException.class)
	public void testFromElementNoResult() throws IfmapErrorResult, UnmarshalException {
		Document doc = sDocBuilder.newDocument();
		Element response = doc.createElementNS(IFMAP_URI, "ifmap:response");
		Result res = sHandler.fromElement(response);
		assertNull(res);
	}

	@Test(expected=UnmarshalException.class)
	public void testFromElementWrongResult() throws IfmapErrorResult, UnmarshalException {
		Document doc = sDocBuilder.newDocument();
		Element response = doc.createElementNS(IFMAP_URI, "ifmap:response");
		Element result = doc.createElementNS(null, "subscribReceived");
		response.appendChild(result);
		Result res = sHandler.fromElement(response);
		assertNull(res);
	}

	@Test
	public void testToElementUpdateSessionLifetime() throws MarshalException {
		PublishRequest req = makeSimpleRequest();
		PublishUpdate up = (PublishUpdate) req.getPublishElements().get(0);
		up.setLifeTime(MetadataLifetime.session);
		req.setSessionId("1234");
		Document doc = sDocBuilder.newDocument();
		Element ret = sHandler.toElement(req, doc);
		assertNotNull(ret);
		assertEquals(REQ_EL_NAME, ret.getLocalName());
		assertEquals(IFMAP_URI, ret.getNamespaceURI());
		assertEquals(1, ret.getAttributes().getLength());
		assertEquals("1234", ret.getAttribute("session-id"));
		assertEquals(1, ret.getChildNodes().getLength());
		Element updateEl = (Element) ret.getChildNodes().item(0);
		assertEquals("update", updateEl.getLocalName());
		assertNull(updateEl.getNamespaceURI());
		assertNotNull(updateEl.getAttributeNode("lifetime"));
		assertEquals("session", updateEl.getAttribute("lifetime"));
	}

	@Test
	public void testToElementUpdateForeverLifetime() throws MarshalException {
		PublishRequest req = makeSimpleRequest();
		PublishUpdate up = (PublishUpdate) req.getPublishElements().get(0);
		up.setLifeTime(MetadataLifetime.forever);
		req.setSessionId("1234");
		Document doc = sDocBuilder.newDocument();
		Element ret = sHandler.toElement(req, doc);
		assertNotNull(ret);
		assertEquals(REQ_EL_NAME, ret.getLocalName());
		assertEquals(IFMAP_URI, ret.getNamespaceURI());
		assertEquals(1, ret.getAttributes().getLength());
		assertEquals("1234", ret.getAttribute("session-id"));
		assertEquals(1, ret.getChildNodes().getLength());
		Element updateEl = (Element) ret.getChildNodes().item(0);
		assertEquals("update", updateEl.getLocalName());
		assertNull(updateEl.getNamespaceURI());
		assertNotNull(updateEl.getAttributeNode("lifetime"));
		assertEquals("forever", updateEl.getAttribute("lifetime"));
	}

	/**
	 * Null should default to session lifetime
	 * @throws MarshalException
	 */
	@Test
	public void testToElementUpdateNullLifetime() throws MarshalException {
		PublishRequest req = makeSimpleRequest();
		PublishUpdate up = (PublishUpdate) req.getPublishElements().get(0);
		assertTrue(up.getLifeTime() == null);
		req.setSessionId("1234");
		Document doc = sDocBuilder.newDocument();
		Element ret = sHandler.toElement(req, doc);
		assertNotNull(ret);
		assertEquals(REQ_EL_NAME, ret.getLocalName());
		assertEquals(IFMAP_URI, ret.getNamespaceURI());
		assertEquals(1, ret.getAttributes().getLength());
		assertEquals("1234", ret.getAttribute("session-id"));
		assertEquals(1, ret.getChildNodes().getLength());
		Element updateEl = (Element) ret.getChildNodes().item(0);
		assertEquals("update", updateEl.getLocalName());
		assertNull(updateEl.getNamespaceURI());
		assertNotNull(updateEl.getAttributeNode("lifetime"));
		assertEquals("session", updateEl.getAttribute("lifetime"));
	}

	@Test
	public void testToElementUpdateSingleIdentifier() throws MarshalException {
		PublishRequest req = makeRequest();

		PublishElement pe = TestHelpers.publishUpdate(
				TestHelpers.mac("aa:bb:cc:dd:ee:ff", null),
				null,
				TestHelpers.simpleMetadata(sDocBuilder, "mymeta", "singleValue"));
		req.addPublishElement(pe);

		req.setSessionId("1234");
		Document doc = sDocBuilder.newDocument();
		Element ret = sHandler.toElement(req, doc);
		assertNotNull(ret);
		assertEquals(REQ_EL_NAME, ret.getLocalName());
		assertEquals(IFMAP_URI, ret.getNamespaceURI());
		assertEquals(1, ret.getAttributes().getLength());
		assertEquals("1234", ret.getAttribute("session-id"));
		assertEquals(1, ret.getChildNodes().getLength());

		Element updateEl = (Element) ret.getChildNodes().item(0);
		assertEquals("update", updateEl.getLocalName());
		assertNull(updateEl.getNamespaceURI());
		assertNotNull(updateEl.getAttributeNode("lifetime"));
		assertEquals("session", updateEl.getAttribute("lifetime"));
		NodeList children = updateEl.getChildNodes();
		assertEquals(2, children.getLength());
		assertEquals("mac-address", children.item(0).getLocalName());
		Element metadata = (Element) children.item(1);
		assertEquals("metadata", metadata.getLocalName());
		assertEquals(1, metadata.getChildNodes().getLength());
		assertEquals("mymeta", metadata.getChildNodes().item(0).getLocalName());
	}

	@Test
	public void testToElementUpdateTwoIdentifiers() throws MarshalException {
		PublishRequest req = makeRequest();

		PublishElement pe = TestHelpers.publishUpdate(
				TestHelpers.mac("aa:bb:cc:dd:ee:ff", null),
				TestHelpers.ip("192.168.0.1", IpAddressType.IPv4, null),
				TestHelpers.simpleMetadata(sDocBuilder, "mymeta", "singleValue"),
				TestHelpers.simpleMetadata(sDocBuilder, "mymeta2", "singleValue"));
		req.addPublishElement(pe);

		req.setSessionId("1234");
		Document doc = sDocBuilder.newDocument();
		Element ret = sHandler.toElement(req, doc);
		assertNotNull(ret);
		assertEquals(REQ_EL_NAME, ret.getLocalName());
		assertEquals(IFMAP_URI, ret.getNamespaceURI());
		assertEquals(1, ret.getAttributes().getLength());
		assertEquals("1234", ret.getAttribute("session-id"));
		assertEquals(1, ret.getChildNodes().getLength());

		Element updateEl = (Element) ret.getChildNodes().item(0);
		assertEquals("update", updateEl.getLocalName());
		assertNull(updateEl.getNamespaceURI());
		assertNotNull(updateEl.getAttributeNode("lifetime"));
		assertEquals("session", updateEl.getAttribute("lifetime"));
		NodeList children = updateEl.getChildNodes();
		assertEquals(3, children.getLength());
		assertEquals("mac-address", children.item(0).getLocalName());
		assertEquals("ip-address", children.item(1).getLocalName());
		Element metadata = (Element) children.item(2);
		assertEquals("metadata", metadata.getLocalName());
		assertEquals(2, metadata.getChildNodes().getLength());
		assertEquals("mymeta", metadata.getChildNodes().item(0).getLocalName());
		assertEquals("mymeta2", metadata.getChildNodes().item(1).getLocalName());
	}

	@Test
	public void testToElementDeleteWithFilter() throws MarshalException {
		PublishRequest req = makeRequest();
		Identifier i1 = TestHelpers.ip("192.168.0.1", IpAddressType.IPv4, null);
		Identifier i2 = TestHelpers.mac("aa:bb:cc:dd:ee:ff", null);
		req.addPublishElement(TestHelpers.publishDelete(i1, i2, "myfilter"));
		req.setSessionId("1234");
		Document doc = sDocBuilder.newDocument();
		Element ret = sHandler.toElement(req, doc);
		assertNotNull(ret);
		assertEquals(REQ_EL_NAME, ret.getLocalName());
		assertEquals(IFMAP_URI, ret.getNamespaceURI());
		assertEquals(1, ret.getAttributes().getLength());
		assertEquals("1234", ret.getAttribute("session-id"));
		assertEquals(1, ret.getChildNodes().getLength());
		Element deleteEl = (Element) ret.getChildNodes().item(0);
		assertEquals("delete", deleteEl.getLocalName());
		assertNull(deleteEl.getNamespaceURI());
		assertNull(deleteEl.getAttributeNode("lifetime"));
		assertNotNull(deleteEl.getAttributeNode("filter"));
		assertEquals("myfilter", deleteEl.getAttribute("filter"));
		assertEquals(2, deleteEl.getChildNodes().getLength());
	}

	@Test
	public void testToElementDeleteEmptyFilter() throws MarshalException {
		PublishRequest req = makeRequest();
		Identifier i1 = TestHelpers.ip("192.168.0.1", IpAddressType.IPv4, null);
		Identifier i2 = TestHelpers.mac("aa:bb:cc:dd:ee:ff", null);
		req.addPublishElement(TestHelpers.publishDelete(i1, i2, ""));
		req.setSessionId("1234");
		Document doc = sDocBuilder.newDocument();
		Element ret = sHandler.toElement(req, doc);
		assertNotNull(ret);
		assertEquals(REQ_EL_NAME, ret.getLocalName());
		assertEquals(IFMAP_URI, ret.getNamespaceURI());
		assertEquals(1, ret.getAttributes().getLength());
		assertEquals("1234", ret.getAttribute("session-id"));
		assertEquals(1, ret.getChildNodes().getLength());
		Element deleteEl = (Element) ret.getChildNodes().item(0);
		assertEquals("delete", deleteEl.getLocalName());
		assertNull(deleteEl.getNamespaceURI());
		assertNull(deleteEl.getAttributeNode("lifetime"));
		assertNotNull(deleteEl.getAttributeNode("filter"));
		assertEquals("", deleteEl.getAttribute("filter"));
		assertEquals(2, deleteEl.getChildNodes().getLength());
	}

	@Test
	public void testToElementDeleteNullFilter() throws MarshalException {
		PublishRequest req = makeRequest();
		Identifier i1 = TestHelpers.ip("192.168.0.1", IpAddressType.IPv4, null);
		Identifier i2 = TestHelpers.mac("aa:bb:cc:dd:ee:ff", null);
		req.addPublishElement(TestHelpers.publishDelete(i1, i2, null));
		req.setSessionId("1234");
		Document doc = sDocBuilder.newDocument();
		Element ret = sHandler.toElement(req, doc);
		assertNotNull(ret);
		assertEquals(REQ_EL_NAME, ret.getLocalName());
		assertEquals(IFMAP_URI, ret.getNamespaceURI());
		assertEquals(1, ret.getAttributes().getLength());
		assertEquals("1234", ret.getAttribute("session-id"));
		assertEquals(1, ret.getChildNodes().getLength());
		Element deleteEl = (Element) ret.getChildNodes().item(0);
		assertEquals("delete", deleteEl.getLocalName());
		assertNull(deleteEl.getNamespaceURI());
		assertNull(deleteEl.getAttributeNode("lifetime"));
		assertNull(deleteEl.getAttributeNode("filter"));
	}

	@Test
	public void testToElementDeleteTwoIdentifiers() throws MarshalException {
		PublishRequest req = makeRequest();
		Identifier i1 = TestHelpers.ip("192.168.0.1", IpAddressType.IPv4, null);
		Identifier i2 = TestHelpers.mac("aa:bb:cc:dd:ee:ff", null);
		req.addPublishElement(TestHelpers.publishDelete(i1, i2, "myfilter"));
		req.setSessionId("1234");
		Document doc = sDocBuilder.newDocument();
		Element ret = sHandler.toElement(req, doc);
		assertNotNull(ret);
		assertEquals(REQ_EL_NAME, ret.getLocalName());
		assertEquals(IFMAP_URI, ret.getNamespaceURI());
		assertEquals(1, ret.getAttributes().getLength());
		assertEquals("1234", ret.getAttribute("session-id"));
		assertEquals(1, ret.getChildNodes().getLength());
		Element deleteEl = (Element) ret.getChildNodes().item(0);
		assertEquals("delete", deleteEl.getLocalName());
		assertNull(deleteEl.getNamespaceURI());
		assertEquals(2, deleteEl.getChildNodes().getLength());
		NodeList idents = deleteEl.getChildNodes();
		assertEquals("ip-address", idents.item(0).getLocalName());
		assertEquals("mac-address", idents.item(1).getLocalName());
	}

	@Test
	public void testToElementDeleteSingleIdentifier() throws MarshalException {
		PublishRequest req = makeRequest();
		Identifier i1 = TestHelpers.ip("192.168.0.1", IpAddressType.IPv4, null);
		req.addPublishElement(TestHelpers.publishDelete(i1, null, "myfilter"));
		req.setSessionId("1234");
		Document doc = sDocBuilder.newDocument();
		Element ret = sHandler.toElement(req, doc);
		assertNotNull(ret);
		assertEquals(REQ_EL_NAME, ret.getLocalName());
		assertEquals(IFMAP_URI, ret.getNamespaceURI());
		assertEquals(1, ret.getAttributes().getLength());
		assertEquals("1234", ret.getAttribute("session-id"));
		assertEquals(1, ret.getChildNodes().getLength());
		Element deleteEl = (Element) ret.getChildNodes().item(0);
		assertEquals("delete", deleteEl.getLocalName());
		assertNull(deleteEl.getNamespaceURI());
		assertEquals(1, deleteEl.getChildNodes().getLength());
		NodeList idents = deleteEl.getChildNodes();
		assertEquals("ip-address", idents.item(0).getLocalName());
	}

	@Test
	public void testToElementNotifyNoLifetime() throws MarshalException {
		PublishRequest req = makeRequest();
		req.addPublishElement(TestHelpers.publishNotify(
				TestHelpers.ip("192.168.0.1", IpAddressType.IPv4, null),
				null,
				TestHelpers.simpleMetadata(sDocBuilder, "mymeta3", "multiValue")));
		req.setSessionId("1234");
		Document doc = sDocBuilder.newDocument();
		Element ret = sHandler.toElement(req, doc);
		assertNotNull(ret);
		assertEquals(REQ_EL_NAME, ret.getLocalName());
		assertEquals(IFMAP_URI, ret.getNamespaceURI());
		assertEquals(1, ret.getAttributes().getLength());
		assertEquals("1234", ret.getAttribute("session-id"));
		assertEquals(1, ret.getChildNodes().getLength());
		Element notifyEl = (Element) ret.getChildNodes().item(0);
		assertEquals("notify", notifyEl.getLocalName());
		assertNull(notifyEl.getNamespaceURI());

		// don't set lifetime!
		assertNull(notifyEl.getAttributeNode("lifetime"));
	}

	@Test
	public void testToElementNotifyTwoIdentifiers() throws MarshalException {
		PublishRequest req = makeRequest();
		req.addPublishElement(TestHelpers.publishNotify(
				TestHelpers.ip("192.168.0.1", IpAddressType.IPv4, null),
				TestHelpers.mac("aa:bb:cc:dd:ee:ff", null),
				TestHelpers.simpleMetadata(sDocBuilder, "mymeta3", "multiValue")));
		req.setSessionId("1234");
		Document doc = sDocBuilder.newDocument();
		Element ret = sHandler.toElement(req, doc);
		assertNotNull(ret);
		assertEquals(REQ_EL_NAME, ret.getLocalName());
		assertEquals(IFMAP_URI, ret.getNamespaceURI());
		assertEquals(1, ret.getAttributes().getLength());
		assertEquals("1234", ret.getAttribute("session-id"));
		assertEquals(1, ret.getChildNodes().getLength());
		Element notifyEl = (Element) ret.getChildNodes().item(0);
		assertEquals("notify", notifyEl.getLocalName());
		assertNull(notifyEl.getNamespaceURI());

		// don't set lifetime!
		assertNull(notifyEl.getAttributeNode("lifetime"));

		NodeList children = notifyEl.getChildNodes();
		assertEquals(3, children.getLength());
		assertEquals("ip-address", children.item(0).getLocalName());
		assertEquals("mac-address", children.item(1).getLocalName());
		assertEquals("metadata", children.item(2).getLocalName());
	}

	@Test
	public void testToElementMix() throws MarshalException {
		PublishRequest req = makeRequest();
		req.addPublishElement(TestHelpers.publishNotify(
				TestHelpers.ip("192.168.0.1", IpAddressType.IPv4, null),
				TestHelpers.mac("aa:bb:cc:dd:ee:ff", null),
				TestHelpers.simpleMetadata(sDocBuilder, "mymeta3", "multiValue")));

		req.addPublishElement(TestHelpers.publishDelete(
				TestHelpers.ip("192.168.0.1", IpAddressType.IPv4, null), null,
				"myfilter"));

		req.addPublishElement(TestHelpers.publishUpdate(
				TestHelpers.ip("192.168.0.1", IpAddressType.IPv4, null), null,
				TestHelpers.simpleMetadata(sDocBuilder, "mymeta4", "multiValue")));

		req.setSessionId("1234");
		Document doc = sDocBuilder.newDocument();
		Element ret = sHandler.toElement(req, doc);
		assertNotNull(ret);
		assertEquals(REQ_EL_NAME, ret.getLocalName());
		assertEquals(IFMAP_URI, ret.getNamespaceURI());
		assertEquals(1, ret.getAttributes().getLength());
		assertEquals("1234", ret.getAttribute("session-id"));
		assertEquals(3, ret.getChildNodes().getLength());
		Element notifyEl = (Element) ret.getChildNodes().item(0);
		Element deleteEl = (Element) ret.getChildNodes().item(1);
		Element updateEl = (Element) ret.getChildNodes().item(2);
		assertEquals("notify", notifyEl.getLocalName());
		assertEquals("delete", deleteEl.getLocalName());
		assertEquals("update", updateEl.getLocalName());
		assertEquals(3, notifyEl.getChildNodes().getLength());
		assertEquals(1, deleteEl.getChildNodes().getLength());
		assertEquals(2, updateEl.getChildNodes().getLength());
	}
}
