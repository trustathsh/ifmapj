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
 * Website: http://trust.f4.hs-hannover.de
 * 
 * This file is part of ifmapj, version 1.0.0, implemented by the Trust@HsH
 * research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2010 - 2013 Trust@HsH
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
package de.hshannover.f4.trust.ifmapj.messages;

import java.util.List;

import org.w3c.dom.Document;

import de.hshannover.f4.trust.ifmapj.identifier.Identifier;

/**
 * Implementation of {@link ResultItem}
 *
 * @author aw
 *
 */
class ResultItemImpl extends MetadataHolderImpl implements ResultItem {

	ResultItemImpl(Identifier id1, Identifier id2, List<Document> mdlist) {
		if (id1 == null)
			throw new NullPointerException("id1 not allowed to be null");

		if (mdlist == null)
			throw new NullPointerException("metadata list not allowed to be null");

		setIdentifier1(id1);
		setIdentifier2(id2);

		for (Document doc : mdlist)
			this.addMetadata(doc);
	}

	ResultItemImpl(Identifier id1, List<Document> mdlist) {
		this(id1, null, mdlist);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("ri{");
		for (Identifier id : getIdentifier())
			if (id != null) {
				sb.append(id);
				sb.append(", ");
			}

		sb.append("#metadata=");
		sb.append(getMetadata().size());
		sb.append("}");
		return sb.toString();
	}
}
