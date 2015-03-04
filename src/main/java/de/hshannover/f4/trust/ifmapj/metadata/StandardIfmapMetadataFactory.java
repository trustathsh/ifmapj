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

import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Document;

/**
 * Inteface to create some of the standard metadata specified by IF-MAP 2.0.
 *
 * @author aw
 * @author ib
 *
 */
public interface StandardIfmapMetadataFactory {

	/**
	 * Create a {@link Document} representing ip-mac metadata based on the given
	 * parameters.
	 *
	 * <pre>
	 *  ip-mac is link metadata that associates an
	 *  ip-address identifier with a mac-address identifier
	 *  and which includes optional DHCP lease information
	 * </pre>
	 *
	 * @param startTime the start-time value or null
	 * @param endTime  the end-time value or null
	 * @param dhcpServer the dhcp-server value or null
	 * @return a {@link Document} that represents the metadata
	 */
	Document createIpMac(String startTime, String endTime, String dhcpServer);

	/**
	 * Create a {@link Document} representing ip-mac metadata.
	 *
	 * <pre>
	 *  ip-mac is link metadata that associates an
	 *  ip-address identifier with a mac-address identifier
	 *  and which includes optional DHCP lease information
	 * </pre>
	 *
	 * @return a {@link Document} that represents the metadata
	 */
	Document createIpMac();


	/**
	 * Create a {@link Document} representing access-request-mac metadata.
	 *
	 * <pre>
	 *  access-request-mac is link metadata that
	 *  associates an access-request identifier with
	 *  a mac-address identifier
	 * </pre>
	 *
	 * @return a {@link Document} that represents the metadata
	 */
	Document createArMac();

	/**
	 * Create a {@link Document} representing access-request-device metadata.
	 *
	 * <pre>
	 *  access-request-device is link metadata that
	 *  associates an access-request identifier with
	 *  a device identifier
	 * </pre>
	 *
	 * @return a {@link Document} that represents the metadata
	 */
	Document createArDev();

	/**
	 * Create a {@link Document} representing access-request-ip metadata.
	 *
	 * <pre>
	 *  access-request-ip is link metadata that
	 *  associates an access-request identifier with
	 *  an ip-address identifier
	 * </pre>
	 *
	 * @return a {@link Document} that represents the metadata
	 */
	Document createArIp();

	/**
	 * Create a {@link Document} representing authenticated-as metadata.
	 *
	 * <pre>
	 *  authenticated-as is link metadata that
	 *  associates an access-request identifier with
	 *  an identity identifier
	 * </pre>
	 *
	 * @return a {@link Document} that represents the metadata
	 */
	Document createAuthAs();

	/**
	 * Create a {@link Document} representing authenticated-by metadata.
	 *
	 * <pre>
	 *  authenticated-by is link metadata that
	 *  associates an access-request identifier with
	 *  the device identifier of the PDP that
	 *  authenticated the access-request
	 * </pre>
	 *
	 * @return a {@link Document} that represents the metadata
	 */
	Document createAuthBy();


	/**
	 * Create a {@link Document} representing device-ip metadata.
	 *
	 * <pre>
	 *  device-ip is link metadata that associates a device
	 *  identifier of a PDP with an IP address which it has
	 *  authenticated
	 * </pre>
	 *
	 * @return a {@link Document} that represents the metadata
	 */
	Document createDevIp();

	/**
	 * Create a {@link Document} representing discovered-by metadata.
	 *
	 * <pre>
	 *  discovered-by is link metadata that associates
	 *  an ip-address or mac-address identifier of an endpoint
	 *  with the device identifier of a MAP Client that has
	 *  noticed the endpoint on the network
	 * </pre>
	 *
	 * @return  a {@link Document} that represents the metadata
	 */
	Document createDiscoveredBy();


	/**
	 * Create a {@link Document} representing role metadata.
	 *
	 * <pre>
	 *  role is link metadata that associates an
	 *  access-request identifier with an identity
	 *  identifier and which names collections of
	 *  privileges associated with the end-user
	 * </pre>
	 *
	 * @param name the name of the role
	 * @param administrativeDomain the administrative-domain or null
	 * @return  a {@link Document} that represents the metadata
	 */
	Document createRole(String name, String administrativeDomain);

	/**
	 * Create a {@link Document} representing role metadata.
	 *
	 * <pre>
	 *  role is link metadata that associates an
	 *  access-request identifier with an identity
	 *  identifier and which names collections of
	 *  privileges associated with the end-user
	 * </pre>
	 *
	 * @param name
	 * @return  a {@link Document} that represents the metadata
	 */
	Document createRole(String name);


	/**
	 * Create a {@link Document} representing device-attribute metadata.
	 *
	 * <pre>
	 *  device-attribute is link metadata that associates
	 *  an access-request identifier with a device identifier
	 *  and which includes information about the device such
	 *  as its health
	 * </pre>
	 *
	 * @param name the name of the attribute
	 * @return a {@link Document} that represents the metadata
	 */
	Document createDevAttr(String name);

	/**
	 * Create a {@link Document} representing capability metadata.
	 *
	 * <pre>
	 *  capability metadata refers to a collection of privileges
	 *  assigned to an endpoint as a result of an access request.
	 * </pre>
	 *
	 * @param name the name of the capability
	 * @param administrativeDomain the administrative-domain or null
	 *
	 * @return a {@link Document} that represents the metadata
	 */
	Document createCapability(String name, String administrativeDomain);

	/**
	 * Create a {@link Document} representing capability metadata.
	 *
	 * <pre>
	 *  capability metadata refers to a collection of privileges
	 *  assigned to an endpoint as a result of an access request.
	 * </pre>
	 *
	 * @param name the name of the capability
	 * @return a {@link Document} that represents the metadata
	 */
	Document createCapability(String name);

	/**
	 * Create a {@link Document} representing device-characteristic metadata.
	 *
	 * <pre>
	 *   device-characteristic is metadata assigned to a specific endpoint
	 *   by a MAP Client (usually a PDP or Sensor) to reflect an inherent
	 *   characteristic of that endpoint, such as its manufacturer or what
	 *   operating system it is running, along with what element discovered
	 *   the information and what method of discovery was used.
	 * </pre>
	 *
	 * @param manufacturer the manufacturer of the endpoint
	 * @param model the model of the endpoint
	 * @param os the operating system of the endpoint
	 * @param osVersion the version of the endpoint's operating system
	 * @param deviceType the type of the endpoint
	 * @param discoveredTime (mandatory) the time at which this
	 * 						 device-characteristic was first detected
	 * @param discovererId (mandatory)
	 * @param discoveryMethod (mandatory) the element that discovered the characteristic
	 * @return a {@link Document} that represents the metadata
	 */
	Document createDevChar(String manufacturer,
			String model, String os, String osVersion, String deviceType,
			String discoveredTime, String discovererId, String discoveryMethod);

	/**
	 * Create a {@link Document} representing enforcement-report metadata.
	 *
	 * <pre>
	 *   enforcement-report metadata is attached to a link to associate a
	 *   specific mac-address identifier or ip-address identifier with a
	 *   specific device identifier representing a PEP or Flow Controller.
	 *   A Flow controller may create the association when it takes
	 *   enforcement action against an endpoint. A PDP may create the
	 *   association when it instructs a PEP to take enforcement action
	 *   against an endpoint.
	 * </pre>
	 *
	 * @param enforcementAction the enforcement action that should be taken
	 * @param otherTypeDefinition this must not be null if action is
	 * 	{@link EnforcementAction#block}
	 * @param enforcementReason the reason of the enforcement
	 * @return a {@link Document} that represents the metadata
	 */
	Document createEnforcementReport(EnforcementAction enforcementAction,
			String otherTypeDefinition, String enforcementReason);

	/**
	 * Create a {@link Document} representing event metadata.
	 *
	 * <pre>
	 *   event metadata refers to activity of interest detected on
	 *   the network. Examples include network traffic that matches
	 *   the profile of a virus attack, excessive network traffic
	 *   originating from a particular endpoint, and the use of a
	 *   specific protocol such as an Instant Messaging protocol.
	 * </pre>
	 *
	 * @param name the name of the event
	 * @param discoveredTime the time it was discovered
	 * @param discovererId the entity that discovered the event
	 * @param magnitude must be between 0 and 100
	 * @param confidence must be between 0 and 100
	 * @param significance the significance of the event
	 * @param type the type of the event
	 * @param otherTypeDefinition must not be null if type is {@link EventType#other}
	 * @param information a human readable {@link String} containgin further information
	 * @param vulnerabilityUri must not be null if event is of type {@link EventType#cve}
	 * @return a {@link Document} that represents the metadata
	 */
	Document createEvent(String name, String discoveredTime, String discovererId,
			Integer magnitude, Integer confidence, Significance significance,
			EventType type, String otherTypeDefinition, String information,
			String vulnerabilityUri);

	/**
	 * Create a {@link Document} representing custom metadata.
	 *
	 * <pre>
	 *   any single metadata element can be added with one attribute
	 * </pre>
	 *
	 * @param elementName element name
	 * @param qualifiedName namespace qualifier
	 * @param uri namespace uri
	 * @param cardinality ifmap cardinality
	 * @return
	 */
	Document create(String elementName, String qualifiedName, String uri,
			Cardinality cardinality);

	/**
	 * Create a {@link Document} representing custom metadata.
	 *
	 * <pre>
	 *   any single metadata element can be added with one attribute
	 * </pre>
	 *
	 * @param elementName element name
	 * @param qualifiedName namespace qualifier
	 * @param uri namespace uri
	 * @param cardinality ifmap cardinality
	 * @param attrName attribute name
	 * @param attrValue attribute value
	 * @return
	 */
	Document create(String elementName, String qualifiedName, String uri,
			Cardinality cardinality, String attrName, String attrValue);

	/**
	 * Create a {@link Document} representing custom metadata.
	 *
	 * <pre>
	 *   any single metadata element can be added with attributes
	 * </pre>
	 *
	 * @param elementName element name
	 * @param qualifiedName namespace qualifier
	 * @param uri namespace uri
	 * @param cardinality ifmap cardinality
	 * @param attributes hashmap with attributes
	 * @return
	 */
	Document create(String elementName, String qualifiedName, String uri,
			Cardinality cardinality, HashMap<String, String> attributes);

	/**
	 * Create a {@link Document} representing layer2-information metadata.
	 *
	 * <pre>
	 *   layer2-information is attached to a link between an
	 *   access-request and the device identifier of the PEP through
	 *   which access is occurring. layer2-information includes vlan,
	 *   which specifies the VLAN assigned to the access request;
	 *   port, which specifies the port on the layer 2 PEP that the
	 *   access-request originates from; and an optional
	 *   administrative-domain, which may be used to distinguish between
	 *   two instances of the same VLAN number in different parts of a network.
	 * </pre>
	 *
	 * @param vlan the ID of the VLAN
	 * @param vlanName the name of the VLAN
	 * @param port the port number
	 * @param administrativeDomain the administrative domain (may be null)
	 * @return a {@link Document} that represents the metadata
	 */
	Document createLayer2Information(Integer vlan, String vlanName,
			Integer port, String administrativeDomain);

	/**
	 * Create a {@link Document} representing location metadata.
	 *
	 * <pre>
	 *   The location metadata element represents a named region
	 *   of space – usually a region with security import. The
	 *   region may be contiguous or discontiguous and may have any
	 *   shape and boundaries as defined by an organization.
	 * </pre>
	 *
	 * @param locationInformation list of all location information elements
	 * @param discoveredTime the time the location information was discovered
	 * @param discovererId the entity that discovered the location information
	 * @return a {@link Document} that represents the metadata
	 */
	Document createLocation(List<LocationInformation> locationInformation,
			String discoveredTime, String discovererId);

	/**
	 * Create a {@link Document} representing request-for-investigation metadata.
	 *
	 * <pre>
	 *   request-for-investigation metadata indicates that specified
	 *   device, which may be a PDP or other MAP Client, wants Sensors
	 *   to publish device-characteristic metadata about the specified
	 *   MAC or IP address.
	 * </pre>
	 *
	 * @param qualifier indicate what type of investigation should be done
	 * @return a {@link Document} that represents the metadata
	 */
	Document createRequestForInvestigation(String qualifier);

	/**
	 * Create a {@link Document} representing wlan-information metadata.
	 *
	 * <pre>
	 *   wlan-information is attached to a link to associate
	 *   a specific access-request identifier with a specific
	 *   device identifier representing the PEP through which
	 *   access is occurring.
	 * </pre>
	 *
	 * @param ssidUnicastSecurity the unicast security
	 * @param ssidGroupSecurity the group security
	 * @param ssidManagementSecurity the management security
	 * @return a {@link Document} that represents the metadata
	 */
	Document createWlanInformation(String ssid,
			List<WlanSecurityType> ssidUnicastSecurity,
			WlanSecurityType ssidGroupSecurity,
			List<WlanSecurityType> ssidManagementSecurity);

	/**
	 * Create a {@link Document} representing unexpected-behavior metadata.
	 *
	 * <pre>
	 *   Unexpected-behavior metadata indicates that an endpoint
	 *   is behaving in an unauthorized or unexpected manner (e.g.
	 *   an endpoint previously profiled as a printer that starts
	 *   sending non-print-related traffic).
	 * </pre>
	 *
	 * @param discoveredTime the time it was discovered
	 * @param discovererId the entity that discovered it
	 * @param magnitude element indicates how severe the effects of the activity
	 * 		  are. Ranges from 0 to 100.
	 * @param confidence indicates how confident the MAP Client that published
	 * 		  the metadata is that it accurately describes the activity of
	 * 		  interest. Ranges from 0 to 100.
	 * @param significance indicates how important the unexpected behavior is
	 * @param type machine consumable {@link String} indicating the nature of
	 * 		  the unexpected behavior
	 * @return a {@link Document} that represents the metadata
	 */
	Document createUnexpectedBehavior(String discoveredTime, String discovererId,
			Integer magnitude, Integer confidence, Significance significance,
			String type);

	/**
	 * Create a {@link Document} representing client time metadata.
	 *
	 * <pre>
	 *   Client time metadata is used for clock skew detection. The
	 *   clients timestamp can be compared to the servers timestamp
	 *   in order to detect and compensate clock skew between both systems.
	 * </pre>
	 *
	 * @param time the clients ISO-8601 UTC timestamp
	 * @return a {@link Document} that represents the metadata
	 */
	Document createClientTime(String time);

}
