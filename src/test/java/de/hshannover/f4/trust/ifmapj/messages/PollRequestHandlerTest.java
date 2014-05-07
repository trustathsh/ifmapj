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
package de.hshannover.f4.trust.ifmapj.messages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.xml.parsers.DocumentBuilder;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.hshannover.f4.trust.ifmapj.exception.IfmapErrorCode;
import de.hshannover.f4.trust.ifmapj.exception.IfmapErrorResult;
import de.hshannover.f4.trust.ifmapj.exception.MarshalException;
import de.hshannover.f4.trust.ifmapj.exception.UnmarshalException;
import de.hshannover.f4.trust.ifmapj.messages.SearchResult.Type;
import util.DomHelpers;

@SuppressWarnings("deprecation")
public class PollRequestHandlerTest {

	private static RequestHandler<? extends Request> sHandler = makeHandler();
	private static DocumentBuilder sDocBuilder = DomHelpers.newDocumentBuilder();
	private static final String REQ_EL_NAME = "poll";
	private static final String RES_EL_NAME = "pollResult";
	private static final String IFMAP_URI = "http://www.trustedcomputinggroup.org/2010/IFMAP/2";

	private static Request makeRequest() {
		return new PollRequestImpl();
	}

	private static RequestHandler<? extends Request> makeHandler() {
		return new PollRequestHandler();
	}

	@Test
	public void testToElementGood() throws MarshalException {
		Request req = makeRequest();
		req.setSessionId("1234");
		Document doc = sDocBuilder.newDocument();
		Element ret = sHandler.toElement(req, doc);
		assertNotNull(ret);
		assertEquals(REQ_EL_NAME, ret.getLocalName());
		assertEquals(IFMAP_URI, ret.getNamespaceURI());
		assertEquals(1, ret.getAttributes().getLength());
		assertEquals("1234", ret.getAttribute("session-id"));
	}

	@Test(expected = MarshalException.class)
	public void testToElementNullSessionId() throws MarshalException {
		Request req = makeRequest();
		Document doc = sDocBuilder.newDocument();
		Element ret = sHandler.toElement(req, doc);
		assertNull(ret);
	}

	@Test(expected = MarshalException.class)
	public void testToElementEmptySessionId() throws MarshalException {
		Request req = makeRequest();
		req.setSessionId("");
		Document doc = sDocBuilder.newDocument();
		Element ret = sHandler.toElement(req, doc);
		assertNull(ret);
	}

	@Test(expected = MarshalException.class)
	public void testToElementWrongType() throws MarshalException {
		Request req = new PublishRequestImpl();
		req.setSessionId("1234");
		Document doc = sDocBuilder.newDocument();
		Element ret = sHandler.toElement(req, doc);
		assertNull(ret);
	}

	@Test
	public void testFromElementSingleSearchResult() throws UnmarshalException,
			IfmapErrorResult {
		Document doc = sDocBuilder.newDocument();
		Element response = TestHelpers.makeResponse(doc);
		Element result = TestHelpers.addResultToResponse(RES_EL_NAME, doc, response);
		TestHelpers.addGoodSingleResultItemResult("searchResult", doc, result);

		Result res = sHandler.fromElement(response);
		assertNotNull(res);
		assertTrue(res instanceof PollResult);
		PollResult pr = (PollResult) res;
		assertEquals(1, pr.getResults().size());
		assertEquals(SearchResult.Type.searchResult, pr.getResults().get(0).getType());

		// Remove if they are gone...
		assertEquals(1, pr.getSearchResults().size());
		assertEquals(0, pr.getUpdateResults().size());
		assertEquals(0, pr.getDeleteResults().size());
		assertEquals(0, pr.getNotifyResults().size());
		assertEquals("mysub", pr.getSearchResults().iterator().next().getName());
	}

	@Test
	public void testFromElementSingleUpdateResult() throws UnmarshalException,
			IfmapErrorResult {
		Document doc = sDocBuilder.newDocument();
		Element response = TestHelpers.makeResponse(doc);
		Element result = TestHelpers.addResultToResponse(RES_EL_NAME, doc, response);
		TestHelpers.addGoodSingleResultItemResult("updateResult", doc, result);

		Result res = sHandler.fromElement(response);
		assertNotNull(res);
		assertTrue(res instanceof PollResult);
		PollResult pr = (PollResult) res;
		assertEquals(1, pr.getResults().size());
		assertEquals(SearchResult.Type.updateResult, pr.getResults().get(0).getType());
		assertEquals("mysub", pr.getResults().get(0).getName());

		// Remove if they are gone...
		assertEquals(0, pr.getSearchResults().size());
		assertEquals(1, pr.getUpdateResults().size());
		assertEquals(0, pr.getDeleteResults().size());
		assertEquals(0, pr.getNotifyResults().size());
		assertEquals("mysub", pr.getUpdateResults().iterator().next().getName());
	}

	@Test
	public void testFromElementSingleDeleteResult() throws UnmarshalException,
			IfmapErrorResult {
		Document doc = sDocBuilder.newDocument();
		Element response = TestHelpers.makeResponse(doc);
		Element result = TestHelpers.addResultToResponse(RES_EL_NAME, doc, response);
		TestHelpers.addGoodSingleResultItemResult("deleteResult", doc, result);

		Result res = sHandler.fromElement(response);
		assertNotNull(res);
		assertTrue(res instanceof PollResult);
		PollResult pr = (PollResult) res;
		assertEquals(1, pr.getResults().size());
		assertEquals(SearchResult.Type.deleteResult, pr.getResults().get(0).getType());
		assertEquals("mysub", pr.getResults().get(0).getName());

		// Remove if they are gone...
		assertEquals(0, pr.getSearchResults().size());
		assertEquals(0, pr.getUpdateResults().size());
		assertEquals(1, pr.getDeleteResults().size());
		assertEquals(0, pr.getNotifyResults().size());
		assertEquals("mysub", pr.getDeleteResults().iterator().next().getName());
	}

	@Test
	public void testFromElementSingleNotifyResult() throws UnmarshalException,
			IfmapErrorResult {
		Document doc = sDocBuilder.newDocument();
		Element response = TestHelpers.makeResponse(doc);
		Element result = TestHelpers.addResultToResponse(RES_EL_NAME, doc, response);
		TestHelpers.addGoodSingleResultItemResult("notifyResult", doc, result);

		Result res = sHandler.fromElement(response);
		assertNotNull(res);
		assertTrue(res instanceof PollResult);
		PollResult pr = (PollResult) res;
		assertEquals(1, pr.getResults().size());
		assertEquals(SearchResult.Type.notifyResult, pr.getResults().get(0).getType());
		assertEquals("mysub", pr.getResults().get(0).getName());

		// Remove if they are gone...
		assertEquals(0, pr.getSearchResults().size());
		assertEquals(0, pr.getUpdateResults().size());
		assertEquals(0, pr.getDeleteResults().size());
		assertEquals(1, pr.getNotifyResults().size());
		assertEquals("mysub", pr.getNotifyResults().iterator().next().getName());
	}

	@Test
	public void testFromElementSingleErrorResult() throws UnmarshalException,
			IfmapErrorResult {
		Document doc = sDocBuilder.newDocument();
		Element response = TestHelpers.makeResponse(doc);
		Element result = TestHelpers.addResultToResponse(RES_EL_NAME, doc, response);
		TestHelpers.appendErrorResult(doc, result, "SearchResultsTooBig", "TOO BIG", "mysub");

		Result res = sHandler.fromElement(response);
		assertNotNull(res);
		assertTrue(res instanceof PollResult);
		PollResult pr = (PollResult) res;
		assertEquals(0, pr.getResults().size());
		assertEquals(1, pr.getErrorResults().size());
		assertEquals(IfmapErrorCode.SearchResultsTooBig, pr.getErrorResults().iterator().next().getErrorCode());
		assertEquals("TOO BIG", pr.getErrorResults().iterator().next().getErrorString());
		assertEquals("mysub", pr.getErrorResults().iterator().next().getName());


		// Remove when they are gone
		assertEquals(0, pr.getSearchResults().size());
		assertEquals(0, pr.getUpdateResults().size());
		assertEquals(0, pr.getDeleteResults().size());
		assertEquals(0, pr.getNotifyResults().size());
	}

	@Test(expected = UnmarshalException.class)
	public void testFromElementSingleErrorResultNoName() throws UnmarshalException,
			IfmapErrorResult {
		Document doc = sDocBuilder.newDocument();
		Element response = TestHelpers.makeResponse(doc);
		Element result = TestHelpers.addResultToResponse(RES_EL_NAME, doc, response);
		Element err = TestHelpers.makeErrorResult(doc, "SearchResultsTooBig", "TOO BIG");
		result.appendChild(err);

		Result res = sHandler.fromElement(response);
		assertNull(res);
	}

	@Test
	public void testFromElementSingleOfEveryResult() throws UnmarshalException,
			IfmapErrorResult {
		Document doc = sDocBuilder.newDocument();
		Element response = TestHelpers.makeResponse(doc);
		Element result = TestHelpers.addResultToResponse(RES_EL_NAME, doc, response);
		TestHelpers.addGoodSingleResultItemResult("searchResult", doc, result);
		TestHelpers.addGoodSingleResultItemResult("updateResult", doc, result);
		TestHelpers.addGoodSingleResultItemResult("deleteResult", doc, result);
		TestHelpers.addGoodSingleResultItemResult("notifyResult", doc, result);

		Result res = sHandler.fromElement(response);
		assertNotNull(res);
		assertTrue(res instanceof PollResult);
		PollResult pr = (PollResult) res;
		assertEquals(4, pr.getResults().size());
		assertEquals(Type.searchResult, pr.getResults().get(0).getType());
		assertEquals(Type.updateResult, pr.getResults().get(1).getType());
		assertEquals(Type.deleteResult, pr.getResults().get(2).getType());
		assertEquals(Type.notifyResult, pr.getResults().get(3).getType());
		assertEquals("mysub", pr.getResults().get(0).getName());
		assertEquals("mysub", pr.getResults().get(1).getName());
		assertEquals("mysub", pr.getResults().get(2).getName());
		assertEquals("mysub", pr.getResults().get(3).getName());

		// Remove when they are gone
		assertEquals(1, pr.getSearchResults().size());
		assertEquals(1, pr.getUpdateResults().size());
		assertEquals(1, pr.getDeleteResults().size());
		assertEquals(1, pr.getNotifyResults().size());
		assertEquals("mysub", pr.getSearchResults().iterator().next().getName());
		assertEquals("mysub", pr.getUpdateResults().iterator().next().getName());
		assertEquals("mysub", pr.getDeleteResults().iterator().next().getName());
		assertEquals("mysub", pr.getNotifyResults().iterator().next().getName());
	}

	@Test(expected = UnmarshalException.class)
	public void testFromElementNoResults() throws UnmarshalException, IfmapErrorResult {
		Document doc = sDocBuilder.newDocument();
		Element response = TestHelpers.makeResponse(doc);
		TestHelpers.addResultToResponse(RES_EL_NAME, doc, response);
		Result res = sHandler.fromElement(response);
		assertNull(res);
	}

	@Test(expected = UnmarshalException.class)
	public void testFromElementEmptySearchResult() throws UnmarshalException,
			IfmapErrorResult {
		Document doc = sDocBuilder.newDocument();
		Element response = TestHelpers.makeResponse(doc);
		Element result = TestHelpers.addResultToResponse(RES_EL_NAME, doc, response);
		Element resEl = TestHelpers.searchResult(doc);
		result.appendChild(resEl);
		Result res = sHandler.fromElement(response);
		assertNull(res);
	}

	@Test(expected = UnmarshalException.class)
	public void testFromElementSearchResultWithoutName() throws UnmarshalException,
			IfmapErrorResult {
		Document doc = sDocBuilder.newDocument();
		Element response = TestHelpers.makeResponse(doc);
		Element result = TestHelpers.addResultToResponse(RES_EL_NAME, doc, response);
		Element resEl = TestHelpers.searchResult(doc, TestHelpers.resultItem(doc,
				TestHelpers.ipElement(doc, "192.168.0.1", "IPv4", null)));
		result.appendChild(resEl);
		Result res = sHandler.fromElement(response);
		assertNull(res);
	}

	@Test(expected = UnmarshalException.class)
	public void testFromElementSearchResultWithAndWithoutName() throws UnmarshalException,
			IfmapErrorResult {
		Document doc = sDocBuilder.newDocument();
		Element response = TestHelpers.makeResponse(doc);
		Element result = TestHelpers.addResultToResponse(RES_EL_NAME, doc, response);
		Element resEl = TestHelpers.searchResult(doc, TestHelpers.resultItem(doc,
				TestHelpers.ipElement(doc, "192.168.0.1", "IPv4", null)));
		resEl.setAttribute("name", "mysub");
		result.appendChild(resEl);
		resEl = TestHelpers.searchResult(doc, TestHelpers.resultItem(doc,
				TestHelpers.ipElement(doc, "192.168.0.1", "IPv4", null)));
		result.appendChild(resEl);
		Result res = sHandler.fromElement(response);
		assertNull(res);
	}

	@Test(expected = UnmarshalException.class)
	public void testFromElementOverFullSearchResult() throws UnmarshalException,
			IfmapErrorResult {
		Document doc = sDocBuilder.newDocument();
		Element response = TestHelpers.makeResponse(doc);
		Element result = TestHelpers.addResultToResponse(RES_EL_NAME, doc, response);

		Element[] els = new Element[4];
		// add 4 elements under the searchResult

		for (int i = 0; i < els.length; i++) {
			els[i] = TestHelpers.resultItem(doc,
					TestHelpers.ipElement(doc, "192.168.0.1", "IPv4", null));
		}

		Element resEl = TestHelpers.makeResult("searchResult", doc, els);

		result.appendChild(resEl);
		Result res = sHandler.fromElement(response);
		assertNull(res);
	}

	@Test(expected = UnmarshalException.class)
	public void testFromElementWithUnknownResult() throws UnmarshalException,
			IfmapErrorResult {
		Document doc = sDocBuilder.newDocument();
		Element response = TestHelpers.makeResponse(doc);
		Element result = TestHelpers.addResultToResponse(RES_EL_NAME, doc, response);
		Element resEl = doc.createElementNS(null, "unknownResult");
		result.appendChild(resEl);
		Result res = sHandler.fromElement(response);
		assertNull(res);
	}

	@Test(expected = UnmarshalException.class)
	public void testFromElementWithKnownAndUnknownResult() throws UnmarshalException,
			IfmapErrorResult {
		Document doc = sDocBuilder.newDocument();
		Element response = TestHelpers.makeResponse(doc);
		Element result = TestHelpers.addResultToResponse(RES_EL_NAME, doc, response);
		Element resEl = doc.createElementNS(null, "searchResult");
		result.appendChild(resEl);
		resEl = doc.createElementNS(null, "unknownResult");
		Result res = sHandler.fromElement(response);
		assertNull(res);
	}

	@Test(expected = IfmapErrorResult.class)
	public void testFromElementWithErrorResult() throws IfmapErrorResult, UnmarshalException {
		Document doc = sDocBuilder.newDocument();
		Element response = doc.createElementNS(IFMAP_URI, "ifmap:response");
		Element result = doc.createElementNS(null, "errorResult");
		result.setAttribute("errorCode", "SystemError");
		response.appendChild(result);
		Result res = sHandler.fromElement(response);
		assertNull(res);
	}

	@Test(expected = UnmarshalException.class)
	public void testFromElementNoResultUnderResponse() throws IfmapErrorResult,
			UnmarshalException {
		Document doc = sDocBuilder.newDocument();
		Element response = doc.createElementNS(IFMAP_URI, "ifmap:response");
		Result res = sHandler.fromElement(response);
		assertNull(res);
	}

	@Test(expected = UnmarshalException.class)
	public void testFromElementWrongResult() throws IfmapErrorResult, UnmarshalException {
		Document doc = sDocBuilder.newDocument();
		Element response = doc.createElementNS(IFMAP_URI, "ifmap:response");
		Element result = doc.createElementNS(null, "publishReceived");
		response.appendChild(result);
		Result res = sHandler.fromElement(response);
		assertNull(res);
	}
}
