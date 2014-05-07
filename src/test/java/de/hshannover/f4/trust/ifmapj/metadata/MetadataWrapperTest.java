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
