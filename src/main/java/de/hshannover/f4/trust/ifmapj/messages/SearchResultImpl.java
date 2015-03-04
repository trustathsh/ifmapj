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
package de.hshannover.f4.trust.ifmapj.messages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A {@link SearchResult} simply contains a list of {@link ResultItem} objects.
 * Optionally it may contain a name in case this {@link SearchResult} is
 * contained in a {@link PollResult};
 *
 * @author aw
 */
class SearchResultImpl implements SearchResult {

	private final List<ResultItem> mResultItems;

	private final String mName;

	private final Type mType;

	SearchResultImpl(List<ResultItem> ritems, String name, Type type) {

		if (ritems == null) {
			throw new NullPointerException("ritems is null");
		}

		if (type == null) {
			throw new NullPointerException("type is null");
		}

		mResultItems = new ArrayList<ResultItem>(ritems);
		mName = name;
		mType = type;
	}

	/**
	 * Easy constructor for searchResults
	 *
	 * @param ritems
	 */
	SearchResultImpl(List<ResultItem> ritems) {
		this(ritems, null, Type.searchResult);
	}

	@Override
	public List<ResultItem> getResultItems() {
		return Collections.unmodifiableList(mResultItems);
	}

	@Override
	public String getName() {
		return mName;
	}

	@Override
	public Type getType() {
		return mType;
	}

	@Override
	public String toString() {
		int i = 0;
		StringBuilder sb = new StringBuilder("sr{");
		if (mName != null) {
			sb.append(mName);
			sb.append(", ");
		}
		for (ResultItem ri : mResultItems) {
			i++;
			sb.append(ri.toString());
			if (i != mResultItems.size()) {
				sb.append(", ");
			}
		}
		sb.append("}");
		return sb.toString();
	}
}
