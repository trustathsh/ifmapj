package de.hshannover.f4.trust.ifmapj.metadata;

import org.w3c.dom.Document;

/**
 * Interface for vendor-specific metadata factories.
 *
 */
public interface VendorSpecificMetadataFactory {

	/**
	 * Create a metadata document from the given string. The string must include
	 * all necessary namespace declarations.
	 *
	 * @param content a metadata XML string
	 * @return the parsed metadata document
	 * @throws IllegalArgumentException if the given string can not be parsed as XML
	 */
	public Document createMetadata(String content);

}
