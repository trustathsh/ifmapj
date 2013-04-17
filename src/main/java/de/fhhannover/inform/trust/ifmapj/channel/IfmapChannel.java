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

import de.fhhannover.inform.trust.ifmapj.exception.CommunicationException;
import de.fhhannover.inform.trust.ifmapj.exception.IfmapErrorResult;
import de.fhhannover.inform.trust.ifmapj.exception.IfmapException;
import de.fhhannover.inform.trust.ifmapj.messages.Request;
import de.fhhannover.inform.trust.ifmapj.messages.Result;

/**
 * Interface for an IF-MAP communication channel
 * 
 * @author aw
 *
 */
public interface IfmapChannel {
	
	/**
	 * @return the current session-id for this channel, either set by
	 * a call to {@link SSRC#newSession()} or {@link #setSessionId(String)},
	 * or null if none of these calls was made.
	 */
	public String getSessionId();
	
	/**
	 * Set the session-id to be used for this channel. Be aware, calling this
	 * on an {@link ARC} will change the {@link SSRC} object attached to the
	 * {@link ARC}. Calling it on an {@link SSRC} will also change the attached
	 * {@link ARC} objects for the {@link SSRC}.
	 */
	public void setSessionId(String sessionId);
	
	/**
	 * @return the current ifmap-publisher-id for this channel, either set by
	 * a call to {@link SSRC#newSession()} or {@link #setPublisherId(String)},
	 * or null if none of these calls was made.
	 */
	public String getPublisherId();
	
	/**
	 * Set the ifmap-publisher-id for this channel. Be aware, calling this
	 * on an {@link ARC} will change the {@link SSRC} object attached to the
	 * {@link ARC}. Calling it on an {@link SSRC} will also change the attached
	 * {@link ARC} objects for the {@link SSRC}.
	 * 
	 * This call will only result in having {@link #getPublisherId()} return
	 * the set value. Requests won't be influenced.
	 */
	public void setPublisherId(String publisherId);
	
	/**
	 * @return the current max-poll-result-size for this channel, either set by
	 * a call to {@link SSRC#newSession()} or {@link #setMaxPollResSize(Integer)},
	 * or null if none of these calls was made.
	 */
	public Integer getMaxPollResSize();

	/**
	 * Set the max-poll-result-size for this channel. Be aware, calling this
	 * on an {@link ARC} will change the {@link SSRC} object attached to the
	 * {@link ARC}. Calling it on an {@link SSRC} will also change the attached
	 * {@link ARC} objects for the {@link SSRC}.
	 * 
	 * This call will only result in having {@link #getMaxPollResSize()} return
	 * the set value. Requests won't be influenced.
	 */
	public void setMaxPollResSize(Integer mprs);
	
	/**
	 * Closes the underlying TCP connection.
	 * 
	 * @throws CommunicationException
	 */
	public void closeTcpConnection() throws CommunicationException;
	
	/**
	 * Specifies whether GZIP compression should be used.
	 * 
	 * @param useGzip true if GZIP should be used
	 */
	public void setGzip(boolean useGzip);
	
	/**
	 * Indicates whether GZIP compression is used by this channel
	 * 
	 * @return true, if GZIP is used
	 */
	public boolean usesGzip();
	
	/**
	 * Generic Request Interface
	 * 
	 * @param req
	 * @return
	 * @throws IfmapErrorResult
	 * @throws IfmapException
	 */
	public Result genericRequest(Request req) throws IfmapErrorResult, IfmapException;
	public Result genericRequestWithSessionId(Request req) throws IfmapErrorResult, IfmapException;
}
