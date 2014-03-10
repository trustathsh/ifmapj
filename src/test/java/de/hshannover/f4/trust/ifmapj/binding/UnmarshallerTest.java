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
package de.hshannover.f4.trust.ifmapj.binding;

//public class UnmarshallerTest {
//
//	private final IdentifierFactory sIdentFac = IfmapJ.createIdentifierFactory();
//	private final Unmarshaller sUnmarshaller = BindingFactory.newUnmarshaller();
//	private final TestResponseCreator sRespCreator = new TestResponseCreator();
//
//	private final String ipElement = "<ip-address value=\"192.168.0.1\" type=\"IPv4\"/>";
//	private final String arElement = "<access-request name=\"AR012\"/>";
//
//	@Test
//	public void testGoodDeviceUnmarshalling() {
//
//		String resultItems[] = {
//				"<device><name>xyz</name></device>",
//				"\n\n<device>\n\t<name>xyz2</name>\n\t</device>\n\n",
//				"\n\n<device>\n\t<name>xyz3</name>\n\t</device>\n\n" };
//
//		Device expectedDevcies[] = {
//				sIdentFac.createDev("xyz"),
//				sIdentFac.createDev("xyz2"),
//				sIdentFac.createDev("xyz3")
//		};
//
//		try {
//			for (int i = 0; i < resultItems.length; i++) {
//				InputStream is =  sRespCreator.createSearchResultResponse(resultItems[i]);
//				SearchResult sr = sUnmarshaller.unmarshalSearchResult(is);
//				assertEquals(1, sr.getResultItems().size());
//				ResultItem ri = sr.getResultItems().iterator().next();
//				assertNotNull(ri);
//				assertNotNull(ri.getIdentifier1());
//				assertNull(ri.getIdentifier2());
//				assertEquals(expectedDevcies[i].toString(), ri.getIdentifier1().toString());
//			}
//		} catch (IfmapException e) {
//			fail(e.getMessage());
//		} catch (IfmapErrorResult e) {
//			fail(e.getMessage());
//		}
//	}
//
//	@Test
//	public void testNoElementsInResultItem() {
//		InputStream resp = sRespCreator.createSearchResultResponse("");
//		try {
//			sUnmarshaller.unmarshalSearchResult(resp);
//			fail("No exception?");
//		} catch (UnmarshalException e) {
//			//System.out.println(e.getMessage());
//		} catch (IfmapErrorResult e) {
//			fail("Wrong exception");
//		}
//	}
//
//	@Test
//	public void testResultItemTooManyElements() {
//		String resultItem = ipElement + arElement + "<metadata/>" +
//			"<justsomething/>";
//		InputStream resp = sRespCreator.createSearchResultResponse(resultItem);
//		try {
//			sUnmarshaller.unmarshalSearchResult(resp);
//			fail("No exception?");
//		} catch (UnmarshalException e) {
//			//System.out.println(e.getMessage());
//		} catch (IfmapErrorResult e) {
//			fail("Wrong exception");
//		}
//	}
//
//	@Test
//	public void testResultItemBadElement() {
//		String resultItem = ipElement + arElement + "<justsomething/>";
//		InputStream resp = sRespCreator.createSearchResultResponse(resultItem);
//		try {
//			sUnmarshaller.unmarshalSearchResult(resp);
//			fail("No exception?");
//		} catch (UnmarshalException e) {
//			//System.out.println(e.getMessage());
//		} catch (IfmapErrorResult e) {
//			fail("Wrong exception");
//		}
//	}
//
//	@Test
//	public void testParseGoodPollResult() {
//		String resultItem = ipElement + arElement + "<metadata/>";
//		String searchResult = sRespCreator.createSearchResult("sub1", resultItem);
//		InputStream resp = sRespCreator.createPollResultResponse(searchResult);
//		try {
//			sUnmarshaller.unmarshalPollResult(resp);
//		} catch (UnmarshalException e) {
//			fail(e.getMessage());
//		} catch (IfmapErrorResult e) {
//			fail(e.getMessage());
//		} catch (EndSessionException e) {
//			fail(e.getMessage());
//		}
//	}
//
//	@Test
//	public void testInvalidPollElement() {
//		String invalidResult = "<murxResult><somecontent/></murxResult>";
//		InputStream resp = sRespCreator.createPollResultResponse(invalidResult);
//		try {
//			sUnmarshaller.unmarshalPollResult(resp);
//			fail("No Exception?");
//		} catch (UnmarshalException e) {
//			//System.out.println(e.getMessage());
//		} catch (IfmapErrorResult e) {
//			fail("Wrong Exception?");
//		} catch (EndSessionException e) {
//			fail("Wrong Exception?");
//		}
//	}
//
//	@Test
//	public void testPollResultElementWithoutName() {
//		String resultItem = ipElement + arElement + "<metadata/>";
//		String searchResult = sRespCreator.createSearchResult(null, resultItem);
//		InputStream resp = sRespCreator.createPollResultResponse(searchResult);
//		try {
//			sUnmarshaller.unmarshalPollResult(resp);
//			fail("No Exception?");
//		} catch (UnmarshalException e) {
//			//System.out.println(e.getMessage());
//		} catch (IfmapErrorResult e) {
//			fail("Wrong Exception?");
//		} catch (EndSessionException e) {
//			fail("Wrong Exception?");
//		}
//	}
//
//	@Test
//	public void testEndSessionResultInPollResult() {
//		// This give UnmarshalException, because endSessionResult is not supposed
//		// to be located in pollResult
//		InputStream resp = sRespCreator.createPollResultResponse("<endSessionResult/>");
//		try {
//			sUnmarshaller.unmarshalPollResult(resp);
//			fail("No Exception?");
//		} catch (UnmarshalException e) {
//			//System.out.println(e.getMessage());
//		} catch (IfmapErrorResult e) {
//			fail("Wrong Exception?");
//		} catch (EndSessionException e) {
//			fail("Wrong Exception?");
//		}
//	}
//
//	@Test
//	public void testEndSessionResultInsteadPollResult() {
//		String respString = sRespCreator.createProlog() + "<endSessionResult/>" +
//			sRespCreator.createEpilog();
//		InputStream resp = new ByteArrayInputStream(respString.getBytes());
//		try {
//			sUnmarshaller.unmarshalPollResult(resp);
//			fail("No Exception?");
//		} catch (UnmarshalException e) {
//			fail("Wrong Exception?");
//		} catch (IfmapErrorResult e) {
//			fail("Wrong Exception?");
//		} catch (EndSessionException e) {
//			//System.out.println(e.getMessage());
//		}
//	}
//
//	@Test
//	public void testMetadataUnmarshal() {
//			String resultItems[] = {
//					ipElement + arElement,
//					ipElement + arElement + "<metadata/>",
//					ipElement + arElement + "<metadata><justsomething/></metadata>",
//					ipElement + arElement + "<metadata><justsomething/><somethingmore/></metadata>",
//					ipElement + arElement + "<metadata><justsomething><somethingmore/></justsomething></metadata>"
//			};
//
//			int mdelements[] = {
//					0,
//					0,
//					1,
//					2,
//					1
//			};
//
//			List<String> resultItemList = new LinkedList<String>();
//
//			for (String ri : resultItems)
//				resultItemList.add(ri);
//
//			InputStream resp = sRespCreator.createSearchResultResponse(resultItemList);
//			try {
//				SearchResult sr = sUnmarshaller.unmarshalSearchResult(resp);
//				assertEquals(5, sr.getResultItems().size());
//				Iterator<ResultItem> rit = sr.getResultItems().iterator();
//
//				for (int i = 0; i < sr.getResultItems().size(); i++) {
//					ResultItem ri = rit.next();
//					assertEquals(mdelements[i], ri.getMetadata().size());
//					assertNotNull(ri.getIdentifier1());
//					assertNotNull(ri.getIdentifier2());
//				}
//
//			} catch (UnmarshalException e) {
//				fail(e.getMessage());
//			} catch (IfmapErrorResult e) {
//				fail(e.getMessage());
//			}
//	}
//
//	@Test
//	public void testNoElementInResponse() {
//		String emptyResponse =  sRespCreator.createProlog() + sRespCreator.createEpilog();
//		failAllUnmarshalMethods(emptyResponse);
//	}
//
//	@Test
//	public void testNoMultipleElementsInResponse() {
//		String emptyResponse =  sRespCreator.createProlog() +
//		"<endSessionResult/>\n<subscribeReceived/>"
//		+ sRespCreator.createEpilog();
//		failAllUnmarshalMethods(emptyResponse);
//	}
//
//	private void failAllUnmarshalMethods(String response) {
//		for (Method m : sUnmarshaller.getClass().getMethods()) {
//			if (m.getName().startsWith("check") || m.getName().startsWith("unmarshal")) {
//				//System.out.print("TRYING: " + m.getName() + "... ");
//				InputStream is = new ByteArrayInputStream(response.getBytes());
//				try {
//					m.invoke(sUnmarshaller, is);
//					fail("No exception for " + m.getName());
//				} catch (IllegalArgumentException e) {
//					fail("Uhm");
//				} catch (IllegalAccessException e) {
//					fail("Uhm");
//				} catch (InvocationTargetException e) {
//					if (!(e.getCause() instanceof UnmarshalException)) {
//						fail("Exception not UnmarshalException... " + e.getCause().getMessage());
//					} else {
//						//System.out.println(e.getCause().getMessage());
//					}
//
//				}
//			}
//		}
//	}
//}
