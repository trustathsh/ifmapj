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
 * This file is part of ifmapj, version 2.3.0, implemented by the Trust@HsH
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
package de.hshannover.f4.trust.ifmapj.result;

/**
 * Trivial tests for class {@link SearchResult}.
 *
 * @author ibente
 *
 * TODO: It doesn't really work like that anymore... :(
 *
 * ResultFactory is now used to create Results which are visible
 * for the user. It is not possible to modify these results after
 * they have been created...
 * I'm too tired right now to write real tests...
 *
 * FIXME: It's still no real test.
 *
 *
public class SearchResultTest {

	private ResultFactory rf = new ResultFactoryImpl();
	private StandardIfmapMetadataFactory mf = IfmapJ.createStandardMetadataFactory();

	@Test (expected=NullPointerException.class)
	public void testAddResultItemNull() {
		SearchResult sr = rf.createSearchRes(null);
		sr.getName();
	}

	@Test
	public void testAddResultItem() {
		IdentifierFactory iFactory = IfmapJ.createIdentifierFactory();
		List<ResultItem> rlist = new LinkedList<ResultItem>();
		List<Document> mdlist = new LinkedList<Document>();

		IpAddress ip = iFactory.createIp4("192.168.0.1");
		MacAddress mac = iFactory.createMac("11:22:33:aa:bb:cc");
		mdlist.add(mf.createIpMac());

		ResultItem ri = rf.createResultItem(ip, mac, mdlist);
		ri.addMetadata(mf.createIpMac());
		rlist.add(ri);

		SearchResult sr = rf.createSearchRes(rlist);

		assertTrue(ri.holdsLink());
		assertEquals(1, sr.getResultItems().size());
		assertTrue(sr.getResultItems().contains(ri));
		assertEquals(2, sr.getResultItems().iterator().next().getMetadata().size());
		assertTrue(sr.getResultItems().iterator().next().holdsLink());
	}
}
*/
