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
 * This file is part of ifmapj, version 2.0.1, implemented by the Trust@HsH
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
package de.hshannover.f4.trust.ifmapj.messages;

import java.util.Collection;
import java.util.List;

import de.hshannover.f4.trust.ifmapj.exception.IfmapErrorResult;

/**
 * Interface to access {@link SearchResult} objects contained in a {@link PollResult}.
 *
 * <b>Note:</b> All returned collections should be assumed to be unmodifiable.
 *
 * @author aw
 *
 * FIXME: We have to return the SearchResults in ordered...!!!
 *
 */
public interface PollResult extends Result {

	/**
	 * Get all {@link SearchResult} objects that were received after the initial
	 * poll has been sent to the MAPS.
	 *
	 * @return an unmodifiable collection of {@link SearchResult} objects
	 *
	 * @deprecated
	 * No ordering enforced. Replace with {@link #getResults()} and
	 * {@link SearchResult#getType()}.
	 */
	@Deprecated
	Collection<SearchResult> getSearchResults();

	/**
	 * Get all {@link SearchResult} objects for new metadata that has been
	 * published to the MAPS since the last poll.
	 *
	 * @return an unmodifiable collection of {@link SearchResult} objects
	 *
	 * @deprecated
	 * No ordering enforced. Replace with {@link #getResults()} and
	 * {@link SearchResult#getType()}.
	 */
	@Deprecated
	Collection<SearchResult> getUpdateResults();

	/**
	 * Get all {@link SearchResult} objects for metadata that has been
	 * deleted from the MAPS since the last poll.
	 *
	 * @return an unmodifiable collection of {@link SearchResult} objects
	 *
	 * @deprecated
	 * No ordering enforced. Replace with {@link #getResults()} and
	 * {@link SearchResult#getType()}.
	 */
	@Deprecated
	Collection<SearchResult> getDeleteResults();

	/**
	 * Get all {@link SearchResult} objects for metadata that has been
	 * published to the MAPS by using the publish notify request since the last
	 * poll.
	 *
	 * @return an unmodifiable collection of {@link SearchResult} objects
	 *
	 * @deprecated
	 * No ordering enforced. Replace with {@link #getResults()} and
	 * {@link SearchResult#getType()}.
	 */
	@Deprecated
	Collection<SearchResult> getNotifyResults();

	/**
	 * Get all {@link IfmapErrorResult} objects that indicate errors that
	 * occured since the last poll.
	 *
	 * @return an unmodifiable collection of {@link IfmapErrorResult} objects
	 */
	Collection<IfmapErrorResult> getErrorResults();

	/**
	 * @return an unmodifieable list of {@link SearchResult} instances,
	 * ordered the way they were received from the MAPS.
	 */
	List<SearchResult> getResults();
}
