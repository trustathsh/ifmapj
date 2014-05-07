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
 * This file is part of ifmapj, version 2.0.0, implemented by the Trust@HsH
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
package de.hshannover.f4.trust.ifmapj.channel;

import javax.net.ssl.KeyManager;
import javax.net.ssl.TrustManager;

import de.hshannover.f4.trust.ifmapj.exception.EndSessionException;
import de.hshannover.f4.trust.ifmapj.exception.IfmapErrorResult;
import de.hshannover.f4.trust.ifmapj.exception.IfmapException;
import de.hshannover.f4.trust.ifmapj.exception.InitializationException;
import de.hshannover.f4.trust.ifmapj.messages.PollRequest;
import de.hshannover.f4.trust.ifmapj.messages.PollResult;
import de.hshannover.f4.trust.ifmapj.messages.Requests;
import de.hshannover.f4.trust.ifmapj.messages.Result;

/**
 * Implementation of {@link ARC}
 *
 * @author aw
 *
 */
public class ArcImpl extends AbstractChannel implements ARC {

	/**
	 * {@link SSRC} object used to create this {@link ARC} instance
	 */
	private final SSRC mSsrc;

	ArcImpl(SSRC ssrc, String url, KeyManager[] kms, TrustManager[] tms, int initialConnectionTimeout)
			throws InitializationException {
		super(url, kms, tms, initialConnectionTimeout);
		if (ssrc == null) {
			throw new NullPointerException("Need a valid SSRC instance");
		}

		mSsrc = ssrc;
	}

	ArcImpl(SSRC ssrc, String url, String user, String password, TrustManager[] tms, int initialConnectionTimeout)
			throws InitializationException {
		super(url, user, password, tms, initialConnectionTimeout);
		if (ssrc == null) {
			throw new NullPointerException("Need a valid SSRC instance");
		}

		mSsrc = ssrc;
	}

	@Override
	public PollResult poll()
			throws IfmapErrorResult, EndSessionException, IfmapException {
		PollRequest pollReq = Requests.createPollReq();
		Result res = genericRequestWithSessionId(pollReq);

		if (res instanceof EndSessionException) {
			throw (EndSessionException) res;
		}

		if (!(res instanceof PollResult)) {
			throw new RuntimeException("Wrong result type for poll?");
		}

		return (PollResult) res;
	}

	@Override
	public String getSessionId() {
		return mSsrc.getSessionId();
	}

	@Override
	public String getPublisherId() {
		return mSsrc.getPublisherId();
	}

	@Override
	public Integer getMaxPollResSize() {
		return mSsrc.getMaxPollResSize();
	}

	@Override
	public void setSessionId(String sessionId) {
		mSsrc.setSessionId(sessionId);
	}

	@Override
	public void setPublisherId(String publisherId) {
		mSsrc.setPublisherId(publisherId);
	}

	@Override
	public void setMaxPollResSize(Integer mprs) {
		mSsrc.setMaxPollResSize(mprs);
	}
}
