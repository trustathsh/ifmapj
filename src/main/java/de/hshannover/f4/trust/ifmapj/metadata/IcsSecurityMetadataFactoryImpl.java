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
 * This file is part of ifmapj, version 2.2.2, implemented by the Trust@HsH
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

import de.hshannover.f4.trust.ifmapj.binding.IfmapStrings;
import de.hshannover.f4.trust.ifmapj.log.IfmapJLog;

/**
 * Simple implementation of the {@link IcsSecurityMetadataFactory} interface.
 *
 * @author pe
 *
 */
public class IcsSecurityMetadataFactoryImpl implements IcsSecurityMetadataFactory {

	private DocumentBuilder mDocumentBuilder;

	/**
	 * Constructor
	 * Create a {@link IcsSecurityMetadataFactoryImpl}
	 */
	public IcsSecurityMetadataFactoryImpl() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		try {
			mDocumentBuilder = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			IfmapJLog.error("Could not get DocumentBuilder instance [" + e.getMessage() + "]");
			throw new RuntimeException(e);
		}
	}

	@Override
	public Document createBackhlPol(String netwName, String policy) {
		Document doc = createStdSingleElementDocument("backhaul-policy",
				Cardinality.multiValue);

		Element root = (Element) doc.getFirstChild();

		createAndAppendTextElementCheckNull(doc, root, "name", netwName);
		createAndAppendTextElementCheckNull(doc, root, "policy", policy);

		return doc;
	}

	@Override
	public Document createBhiAdd() {
		return createStdSingleElementDocument("bhi-address", Cardinality.singleValue);
	}

	@Override
	public Document createBhiCert(String certificate) {
		Document doc = createStdSingleElementDocument("bhi-certificate",
				Cardinality.multiValue);

		Element root = (Element) doc.getFirstChild();

		createAndAppendTextElementCheckNull(doc, root, "data", certificate);

		return doc;
	}

	@Override
	public Document createBhiIdent() {
		return createStdSingleElementDocument("bhi-identity", Cardinality.singleValue);
	}

	@Override
	public Document createDnHit() {
		return createStdSingleElementDocument("dn-hit", Cardinality.singleValue);
	}

	@Override
	public Document createGrpXref(String ldapUri) {
		Document doc = createStdSingleElementDocument("group-xref",
				Cardinality.multiValue);

		Element root = (Element) doc.getFirstChild();

		createAndAppendTextElementCheckNull(doc, root, "uri", ldapUri);

		return doc;
	}

	@Override
	public Document createMngOf() {
		return createStdSingleElementDocument("manager-of", Cardinality.singleValue);
	}

	@Override
	public Document createMembOf() {
		return createStdSingleElementDocument("member-of", Cardinality.singleValue);
	}

	@Override
	public Document createObsBy() {
		return createStdSingleElementDocument("observed-by", Cardinality.multiValue);
	}

	@Override
	public Document createOverlPol(String netwName, String policy) {
		Document doc = createStdSingleElementDocument("overlay-policy",
				Cardinality.multiValue);

		Element root = (Element) doc.getFirstChild();

		createAndAppendTextElementCheckNull(doc, root, "name", netwName);
		createAndAppendTextElementCheckNull(doc, root, "policy", policy);

		return doc;
	}

	@Override
	public Document createProtBy() {
		return createStdSingleElementDocument("protected-by", Cardinality.multiValue);
	}

	private Document createStdSingleElementDocument(String name, Cardinality card) {
		return createSingleElementDocument(
				IfmapStrings.ICS_METADATA_PREFIX + ":" + name,
				IfmapStrings.ICS_METADATA_NS_URI,
				card);
	}

	private Document createSingleElementDocument(String qualifiedName,
			String uri, Cardinality cardinality) {
		Document doc = mDocumentBuilder.newDocument();
		Element e = doc.createElementNS(uri, qualifiedName);
		e.setAttributeNS(null, "ifmap-cardinality", cardinality.toString());
		doc.appendChild(e);
		return doc;
	}

	/**
	 * Helper to create a new element with name elName and append it to the
	 * {@link Element} given by parent. The new {@link Element} will have
	 * {@link Text} node containing value.
	 *
	 * @param doc {@link Document} where parent is located in
	 * @param parent where to append the new element
	 * @param elName the name of the new element.
	 * @param value the value of the {@link Text} node appended to the new element,
	 *	using toString() on the object.
	 *	 is null
	 * @return the new {@link Element}
	 */
	private Element createAndAppendTextElementCheckNull(Document doc, Element parent,
			String elName, Object value) {

		if (doc == null || parent == null || elName == null) {
			throw new NullPointerException("bad parameters given");
		}

		if (value == null) {
			throw new NullPointerException("null is not allowed for " + elName
					+ " in " + doc.getFirstChild().getLocalName());
		}

		String valueStr = value.toString();
		if (valueStr == null) {
			throw new NullPointerException("null-string not allowed for " + elName
					+ " in " + doc.getFirstChild().getLocalName());
		}

		Element child = createAndAppendElement(doc, parent, elName);
		Text txtcElement = doc.createTextNode(valueStr);
		child.appendChild(txtcElement);
		return child;
	}

	/**
	 * Helper to create an {@link Element} without a namespace in
	 * {@link Document} doc and append it to the {@link Element} given by
	 * parent.
	 *
	 * @param doc the target {@link Document}
	 * @param parent the parent {@link Element}
	 * @param elName the name of the new {@link Element}
	 * @return the new {@link Element}
	 */
	private Element createAndAppendElement(Document doc, Element parent, String elName) {
		Element el = doc.createElementNS(null, elName);
		parent.appendChild(el);
		return el;
	}

}
