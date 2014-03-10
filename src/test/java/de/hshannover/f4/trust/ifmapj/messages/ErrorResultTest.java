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
 * This file is part of ifmapj, version 1.0.1, implemented by the Trust@HsH
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
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import util.DomHelpers;
import de.hshannover.f4.trust.ifmapj.exception.IfmapErrorCode;
import de.hshannover.f4.trust.ifmapj.exception.IfmapErrorResult;
import de.hshannover.f4.trust.ifmapj.exception.UnmarshalException;

@RunWith(Parameterized.class)
public class ErrorResultTest {
	private static DocumentBuilder sDocBuilder = DomHelpers.newDocumentBuilder();
	private static RequestHandler<? extends Request> sHandler = new NewSessionRequestHandler();

	private final String mCodeStr;
	private final IfmapErrorCode mCodeType;

	@SuppressWarnings("rawtypes")
	@Parameters
	public static Collection testParams() {
		return Arrays.asList(new Object[][] {
				{"AccessDenied", IfmapErrorCode.AccessDenied},
				{"Failure", IfmapErrorCode.Failure},
				{"InvalidIdentifier", IfmapErrorCode.InvalidIdentifier},
				{"InvalidIdentifierType", IfmapErrorCode.InvalidIdentifierType},
				{"IdentifierTooLong", IfmapErrorCode.IdentifierTooLong},
				{"InvalidMetadata", IfmapErrorCode.InvalidMetadata},
				{"InvalidSchemaVersion", IfmapErrorCode.InvalidSchemaVersion},
				{"InvalidSessionID", IfmapErrorCode.InvalidSessionID},
				{"MetadataTooLong", IfmapErrorCode.MetadataTooLong},
				{"SearchResultsTooBig", IfmapErrorCode.SearchResultsTooBig},
				{"PollResultsTooBig", IfmapErrorCode.PollResultsTooBig},
				{"SystemError", IfmapErrorCode.SystemError}});
	}

	public ErrorResultTest(String errStr, IfmapErrorCode type) {
		mCodeStr = errStr;
		mCodeType = type;
	}

	@Test
	public void fromElementTypeTest() throws UnmarshalException {

		Document doc = sDocBuilder.newDocument();
		Element resp = TestHelpers.makeResponse(doc);
		TestHelpers.appendErrorResult(doc, resp, mCodeStr, "GENERIC", null);

		try {
			sHandler.fromElement(resp);
			fail("IfmapErrorResult expected");
		} catch (IfmapErrorResult e) {
			assertEquals(mCodeType, e.getErrorCode());
		}
	}

}
