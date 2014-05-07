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
 * This file is part of ifmapj, version 2.0.0, implemented by the Trust@HsH
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
package de.hshannover.f4.trust.ifmapj.identifier;

import de.hshannover.f4.trust.ifmapj.binding.IfmapStrings;
import de.hshannover.f4.trust.ifmapj.exception.MarshalException;
import de.hshannover.f4.trust.ifmapj.exception.UnmarshalException;
import de.hshannover.f4.trust.ifmapj.identifier.Identifiers.Helpers;
import de.hshannover.f4.trust.ifmapj.log.IfmapJLog;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import util.DomHelpers;
import util.StringHelpers;

/**
 * Provides access to {@link Identifier} associated classes.
 *
 * @since 0.1.4
 * @author aw
 */
public final class Identifiers {


	@SuppressWarnings("deprecation")
	private static IdentifierFactory sIdentifierFactoryInstance;
	private static Map<Class<? extends Identifier>,
			IdentifierHandler<? extends Identifier>> sIdentifierHandlers;

	private Identifiers() { }

	@SuppressWarnings("deprecation")
	public static IdentifierFactory getIdentifierFactory() {
		if (sIdentifierFactoryInstance == null) {
			sIdentifierFactoryInstance = new IdentifierFactoryImpl();
		}

		return sIdentifierFactoryInstance;
	}

	public static void registerIdentifierHandler(IdentifierHandler<? extends Identifier> ih) {
		if (ih == null) {
			throw new NullPointerException("ih is null");
		}

		if (ih.handles() == null) {
			throw new NullPointerException("ih.handles() returns null");
		}

		if (sIdentifierHandlers == null) {
			initializeDefaultHandlers();
		}

		if (sIdentifierHandlers.containsKey(ih.handles())) {
			throw new RuntimeException("IdentifierHandler already registered for "
					+ ih.handles());
		}

		sIdentifierHandlers.put(ih.handles(), ih);
	}

	public static IdentifierHandler<? extends Identifier> getHandlerFor(Identifier i) {

		if (sIdentifierHandlers == null) {
			initializeDefaultHandlers();
		}

		for (Entry<Class<? extends Identifier>,
				IdentifierHandler<? extends Identifier>> entry
				: sIdentifierHandlers.entrySet()) {
			if (entry.getKey().isInstance(i)) {
				return entry.getValue();
			}
		}

		return null;
	}

	private static void initializeDefaultHandlers() {
		sIdentifierHandlers = new HashMap<Class<? extends Identifier>,
				IdentifierHandler<? extends Identifier>>();

		registerIdentifierHandler(new AccessRequestHandler());
		registerIdentifierHandler(new DeviceHandler());
		registerIdentifierHandler(new IdentityHandler());
		registerIdentifierHandler(new IpAddressHandler());
		registerIdentifierHandler(new MacAddressHandler());
	}

	public static Identifier tryFromElement(Element el)
			throws UnmarshalException {
		Identifier ret = null;

		if (sIdentifierHandlers == null) {
			initializeDefaultHandlers();
		}

		for (IdentifierHandler<? extends Identifier> handler
				: sIdentifierHandlers.values()) {

			ret = handler.fromElement(el);

			if (ret != null) {
				break;
			}
		}

		return ret;
	}

	public static Identifier fromElement(Element el)
			throws UnmarshalException {
		Identifier ret = tryFromElement(el);

		if (ret == null) {
			throw new UnmarshalException("No IdentifierHandler could parse element with name "
					+ el.getLocalName() + " in namespace " + el.getNamespaceURI());
		}
		return ret;
	}

	public static Element tryToElement(Identifier i, Document doc) {

		IdentifierHandler<? extends Identifier> ih = null;
		Element elId = null;

		ih = Identifiers.getHandlerFor(i);

		if (ih == null) {
			IfmapJLog.warn("No IdentifierHandler for " + i.getClass());
			return null;
		}

		try {
			elId = ih.toElement(i, doc);
		} catch (MarshalException e) {
			IfmapJLog.warn("IdentifierHandler for " + i.getClass()
					+ " threw exception:" + e.getMessage());
			return null;
		}

		if (elId == null) {
			IfmapJLog.warn("IdentifierHandler for " + i.getClass()
					+ " returned null!");
		}

		return elId;
	}

	public static Element toElement(Identifier i, Document doc)
			throws MarshalException {
		Element ret = tryToElement(i, doc);

		if (ret == null) {
			throw new MarshalException("No IdentifierHandler or error during conversion of Identifier to Element");
		}

		return ret;
	}

	public static IdentityType getIdentityType(String s) {
		for (IdentityType i : IdentityType.values()) {
			if (i.toString().equals(s)) {
				return i;
			}
		}
		return null;
	}

	/**
	 * Create an access-request identifier with an random UUID as name.
	 *
	 * @return the new {@link AccessRequest} instance.
	 */
	public static AccessRequest createArRandomUuid() {
		return new AccessRequest(java.util.UUID.randomUUID().toString());
	}

	/**
	 * Create an access-request identifier with an random UUID as name
	 * and administrative-domain as given.
	 *
	 * @return the new {@link AccessRequest} instance.
	 * @deprecated administrative-domain should not be used for access-request
	 *			   anymore.
	 */
	@Deprecated
	public static AccessRequest createArRandomUuid(String admDom) {
		return new AccessRequest(java.util.UUID.randomUUID().toString(), admDom);
	}


	/**
	 * Create an access-request identifier with a random string of length
	 * 16 as name.
	 *
	 * @return the new {@link AccessRequest} instance.
	 */
	public static AccessRequest createArRandom() {
		return new AccessRequest(StringHelpers.getRandomString(16));
	}

	/**
	 * Create an access-request identifier with a random string of length
	 * 16 as name and administrative-domain as given.
	 *
	 * @return the new {@link AccessRequest} instance.
	 */
	public static AccessRequest createArRandom(String admDom) {
		return new AccessRequest(StringHelpers.getRandomString(16), admDom);
	}

	/**
	 * Create an access-request identifier with the given name. You may optionally
	 * set the administrative-domain with {@link AccessRequest#setAdministrativeDomain(String)}
	 * too (default is left out).
	 *
	 * @param name the name of the access-request
	 * @return the new {@link AccessRequest} instance
	 */
	public static AccessRequest createAr(String name) {
		return new AccessRequest(name);
	}

	/**
	 * Create an access-request identifier with the given name and the given
	 * administrative-domain.
	 *
	 * @param name the name of the access-request
	 * @param admDom the administrative-domain of the access-request
	 * @return the new {@link AccessRequest} instance
	 * @deprecated administrative-domain should not be used for access-request
	 *			   anymore.
	 */
	@Deprecated
	public static AccessRequest createAr(String name, String admDom) {
		return new AccessRequest(name, admDom);
	}

	/**
	 * Create an access-request identifier with with "pubId:name" as name and
	 * the given administrative-domain.
	 *
	 * @param name the name of the access-request
	 * @param admDom the administrative-domain of the access-request
	 * @param pubId the publisher id
	 * @deprecated administrative-domain should not be used for access-request
	 *			   anymore.
	 * @return the new {@link AccessRequest} instance
	 */
	@Deprecated
	public static AccessRequest createArPubPrefixed(String name,  String pubId,
			String admDom) {
		return new AccessRequest(pubId + ":" + name, admDom);
	}

	/**
	 * Create an access-request identifier with with "pubId:name" as name.
	 *
	 * @param name the name of the access-request
	 * @param pubId the publisher id
	 * @return the new {@link AccessRequest} instance
	 */
	public static AccessRequest createArPubPrefixed(String name, String pubId) {
		return new AccessRequest(pubId + ":" + name);
	}

	/**
	 * Create a device identifier with a random UUID as name.
	 *
	 * @return the new {@link Device} instance
	 */
	public static Device createDevRandomUuid() {
		return new Device(java.util.UUID.randomUUID().toString());
	}

	/**
	 * Create a device identifier with a random string of length 16.
	 *
	 * @return the new {@link Device} instance
	 */
	public static Device createDevRandom() {
		return new Device(StringHelpers.getRandomString(16));
	}

	/**
	 * Create a device identifier with the given name.
	 *
	 * @param name the name of the device identifier
	 * @return the new {@link Device} instance
	 */
	public static Device createDev(String name) {
		return new Device(name);
	}

	/**
	 * Create a device identifier with the given name and prefix it
	 * with the publisher id. (pubID:UID format)
	 *
	 * @param name the name of the device identifier
	 * @param pubId the devices publisher id
	 * @return the new {@link Device} instance
	 */
	public static Device createDevPubPrefixed(String name, String pubId) {
		return new Device(pubId + ":" + name);
	}

	/**
	 * Create an identity identifier with the given name and the given type.
	 *
	 * @param type the type of the identity identifier
	 * @param name the name of the identity identifier
	 * @return the new {@link Identity} instance
	 */
	public static Identity createIdentity(IdentityType type, String name) {
		return createIdentity(type, name, null, null);
	}

	/**
	 * Create an identity identifier with the given parameters.
	 *
	 * @param type the type of the identity identifier
	 * @param name the name of the identity identifier
	 * @param admDom the administrative-domain of the identity identifier
	 * @return the new {@link Identity} instance
	 */
	public static Identity createIdentity(IdentityType type, String name, String admDom) {
		return createIdentity(type, name, admDom, null);
	}

	/**
	 * Create an other identity identifier.
	 *
	 * <b>Note: The type is set to {@link IdentityType#other} by default.</b>
	 *
	 * @param name the name of the identity identifier
	 * @param admDom the administrative-domain of the identity identifier
	 * @param otherTypeDef vendor specific {@link String}
	 * @return the new {@link Identity} instance
	 */
	public static Identity createOtherIdentity(String name, String admDom,
			String otherTypeDef) {
		return createIdentity(IdentityType.other, name, admDom, otherTypeDef);
	}

	/**
	 * Create an other identity identifier.
	 *
	 * <b>Note: The type is set to {@link IdentityType#other} by default.</b>
	 *
	 * @param name the name of the identity identifier
	 * @param admDom the administrative-domain of the identity identifier
	 * @param otherTypeDef vendor specific {@link String}
	 * @param vendorId the vendor id to prefix the identity name with
	 * @return the new {@link Identity} instance
	 */
	public static Identity createOtherIdentity(String name, String admDom,
			String otherTypeDef, String vendorId) {
		return createIdentity(IdentityType.other, vendorId + ":" + name,
				admDom, otherTypeDef);
	}

	/**
	 * Create an identity identifier.
	 * <br/><br/>
	 * <b>Note: otherTypeDef != null should imply type {@link IdentityType#other}.</b>
	 *
	 * @param type the type of the identity identifier
	 * @param name the name of the identity identifier
	 * @param admDom the administrative-domain of the identity identifier
	 * @param otherTypeDef vendor specific {@link String}
	 * @return the new {@link Identity} instance
	 */
	public static Identity createIdentity(IdentityType type, String name, String admDom,
			String otherTypeDef) {
		return new Identity(type, name, admDom, otherTypeDef);
	}

	/**
	 * Create an extended {@link Identity} identifier
	 * with empty administrative domain.
	 *
	 * @return the new extended {@link Identity} instance
	 * @throws TransformerConfigurationException
	 */
	public static Identity createExtendedIdentity(Document doc) throws MarshalException {
		String idName = DomHelpers.prepareExtendedIdentifier(doc);
		return createIdentity(IdentityType.other, idName, null,
				IfmapStrings.OTHER_TYPE_EXTENDED_IDENTIFIER);
	}

	/**
	 * Create an extended {@link Identity} identifier
	 * with empty administrative domain.
	 *
	 * @param extendedIdentifier the extended identifier XML
	 * @return the new extended {@link Identity} instance
	 * @throws TransformerConfigurationException
	 */
	public static Identity createExtendedIdentity(InputStream extendedIdentifier)
			throws MarshalException {
		return createExtendedIdentity(DomHelpers.toDocument(extendedIdentifier));
	}

	/**
	 * Create an extended {@link Identity} identifier
	 * with empty administrative domain.
	 *
	 * @param extendedIdentifier the extended identifier XML
	 * @return the new extended {@link Identity} instance
	 * @throws TransformerConfigurationException
	 */
	public static Identity createExtendedIdentity(String extendedIdentifier)
			throws MarshalException {
		return createExtendedIdentity(extendedIdentifier, null);
	}

	/**
	 * Create an extended {@link Identity} identifier.
	 *
	 * @param extendedIdentifier the extended identifier XML
	 * @param c charset used for encoding the string
	 * @return the new extended identity instance
	 * @throws IfmapException
	 */
	public static Identity createExtendedIdentity(String extendedIdentifier,
			Charset c) throws MarshalException {
		return createExtendedIdentity(DomHelpers.toDocument(extendedIdentifier, c));
	}

	/**
	 * Create an ip-address identifier for IPv4 with the given value.
	 *
	 * @param value a {@link String} that represents a valid IPv4 address
	 * @return the new ip-address identifier
	 */
	public static IpAddress createIp4(String value) {
		return createIp4(value, null);
	}

	/**
	 * Create an ip-address identifier for IPv4 with the given value and the
	 * given administrative-domain.
	 *
	 * @param value a {@link String} that represents a valid IPv4 address
	 * @param admDom the administrative-domain
	 * @return the new ip-address identifier
	 */
	public static IpAddress createIp4(String value, String admDom) {
		return createIp(IpAddressType.IPv4, value, admDom);
	}

	/**
	 * Create an ip-address identifier for IPv6 with the given value.
	 *
	 * @param value a {@link String} that represents a valid IPv6 address
	 * @return the new ip-address identifier
	 */
	public static IpAddress createIp6(String value) {
		return createIp6(value, null);
	}

	/**
	 * Create an ip-address identifier for IPv6 with the given parameters.
	 *
	 * @param value a {@link String} that represents a valid IPv4 address
	 * @param admDom the administrative-domain
	 * @return the new ip-address identifier
	 */
	public static IpAddress createIp6(String value, String admDom) {
		return createIp(IpAddressType.IPv6, value, admDom);
	}

	/**
	 * Create an ip-address identifier with the given parameters.
	 *
	 * @param type the type of the ip-address identifier
	 * @param value a {@link String} that represents a valid IPv4/6 address
	 * @param admDom the administrative-domain
	 * @return the new ip-address identifier
	 */
	public static IpAddress createIp(IpAddressType type, String value, String admDom) {
		return new IpAddress(type, value, admDom);
	}

	/**
	 * Create a mac-address identifier with the given value.
	 *
	 * @param value a {@link String} that represents a valid MAC address
	 * @return the new mac-address identifier
	 */
	public static MacAddress createMac(String value) {
		return createMac(value, null);
	}

	/**
	 * Create a mac-address identifier with the given parameters.
	 *
	 * @param value a {@link String} that represents a valid MAC address
	 * @param admDom the administrative-domain
	 * @return the new mac-address identifier
	 */
	public static MacAddress createMac(String value, String admDom) {
		return new MacAddress(value, admDom);
	}

	/**
	 * Create a mac-address identifier with the given value.
	 *
	 * @param value a {@link byte} array that represents a valid MAC address
	 * @return the new mac-address identifier
	 */
	public static MacAddress createMacFromByte(byte[] value) {
		return createMacFromByte(value, null);
	}

	/**
	 * Create a mac-address identifier with the given parameters.
	 *
	 * @param value a {@link byte} array that represents a valid MAC address
	 * @param admDom the administrative-domain
	 * @return the new mac-address identifier
	 */
	public static MacAddress createMacFromByte(byte[] value, String admDom) {
		return new MacAddress(value, admDom);
	}

	public static final class Helpers {

		private Helpers() { }

		public static String getAdministrativeDomain(Element el) {
			Attr node = el.getAttributeNode(IfmapStrings.IDENTIFIER_ATTR_ADMIN_DOMAIN);
			return node != null && node.getValue().length() > 0 ? node.getValue() : null;
		}

		/**
		 * Check if the {@link Identifier} instance is one that can be handled by
		 * the given {@link IdentifierHandler}.
		 *
		 * @param i
		 * @param ih
		 * @throws MarshalException
		 */
		public static void checkIdentifierType(Identifier i,
				IdentifierHandler<? extends Identifier> ih) throws MarshalException {
			Class<? extends Identifier> clazz = ih.handles();

			if (!clazz.isInstance(i)) {
				throw new MarshalException("Handler for identifier of type "
						+ ih.handles() + " got identifier of type " + i.getClass());
			}
		}

		/**
		 * If the given {@link Identifier} has an administrative-domain set, add
		 * it to the given {@link Element} instance.
		 *
		 * @param to
		 * @param i
		 */
		public static void addAdministrativeDomain(Element to, IdentifierWithAd i) {
			String adom = i.getAdministrativeDomain();
			if (adom != null && adom.length() > 0) {
				DomHelpers.addAttribute(to,
						IfmapStrings.IDENTIFIER_ATTR_ADMIN_DOMAIN, adom);
			}
		}
	}
}


// IDENTIFIER HANDLER IMPLEMENTATIONS

class AccessRequestHandler implements IdentifierHandler<AccessRequest> {

	@Override
	public Element toElement(Identifier i, Document doc)
			throws MarshalException {

		Helpers.checkIdentifierType(i, this);
		AccessRequest ar = (AccessRequest) i;
		String name = ar.getName();

		if (name == null) {
			throw new MarshalException("AccessRequest with null name not allowed");
		}

		if (name.length() == 0) {
			throw new MarshalException("AccessRequest with empty name not allowed");
		}

		Element ret = DomHelpers.createNonNsElement(doc,
				IfmapStrings.ACCESS_REQUEST_EL_NAME);
		DomHelpers.addAttribute(ret, IfmapStrings.ACCESS_REQUEST_ATTR_NAME, name);
		Helpers.addAdministrativeDomain(ret, ar);

		return ret;
	}

	@Override
	public AccessRequest fromElement(Element el) throws UnmarshalException {

		String name = null;

		// Are we responsible? return null if not.
		if (!DomHelpers.elementMatches(el, IfmapStrings.ACCESS_REQUEST_EL_NAME)) {
			return null;
		}

		name = el.getAttribute(IfmapStrings.ACCESS_REQUEST_ATTR_NAME);

		if (name == null || name.length() == 0) {
			throw new UnmarshalException("No or empty " + IfmapStrings.ACCESS_REQUEST_ATTR_NAME + " found for "
					+ IfmapStrings.ACCESS_REQUEST_EL_NAME);
		}


		return Identifiers.createAr(name, Helpers.getAdministrativeDomain(el));
	}

	@Override
	public Class<AccessRequest> handles() {
		return AccessRequest.class;
	}
}

class DeviceHandler implements IdentifierHandler<Device> {

	@Override
	public Element toElement(Identifier i, Document doc)
			throws MarshalException {

		Helpers.checkIdentifierType(i, this);
		Device dev = (Device) i;
		String name = dev.getName();

		if (name == null) {
			throw new MarshalException("Device with null name not allowed");
		}

		if (name.length() == 0) {
			throw new MarshalException("Device with empty name not allowed");
		}

		Element ret = DomHelpers.createNonNsElement(doc,
				IfmapStrings.DEVICE_EL_NAME);
		Element nameEl = DomHelpers.createNonNsElement(doc,
					IfmapStrings.DEVICE_NAME_EL_NAME);
		Node text = doc.createTextNode(name);
		ret.appendChild(nameEl);
		nameEl.appendChild(text);

		return ret;
	}

	@Override
	public Device fromElement(Element el) throws UnmarshalException {

		Element child = null;
		String name = null;

		// Are we responsible? return null if not.
		if (!DomHelpers.elementMatches(el, IfmapStrings.DEVICE_EL_NAME)) {
			return null;
		}

		List<Element> children = DomHelpers.getChildElements(el);

		if (children.size() != 1) {
			throw new UnmarshalException("Bad " + IfmapStrings.DEVICE_EL_NAME
					+ " element? Has " + children.size() + " child elements.");
		}

		child = children.get(0);

		if (!DomHelpers.elementMatches(child, IfmapStrings.DEVICE_NAME_EL_NAME)) {
			throw new UnmarshalException("Unknown child element in " + IfmapStrings.DEVICE_EL_NAME + " element: "
					+ child.getLocalName());
		}

		name = child.getTextContent();

		if (name == null || name.length() == 0) {
			throw new UnmarshalException("No text content found");
		}

		return Identifiers.createDev(name);
	}

	@Override
	public Class<Device> handles() {
		return Device.class;
	}
}

class IdentityHandler implements IdentifierHandler<Identity> {

	@Override
	public Element toElement(Identifier i, Document doc)
			throws MarshalException {
		Helpers.checkIdentifierType(i, this);
		Identity id = (Identity) i;
		String name = id.getName();
		IdentityType type = id.getType();
		String otherTypeDef = id.getOtherTypeDefinition();

		if (type == null) {
			throw new MarshalException("No Identity type set");
		}

		if (name == null) {
			throw new MarshalException("Identity with null name not allowed");
		}

		if (name.length() == 0) {
			throw new MarshalException("Identity with empty name not allowed");
		}

		Element ret = DomHelpers.createNonNsElement(doc,
				IfmapStrings.IDENTITY_EL_NAME);

		DomHelpers.addAttribute(ret, IfmapStrings.IDENTITY_ATTR_NAME, name);
		DomHelpers.addAttribute(ret, IfmapStrings.IDENTITY_ATTR_TYPE, type.toString());

		if (type == IdentityType.other) {
			if (otherTypeDef == null || otherTypeDef.length() == 0) {
				throw new MarshalException("Identity type=other requires other-type-definition");
			}
			DomHelpers.addAttribute(ret, IfmapStrings.IDENTITY_ATTR_OTHER_TYPE_DEF,
					otherTypeDef);
		} else {
			if (otherTypeDef != null && otherTypeDef.length() > 0) {
				throw new MarshalException("Identity other-type-definition is set, but type != other");
			}
		}

		Helpers.addAdministrativeDomain(ret, id);

		return ret;
	}

	@Override
	public Identity fromElement(Element el) throws UnmarshalException {

		IdentityType typeEnum = null;
		String name = null;
		String type = null;
		String otherTypeDef = null;

		// Are we responsible? return null if not.
		if (!DomHelpers.elementMatches(el, IfmapStrings.IDENTITY_EL_NAME)) {
			return null;
		}

		name = el.getAttribute(IfmapStrings.IDENTITY_ATTR_NAME);
		type = el.getAttribute(IfmapStrings.IDENTITY_ATTR_TYPE);
		otherTypeDef = el.getAttribute(IfmapStrings.IDENTITY_ATTR_OTHER_TYPE_DEF);

		if (name == null || name.length() == 0) {
			throw new UnmarshalException(IfmapStrings.IDENTITY_ATTR_NAME
					+ " not set or empty for " + IfmapStrings.IDENTITY_EL_NAME);
		}

		if (type == null || type.length() == 0) {
			throw new UnmarshalException(IfmapStrings.IDENTITY_ATTR_TYPE
					+ " not set or empty for " + IfmapStrings.IDENTITY_EL_NAME);
		}

		for (IdentityType t : IdentityType.values()) {
			if (t.toString().equals(type)) {
				typeEnum = t;
				break;
			}
		}

		if (typeEnum == null) {
			throw new UnmarshalException("Illegal value for " +	IfmapStrings.IDENTITY_ATTR_TYPE + ":" + type);
		}

		if (typeEnum == IdentityType.other && otherTypeDef.length() == 0) {
			throw new UnmarshalException(IfmapStrings.IDENTITY_EL_NAME
					+ " with " + IfmapStrings.IDENTITY_ATTR_TYPE + "=other, but"
					+ IfmapStrings.IDENTITY_ATTR_OTHER_TYPE_DEF + " not set");
		}


		// NOTE: THIS CHECK IS PRETTY HARSH AND MIGHT THORW AN EXCEPTION WHEN A
		// MAPS IS DOING SOMETHING WEIRD.
		// This is not the robustness principle...
		if (typeEnum != IdentityType.other
				&& el.getAttributeNode(IfmapStrings.IDENTITY_ATTR_OTHER_TYPE_DEF) != null) {
			throw new UnmarshalException(IfmapStrings.IDENTITY_EL_NAME + " with "
				+ IfmapStrings.IDENTITY_ATTR_OTHER_TYPE_DEF + " set, but type not other");
		}

		return Identifiers.createIdentity(
				typeEnum,
				name,
				Helpers.getAdministrativeDomain(el),
				otherTypeDef);
	}

	@Override
	public Class<Identity> handles() {
		return Identity.class;
	}
}

class IpAddressHandler implements IdentifierHandler<IpAddress> {

	@Override
	public Element toElement(Identifier i, Document doc)
			throws MarshalException {
		Helpers.checkIdentifierType(i, this);
		IpAddress ip = (IpAddress) i;
		String value = ip.getValue();
		IpAddressType type = IpAddressType.IPv4;

		if (value == null) {
			throw new MarshalException("IpAddress with null value not allowed");
		}

		if (value.length() == 0) {
			throw new MarshalException("IpAddress with empty value not allowed");
		}

		Element ret = DomHelpers.createNonNsElement(doc,
				IfmapStrings.IP_ADDRESS_EL_NAME);

		type = ip.getType() != null ? ip.getType() : type;

		DomHelpers.addAttribute(ret, IfmapStrings.IP_ADDRESS_ATTR_VALUE, value);
		DomHelpers.addAttribute(ret, IfmapStrings.IP_ADDRESS_ATTR_TYPE, type.toString());
		Helpers.addAdministrativeDomain(ret, ip);

		return ret;
	}

	@Override
	public IpAddress fromElement(Element el) throws UnmarshalException {

		String value = null;
		String type = null;
		IpAddressType typeEnum = IpAddressType.IPv4;

		// Are we responsible? return null if not.
		if (!DomHelpers.elementMatches(el, IfmapStrings.IP_ADDRESS_EL_NAME)) {
			return null;
		}

		value = el.getAttribute(IfmapStrings.IP_ADDRESS_ATTR_VALUE);

		if (el.getAttributeNode(IfmapStrings.IP_ADDRESS_ATTR_TYPE) != null) {
			type = el.getAttribute(IfmapStrings.IP_ADDRESS_ATTR_TYPE);
		}

		if (value == null || value.length() == 0) {
			throw new UnmarshalException("No or empty " + IfmapStrings.IP_ADDRESS_ATTR_VALUE + " for "
					+ IfmapStrings.IP_ADDRESS_EL_NAME + " found");
		}

		if (type != null) {	// if null then it's IPv4 anyway
			try {
				typeEnum = IpAddressType.valueOf(type);
			} catch (IllegalArgumentException e) {
				throw new UnmarshalException("Invalid type for " + IfmapStrings.IP_ADDRESS_EL_NAME + " found:" + type);
			}
		}

		return Identifiers.createIp(
				typeEnum,
				value,
				Helpers.getAdministrativeDomain(el));
	}

	@Override
	public Class<IpAddress> handles() {
		return IpAddress.class;
	}
}

class MacAddressHandler implements IdentifierHandler<MacAddress> {

	@Override
	public Element toElement(Identifier i, Document doc)
			throws MarshalException {
		Helpers.checkIdentifierType(i, this);
		MacAddress mac = (MacAddress) i;
		String value = mac.getValue();

		if (value == null) {
			throw new MarshalException("MacAddress with null value not allowed");
		}

		if (value.length() == 0) {
			throw new MarshalException("MacAddress with empty value not allowed");
		}

		if (!java.util.regex.Pattern.matches("^([0-9a-f]{2}[:]){5}([0-9a-f]{2})$", value)) {
			throw new MarshalException("MacAddress not valid");
		}

		Element ret = DomHelpers.createNonNsElement(doc,
				IfmapStrings.MAC_ADDRESS_EL_NAME);

		DomHelpers.addAttribute(ret, IfmapStrings.MAC_ADDRESS_ATTR_VALUE, value);
		Helpers.addAdministrativeDomain(ret, mac);

		return ret;
	}

	@Override
	public MacAddress fromElement(Element el) throws UnmarshalException {

		String value = null;

		// Are we responsible? return null if not.
		if (!DomHelpers.elementMatches(el, IfmapStrings.MAC_ADDRESS_EL_NAME)) {
			return null;
		}

		value = el.getAttribute(IfmapStrings.MAC_ADDRESS_ATTR_VALUE);

		if (value == null || value.length() == 0) {
			throw new UnmarshalException("No or empty value for " + IfmapStrings.MAC_ADDRESS_EL_NAME + " found");
		}


		return Identifiers.createMac(value, Helpers.getAdministrativeDomain(el));
	}

	@Override
	public Class<MacAddress> handles() {
		return MacAddress.class;
	}
}

