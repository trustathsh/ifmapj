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
 * Inteface to create the IF-MAP Metadata for Content Authorization
 * this interface only implements the "ifmap-client-has-task Metadata Type", 
 * becaus it is necessary for the ICS Security Metadata
 * 
 * @author pe
 *
 */

public interface ContentAuthorizationMetadataFactory {

	/**
	 * Create a {@link Document} representing ifmap-client-has-task metadata.
	 *
	 * <pre>
	 *  ifmap-client-has-task is link metadata that
	 *  associates a non-extended identity identifier with administrative-domain ifmap:client 
	 *  with an another identifier that has:
	 *  	1. A nonempty name attribute
	 *  	2. An empty or no administrative-domain attribute
	 *  	3. No other attributes
	 *  	4. No child elements
	 * </pre>
	 *
	 * @param relationship type of relationship between the identifiers 
	 * 
	 * @return a {@link Document} that represents the metadata
	 */
	Document creatIfmapClientHasTask(String relationship);

}
