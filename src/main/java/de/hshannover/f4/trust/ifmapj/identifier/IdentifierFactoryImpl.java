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
 * This file is part of ifmapj, version 2.2.0, implemented by the Trust@HsH
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
package de.hshannover.f4.trust.ifmapj.identifier;

import de.hshannover.f4.trust.ifmapj.binding.IfmapStrings;
import de.hshannover.f4.trust.ifmapj.exception.MarshalException;

import org.w3c.dom.Document;

import util.DomHelpers;

/**
 * Simple implementation of the {@link IdentifierFactory} interface.
 *
 * @author aw
 *
 * @deprecated
 * Simply use {@link Identifiers} directly.
 *
 */
@Deprecated
class IdentifierFactoryImpl implements IdentifierFactory {

	@Override
	public AccessRequest createAr() {
		return createAr(null, null);
	}

	@Override
	public AccessRequest createAr(String name) {
		return createAr(name, null);
	}

	@Deprecated
	@Override
	public AccessRequest createAr(String name, String admDom) {
		return Identifiers.createAr(name, admDom);
	}

	@Override
	public Device createDev() {
		return createDev(null);
	}

	@Override
	public Device createDev(String name) {
		return Identifiers.createDev(name);
	}

	@Override
	public Identity createIdentity() {
		return createIdentity(null, null, null, null);
	}

	@Override
	public Identity createIdentity(IdentityType type, String name) {
		return createIdentity(type, name, null, null);
	}

	@Override
	public Identity createIdentity(IdentityType type, String name, String admDom) {
		return createIdentity(type, name, admDom, null);
	}

	@Override
	public Identity createIdentity(String name, String admDom, String otherTypeDef) {
		return createIdentity(IdentityType.other, name, admDom, otherTypeDef);
	}

	@Override
	public Identity createIdentity(Document doc)
			throws MarshalException {
		Identity ret = null;
		String name = DomHelpers.prepareExtendedIdentifier(doc);
		ret = createIdentity(name, null, IfmapStrings.OTHER_TYPE_EXTENDED_IDENTIFIER);
		return ret;
	}

	@Override
	public Identity createIdentity(IdentityType type, String name, String admDom,
			String otherTypeDef) {
		return Identifiers.createIdentity(type, name, admDom, otherTypeDef);
	}

	@Override
	public IpAddress createIp4() {
		return createIp4(null, null);
	}

	@Override
	public IpAddress createIp4(String value) {
		return createIp4(value, null);
	}

	@Override
	public IpAddress createIp4(String value, String admDom) {
		return createIp(IpAddressType.IPv4, value, admDom);
	}

	@Override
	public IpAddress createIp6() {
		return createIp6(null, null);
	}

	@Override
	public IpAddress createIp6(String value) {
		return createIp6(value, null);
	}

	@Override
	public IpAddress createIp6(String value, String admDom) {
		return createIp(IpAddressType.IPv6, value, admDom);
	}

	@Override
	public IpAddress createIp(IpAddressType type, String value, String admDom) {
		return Identifiers.createIp(type, value, admDom);
	}

	@Override
	public MacAddress createMac() {
		return createMac(null, null);
	}

	@Override
	public MacAddress createMac(String value) {
		return createMac(value, null);
	}

	@Override
	public MacAddress createMac(String value, String admDom) {
		return Identifiers.createMac(value, admDom);
	}
}
