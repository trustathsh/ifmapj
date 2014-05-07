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
package de.hshannover.f4.trust.ifmapj.metadata;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
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

import de.hshannover.f4.trust.ifmapj.log.IfmapJLog;

/**
 * Factory for {@link Metadata} wrapper instances.
 */
public class MetadataWrapper {

	private static final XPathFactory XPATH_FACTORY = XPathFactory.newInstance();

	private static final TransformerFactory TRANSFORMER_FACTORY =
			TransformerFactory.newInstance();

	/**
	 * Create a {@link Metadata} instance for the given document.
	 *
	 * @param document a metadata document
	 * @return the wrapped metadata
	 */
	public static Metadata metadata(Document document) {
		try {
			Transformer transformer = TRANSFORMER_FACTORY.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			return new MetadataWrapperImpl(
					document, XPATH_FACTORY.newXPath(), transformer);
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

		Document mDocument;
		XPath mXpath;
		Transformer mTransformer;

		/**
		 * Create a wrapper instance for the given document.
		 *
		 * @param document the document to wrap
		 * @param xpath the XPATH instance for this wrapper
		 */
		public MetadataWrapperImpl(
				Document document, XPath xpath, Transformer transformer) {
			mDocument = document;
			mXpath = xpath;
			mTransformer = transformer;
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
		public String toFormattedString() {
			StringWriter writer = new StringWriter();
			try {
				mTransformer.transform(
						new DOMSource(mDocument), new StreamResult(writer));
			} catch (TransformerException e) {
				throw new RuntimeException(e);
			}
			return writer.toString();
		}

	}

}
