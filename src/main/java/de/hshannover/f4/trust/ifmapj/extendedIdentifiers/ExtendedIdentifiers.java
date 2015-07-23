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
package de.hshannover.f4.trust.ifmapj.extendedIdentifiers;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.hshannover.f4.trust.ifmapj.exception.MarshalException;
import de.hshannover.f4.trust.ifmapj.identifier.Identifiers;
import de.hshannover.f4.trust.ifmapj.identifier.Identity;
import de.hshannover.f4.trust.ifmapj.log.IfmapJLog;

/**
 * Static class that helps to create Extended Identifiers.
 *
 * @author Bastian Hellmann
 *
 */
public class ExtendedIdentifiers {

	private ExtendedIdentifiers() {
	}

	private static DocumentBuilder mDocumentBuilder;

	static {
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
	 * Creates an Extended Identifier.
	 *
	 * @param namespaceUri
	 *            URI of the namespace of the inner XML of the Extended Identifier
	 * @param namespacePrefix
	 *            prefix of the namespace of the inner XML of the Extended Identifier
	 * @param identifierName
	 *            the name value of the Extended Identifier (will be the root node of the inner XML)
	 * @param attributeValue
	 *            a value for the attribute <i>name</i>
	 * @param administrativeDomain
	 *            the value of the administrativeDomain attribute
	 * @return an {@link Identity} instance encapsulating the Extended Identifier
	 */
	public static Identity createExtendedIdentifier(String namespaceUri, String namespacePrefix, String identifierName,
			String attributeValue,
			String administrativeDomain) {
		Document doc = mDocumentBuilder.newDocument();
		Element e = doc.createElementNS(namespaceUri,
				namespacePrefix
						+ ":" + identifierName);
		if (attributeValue != null) {
			e.setAttribute("name", attributeValue);
		}
		e.setAttribute("administrative-domain", administrativeDomain);
		doc.appendChild(e);
		try {
			return Identifiers.createExtendedIdentity(doc);
		} catch (MarshalException e1) {
			IfmapJLog.error("document that contains the extended identifier can't be handled");
			throw new RuntimeException(e1);
		}
	}

	/**
	 * Creates an Extended Identifier; uses an empty string for the administrativeDomain attribute.
	 *
	 * @param namespaceUri
	 *            URI of the namespace of the inner XML of the Extended Identifier
	 * @param namespacePrefix
	 *            prefix of the namespace of the inner XML of the Extended Identifier
	 * @param identifierName
	 *            the name value of the Extended Identifier (will be the root node of the inner XML)
	 * @param attributeValue
	 *            a value for the attribute <i>name</i>
	 * @return an {@link Identity} instance encapsulating the Extended Identifier
	 */
	public static Identity createExtendedIdentifier(String namespaceUri, String namespacePrefix, String identifierName,
			String attributeValue) {
		return createExtendedIdentifier(namespaceUri, namespacePrefix, identifierName, attributeValue, "");
	}

	/**
	 * Creates an Extended Identifier; uses an empty string for the administrativeDomain attribute and does not add an
	 * attribute <i>name</i>.
	 *
	 * @param namespaceUri
	 *            URI of the namespace of the inner XML of the Extended Identifier
	 * @param namespacePrefix
	 *            prefix of the namespace of the inner XML of the Extended Identifier
	 * @param identifierName
	 *            the name value of the Extended Identifier (will be the root node of the inner XML)
	 * @return an {@link Identity} instance encapsulating the Extended Identifier
	 */
	public static Identity createExtendedIdentifier(String namespaceUri, String namespacePrefix, String identifierName) {
		return createExtendedIdentifier(namespaceUri, namespacePrefix, identifierName, null, "");
	}

}
