package de.hshannover.f4.trust.ifmapj.metadata;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

import de.hshannover.f4.trust.ifmapj.log.IfmapJLog;

/**
 * Factory for {@link Metadata} wrapper instances.
 */
public class MetadataWrapper {

	private static final XPathFactory XPATH_FACTORY = XPathFactory.newInstance();

	/**
	 * Create a {@link Metadata} instance for the given document.
	 *
	 * @param document a metadata document
	 * @return the wrapped metadata
	 */
	public static Metadata metadata(Document document) {
		return new MetadataWrapperImpl(document, XPATH_FACTORY.newXPath());
	}

	private static class MetadataWrapperImpl implements Metadata {

		// TODO add lazy initialized attributes for publisherId, publishTimestamp, ...

		Document mDocument;
		XPath mXpath;

		public MetadataWrapperImpl(Document document, XPath xpath) {
			mDocument = document;
			mXpath = xpath;
		}

		/*
		 * Evaluate the given XPATH expression on the given document. Return
		 * the result as a string or null if an error occurred.
		 */
		private String getValueFromExpression(String expression, Document doc) {
			try {
				return mXpath.evaluate(expression, mDocument.getDocumentElement());
			} catch (XPathExpressionException e) {
				IfmapJLog.error("could not evaluate '"+expression+"' on '"+mDocument+"'");
				return null;
			}
		}

		@Override
		public String getPublisherId() {
			return getValueFromExpression("/*/@ifmap-publisher-id", mDocument);
		}

		@Override
		public String getPublishTimestamp() {
			return getValueFromExpression("/*/@ifmap-timestamp", mDocument);
		}

		@Override
		public String getTypename() {
			return getValueFromExpression("name(/*)", mDocument);
		}

		@Override
		public String getLocalname() {
			return getValueFromExpression("local-name(/*)", mDocument);
		}

		@Override
		public String getCardinality() {
			return getValueFromExpression("/*/@ifmap-cardinality", mDocument);
		}

		@Override
		public boolean isSingleValue() {
			return getCardinality().equals("singleValue");
		}

		@Override
		public boolean isMultiValue() {
			return getCardinality().equals("multiValue");
		}

	}

}
