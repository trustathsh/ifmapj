package de.fhhannover.inform.trust.ifmapj.result;

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
