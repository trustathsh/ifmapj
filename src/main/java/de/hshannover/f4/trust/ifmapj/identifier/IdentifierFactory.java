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

import org.w3c.dom.Document;

import de.hshannover.f4.trust.ifmapj.exception.MarshalException;

/**
 * Factory to create different types of {@link Identifier} implementations.
 *
 * @author aw
 * @deprecated
 * {@link Identifiers} class should be used to construct standard identifiers.
 */
@Deprecated
public interface IdentifierFactory {

	/**
	 * Create an access-request identifier. You MUST set the name with
	 * {@link AccessRequest#setName(String)} afterwards! You may optionally set
	 * the administrative-domain with {@link AccessRequest#setAdministrativeDomain(String)}
	 * too (default is left out).
	 * transformAndAddIdentifier
	 * @return the new {@link AccessRequest} instance
	 */
	AccessRequest createAr();


	/**
	 * Create an access-request identifier with the given name. You may optionally
	 * set the administrative-domain with {@link AccessRequest#setAdministrativeDomain(String)}
	 * too (default is left out).
	 *
	 * @param name the name of the access-request
	 * @return the new {@link AccessRequest} instance
	 */
	AccessRequest createAr(String name);


	/**
	 * Create an access-request identifier with the given name and the given
	 * administrative-domain.
	 *
	 * @param name the name of the access-request
	 * @param admDom the administrative-domain of the access-request
	 * @return the new {@link AccessRequest} instance
	 */
	AccessRequest createAr(String name, String admDom);


	/**
	 * Create a device identifier. You MUST set the name with
	 * {@link Device#setName(String)} afterwards!
	 *
	 * @return the new {@link Device} instance
	 */
	Device createDev();


	/**
	 * Create a device identifier with the given name.
	 *
	 * @param name the name of the device identifier
	 * @return the new {@link Device} instance
	 */
	Device createDev(String name);


	/**
	 * Create an identity identifier. You MUST set its name and type by using
	 * {@link Identity#setName(String)} and {@link Identity#setType(IdentityType)}
	 * afterwards!
	 *
	 * @return the new {@link Identity} instance
	 */
	Identity createIdentity();


	/**
	 * Create an identity identifier with the given name and the given type.
	 *
	 * @param type the type of the identity identifier
	 * @param name the name of the identity identifier
	 * @return the new {@link Identity} instance
	 */
	Identity createIdentity(IdentityType type, String name);


	/**
	 * Create an identity identifier with the given parameters.
	 *
	 * @param type the type of the identity identifier
	 * @param name the name of the identity identifier
	 * @param admDom the administrative-domain of the identity identifier
	 * @return the new {@link Identity} instance
	 */
	Identity createIdentity(IdentityType type, String name, String admDom);


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
	Identity createIdentity(IdentityType type, String name, String admDom,
			String otherTypeDef);

	/**
	 * Create an identity identifier.
	 *
	 * <b>Note: The type is set to {@link IdentityType#other} by default.</b>
	 *
	 * @param name the name of the identity identifier
	 * @param admDom the administrative-domain of the identity identifier
	 * @param otherTypeDef vendor specific {@link String}
	 * @return the new {@link Identity} instance
	 */
	Identity createIdentity(String name, String admDom, String otherTypeDef);

	/**
	 * Create an extended identity identifier.
	 *
	 * @param extendedIdentifier the extended identifier
	 * @return the new {@link Identity} instance
	 * @throws MarshalException if the document can't be handled
	 */
	Identity createIdentity(Document extendedIdentifier) throws MarshalException;

	/**
	 * Create an ip-address identifier for IPv4. You MUST set its value with
	 * {@link IpAddress#setValue(String)} afterwards!
	 *
	 * @return the new ip-address identifier
	 */
	IpAddress createIp4();


	/**
	 * Create an ip-address identifier for IPv4 with the given value.
	 *
	 * @param value a {@link String} that represents a valid IPv4 address
	 * @return the new ip-address identifier
	 */
	IpAddress createIp4(String value);

	/**
	 * Create an ip-address identifier for IPv4 with the given value and the
	 * given administrative-domain.
	 *
	 * @param value a {@link String} that represents a valid IPv4 address
	 * @param admDom the administrative-domain
	 * @return the new ip-address identifier
	 */
	IpAddress createIp4(String value, String admDom);

	/**
	 * Create an ip-address identifier for IPv6. You MUST set its value with
	 * {@link IpAddress#setValue(String)} afterwards!
	 *
	 * @return the new ip-address identifier
	 */
	IpAddress createIp6();


	/**
	 * Create an ip-address identifier for IPv6 with the given value.
	 *
	 * @param value a {@link String} that represents a valid IPv6 address
	 * @return the new ip-address identifier
	 */
	IpAddress createIp6(String value);

	/**
	 * Create an ip-address identifier for IPv6 with the given parameters.
	 *
	 * @param value a {@link String} that represents a valid IPv4 address
	 * @param admDom the administrative-domain
	 * @return the new ip-address identifier
	 */
	IpAddress createIp6(String value, String admDom);


	/**
	 * Create an ip-address identifier with the given parameters.
	 *
	 * @param type the type of the ip-address identifier
	 * @param value a {@link String} that represents a valid IPv4/6 address
	 * @param admDom the administrative-domain
	 * @return the new ip-address identifier
	 */
	IpAddress createIp(IpAddressType type, String value, String admDom);


	/**
	 * Create a mac-address identifier. You MUST set its value with
	 * {@link MacAddress#setValue(String)} afterwards!
	 *
	 * @return the new mac-address identifier
	 */
	MacAddress createMac();


	/**
	 * Create a mac-address identifier with the given value.
	 *
	 * @param value a {@link String} that represents a valid MAC address (lower case)
	 * @return the new mac-address identifier
	 */
	MacAddress createMac(String value);


	/**
	 * Create a mac-address identifier with the given parameters.
	 *
	 * @param value a {@link String} that represents a valid MAC address (lower case)
	 * @param admDom the administrative-domain
	 * @return the new mac-address identifier
	 */
	MacAddress createMac(String value, String admDom);
}
