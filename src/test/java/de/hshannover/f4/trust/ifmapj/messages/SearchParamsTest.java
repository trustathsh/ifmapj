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
 * This file is part of ifmapj, version 2.2.1, implemented by the Trust@HsH
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
package de.hshannover.f4.trust.ifmapj.messages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.hshannover.f4.trust.ifmapj.exception.MarshalException;
import de.hshannover.f4.trust.ifmapj.identifier.Identifier;
import util.DomHelpers;

@RunWith(Parameterized.class)
public class SearchParamsTest {

	private static final DocumentBuilder DB = DomHelpers.newDocumentBuilder();
	private static final SearchRequestHandler HANDLER = new SearchRequestHandler();
	private static Identifier mStartIdentifier;

	@BeforeClass
	public static void setUp() {
		mStartIdentifier = TestHelpers.ip("192.168.0.1", null, null);
	}

	private final String mMatchLinksSet;
	private final String mMatchLinksExpected;

	private final String mResultFilterSet;
	private final String mResultFilterExpected;

	private final Integer mMaxDepthSet;
	private final String mMaxDepthExpected;

	private final Integer mMaxSizeSet;
	private final String mMaxSizeExpected;

	private final String mTerminalIdentsSet;
	private final String mTerminalIdentsExpected;


	public SearchParamsTest(String matchLinksSet,
			String matchLinksExpected, String resultFilterSet,
			String resultFilterExpected, Integer maxDepthSet,
			String maxDepthExpected, Integer maxSizeSet,
			String maxSizeExpected, String terminalIdentsSet,
			String terminalIdentsExpected) {

		mMatchLinksSet = matchLinksSet;
		mMatchLinksExpected = matchLinksExpected;
		mResultFilterSet = resultFilterSet;
		mResultFilterExpected = resultFilterExpected;
		mMaxDepthSet = maxDepthSet;
		mMaxDepthExpected = maxDepthExpected;
		mMaxSizeSet = maxSizeSet;
		mMaxSizeExpected = maxSizeExpected;
		mTerminalIdentsSet = terminalIdentsSet;
		mTerminalIdentsExpected = terminalIdentsExpected;
	}


	@SuppressWarnings("rawtypes")
	@Parameters
	public static Collection searchParams() {
		return Arrays.asList(new Object[][] {
				/* match	 result	  depth	   size	   terminal */
				{null, null, null, null, null, null, null, null, null, null},
				{"urgs", "urgs", null, null, null, null, null, null, null, null},
				{null, null, "urgs", "urgs", null, null, null, null, null, null},
				{null, null, null, null, 12, "12", null, null, null, null},
				{null, null, null, null, null, null, 12, "12", null, null},
				{null, null, null, null, null, null, null, null, "ip-address", "ip-address"},
				{"urgs", "urgs", "urgs", "urgs", null, null, null, null, null, null},
				{"urgs", "urgs", "urgs", "urgs", 12, "12", null, null, null, null},
				{"urgs", "urgs", "urgs", "urgs", 4711, "4711", 1200, "1200", null, null},
				{"urgs", "urgs", "urgs", "urgs", 4711, "4711", 1200, "1200", "mac-address", "mac-address"},
		});
	}

	@Test
	public void testToElement() throws MarshalException {
		SearchRequest req = makeRequest();
		Document doc = DB.newDocument();

		Element res = HANDLER.toElement(req, doc);

		assertEquals("search", res.getLocalName());

		Attr attribNode;
		int cntNotNull = 0;

		attribNode = res.getAttributeNode("match-links");
		if (mMatchLinksExpected == null) {
			assertNull(attribNode);
		} else {
			cntNotNull++;
			assertNotNull(attribNode);
			assertEquals(mMatchLinksExpected, attribNode.getValue());
		}

		attribNode = res.getAttributeNode("result-filter");
		if (mResultFilterExpected == null) {
			assertNull(attribNode);
		} else {
			cntNotNull++;
			assertNotNull(attribNode);
			assertEquals(mResultFilterExpected, attribNode.getValue());
		}

		attribNode = res.getAttributeNode("max-depth");
		if (mMaxDepthExpected == null) {
			assertNull(attribNode);
		} else {
			cntNotNull++;
			assertNotNull(attribNode);
			assertEquals(mMaxDepthExpected, attribNode.getValue());
		}

		attribNode = res.getAttributeNode("max-size");
		if (mMaxSizeExpected == null) {
			assertNull(attribNode);
		} else {
			cntNotNull++;
			assertNotNull(attribNode);
			assertEquals(mMaxSizeExpected, attribNode.getValue());
		}

		attribNode = res.getAttributeNode("terminal-identifier-type");
		if (mTerminalIdentsExpected == null) {
			assertNull(attribNode);
		} else {
			cntNotNull++;
			assertNotNull(attribNode);
			assertEquals(mTerminalIdentsExpected, attribNode.getValue());
		}

		// session-id included!
		assertEquals(cntNotNull + 1, res.getAttributes().getLength());
	}

	private SearchRequest makeRequest() {
		SearchHolder sh = new SearchHolderImpl();
		SearchRequest ret = new SearchRequestImpl(sh);
		ret.setSessionId("1234");
		ret.setMatchLinksFilter(mMatchLinksSet);
		ret.setResultFilter(mResultFilterSet);
		ret.setMaxDepth(mMaxDepthSet);
		ret.setMaxSize(mMaxSizeSet);
		ret.setTerminalIdentifierTypes(mTerminalIdentsSet);
		ret.setStartIdentifier(mStartIdentifier);
		return ret;
	}

}
