package de.hshannover.f4.trust.ifmapj.channel;

import de.hshannover.f4.trust.ifmapj.channel.ARC;
import de.hshannover.f4.trust.ifmapj.channel.SSRC;
import de.hshannover.f4.trust.ifmapj.exception.CommunicationException;
import de.hshannover.f4.trust.ifmapj.exception.IfmapErrorResult;
import de.hshannover.f4.trust.ifmapj.exception.IfmapException;
import de.hshannover.f4.trust.ifmapj.exception.InitializationException;
import de.hshannover.f4.trust.ifmapj.messages.PublishRequest;
import de.hshannover.f4.trust.ifmapj.messages.Request;
import de.hshannover.f4.trust.ifmapj.messages.Result;
import de.hshannover.f4.trust.ifmapj.messages.SearchRequest;
import de.hshannover.f4.trust.ifmapj.messages.SearchResult;
import de.hshannover.f4.trust.ifmapj.messages.SubscribeRequest;

/**
 * This class wraps a {@link SSRC} in a thread safe manner.
 *
 * @author Marius Rohde
 *
 */
public class ThreadSafeSsrc implements SSRC {

	private SSRC mSsrc;

	/**
	 * Creates a new thread safe {@link SSRC} based on the given {@link SSRC}.
	 *
	 * @param ssrc the {@link SSRC} instance
	 */
	public ThreadSafeSsrc(SSRC ssrc) throws InitializationException {
		mSsrc = ssrc;
	}

	@Override
	public synchronized String getSessionId() {
		return mSsrc.getSessionId();
	}

	@Override
	public synchronized void setSessionId(String sessionId) {
		mSsrc.setSessionId(sessionId);
	}

	@Override
	public synchronized String getPublisherId() {
		return mSsrc.getPublisherId();
	}

	@Override
	public synchronized void setPublisherId(String publisherId) {
		mSsrc.setPublisherId(publisherId);
	}

	@Override
	public synchronized Integer getMaxPollResSize() {
		return mSsrc.getMaxPollResSize();
	}

	@Override
	public synchronized void setMaxPollResSize(Integer mprs) {
		mSsrc.setMaxPollResSize(mprs);
	}

	@Override
	public synchronized void closeTcpConnection() throws CommunicationException {
		mSsrc.closeTcpConnection();
	}

	@Override
	public synchronized void setGzip(boolean useGzip) {
		mSsrc.setGzip(useGzip);
	}

	@Override
	public synchronized boolean usesGzip() {
		return mSsrc.usesGzip();
	}

	@Override
	public synchronized Result genericRequest(Request req) throws IfmapErrorResult, IfmapException {
		return mSsrc.genericRequest(req);
	}

	@Override
	public synchronized Result genericRequestWithSessionId(Request req) throws IfmapErrorResult, IfmapException {
		return mSsrc.genericRequestWithSessionId(req);
	}

	@Override
	public synchronized void newSession() throws IfmapErrorResult, IfmapException {
		mSsrc.newSession();
	}

	@Override
	public synchronized void newSession(Integer maxPollResSize) throws IfmapErrorResult, IfmapException {
		mSsrc.newSession(maxPollResSize);
	}

	@Override
	public synchronized void endSession() throws IfmapErrorResult, IfmapException {
		mSsrc.endSession();
	}

	@Override
	public synchronized void renewSession() throws IfmapErrorResult, IfmapException {
		mSsrc.renewSession();
	}

	@Override
	public synchronized void purgePublisher() throws IfmapErrorResult, IfmapException {
		mSsrc.purgePublisher();
	}

	@Override
	public synchronized void purgePublisher(String publisherId) throws IfmapErrorResult, IfmapException {
		mSsrc.purgePublisher(publisherId);
	}

	@Override
	public synchronized void publish(PublishRequest req) throws IfmapErrorResult, IfmapException {
		mSsrc.publish(req);
	}

	@Override
	public synchronized void subscribe(SubscribeRequest req) throws IfmapErrorResult, IfmapException {
		mSsrc.subscribe(req);
	}

	@Override
	public synchronized SearchResult search(SearchRequest req) throws IfmapErrorResult, IfmapException {
		return mSsrc.search(req);
	}

	@Override
	public synchronized ARC getArc() throws InitializationException {
		return mSsrc.getArc();
	}

}
