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
 * This file is part of ifmapj, version 2.2.2, implemented by the Trust@HsH
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
package de.hshannover.f4.trust.ifmapj.channel;

import javax.net.ssl.KeyManager;
import javax.net.ssl.TrustManager;

import de.hshannover.f4.trust.ifmapj.exception.CommunicationException;
import de.hshannover.f4.trust.ifmapj.exception.IfmapErrorResult;
import de.hshannover.f4.trust.ifmapj.exception.IfmapException;
import de.hshannover.f4.trust.ifmapj.exception.InitializationException;
import de.hshannover.f4.trust.ifmapj.messages.EndSessionRequest;
import de.hshannover.f4.trust.ifmapj.messages.NewSessionRequest;
import de.hshannover.f4.trust.ifmapj.messages.NewSessionResult;
import de.hshannover.f4.trust.ifmapj.messages.PublishRequest;
import de.hshannover.f4.trust.ifmapj.messages.PurgePublisherRequest;
import de.hshannover.f4.trust.ifmapj.messages.RenewSessionRequest;
import de.hshannover.f4.trust.ifmapj.messages.Requests;
import de.hshannover.f4.trust.ifmapj.messages.Result;
import de.hshannover.f4.trust.ifmapj.messages.SearchRequest;
import de.hshannover.f4.trust.ifmapj.messages.SearchResult;
import de.hshannover.f4.trust.ifmapj.messages.SubscribeRequest;

public class SsrcImpl extends AbstractChannel implements SSRC {

	/**
	 * the session-id received after the last call to {@link #newSession()}
	 */
	private String mSessionId;

	/**
	 * the publisher-id received after the last call to {@link #newSession()}
	 */
	private String mPublisherId;

	/**
	 * the max-poll-result-size received after the last call to {@link #newSession()},
	 * if any
	 */
	private Integer mMaxPollResultSize;

	/**
	 * KeyManager instances to initialize the SSL Context.
	 */
	private KeyManager[] mKeyManagers;

	/**
	 * TrustManager instances to initialize the SSL Context.
	 */
	private TrustManager[] mTrustManagers;

	/**
	 * Basic-Auth constructor.
	 *
	 * @param url
	 * @param user
	 * @param pass
	 * @param tms
	 * @param initialConnectionTimeout the initial connection timeout in milliseconds
	 * @throws InitializationException
	 */
	public SsrcImpl(String url, String user, String pass, TrustManager[] tms, int initialConnectionTimeout)
			throws InitializationException {
		super(url, user, pass, tms, initialConnectionTimeout);
		mTrustManagers = tms;
	}

	/**
	 * Certificate-based constructor.
	 *
	 * @param url
	 * @param kms
	 * @param tms
	 * @param initialConnectionTimeout the initial connection timeout in milliseconds
	 * @throws InitializationException
	 */
	public SsrcImpl(String url, KeyManager[] kms, TrustManager[] tms, int initialConnectionTimeout)
			throws InitializationException {
		super(url, kms, tms, initialConnectionTimeout);
		mKeyManagers = kms;
		mTrustManagers = tms;
	}

	@Override
	public void newSession() throws IfmapErrorResult, IfmapException {
		newSession(null);
	}

	@Override
	public void newSession(Integer maxPollResSize)
			throws IfmapErrorResult, IfmapException {

		NewSessionRequest nsreq = Requests.createNewSessionReq();

		nsreq.setMaxPollResultSize(maxPollResSize);

		Result res = genericRequest(nsreq);

		if (!(res instanceof NewSessionResult)) {
			throw new RuntimeException("wrong result-type");
		}

		NewSessionResult nsres = (NewSessionResult) res;

		// reset session information
		mSessionId = null;
		mMaxPollResultSize = null;
		mPublisherId = null;
		mSessionId = nsres.getSessionId();
		mPublisherId = nsres.getPublisherId();

		if (maxPollResSize != null) {
			mMaxPollResultSize = nsres.getMaxPollResultSize();
			if (mMaxPollResultSize == null) {
				throw new CommunicationException("no max-poll-result-size in newSession result");
			}
		}
	}

	@Override
	public void endSession() throws IfmapErrorResult, IfmapException {
		EndSessionRequest esr = Requests.createEndSessionReq();
		genericRequestWithSessionId(esr);

		mPublisherId = null;
		mSessionId = null;
		mMaxPollResultSize = null;
	}

	@Override
	public void renewSession()
			throws IfmapErrorResult, IfmapException  {
		RenewSessionRequest rsr = Requests.createRenewSessionReq();
		genericRequestWithSessionId(rsr);
	}

	@Override
	public void purgePublisher() throws IfmapErrorResult, IfmapException {
		purgePublisher(getPublisherId());
	}

	@Override
	public void purgePublisher(String publisherId)
			throws IfmapErrorResult, IfmapException {
		PurgePublisherRequest ppr = Requests.createPurgePublisherReq();
		ppr.setPublisherId(publisherId);
		genericRequestWithSessionId(ppr);
	}

	@Override
	public void publish(PublishRequest pr) throws IfmapErrorResult, IfmapException {
		if (pr == null) {
			throw new NullPointerException();
		}

		genericRequestWithSessionId(pr);
	}

	@Override
	public void subscribe(SubscribeRequest sr) throws IfmapErrorResult, IfmapException {
		if (sr == null) {
			throw new NullPointerException();
		}

		genericRequestWithSessionId(sr);
	}

	@Override
	public SearchResult search(SearchRequest sr) throws IfmapErrorResult, IfmapException {
		if (sr == null) {
			throw new NullPointerException();
		}

		Result res = genericRequestWithSessionId(sr);

		if (!(res instanceof SearchResult)) {
			throw new RuntimeException("search returns no SearchResult?");
		}

		return (SearchResult) res;
	}

	@Override
	public ARC getArc() throws InitializationException {
		ARC ret;

		if (isBasicAuth()) {
			ret = new ArcImpl(this, getUrl(), getUser(), getPassword(), mTrustManagers, mInitialConnectionTimeout);
		} else {
			ret = new ArcImpl(this, getUrl(), mKeyManagers, mTrustManagers, mInitialConnectionTimeout);
		}

		if (usesGzip()) {
			ret.setGzip(true);
		}

		return ret;
	}

	@Override
	public String getSessionId() {
		return mSessionId;
	}

	@Override
	public String getPublisherId() {
		return mPublisherId;
	}

	@Override
	public Integer getMaxPollResSize() {
		return mMaxPollResultSize;
	}

	@Override
	public void setSessionId(String sessionId) {
		mSessionId = sessionId;
	}

	@Override
	public void setPublisherId(String publisherId) {
		mPublisherId = publisherId;
	}

	@Override
	public void setMaxPollResSize(Integer mprs) {
		mMaxPollResultSize = mprs;
	}
}
