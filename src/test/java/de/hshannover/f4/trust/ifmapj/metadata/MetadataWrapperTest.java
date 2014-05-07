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

import static de.hshannover.f4.trust.ifmapj.metadata.MetadataWrapper.metadata;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;

import org.junit.Test;
import org.w3c.dom.Document;

import de.hshannover.f4.trust.ifmapj.binding.IfmapStrings;

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

	private Document ipMac = standardFactory.createIpMac(
			"2014-06-12T12:58:50+02:00",
			"2014-06-12T20:58:50+02:00",
			"dhcp-server-42");

	private String ipMacStringWithDifferentNamespace = "<foo:ip-mac "
			+ "ifmap-cardinality=\"multiValue\" "
			+ "xmlns:foo=\""+ IfmapStrings.STD_METADATA_NS_URI +"\">"
			+ "<start-time>2014-06-12T12:58:50+02:00</start-time>"
			+ "<end-time>2014-06-12T20:58:50+02:00</end-time>"
			+ "<dhcp-server>dhcp-server-42</dhcp-server>"
			+ "</foo:ip-mac>";

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

	@Test
	public void testExtractElementWithXPath() {
		Metadata metadata = metadata(ipMac);
		String dhcpServer = metadata.getValueForXpathExpression("/meta:ip-mac/dhcp-server");
		assertEquals("dhcp-server-42", dhcpServer);
	}

	@Test
	public void testExtractNonExistingElementWithXPath() {
		Metadata metadata = metadata(ipMac);
		String error = metadata.getValueForXpathExpression("/meta:ip-mac/foobar");
		assertEquals("", error);
	}

	@Test
	public void testInvalidXPathExpression() {
		Metadata metadata = metadata(ipMac);
		String error = metadata.getValueForXpathExpression("\\ERROR\\");
		assertNull(error);
	}

	@Test
	public void testExtractElementOrDefaultWithXPath() {
		Metadata metadata = metadata(ipMac);
		String dhcpServer = metadata.getValueForXpathExpressionOrElse("/meta:ip-mac/dhcp-server", "error");
		assertEquals("dhcp-server-42", dhcpServer);
	}

	@Test
	public void testExtractNonExistingOrDefaultElementWithXPath() {
		Metadata metadata = metadata(ipMac);
		String error = metadata.getValueForXpathExpressionOrElse("/meta:ip-mac/foobar", "error");
		assertEquals("", error);
	}

	@Test
	public void testDifferentPrefixesForXPathQuery() {
		Metadata metadata = metadata(ipMac);
		NamespaceContext context = new NamespaceContext() {

			@Override
			public Iterator getPrefixes(String namespaceURI) {
				return Arrays.asList("foo").iterator();
			}

			@Override
			public String getPrefix(String namespaceURI) {
				return "foo";
			}

			@Override
			public String getNamespaceURI(String prefix) {
				return IfmapStrings.STD_METADATA_NS_URI;
			}
		};
		metadata.setNamespaceContext(context);
		String dhcpServer = metadata.getValueForXpathExpression("/foo:ip-mac/dhcp-server");
		assertEquals("dhcp-server-42", dhcpServer);
	}

	@Test
	public void testCustomNamespacePrefixForXPathQuery() {
		Metadata metadata = stringToMetadata(ipMacStringWithDifferentNamespace);
		String dhcpServer = metadata.getValueForXpathExpression("/meta:ip-mac/dhcp-server");
		assertEquals("dhcp-server-42", dhcpServer);
	}
}
