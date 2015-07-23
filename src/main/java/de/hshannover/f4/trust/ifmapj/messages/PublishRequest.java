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
package de.hshannover.f4.trust.ifmapj.messages;

import java.util.List;

/**
 * Interface to access data concerning a {@link PublishRequest}.
 *
 * MAPCs can publish or delete metadata on a MAPS. A {@link PublishRequest}
 * is used to do these operations. A {@link PublishRequest} can hold one or
 * more of {@link PublishUpdate}, {@link PublishNotify}
 * and {@link PublishDelete} elements.
 *
 * @author aw
 *
 */
public interface PublishRequest extends Request {

	/**
	 * Add a new {@link PublishElement} to the request
	 *
	 * @param pe the element to add to the list
	 */
	void addPublishElement(PublishElement pe);

	/**
	 * Get all {@link PublishElement} instances of this request
	 *
	 * @return the list of elements in the request. <b>Note:</b> The returned collection is unmodifiable.
	 */
	List<PublishElement> getPublishElements();
}
