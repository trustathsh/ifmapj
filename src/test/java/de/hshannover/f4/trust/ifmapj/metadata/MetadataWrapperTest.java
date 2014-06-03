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
 * This file is part of ifmapj, version 2.0.1, implemented by the Trust@HsH
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
package de.hshannover.f4.trust.ifmapj.metadata;

import org.junit.Test;
import org.w3c.dom.Document;
import static de.hshannover.f4.trust.ifmapj.metadata.MetadataWrapper.*;

import static org.junit.Assert.*;

public class MetadataWrapperTest {

	private VendorSpecificMetadataFactory vendorFactory = new VendorSpecificMetadataFactoryImpl();
	private StandardIfmapMetadataFactory standardFactory = new StandardIfmapMetadataFactoryImpl();

	private Metadata stringToMetadata(String xmlString) {
		return metadata(vendorFactory.createMetadata(xmlString));
	}

	private String publishedMetadataXml = "<foo:bar "
			+ "xmlns:foo=\"http://foo.org\" "
			+ "ifmap-publisher-id=\"adb02031-35ec-4bde-b066-a080341aa66f\" "
			+ "ifmap-timestamp=\"2011-10-27T23:51:42Z\" "
			+ "ifmap-cardinality=\"singleValue\""
			+ "/>";

	private String unPublishedMetadataXml = "<foo:bar "
			+ "xmlns:foo=\"http://foo.org\" "
			+ "ifmap-cardinality=\"singleValue\""
			+ "/>";

	private Document event = standardFactory.createEvent(
			"event42",
			"2011-10-27T23:51:42Z",
			"sensor42",
			42,
			42,
			Significance.critical,
			EventType.policyViolation,
			null,
			null,
			null);

	@Test
	public void testGetExistingPublisherId() {
		Metadata metadata = stringToMetadata(publishedMetadataXml);
		assertEquals("adb02031-35ec-4bde-b066-a080341aa66f", metadata.getPublisherId());
	}

	@Test
	public void testGetNonExistingPublisherId() {
		Metadata metadata = stringToMetadata(unPublishedMetadataXml);
		assertEquals("", metadata.getPublisherId());
	}

	@Test
	public void testGetExistingPublishTimestamp() {
		Metadata metadata = stringToMetadata(publishedMetadataXml);
		assertEquals("2011-10-27T23:51:42Z", metadata.getPublishTimestamp());
	}

	@Test
	public void testGetNonExistingPublishTimestamp() {
		Metadata metadata = stringToMetadata(unPublishedMetadataXml);
		assertEquals("", metadata.getPublishTimestamp());
	}

	@Test
	public void testExtractExistingTypenameFromPrefixedMetadata() {
		Metadata metadata = stringToMetadata(publishedMetadataXml);
		assertEquals("foo:bar", metadata.getTypename());
	}

	@Test
	public void testExtractExistingLocalTypenameFromPrefixedMetadata() {
		Metadata metadata = stringToMetadata(publishedMetadataXml);
		assertEquals("bar", metadata.getLocalname());
	}

	@Test
	public void testExtractExistingCardinalityMetadata() {
		Metadata metadata = stringToMetadata(publishedMetadataXml);
		assertEquals("singleValue", metadata.getCardinality());
	}

	@Test
	public void testCardinalityOfMetadata() {
		Metadata metadata = stringToMetadata(publishedMetadataXml);
		assertTrue(metadata.isSingleValue());
		assertFalse(metadata.isMultiValue());
	}
}
