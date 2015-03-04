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
 * This file is part of ifmapj, version 2.2.1, implemented by the Trust@HsH
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
package de.hshannover.f4.trust.ifmapj.metadata;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import util.CanonicalXML;
import de.hshannover.f4.trust.ifmapj.binding.IfmapStrings;
import de.hshannover.f4.trust.ifmapj.log.IfmapJLog;

/**
 * Factory for {@link Metadata} wrapper instances.
 */
public class MetadataWrapper {

	private static final XPathFactory XPATH_FACTORY = XPathFactory.newInstance();

	private static final TransformerFactory TRANSFORMER_FACTORY =
			TransformerFactory.newInstance();

	/**
	 * Default namespace context which uses the prefixes 'meta' and 'ifmap'
	 * as specified in TNC IF-MAP Binding for SOAP version 2.2.
	 */
	public static final NamespaceContext DEFAULT_NAMESPACE_CONTEXT = new NamespaceContext() {

		@Override
		public Iterator getPrefixes(String namespaceURI) {
			return Arrays.asList(
					IfmapStrings.STD_METADATA_PREFIX,
					IfmapStrings.BASE_PREFIX)
						.iterator();
		}

		@Override
		public String getPrefix(String namespaceURI) {
			if (namespaceURI.equals(IfmapStrings.STD_METADATA_NS_URI)) {
				return IfmapStrings.STD_METADATA_PREFIX;
			} else if (namespaceURI.equals(IfmapStrings.BASE_NS_URI)) {
				return IfmapStrings.BASE_PREFIX;
			} else {
				return null;
			}
		}

		@Override
		public String getNamespaceURI(String prefix) {
			if (prefix.equals(IfmapStrings.STD_METADATA_PREFIX)) {
				return IfmapStrings.STD_METADATA_NS_URI;
			} else if (prefix.equals(IfmapStrings.BASE_PREFIX)) {
				return IfmapStrings.BASE_NS_URI;
			} else {
				return XMLConstants.NULL_NS_URI;
			}
		}
	};

	/**
	 * Create a {@link Metadata} instance for the given document.
	 *
	 * @param document a metadata document
	 * @return the wrapped metadata
	 */
	public static Metadata metadata(Document document) {
		try {
			Transformer printFormatTransformer = TRANSFORMER_FACTORY.newTransformer();
			printFormatTransformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			printFormatTransformer.setOutputProperty(OutputKeys.METHOD, "xml");
			printFormatTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
			printFormatTransformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			printFormatTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

			Transformer equalsTransformer = TRANSFORMER_FACTORY.newTransformer();
			equalsTransformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			equalsTransformer.setOutputProperty(OutputKeys.INDENT, "no");
			equalsTransformer.setOutputProperty(OutputKeys.METHOD, "xml");

			XPath xPath = XPATH_FACTORY.newXPath();

			return new MetadataWrapperImpl(
					document,
					xPath,
					printFormatTransformer,
					DEFAULT_NAMESPACE_CONTEXT,
					equalsTransformer);
		} catch (TransformerConfigurationException e) {
			throw new RuntimeException(e);
		}
	}


	/**
	 * Wrapper implementation which uses {@link XPath} to extract values
	 * from {@link Document} instances.
	 */
	private static class MetadataWrapperImpl implements Metadata {

		// TODO add lazy initialized attributes for publisherId, publishTimestamp, ...

		final Document mDocument;
		final XPath mXpath;
		final Transformer mPrintTransformer;
		final Transformer mEqualsTransformer;

		/**
		 * Create a wrapper instance for the given document.
		 *
		 * @param document the document to wrap
		 * @param xpath the XPATH instance for this wrapper
		 * @param printFormatTransformer the transformer to use for pretty printing
		 * @param namespaceContext the namespace context for XPath operations
		 * @param equalsTransformer the transformer to use for canonical serialization
		 */
		public MetadataWrapperImpl(
				Document document,
				XPath xpath,
				Transformer printFormatTransformer,
				NamespaceContext namespaceContext,
				Transformer equalsTransformer) {
			mDocument = document;
			mXpath = xpath;
			mPrintTransformer = printFormatTransformer;
			mXpath.setNamespaceContext(namespaceContext);
			mEqualsTransformer = equalsTransformer;
		}

		/*
		 * Evaluate the given XPATH expression on the given document. Return
		 * the result as a string or null if an error occurred.
		 */
		private String getValueFromExpression(String expression, Document doc) {
			try {
				return mXpath.evaluate(expression, mDocument.getDocumentElement());
			} catch (XPathExpressionException e) {
				IfmapJLog.error("could not evaluate '" + expression + "' on '" + mDocument + "'");
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
		public double getPublishTimestampFraction() {
			String fractionString = getValueFromExpression(
					"/*/@ifmap-timestamp-fraction", mDocument);
			try {
				return Double.parseDouble(fractionString);
			} catch (NumberFormatException e) {
				return 0.0;
			}
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

		@Override
		public String getValueForXpathExpression(String xPathExpression) {
			return getValueFromExpression(xPathExpression, mDocument);
		}

		@Override
		public String getValueForXpathExpressionOrElse(String xPathExpression,
				String defaultValue) {
			String result = getValueForXpathExpression(xPathExpression);
			if (result == null) {
				return defaultValue;
			} else {
				return result;
			}
		}

		@Override
		public String toFormattedString() {
			StringWriter writer = new StringWriter();
			try {
				mPrintTransformer.transform(
						new DOMSource(mDocument), new StreamResult(writer));
			} catch (TransformerException e) {
				throw new RuntimeException(e);
			}
			return writer.toString();
		}

		@Override
		public void setNamespaceContext(NamespaceContext context) {
			mXpath.setNamespaceContext(context);
		}

		private String toCanonicalXml() {
			try {
				XMLReader reader = XMLReaderFactory.createXMLReader();
				DOMSource domSource = new DOMSource(mDocument.getFirstChild());
				ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream();
				Result result = new StreamResult(byteArrayOutput);
				mEqualsTransformer.transform(domSource, result);
				ByteArrayInputStream byteArrayInput = new ByteArrayInputStream(byteArrayOutput.toByteArray());
				InputSource input = new InputSource(byteArrayInput);
				CanonicalXML canonicalXml = new CanonicalXML();
				return canonicalXml.toCanonicalXml2(reader, input, true);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ toCanonicalXml().hashCode();
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			MetadataWrapperImpl other = (MetadataWrapperImpl) obj;
			return other.toCanonicalXml().equals(toCanonicalXml());
		}
	}

}
