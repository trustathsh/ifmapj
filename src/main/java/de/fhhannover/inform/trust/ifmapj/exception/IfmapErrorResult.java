package de.fhhannover.inform.trust.ifmapj.exception;

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

import de.fhhannover.inform.trust.ifmapj.channel.ARC;
import de.fhhannover.inform.trust.ifmapj.channel.SSRC;
import de.fhhannover.inform.trust.ifmapj.messages.PollResult;

/**
 * Exception to be thrown if a MAPS replied with an ErrorResult to any of the
 * methods {@link SSRC} and {@link ARC} offers.
 * 
 * @author aw
 *
 */
public class IfmapErrorResult extends Exception {
	
	/**
	 * auto-generated
	 */
	private static final long serialVersionUID = 5677886243271636884L;

	/**
	 * represents the errorCode used by the MAPS
	 */
	private final IfmapErrorCode mErrorCode;
	
	/**
	 * represents the errorString used by the MAPS
	 */
	private final String mErrorString;

	/**
	 * represents the name of the subscription if contained in errorResults
	 * of a {@link PollResult}
	 */
	private final String mName;
	
	/**
	 * Construct a {@link IfmapErrorResult} exception with obligatory parameters
	 * 
	 * @param errCode
	 * @param errStr
	 * @throws NullPointerException if errStr is null
	 */
	public IfmapErrorResult(IfmapErrorCode errCode, String errStr) {
		this(errCode, errStr, null);
	}
	
	public IfmapErrorResult(IfmapErrorCode errCode, String errStr, String name) {
		
		if (errStr == null)
			throw new NullPointerException("errStr is not allowed to be null");
		
		mErrorCode = errCode;
		mErrorString = errStr;
		mName = name;
	}

	public IfmapErrorCode getErrorCode() {
		return mErrorCode;
	}

	public String getErrorString() {
		return mErrorString;
	}

	public String getName() {
		return mName;
	}
	
	public String toString() {
		return String.format("error{%s, %s%s}",
				getErrorCode(), getErrorString(),
				(mName != null) ? ", name=" + mName : "");
	}
}
