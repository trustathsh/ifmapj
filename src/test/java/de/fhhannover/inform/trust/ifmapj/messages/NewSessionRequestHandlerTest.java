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

public class NewSessionRequestHandlerTest {
	
	private static RequestHandler<? extends Request> sHandler = makeHandler();
	private static DocumentBuilder sDocBuilder = DomHelpers.newDocumentBuilder();
	private static final String REQ_EL_NAME = "newSession";
	private static final String RES_EL_NAME = "newSessionResult";
	private static final String IFMAP_URI = "http://www.trustedcomputinggroup.org/2010/IFMAP/2";
	
	private static Request makeRequest() {
		return new NewSessionRequestImpl();
	}

	private static RequestHandler<? extends Request> makeHandler() {
		return new NewSessionRequestHandler();
	}
	
	@Test
	public void testToElementGoodNullMPRS() throws MarshalException {
		NewSessionRequest req = (NewSessionRequest) makeRequest();
		req.setMaxPollResultSize(null);
		Document doc = sDocBuilder.newDocument();
		Element ret = sHandler.toElement(req, doc);
		assertNotNull(ret);
		assertEquals(REQ_EL_NAME, ret.getLocalName());
		assertEquals(IFMAP_URI, ret.getNamespaceURI());
		assertEquals(0, ret.getAttributes().getLength());
	}

	@Test
	public void testToElementGoodWithMPRS() throws MarshalException {
		NewSessionRequest req = (NewSessionRequest) makeRequest();
		req.setMaxPollResultSize(1000);
		Document doc = sDocBuilder.newDocument();
		Element ret = sHandler.toElement(req, doc);
		assertNotNull(ret);
		assertEquals(REQ_EL_NAME, ret.getLocalName());
		assertEquals(IFMAP_URI, ret.getNamespaceURI());
		assertEquals(1, ret.getAttributes().getLength());
		assertEquals("1000", ret.getAttribute("max-poll-result-size"));
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
	public void testFromElementGoodNoMPRS() throws UnmarshalException, IfmapErrorResult {
		Document doc = sDocBuilder.newDocument();
		Element response = doc.createElementNS(IFMAP_URI, "ifmap:response");
		Element result = doc.createElementNS(null, RES_EL_NAME);
		result.setAttribute("session-id", "1234");
		result.setAttribute("ifmap-publisher-id", "mypubid");
		response.appendChild(result);
		Result res = sHandler.fromElement(response);
		assertNotNull(res);
		assertTrue(res instanceof NewSessionResult);
		NewSessionResult nsr = (NewSessionResult) res;
		assertEquals("1234", nsr.getSessionId());
		assertEquals("mypubid", nsr.getPublisherId());
		assertNull(nsr.getMaxPollResultSize());
	}

	@Test
	public void testFromElementGoodMPRS() throws UnmarshalException, IfmapErrorResult {
		Document doc = sDocBuilder.newDocument();
		Element response = doc.createElementNS(IFMAP_URI, "ifmap:response");
		Element result = doc.createElementNS(null, RES_EL_NAME);
		result.setAttribute("session-id", "1234");
		result.setAttribute("ifmap-publisher-id", "mypubid");
		result.setAttribute("max-poll-result-size", "1000");
		response.appendChild(result);
		Result res = sHandler.fromElement(response);
		assertNotNull(res);
		assertTrue(res instanceof NewSessionResult);
		NewSessionResult nsr = (NewSessionResult) res;
		assertEquals("1234", nsr.getSessionId());
		assertEquals("mypubid", nsr.getPublisherId());
		assertEquals(new Integer(1000), nsr.getMaxPollResultSize());
	}
	
	
	@Test(expected=UnmarshalException.class)
	public void testFromElementNoSessionId() throws UnmarshalException, IfmapErrorResult {
		Document doc = sDocBuilder.newDocument();
		Element response = doc.createElementNS(IFMAP_URI, "ifmap:response");
		Element result = doc.createElementNS(null, RES_EL_NAME);
		result.setAttribute("ifmap-publisher-id", "mypubid");
		response.appendChild(result);
		Result res = sHandler.fromElement(response);
		assertNull(res);
	}

	@Test(expected=UnmarshalException.class)
	public void testFromElementNoPublisherId() throws UnmarshalException, IfmapErrorResult {
		Document doc = sDocBuilder.newDocument();
		Element response = doc.createElementNS(IFMAP_URI, "ifmap:response");
		Element result = doc.createElementNS(null, RES_EL_NAME);
		result.setAttribute("session-id", "1234");
		response.appendChild(result);
		Result res = sHandler.fromElement(response);
		assertNull(res);
	}

	@Test(expected=UnmarshalException.class)
	public void testFromElementNonNumericMPRS() throws UnmarshalException, IfmapErrorResult {
		Document doc = sDocBuilder.newDocument();
		Element response = doc.createElementNS(IFMAP_URI, "ifmap:response");
		Element result = doc.createElementNS(null, RES_EL_NAME);
		result.setAttribute("session-id", "1234");
		result.setAttribute("ifmap-publisher-id", "mypubid");
		result.setAttribute("max-poll-result-size", "NON_NUMERIC!");
		response.appendChild(result);
		Result res = sHandler.fromElement(response);
		assertNull(res);
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
