package de.fhhannover.inform.trust.ifmapj.channel;

/*
 * #%L
 * ====================================================
 *   _____                _     ____  _____ _   _ _   _
 *  |_   _|_ __ _   _ ___| |_  / __ \|  ___| | | | | | |
 *    | | | '__| | | / __| __|/ / _` | |_  | |_| | |_| |
 *    | | | |  | |_| \__ \ |_| | (_| |  _| |  _  |  _  |
 *    |_| |_|   \__,_|___/\__|\ \__,_|_|   |_| |_|_| |_|
 *                             \____/
 * 
 * =====================================================
 * 
 * Fachhochschule Hannover 
 * (University of Applied Sciences and Arts, Hannover)
 * Faculty IV, Dept. of Computer Science
 * Ricklinger Stadtweg 118, 30459 Hannover, Germany
 * 
 * Email: trust@f4-i.fh-hannover.de
 * Website: http://trust.inform.fh-hannover.de/
 * 
 * This file is part of IfmapJ, version 0.1.5, implemented by the Trust@FHH 
 * research group at the Fachhochschule Hannover.
 * 
 * IfmapJ is a lightweight, platform-independent, easy-to-use IF-MAP client
 * library for Java. IF-MAP is an XML based protocol for sharing data across
 * arbitrary components, specified by the Trusted Computing Group. IfmapJ is
 * maintained by the Trust@FHH group at the Fachhochschule Hannover. IfmapJ 
 * was developed within the ESUKOM research project.
 * %%
 * Copyright (C) 2010 - 2013 Trust@FHH
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

import de.fhhannover.inform.trust.ifmapj.exception.IfmapErrorResult;
import de.fhhannover.inform.trust.ifmapj.exception.IfmapException;
import de.fhhannover.inform.trust.ifmapj.exception.InitializationException;
import de.fhhannover.inform.trust.ifmapj.messages.PublishRequest;
import de.fhhannover.inform.trust.ifmapj.messages.SearchRequest;
import de.fhhannover.inform.trust.ifmapj.messages.SearchResult;
import de.fhhannover.inform.trust.ifmapj.messages.SubscribeRequest;

/**
 * Interface describing the Synchronous Send Receive Channel (SSRC) methods.
 * 
 * @author aw
 * @author jk
 */
public interface SSRC extends IfmapChannel {

	/**
	 * Send a newSession request to MAPS
	 * 
	 * <pre>
	 *  Deprecated: Since IF-MAP 2.1 a device identifier MUST be
	 *  supplied by the MAPC which represents the client itself.
	 * </pre>
	 * 
	 * @throws IfmapErrorResult
	 * @throws IfmapException
	 */
	public void newSession()
			throws IfmapErrorResult, IfmapException;
	
	/**
	 * Send a newSession request to MAPS with the given parameter
	 * 
	 * <pre>
	 *  Deprecated: Since IF-MAP 2.1 a device identifier MUST be
	 *  supplied by the MAPC which represents the client itself.
	 * </pre>
	 * 
	 * @param maxPollResSize the maximum size of poll results that can be
	 * processed by the MAPC given in bytes
	 * @throws IfmapErrorResult
	 * @throws IfmapException
	 */
	public void newSession(Integer maxPollResSize)
			throws IfmapErrorResult, IfmapException;

	/**
	 * Send endSession request to MAPS
	 * 
	 * @throws IfmapErrorResult
	 * @throws IfmapException
	 */
	public void endSession() throws IfmapErrorResult, IfmapException;
	
	/**
	 * Send renewSession request to MAPS
	 * 
	 * @throws IfmapErrorResult
	 * @throws IfmapException
	 */
	public void renewSession()
			throws IfmapErrorResult, IfmapException;

	/**
	 * Send purgePublisher request to MAPS in order to purge the MAPCs
	 * metadata
	 * 
	 * @throws IfmapErrorResult
	 * @throws IfmapException
	 */
	public void purgePublisher() throws IfmapErrorResult, IfmapException;
	
	/**
	 * Send purgePublisher request with the given parameter to MAPS
	 * 
	 * @param publisherId the publisher-id of the MAPC whose metadata should be purged
	 * 
	 * @throws IfmapErrorResult
	 * @throws IfmapException
	 */
	public void purgePublisher(String publisherId) throws IfmapErrorResult, IfmapException;
	
	/**
	 * Send a publish request to MAPS.
	 * 
	 * @param req the publish request that is sent
	 * @throws IfmapErrorResult
	 * @throws IfmapException
	 */
	public void publish(PublishRequest req) throws IfmapErrorResult, IfmapException;

	/**
	 * Send a subscribe request to the MAPS.
	 * 
	 * @param req the subscribe request
	 * @throws IfmapErrorResult
	 * @throws IfmapException
	 */
	public void subscribe(SubscribeRequest req) throws IfmapErrorResult, IfmapException;
	
	/**
	 * Send a search request to the MAPS
	 * 
	 * @param req the search request
	 * @return the results of the search
	 * @throws IfmapErrorResult
	 * @throws IfmapException
	 */
	public SearchResult search(SearchRequest req) throws IfmapErrorResult, IfmapException;

	/* public void dump(...) throws IOException, IfmapError; */
	
	/**
	 * Open a {@link ARC} corresponding to this {@link SSRC} instance
	 * 
	 * @return the new {@link ARC}
	 */
	public ARC getArc() throws InitializationException;
}
