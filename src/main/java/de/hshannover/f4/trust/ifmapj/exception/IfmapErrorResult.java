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
package de.hshannover.f4.trust.ifmapj.exception;

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
	 */
	public IfmapErrorResult(IfmapErrorCode errCode, String errStr) {
		this(errCode, errStr, null);
	}

	public IfmapErrorResult(IfmapErrorCode errCode, String errStr, String name) {

		if (errStr == null) {
			throw new NullPointerException("errStr is not allowed to be null");
		}

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

	@Override
	public String toString() {
		return String.format("error{%s, %s%s}",
				getErrorCode(), getErrorString(),
				mName != null ? ", name=" + mName : "");
	}
}
