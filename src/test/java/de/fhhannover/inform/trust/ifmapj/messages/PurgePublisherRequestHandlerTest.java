package de.fhhannover.inform.trust.ifmapj.messages;

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

import static org.junit.Assert.*;

import javax.xml.parsers.DocumentBuilder;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.fhhannover.inform.trust.ifmapj.exception.IfmapErrorResult;
import de.fhhannover.inform.trust.ifmapj.exception.MarshalException;
import de.fhhannover.inform.trust.ifmapj.exception.UnmarshalException;

import util.DomHelpers;

public class PurgePublisherRequestHandlerTest {
	
	private static RequestHandler<? extends Request> sHandler = makeHandler();
	private static DocumentBuilder sDocBuilder = DomHelpers.newDocumentBuilder();
	private static final String REQ_EL_NAME = "purgePublisher";
	private static final String RES_EL_NAME = "purgePublisherReceived";
	private static final String IFMAP_URI = "http://www.trustedcomputinggroup.org/2010/IFMAP/2";
	
	private static Request makeRequest() {
		return new PurgePublisherRequestImpl();
	}

	private static RequestHandler<? extends Request> makeHandler() {
		return new PurgePublisherRequestHandler();
	}
	
	@Test
	public void testToElementGood() throws MarshalException {
		PurgePublisherRequest req = (PurgePublisherRequest) makeRequest();
		req.setSessionId("1234");
		req.setPublisherId("4321");
		Document doc = sDocBuilder.newDocument();
		Element ret = sHandler.toElement(req, doc);
		assertNotNull(ret);
		assertEquals(REQ_EL_NAME, ret.getLocalName());
		assertEquals(IFMAP_URI, ret.getNamespaceURI());
		assertEquals(2, ret.getAttributes().getLength());
		assertEquals("1234", ret.getAttribute("session-id"));
		assertEquals("4321", ret.getAttribute("ifmap-publisher-id"));
	}

	@Test(expected=MarshalException.class)
	public void testToElementNullSessionId() throws MarshalException {
		PurgePublisherRequest req = (PurgePublisherRequest) makeRequest();
		req.setPublisherId("4321");
		Document doc = sDocBuilder.newDocument();
		Element ret = sHandler.toElement(req, doc);
		assertNull(ret);
	}
	
	@Test(expected=MarshalException.class)
	public void testToElementEmptySessionId() throws MarshalException {
		PurgePublisherRequest req = (PurgePublisherRequest) makeRequest();
		req.setSessionId("");
		req.setPublisherId("4321");
		Document doc = sDocBuilder.newDocument();
		Element ret = sHandler.toElement(req, doc);
		assertNull(ret);
	}
	
	@Test(expected=MarshalException.class)
	public void testToElementNullPublisherId() throws MarshalException {
		PurgePublisherRequest req = (PurgePublisherRequest) makeRequest();
		req.setSessionId("1234");
		Document doc = sDocBuilder.newDocument();
		Element ret = sHandler.toElement(req, doc);
		assertNull(ret);
	}
	
	@Test(expected=MarshalException.class)
	public void testToElementEmptyPublisherId() throws MarshalException {
		PurgePublisherRequest req = (PurgePublisherRequest) makeRequest();
		req.setSessionId("1234");
		req.setPublisherId("");
		Document doc = sDocBuilder.newDocument();
		Element ret = sHandler.toElement(req, doc);
		assertNull(ret);
	}

	@Test(expected=MarshalException.class)
	public void testToElementWrongType() throws MarshalException {
		Request req = new PublishRequestImpl();
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
		Element result = doc.createElementNS(null, "publishReceived");
		response.appendChild(result);
		Result res = sHandler.fromElement(response);
		assertNull(res);
	}
}
