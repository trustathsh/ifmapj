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
 * This file is part of ifmapj, version 2.3.2, implemented by the Trust@HsH
 * research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2010 - 2016 Trust@HsH
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

import de.hshannover.f4.trust.ifmapj.binding.IfmapStrings;
import de.hshannover.f4.trust.ifmapj.identifier.Identity;

/**
 * Contains methods to create ICS Identifiers.
 *
 * @author pe
 *
 */
public final class IcsIdentifiers {

	/**
	 * Create a backhaul-interface identifier,
	 * that is an extended identity identifier.
	 *
	 * @param name
	 *            unique name of a given BHI
	 * @return the new {@link Identity} instance
	 */
	public static Identity createBackhaulInterface(String name) {
		return ExtendedIdentifiers.createExtendedIdentifier(IfmapStrings.ICS_METADATA_NS_URI,
				IfmapStrings.ICS_METADATA_PREFIX, "backhaul-interface", name);
	}

	/**
	 * Create a overlay-manager-group identifier,
	 * that is an extended identity identifier.
	 *
	 * @param name
	 *            name of a particular Overlay Manager group
	 * @return the new {@link Identity} instance
	 */
	public static Identity createOverlayManagerGroup(String name) {
		return ExtendedIdentifiers.createExtendedIdentifier(IfmapStrings.ICS_METADATA_NS_URI,
				IfmapStrings.ICS_METADATA_PREFIX, "overlay-manager-group", name);
	}

	/**
	 * Create a overlay-network-group identifier,
	 * that is an extended identity identifier.
	 *
	 * @param name
	 *            name of the overlay network
	 * @return the new {@link Identity} instance
	 */
	public static Identity createOverlayNetworkGroup(String name) {
		return ExtendedIdentifiers.createExtendedIdentifier(IfmapStrings.ICS_METADATA_NS_URI,
				IfmapStrings.ICS_METADATA_PREFIX, "overlay-network-group", name);
	}

}
