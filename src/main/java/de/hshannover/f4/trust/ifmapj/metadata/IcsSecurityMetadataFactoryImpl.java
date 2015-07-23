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
package de.hshannover.f4.trust.ifmapj.metadata;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.hshannover.f4.trust.ifmapj.binding.IfmapStrings;

/**
 * Simple implementation of the {@link IcsSecurityMetadataFactory} interface.
 *
 * @author pe
 *
 */
public class IcsSecurityMetadataFactoryImpl extends IfmapMetadataFactory implements IcsSecurityMetadataFactory {

	@Override
	public Document createBackhlPol(String netwName, String policy) {
		Document doc =
				createSingleElementDocument(IfmapStrings.ICS_METADATA_NS_URI, IfmapStrings.ICS_METADATA_PREFIX,
						"backhaul-policy",
						Cardinality.multiValue);

		Element root = (Element) doc.getFirstChild();

		createAndAppendTextElementCheckNull(doc, root, "name", netwName);
		createAndAppendTextElementCheckNull(doc, root, "policy", policy);

		return doc;
	}

	@Override
	public Document createBhiAdd() {
		return createSingleElementDocument(IfmapStrings.ICS_METADATA_NS_URI, IfmapStrings.ICS_METADATA_PREFIX,
				"bhi-address", Cardinality.singleValue);
	}

	@Override
	public Document createBhiCert(String certificate) {
		Document doc =
				createSingleElementDocument(IfmapStrings.ICS_METADATA_NS_URI, IfmapStrings.ICS_METADATA_PREFIX,
						"bhi-certificate",
						Cardinality.multiValue);

		Element root = (Element) doc.getFirstChild();

		createAndAppendTextElementCheckNull(doc, root, "data", certificate);

		return doc;
	}

	@Override
	public Document createBhiIdent() {
		return createSingleElementDocument(IfmapStrings.ICS_METADATA_NS_URI, IfmapStrings.ICS_METADATA_PREFIX,
				"bhi-identity", Cardinality.singleValue);
	}

	@Override
	public Document createDnHit() {
		return createSingleElementDocument(IfmapStrings.ICS_METADATA_NS_URI, IfmapStrings.ICS_METADATA_PREFIX,
				"dn-hit", Cardinality.singleValue);
	}

	@Override
	public Document createGrpXref(String ldapUri) {
		Document doc =
				createSingleElementDocument(IfmapStrings.ICS_METADATA_NS_URI, IfmapStrings.ICS_METADATA_PREFIX,
						"group-xref",
						Cardinality.multiValue);

		Element root = (Element) doc.getFirstChild();

		createAndAppendTextElementCheckNull(doc, root, "uri", ldapUri);

		return doc;
	}

	@Override
	public Document createMngOf() {
		return createSingleElementDocument(IfmapStrings.ICS_METADATA_NS_URI, IfmapStrings.ICS_METADATA_PREFIX,
				"manager-of", Cardinality.singleValue);
	}

	@Override
	public Document createMembOf() {
		return createSingleElementDocument(IfmapStrings.ICS_METADATA_NS_URI, IfmapStrings.ICS_METADATA_PREFIX,
				"member-of", Cardinality.singleValue);
	}

	@Override
	public Document createObsBy() {
		return createSingleElementDocument(IfmapStrings.ICS_METADATA_NS_URI, IfmapStrings.ICS_METADATA_PREFIX,
				"observed-by", Cardinality.multiValue);
	}

	@Override
	public Document createOverlPol(String netwName, String policy) {
		Document doc =
				createSingleElementDocument(IfmapStrings.ICS_METADATA_NS_URI, IfmapStrings.ICS_METADATA_PREFIX,
						"overlay-policy",
						Cardinality.multiValue);

		Element root = (Element) doc.getFirstChild();

		createAndAppendTextElementCheckNull(doc, root, "name", netwName);
		createAndAppendTextElementCheckNull(doc, root, "policy", policy);

		return doc;
	}

	@Override
	public Document createProtBy() {
		return createSingleElementDocument(IfmapStrings.ICS_METADATA_NS_URI, IfmapStrings.ICS_METADATA_PREFIX,
				"protected-by", Cardinality.multiValue);
	}

}
