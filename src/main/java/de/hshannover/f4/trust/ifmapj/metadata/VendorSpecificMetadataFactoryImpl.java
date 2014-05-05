package de.hshannover.f4.trust.ifmapj.metadata;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * A implementation of the {@link VendorSpecificMetadataFactory} which wraps the
 * creation of {@link Document}s.
 *
 */
public class VendorSpecificMetadataFactoryImpl implements VendorSpecificMetadataFactory {

	private DocumentBuilder mDocumentBuilder;

	public VendorSpecificMetadataFactoryImpl() {
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		try {
			mDocumentBuilder = builderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new RuntimeException("could not create document builder", e);
		}
	}

	@Override
	public Document createMetadata(String content) {
		StringReader reader = new StringReader(content);
		InputSource input = new InputSource(reader);
		try {
			return mDocumentBuilder.parse(input);
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"could not parse '"+content+"' as XML", e);
		}
	}


}
