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
package de.hshannover.f4.trust.ifmapj.metadata;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import de.hshannover.f4.trust.ifmapj.log.IfmapJLog;

/**
 * Abstract base class for metadata factories. Provides methods to create Document objects.
 *
 * @author Bastian Hellmann
 *
 */
public abstract class IfmapMetadataFactory {

	protected DocumentBuilder mDocumentBuilder;

	public IfmapMetadataFactory() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		try {
			mDocumentBuilder = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			IfmapJLog.error("Could not get DocumentBuilder instance ["
					+ e.getMessage() + "]");
			throw new RuntimeException(e);
		}
	}

	/**
	 * Creates a metadata document with a single element and given namespace information.
	 *
	 * @param namespaceUri
	 *            URI of the namespace of the metadata document
	 * @param namespacePrefix
	 *            prefix of the namespace of the metadata document
	 * @param name
	 *            value of the root element of the metadata document
	 * @param cardinality
	 *            the cardinality of the metadata document
	 * @return a {@link Document} instance representing the metadata object
	 */
	protected Document createSingleElementDocument(String namespaceUri, String namespacePrefix, String name,
			Cardinality cardinality) {
		Document doc = mDocumentBuilder.newDocument();
		String qualifiedName = namespacePrefix
				+ ":" + name;
		Element e = doc.createElementNS(namespaceUri, qualifiedName);
		e.setAttributeNS(null, "ifmap-cardinality", cardinality.toString());
		doc.appendChild(e);
		return doc;
	}

	/**
	 * Helper to create a new element with name elName and append it to the {@link Element} given by parent if the given
	 * value is non-null.
	 * The new {@link Element} will have {@link Text} node containing value.
	 *
	 * @param doc
	 *            {@link Document} where parent is located in
	 * @param parent
	 *            where to append the new element
	 * @param elName
	 *            the name of the new element.
	 * @param value
	 *            the value of the {@link Text} node appended to the new element,
	 *            using toString() on the object.
	 */
	protected void appendTextElementIfNotNull(Document doc, Element parent,
			String elName, Object value) {

		if (value == null) {
			return;
		}

		createAndAppendTextElementCheckNull(doc, parent, elName, value);
	}

	/**
	 * Helper to create a new element with name elName and append it to the {@link Element} given by parent. The new
	 * {@link Element} will have {@link Text} node containing value.
	 *
	 * @param doc
	 *            {@link Document} where parent is located in
	 * @param parent
	 *            where to append the new element
	 * @param elName
	 *            the name of the new element.
	 * @param value
	 *            the value of the {@link Text} node appended to the new element,
	 *            using toString() on the object.
	 *            is null
	 * @return the new {@link Element}
	 */
	protected Element createAndAppendTextElementCheckNull(Document doc, Element parent,
			String elName, Object value) {

		if (doc == null
				|| parent == null || elName == null) {
			throw new NullPointerException("bad parameters given");
		}

		if (value == null) {
			throw new NullPointerException("null is not allowed for "
					+ elName
					+ " in " + doc.getFirstChild().getLocalName());
		}

		String valueStr = value.toString();
		if (valueStr == null) {
			throw new NullPointerException("null-string not allowed for "
					+ elName
					+ " in " + doc.getFirstChild().getLocalName());
		}

		Element child = createAndAppendElement(doc, parent, elName);
		Text txtcElement = doc.createTextNode(valueStr);
		child.appendChild(txtcElement);
		return child;
	}

	/**
	 * Helper to create an {@link Element} without a namespace in {@link Document} doc and append it to the
	 * {@link Element} given by
	 * parent.
	 *
	 * @param doc
	 *            the target {@link Document}
	 * @param parent
	 *            the parent {@link Element}
	 * @param elName
	 *            the name of the new {@link Element}
	 * @return the new {@link Element}
	 */
	protected Element createAndAppendElement(Document doc, Element parent, String elName) {
		Element el = doc.createElementNS(null, elName);
		parent.appendChild(el);
		return el;
	}

	/**
	 * Helper to create an {@link Element} without a namespace in {@link Document} doc and append it to the
	 * {@link Element} given by
	 * parent. Further, add all attributes to the newly created {@link Element} specified by attrList.
	 *
	 * @param doc
	 *            the target {@link Document}
	 * @param parent
	 *            the parent {@link Element}
	 * @param elName
	 *            the name of the new {@link Element}
	 * @param attrList
	 *            name value pairs of attributes
	 * @return the new {@link Element}
	 */
	protected Element appendElementWithAttributes(Document doc, Element parent,
			String elName, String... attrList) {

		if (attrList.length % 2 != 0) {
			throw new RuntimeException("Bad attrList length");
		}

		Element el = createAndAppendElement(doc, parent, elName);

		for (int i = 0; i < attrList.length; i += 2) {
			el.setAttributeNS(null, attrList[i], attrList[i + 1]);
		}

		return el;
	}
}
