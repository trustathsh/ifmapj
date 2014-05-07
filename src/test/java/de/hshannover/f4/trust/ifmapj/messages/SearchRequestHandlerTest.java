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

import java.util.List;

import javax.xml.parsers.DocumentBuilder;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import util.DomHelpers;
import de.hshannover.f4.trust.ifmapj.exception.IfmapErrorResult;
import de.hshannover.f4.trust.ifmapj.exception.MarshalException;
import de.hshannover.f4.trust.ifmapj.exception.UnmarshalException;
import de.hshannover.f4.trust.ifmapj.identifier.Identifier;
import de.hshannover.f4.trust.ifmapj.identifier.IpAddressType;

public class SearchRequestHandlerTest {

	private static RequestHandler<? extends Request> sHandler = makeHandler();
	private static DocumentBuilder sDocBuilder = DomHelpers.newDocumentBuilder();
	private static final String REQ_EL_NAME = "search";
	private static final String RES_EL_NAME = "searchResult";
	private static final String IFMAP_URI = "http://www.trustedcomputinggroup.org/2010/IFMAP/2";

	private static SearchHolder makeSearchHolder() {
		return new SearchHolderImpl();
	}

	private static Request makeRequest(SearchHolder sh) {
		return new SearchRequestImpl(sh);
	}

	private static RequestHandler<? extends Request> makeHandler() {
		return new SearchRequestHandler();
	}

	private static Request  makeSimpleRequest() {
		SearchHolder sh = makeSearchHolder();
		sh.setStartIdentifier(TestHelpers.ip("192.168.0.1", IpAddressType.IPv4, "mydomain"));
		return makeRequest(sh);
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
	}

	@Test(expected = MarshalException.class)
	public void testToElementNullSessionId() throws MarshalException {
		Request req = makeSimpleRequest();
		Document doc = sDocBuilder.newDocument();
		Element ret = sHandler.toElement(req, doc);
		assertNull(ret);
	}

	@Test(expected = MarshalException.class)
	public void testToElementEmptySessionId() throws MarshalException {
		Request req = makeSimpleRequest();
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
		TestHelpers.addGoodSingleResultItemSearchResult(doc, response);

		Result res = sHandler.fromElement(response);
		assertNotNull(res);
		assertTrue(res instanceof SearchResult);
		SearchResult sr = (SearchResult) res;
		assertEquals(1, sr.getResultItems().size());
		assertNotNull(sr.getResultItems().iterator().next().getIdentifier1());
		assertNull(sr.getResultItems().iterator().next().getIdentifier2());
	}

	@Test(expected = UnmarshalException.class)
	public void testFromElementEmptySearchResult() throws UnmarshalException, IfmapErrorResult {
		Document doc = sDocBuilder.newDocument();
		Element response = TestHelpers.makeResponse(doc);
		TestHelpers.addResultToResponse(RES_EL_NAME, doc, response);
		Result res = sHandler.fromElement(response);
		assertNull(res);
	}

	@Test(expected = IfmapErrorResult.class)
	public void testFromElementWithErrorResult() throws IfmapErrorResult, UnmarshalException {
		Document doc = sDocBuilder.newDocument();
		Element response = TestHelpers.makeResponse(doc);
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
		Element response = TestHelpers.makeResponse(doc);
		Result res = sHandler.fromElement(response);
		assertNull(res);
	}

	@Test(expected = UnmarshalException.class)
	public void testFromElementWrongResult() throws IfmapErrorResult, UnmarshalException {
		Document doc = sDocBuilder.newDocument();
		Element response = TestHelpers.makeResponse(doc);
		Element result = doc.createElementNS(null, "publishReceived");
		response.appendChild(result);
		Result res = sHandler.fromElement(response);
		assertNull(res);
	}




	@Test(expected = UnmarshalException.class)
	public void testFromElementNonResultItem() throws UnmarshalException, IfmapErrorResult {
		Document doc = sDocBuilder.newDocument();
		Element response = TestHelpers.makeResponse(doc);
		Element ri = doc.createElementNS(null, "thisIsNotAnResultItem");
		Element sres = TestHelpers.searchResult(doc, ri);
		response.appendChild(sres);
		Result res = sHandler.fromElement(response);
		assertNull(res);
	}

	@Test(expected = UnmarshalException.class)
	public void testFromElementEmptyResultItem() throws UnmarshalException, IfmapErrorResult {
		Document doc = sDocBuilder.newDocument();
		Element response = TestHelpers.makeResponse(doc);
		Element ri = TestHelpers.resultItem(doc);
		Element sres = TestHelpers.searchResult(doc, ri);
		response.appendChild(sres);
		Result res = sHandler.fromElement(response);
		assertNull(res);
	}

	@Test(expected = UnmarshalException.class)
	public void testFromElementOverFullResultItem() throws UnmarshalException, IfmapErrorResult {
		Document doc = sDocBuilder.newDocument();
		Element response = TestHelpers.makeResponse(doc);
		Element ri = TestHelpers.resultItem(doc);

		for (int i = 0; i < 3; i++) {
			ri.appendChild(TestHelpers.macElement(doc, "aa:bb:cc:dd:ee:ff", null));
		}

		Element sres = TestHelpers.searchResult(doc, ri);
		response.appendChild(sres);
		Result res = sHandler.fromElement(response);
		assertNull(res);
	}

	@Test
	public void testFromElementFourResultItems() throws UnmarshalException, IfmapErrorResult {
		// There was a bug where more than 3 resultItems were not allowed.
		// Check this...
		Document doc = sDocBuilder.newDocument();
		Element response = TestHelpers.makeResponse(doc);
		Element sres = TestHelpers.searchResult(doc);

		for (int i = 0; i < 4; i++) {
			Element ri = TestHelpers.resultItem(doc, TestHelpers.macElement(
					doc, "aa:bb:cc:dd:ee:ff", null));
			sres.appendChild(ri);
		}

		response.appendChild(sres);
		Result res = sHandler.fromElement(response);
		assertNotNull(res);
	}


	@Test(expected = UnmarshalException.class)
	public void testFromElementNoIdentifierInResultItem() throws UnmarshalException, IfmapErrorResult {
		Document doc = sDocBuilder.newDocument();
		Element response = TestHelpers.makeResponse(doc);
		Element ri = TestHelpers.resultItem(doc);
		Element md1 = doc.createElementNS(null, "somemetadata");
		ri.appendChild(TestHelpers.metadata(doc, md1));

		Element sres = TestHelpers.searchResult(doc, ri);
		response.appendChild(sres);
		Result res = sHandler.fromElement(response);
		assertNull(res);
	}

	@Test(expected = UnmarshalException.class)
	public void testFromElementTwoMetadataElementsInResultItem() throws UnmarshalException, IfmapErrorResult {
		Document doc = sDocBuilder.newDocument();
		Element response = TestHelpers.makeResponse(doc);
		Element ri = TestHelpers.resultItem(doc);
		Element md1 = doc.createElementNS(null, "somemetadata");
		Element md2 = doc.createElementNS(null, "somemetadata");

		ri.appendChild(TestHelpers.metadata(doc, md1));
		ri.appendChild(TestHelpers.metadata(doc, md2));

		Element sres = TestHelpers.searchResult(doc, ri);
		response.appendChild(sres);
		Result res = sHandler.fromElement(response);
		assertNull(res);
	}

	@Test(expected = UnmarshalException.class)
	public void testFromElementUnknownElementInResultItem() throws UnmarshalException, IfmapErrorResult {
		Document doc = sDocBuilder.newDocument();
		Element response = TestHelpers.makeResponse(doc);
		Element ri = TestHelpers.resultItem(doc);
		Element md1 = doc.createElementNS(null, "somemetadata");

		ri.appendChild(TestHelpers.macElement(doc, "aa:bb:cc:dd:ee:ff", null));
		ri.appendChild(TestHelpers.metadata(doc, md1));
		ri.appendChild(doc.createElementNS(null, "notallowed"));

		Element sres = TestHelpers.searchResult(doc, ri);
		response.appendChild(sres);
		Result res = sHandler.fromElement(response);
		assertNull(res);
	}

	@Test(expected = UnmarshalException.class)
	public void testFromElementSearchResultWithName() throws UnmarshalException, IfmapErrorResult {
		Document doc = sDocBuilder.newDocument();
		Element response = TestHelpers.makeResponse(doc);
		Element ri = TestHelpers.resultItem(doc);
		Element md1 = doc.createElementNS(null, "somemetadata");

		ri.appendChild(TestHelpers.macElement(doc, "aa:bb:cc:dd:ee:ff", null));
		ri.appendChild(TestHelpers.metadata(doc, md1));

		Element sres = TestHelpers.searchResult(doc, ri);
		sres.setAttribute("name", "mysearch");
		response.appendChild(sres);
		Result res = sHandler.fromElement(response);
		assertNull(res);
	}

	@Test
	public void testFromElementGood() throws UnmarshalException, IfmapErrorResult {
		Document doc = sDocBuilder.newDocument();
		Element response = TestHelpers.makeResponse(doc);
		Element ri = TestHelpers.resultItem(doc);
		Element md1 = doc.createElementNS(null, "somemetadata");

		ri.appendChild(TestHelpers.macElement(doc, "aa:bb:cc:dd:ee:ff", null));
		ri.appendChild(TestHelpers.ipElement(doc, "192.168.0.1", "IPv4", null));
		ri.appendChild(TestHelpers.metadata(doc, md1));

		Element sres = TestHelpers.searchResult(doc, ri);
		response.appendChild(sres);
		Result res = sHandler.fromElement(response);

		assertNotNull(res);
		assertTrue(res instanceof SearchResult);
		SearchResult sr = (SearchResult) res;
		assertEquals(1, sr.getResultItems().size());
		ResultItem resultItem = sr.getResultItems().get(0);
		assertNotNull(resultItem.getIdentifier1());
		assertNotNull(resultItem.getIdentifier2());
		Identifier[] idents = resultItem.getIdentifier();
		assertNotNull(idents);
		assertEquals(2, idents.length);
		assertNotNull(idents[0]);
		assertNotNull(idents[1]);
		assertTrue(idents[0] == resultItem.getIdentifier1());
		assertTrue(idents[1] == resultItem.getIdentifier2());
		List<Document> mdlist = resultItem.getMetadata();
		assertEquals(1, mdlist.size());
		assertTrue(mdlist.get(0) != md1);
	}
}
