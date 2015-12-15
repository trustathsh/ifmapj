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
 * This file is part of ifmapj, version 2.3.1, implemented by the Trust@HsH
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

import org.w3c.dom.Document;

/**
 * Inteface to create the IF-MAP Metadata for ICS Security
 *
 * @author pe
 *
 */
public interface IcsSecurityMetadataFactory {

	/**
	 * Create a {@link Document} representing backhaul-policy metadata.
	 *
	 * <pre>
	 *  backhaul-policy is metadata assigned to an  
	 *  overlay-network-group identifier
	 *  or associates a backhaul-interface identifier
	 *  with a backhaul-interface identifier
	 * </pre>
	 *
	 * @param netwName name of an overlay network
	 * @param policy MUST have a value of either "allow" or "deny"
	 * 
	 * @return a {@link Document} that represents the metadata
	 */
	Document createBackhlPol(String netwName, String policy);

	/**
	 * Create a {@link Document} representing bhi-address metadata.
	 *
	 * <pre>
	 *  bhi-address is link metadata that
	 *  associates a backhaul-interface identifier with
	 *  an ip-address identifier
	 * </pre>
	 *
	 * @return a {@link Document} that represents the metadata
	 */
	Document createBhiAdd();

	/**
	 * Create a {@link Document} representing bhi-certificate metadata.
	 *
	 * <pre>
	 *  bhi-certificate is metadata assigned to a  
	 *  distinguished-name identifier
	 *  the metadata contains a base64-encoded  X.509 certificate
	 * </pre>
	 *
	 * @param certificate the X.509 certificate
	 *
	 * @return a {@link Document} that represents the metadata
	 */
	Document createBhiCert(String certificate);

	/**
	 * Create a {@link Document} representing bhi-identity metadata.
	 *
	 * <pre>
	 *  bhi-identity is link metadata that
	 *  associates a backhaul-interface identifier with
	 *  a distinguished-name identifier
	 * </pre>
	 *
	 * @return a {@link Document} that represents the metadata
	 */
	Document createBhiIdent();

	/**
	 * Create a {@link Document} representing dn-hit metadata.
	 *
	 * <pre>
	 *  dn-hit is link metadata that
	 *  associates a  distinguished-name identifier with
	 *  a hip-hit identifier
	 * </pre>
	 *
	 * @return a {@link Document} that represents the metadata
	 */
	Document createDnHit();

	/**
	 * Create a {@link Document} representing group-xref metadata.
	 *
	 * <pre>
	 *  group-xref is metadata assigned to a  
	 *  overlay-manager-group identifier
	 *  the metadata contains a URI  that contains an LDAP directory 
	 *  search specification that will result in a list of principals
	 * </pre>
	 *
	 * @param ldapUri contains URI for LDAP directory search specification
	 *
	 * @return a {@link Document} that represents the metadata
	 */
	Document createGrpXref(String ldapUri);

	/**
	 * Create a {@link Document} representing manager-of metadata.
	 *
	 * <pre>
	 *  manager-of is link metadata that
	 *  associates an overlay-manager-group identifier with
	 *  a backhaul-interface identifier or
	 *  an overlay-manager-group identifier with
	 *  an overlay-network-group identifier
	 * </pre>
	 *
	 * @return a {@link Document} that represents the metadata
	 */
	Document createMngOf();

	/**
	 * Create a {@link Document} representing member-of metadata.
	 *
	 * <pre>
	 *  member-of is link metadata that
	 *  associates an overlay-manager-group identifier with
	 *  a backhaul-interface identifier or
	 *  an overlay-manager-group identifier with
	 *  a distinguished-name
	 * </pre>
	 *
	 * @return a {@link Document} that represents the metadata
	 */
	Document createMembOf();

	/**
	 * Create a {@link Document} representing observed-by metadata.
	 *
	 * <pre>
	 *  observed-by is link metadata that
	 *  associates a backhaul-interface identifier with
	 *  a ip-address identifier or
	 *  a backhaul-interface identifier with
	 *  a mac-address identifier
	 * </pre>
	 *
	 * @return a {@link Document} that represents the metadata
	 */
	Document createObsBy();

	/**
	 * Create a {@link Document} representing overlay-policy metadata.
	 *
	 * <pre>
	 *  overlay-policy is metadata assigned to a  
	 *  backhaul-interface identifier
	 *  or associates a backhaul-interface identifier
	 *  with a ip-address identifier
	 *  or associates a backhaul-interface identifier
	 *  with a mac-address identifier
	 * </pre>
	 *
	 * @param netwName name of an overlay network
	 * @param policy MUST have a value of either "allow" or "deny"
	 * 
	 * @return a {@link Document} that represents the metadata
	 */
	Document createOverlPol(String netwName, String policy);

	/**
	 * Create a {@link Document} representing protected-by metadata.
	 *
	 * <pre>
	 *  protected-by is link metadata that
	 *  associates a backhaul-interface identifier with
	 *  a ip-address identifier or
	 *  a backhaul-interface identifier with
	 *  a mac-address identifier
	 * </pre>
	 *
	 * @return a {@link Document} that represents the metadata
	 */
	Document createProtBy();
}
