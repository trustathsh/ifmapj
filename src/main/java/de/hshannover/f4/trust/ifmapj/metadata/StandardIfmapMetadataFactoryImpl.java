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

import de.hshannover.f4.trust.ifmapj.binding.IfmapStrings;
import de.hshannover.f4.trust.ifmapj.log.IfmapJLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 * Simple implementation of the {@link StandardIfmapMetadataFactory} interface.
 *
 * @author aw
 * @author ib
 *
 */
public class StandardIfmapMetadataFactoryImpl implements StandardIfmapMetadataFactory {

	private DocumentBuilder mDocumentBuilder;

	public StandardIfmapMetadataFactoryImpl() {
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
	public Document createIpMac(String startTime, String endTime,
			String dhcpServer) {
		Document doc = createStdSingleElementDocument("ip-mac",
				Cardinality.multiValue);

		Element root = (Element) doc.getFirstChild();

		appendTextElementIfNotNull(doc, root, "start-time", startTime);
		appendTextElementIfNotNull(doc, root, "end-time", endTime);
		appendTextElementIfNotNull(doc, root, "dhcp-server", dhcpServer);

		return doc;
	}

	@Override
	public Document createIpMac() {
		return createIpMac(null, null, null);
	}

	@Override
	public Document createArMac() {
		return createStdSingleElementDocument("access-request-mac",
				Cardinality.singleValue);
	}

	@Override
	public Document createArDev() {
		return createStdSingleElementDocument("access-request-device",
				Cardinality.singleValue);
	}

	@Override
	public Document createArIp() {
		return createStdSingleElementDocument("access-request-ip",
				Cardinality.singleValue);
	}

	@Override
	public Document createAuthAs() {
		return createStdSingleElementDocument("authenticated-as",
				Cardinality.singleValue);
	}

	@Override
	public Document createAuthBy() {
		return createStdSingleElementDocument("authenticated-by",
				Cardinality.singleValue);
	}

	@Override
	public Document createDevIp() {
		return createStdSingleElementDocument("device-ip",
				Cardinality.singleValue);
	}

	@Override
	public Document createDiscoveredBy() {
		return createStdSingleElementDocument("discovered-by",
				Cardinality.singleValue);
	}

	@Override
	public Document createRole(String name, String administrativeDomain) {
		Document doc = createStdSingleElementDocument("role",
				Cardinality.multiValue);
		Element root = (Element) doc.getFirstChild();
		appendTextElementIfNotNull(doc, root, "administrative-domain",
				administrativeDomain);
		createAndAppendTextElementCheckNull(doc, root, "name", name);
		return doc;
	}

	@Override
	public Document createRole(String name) {
		return createRole(name, null);
	}

	@Override
	public Document createDevAttr(String name) {
		Document doc = createStdSingleElementDocument("device-attribute",
				Cardinality.multiValue);
		Element root = (Element) doc.getFirstChild();
		createAndAppendTextElementCheckNull(doc, root, "name", name);
		return doc;
	}

	@Override
	public Document createCapability(String name, String administrativeDomain) {
		Document doc = createStdSingleElementDocument("capability",
				Cardinality.multiValue);
		Element root = (Element) doc.getFirstChild();

		createAndAppendTextElementCheckNull(doc, root, "name", name);
		appendTextElementIfNotNull(doc, root, "administrative-domain",
				administrativeDomain);

		return doc;
	}

	@Override
	public Document createCapability(String name) {
		return createCapability(name, null);
	}

	@Override
	public Document createDevChar(String manufacturer, String model, String os,
			String osVersion, String deviceType, String discoveredTime,
			String discovererId, String discoveryMethod) {
		Document doc = createStdSingleElementDocument("device-characteristic",
				Cardinality.multiValue);
		Element root = (Element) doc.getFirstChild();

		appendTextElementIfNotNull(doc, root, "manufacturer", manufacturer);
		appendTextElementIfNotNull(doc, root, "model", model);
		appendTextElementIfNotNull(doc, root, "os", os);
		appendTextElementIfNotNull(doc, root, "os-version", osVersion);
		appendTextElementIfNotNull(doc, root, "device-type", deviceType);

		createAndAppendTextElementCheckNull(doc, root, "discovered-time", discoveredTime);
		createAndAppendTextElementCheckNull(doc, root, "discoverer-id", discovererId);
		createAndAppendTextElementCheckNull(doc, root, "discovery-method", discoveryMethod);

		return doc;
	}

	@Override
	public Document createEnforcementReport(
			EnforcementAction enforcementAction, String otherTypeDefinition,
			String enforcementReason) {
		Document doc = createStdSingleElementDocument("enforcement-report",
				Cardinality.multiValue);
		Element root = (Element) doc.getFirstChild();

		createAndAppendTextElementCheckNull(doc, root, "enforcement-action", enforcementAction);

		// TODO Do we need to check consistency with the action?
		appendTextElementIfNotNull(doc, root, "other-type-definition", otherTypeDefinition);
		appendTextElementIfNotNull(doc, root, "enforcement-reason", enforcementReason);

		return doc;
	}

	@Override
	public Document createEvent(String name, String discoveredTime,
			String discovererId, Integer magnitude, Integer confidence,
			Significance significance, EventType type,
			String otherTypeDefinition, String information,
			String vulnerabilityUri) {
		Document doc = createStdSingleElementDocument("event",
				Cardinality.multiValue);
		Element root = (Element) doc.getFirstChild();


		createAndAppendTextElementCheckNull(doc, root, "name", name);
		createAndAppendTextElementCheckNull(doc, root, "discovered-time", discoveredTime);
		createAndAppendTextElementCheckNull(doc, root, "discoverer-id", discovererId);
		// TODO add checks for magnitude and confidence for correct range?
		createAndAppendTextElementCheckNull(doc, root, "magnitude", magnitude);
		createAndAppendTextElementCheckNull(doc, root, "confidence", confidence);
		createAndAppendTextElementCheckNull(doc, root, "significance", significance);

		appendTextElementIfNotNull(doc, root, "type", type);
		// TODO Do we need to check consistency with the type?
		appendTextElementIfNotNull(doc, root, "other-type-definition", otherTypeDefinition);
		appendTextElementIfNotNull(doc, root, "information", information);
		// TODO Do we need to check consistency with the type?
		appendTextElementIfNotNull(doc, root, "vulnerability-uri", vulnerabilityUri);

		return doc;
	}

	@Override
	public Document create(String elementName, String qualifiedName, String uri,
			Cardinality cardinality) {
		return create(elementName, qualifiedName, uri, cardinality,
			new HashMap<String, String>());
	}

	@Override
	public Document create(String elementName, String qualifiedName, String uri,
			Cardinality cardinality, String attrName, String attrValue) {
		HashMap<String, String> attributes = new HashMap<String, String>();
		attributes.put(attrName, attrValue);
		return create(elementName, qualifiedName, uri, cardinality, attributes);
	}

	@Override
	public Document create(String elementName, String qualifiedName, String uri,
			Cardinality cardinality, HashMap<String, String> attributes) {
		Document doc = mDocumentBuilder.newDocument();
		Element e = doc.createElementNS(uri, qualifiedName + ":" + elementName);
		e.setAttributeNS(null, "ifmap-cardinality", cardinality.toString());
		for (Map.Entry<String, String> attr : attributes.entrySet()) {
			e.setAttributeNS(null, attr.getKey(), attr.getValue());
		}
		doc.appendChild(e);
		doc.createAttributeNS(uri, qualifiedName);
		return doc;
	}

	@Override
	public Document createLayer2Information(Integer vlanNum, String vlanName,
			Integer port, String administrativeDomain) {
		Document doc = createStdSingleElementDocument("layer2-information",
				Cardinality.multiValue);
		Element root = (Element) doc.getFirstChild();

		/* non of them is mandatory */
		appendTextElementIfNotNull(doc, root, "vlan", vlanNum);
		appendTextElementIfNotNull(doc, root, "vlan-name", vlanName);
		appendTextElementIfNotNull(doc, root, "port", port);
		appendTextElementIfNotNull(doc, root, "administrative-domain",
				administrativeDomain);

		return doc;
	}

	@Override
	public Document createLocation(List<LocationInformation> locationInformation,
			String discoveredTime, String discovererId) {
		Document doc = createStdSingleElementDocument("location",
				Cardinality.multiValue);
		Element root = (Element) doc.getFirstChild();

		if (locationInformation == null || locationInformation.size() == 0) {
			throw new NullPointerException("location-information needs to be set for location");
		}

		for (LocationInformation tmpLoc : locationInformation) {
			appendElementWithAttributes(doc, root, "location-information",
					"type", tmpLoc.mType, "value", tmpLoc.mValue);
		}

		createAndAppendTextElementCheckNull(doc, root, "discovered-time", discoveredTime);
		createAndAppendTextElementCheckNull(doc, root, "discoverer-id", discovererId);

		return doc;
	}


	@Override
	public Document createRequestForInvestigation(String qualifier) {
		Document doc = createStdSingleElementDocument("request-for-investigation",
				Cardinality.multiValue);
		Element root = (Element) doc.getFirstChild();

		if (qualifier != null) {
			root.setAttributeNS(null, "qualifier", qualifier);
		}

		return doc;
	}

	@Override
	public Document createWlanInformation(String ssid,
			List<WlanSecurityType> ssidUnicastSecurity,
			WlanSecurityType ssidGroupSecurity,
			List<WlanSecurityType> ssidManagementSecurity) {
		Document doc = createStdSingleElementDocument("wlan-information",
				Cardinality.singleValue);
		Element root = (Element) doc.getFirstChild();
		Element ssidUniSecEl = null;
		Element ssidMgmtSecEl = null;

		if (ssidUnicastSecurity == null || ssidUnicastSecurity.size() == 0) {
			throw new NullPointerException("ssid-unicast-security to be set"
					+ " for wlan-information");
		}

		if (ssidGroupSecurity == null) {
			throw new NullPointerException("ssid-group-security to be set for"
					+ " wlan-information");
		}

		if (ssidManagementSecurity == null || ssidManagementSecurity.size() == 0) {
			throw new NullPointerException("ssid-management-security to be set"
					+ " for wlan-information");
		}

		appendTextElementIfNotNull(doc, root, "ssid", ssid);

		for (WlanSecurityType wlanSec : ssidUnicastSecurity) {
			ssidUniSecEl = createAndAppendTextElementCheckNull(doc, root,
					"ssid-unicast-security", wlanSec.mWlanSecurityType.toString());

			if (wlanSec.mOtherTypeDefinition != null) {
				ssidUniSecEl.setAttributeNS(null, "other-type-definition",
						wlanSec.mOtherTypeDefinition);
			}
		}

		Element grpSecEl = createAndAppendTextElementCheckNull(doc, root,
				"ssid-group-security", ssidGroupSecurity.mWlanSecurityType.toString());

		if (ssidGroupSecurity.mOtherTypeDefinition != null) {
			grpSecEl.setAttributeNS(null, "other-type-definition",
					ssidGroupSecurity.mOtherTypeDefinition);
		}

		for (WlanSecurityType wlanSec : ssidManagementSecurity) {
			ssidMgmtSecEl = createAndAppendTextElementCheckNull(doc, root,
					"ssid-management-security", wlanSec.mWlanSecurityType.toString());

			if (wlanSec.mOtherTypeDefinition != null) {
				ssidMgmtSecEl.setAttributeNS(null, "other-type-definition",
						wlanSec.mOtherTypeDefinition);
			}
		}

		return doc;
	}

	@Override
	public Document createUnexpectedBehavior(String discoveredTime, String discovererId,
			Integer magnitude, Integer confidence, Significance significance,
			String type) {
		Document doc = createStdSingleElementDocument("unexpected-behavior",
				Cardinality.multiValue);
		Element root = (Element) doc.getFirstChild();

		createAndAppendTextElementCheckNull(doc, root, "discovered-time", discoveredTime);
		createAndAppendTextElementCheckNull(doc, root, "discoverer-id", discovererId);
		// TODO add checks for magnitude and confidence for correct range?
		createAndAppendTextElementCheckNull(doc, root, "magnitude", magnitude);
		createAndAppendTextElementCheckNull(doc, root, "confidence", confidence);
		createAndAppendTextElementCheckNull(doc, root, "significance", significance);

		appendTextElementIfNotNull(doc, root, "type", type);

		return doc;
	}

	@Override
	public Document createClientTime(String time) {
		// Generate XML within OPERATIONAL-METADATA namespace (ifmap 2.1)
		Document doc = createOpSingleElementDocument("client-time",
				Cardinality.singleValue);
		Element root = (Element) doc.getFirstChild();
		root.setAttribute("current-timestamp", time);
		return doc;
	}

	private Document createStdSingleElementDocument(String name, Cardinality card) {
		return createSingleElementDocument(
				IfmapStrings.STD_METADATA_PREFIX + ":" + name,
				IfmapStrings.STD_METADATA_NS_URI,
				card);
	}

	private Document createOpSingleElementDocument(String name, Cardinality card) {
		return createSingleElementDocument(
				IfmapStrings.OP_METADATA_PREFIX + ":" + name,
				IfmapStrings.OP_METADATA_NS_URI,
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
	 * {@link Element} given by parent if the given value is non-null.
	 * The new {@link Element} will have {@link Text} node containing value.
	 *
	 * @param doc {@link Document} where parent is located in
	 * @param parent where to append the new element
	 * @param elName the name of the new element.
	 * @param value the value of the {@link Text} node appended to the new element,
	 *	using toString() on the object.
	 */
	private void appendTextElementIfNotNull(Document doc, Element parent,
			String elName, Object value) {

			if (value == null) {
				return;
			}

			createAndAppendTextElementCheckNull(doc, parent, elName, value);
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

	/**
	 * Helper to create an {@link Element} without a namespace in
	 * {@link Document} doc and append it to the {@link Element} given by
	 * parent. Further, add all attributes to the newly created {@link Element}
	 * specified by attrList.
	 *
	 * @param doc the target {@link Document}
	 * @param parent the parent {@link Element}
	 * @param elName the name of the new {@link Element}
	 * @param attrList name value pairs of attributes
	 * @return the new {@link Element}
	 */
	private Element appendElementWithAttributes(Document doc, Element parent,
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
