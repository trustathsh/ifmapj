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
package de.hshannover.f4.trust.ifmapj21;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import util.DateHelpers;
import de.hshannover.f4.trust.ifmapj.IfmapJ;
import de.hshannover.f4.trust.ifmapj.binding.IfmapStrings;
import de.hshannover.f4.trust.ifmapj.channel.SSRC;
import de.hshannover.f4.trust.ifmapj.exception.IfmapErrorResult;
import de.hshannover.f4.trust.ifmapj.exception.IfmapException;
import de.hshannover.f4.trust.ifmapj.identifier.Device;
import de.hshannover.f4.trust.ifmapj.log.IfmapJLog;
import de.hshannover.f4.trust.ifmapj.messages.MetadataLifetime;
import de.hshannover.f4.trust.ifmapj.messages.PublishDelete;
import de.hshannover.f4.trust.ifmapj.messages.PublishElement;
import de.hshannover.f4.trust.ifmapj.messages.PublishRequest;
import de.hshannover.f4.trust.ifmapj.messages.Requests;
import de.hshannover.f4.trust.ifmapj.messages.ResultItem;
import de.hshannover.f4.trust.ifmapj.messages.SearchRequest;
import de.hshannover.f4.trust.ifmapj.messages.SearchResult;
import de.hshannover.f4.trust.ifmapj.metadata.StandardIfmapMetadataFactory;

/**
 * Helper class to synchronize time between MAPC and MAPS.
 *
 * This class implements the behaviour described in sec. 4.6.1
 * of published TNC IF-MAP Binding for SOAP specification 2.1 Rev 15.
 *
 * @author jk
 * @since 0.1.5
 */
public final class ClockSkewDetector {

	/**
	 * The SSRC to be used for time synchronization.
	 */
	private final SSRC mSsrc;

	/**
	 * Clock skew after the last call to {@link #publishTime()}
	 */
	private Long mClockSkew;

	/**
	 * Last successful clock skew adjustment
	 */
	private Calendar mLastTimeSynchronization;

	/**
	 * Device identifier used for time synchronization.
	 */
	private final Device mDev;

	/**
	 * Private Metadata factory.
	 */
	private StandardIfmapMetadataFactory mMetadataFac;

	/**
	 * Factory method to get a {@link ClockSkewDetector} instance.
	 *
	 * @param ssrc
	 * @param dev
	 * @return
	 */
	public static ClockSkewDetector newInstance(SSRC ssrc, Device dev) {
		return new ClockSkewDetector(ssrc, dev);
	}

	/**
	 * Private constructor.
	 *
	 * @param ssrc
	 * @param dev
	 */
	private ClockSkewDetector(SSRC ssrc, Device dev) {

		if (ssrc == null) {
			throw new NullPointerException();
		}

		if (dev == null) {
			throw new NullPointerException();
		}

		mSsrc = ssrc;
		mDev = dev;
		mMetadataFac = IfmapJ.createStandardMetadataFactory();
	}

	/**
	 * Send a client-time publish request with the current time followed by
	 * a search request in order to synchronize time to MAPS.
	 *
	 * <pre>
	 *  This function implements the behaviour described in sec. 4.6.1
	 *  of published TNC IF-MAP Binding for SOAP specification 2.1 Rev 15.
	 * </pre>
	 *
	 * @since 0.1.5
	 * @throws IfmapErrorResult
	 * @throws IfmapException
	 * @throws EndSessionException
	 */
	public long performClockSynchronization() throws IfmapErrorResult, IfmapException {
		return performClockSkewDetection(null);
	}

	/**
	 * Send a client-time publish request with the current time followed by
	 * a search request in order to synchronize time to MAPS.
	 *
	 * <pre>
	 *  This function implements the behaviour described in sec. 4.6.1
	 *  of published TNC IF-MAP Binding for SOAP specification 2.1 Rev 15.
	 * </pre>
	 *
	 * @since 0.1.5
	 * @throws IfmapErrorResult
	 * @throws IfmapException
	 * @throws EndSessionException
	 */
	public long performClockSkewDetection(Calendar clientTime) throws IfmapErrorResult, IfmapException {

		publishTime(clientTime);
		Element time = searchTime();
		deleteTimes();
		mClockSkew = calculateTimeDiff(time);
		mLastTimeSynchronization = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

		return mClockSkew;
	}
	/**
	 *
	 * Just wrap those crazy transformations into a helper...
	 *
	 * @param time
	 * @return
	 */
	private long calculateTimeDiff(Element time) {

		try {
			String ifmapTs = time.getAttribute("ifmap-timestamp");
			String curTs = time.getAttribute("current-timestamp");

			Calendar ifmapTime = DateHelpers.getTimeFromIso8601String(ifmapTs);
			Calendar curTime = DateHelpers.getTimeFromIso8601String(curTs);

			return ifmapTime.getTimeInMillis() - curTime.getTimeInMillis();
		} catch (DatatypeConfigurationException e) {
			IfmapJLog.warn("time sync: could not convert times: " + e.getMessage());
		}

		// Error case:
		return 0;
	}

	/**
	 * Has time successfully been synchronized?
	 *
	 * <pre>
	 *  Defaults to false until {@link performClockSkewDetection}
	 *  without any parameter has been called. Gets set to true if
	 *  a call to {@link performClockSkewDetection} was successful.
	 * </pre>
	 *
	 * @since 0.1.5
	 * @return true if clock has been detected
	 */
	public boolean getClockSynchronized() {
		return mClockSkew != null;
	}

	/**
	 * Detected clock skew between the client and MAPS
	 *
	 * @since 0.1.5
	 * @return the difference between both systems in milliseconds.
	 */
	public long getClockSkewMilliseconds() {
		return mClockSkew.longValue();
	}

	/**
	 * Detected clock skew between the client and MAPS
	 *
	 * @since 0.1.5
	 * @return the difference between both systems in rounded seconds.
	 */
	public Integer getClockSkewSeconds() {
		return (int) Math.round(mClockSkew.doubleValue() / 1000);
	}

	/**
	 * The corrected server time
	 *
	 * @since 0.1.5
	 * @return current UTC server time
	 */
	public Calendar getClockOfServer() {
		Calendar retCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		retCal.add(Calendar.MILLISECOND, mClockSkew.intValue());
		return retCal;
	}

	/**
	 * Last successful publish of client time
	 *
	 * @since 0.1.5
	 * @return date and time of last successful call to {@link publishClientTime}.
	 */
	public Calendar getClockLastSynchronization() {
		if (getClockSynchronized()) {
			return (Calendar) mLastTimeSynchronization.clone();
		}
		return null;
	}

	/**
	 * Attach the current client time to the {{@link #mDev} identifier.
	 *
	 * if clientTime is null, the current time is used.
	 *
	 * @param clientTime the supplied client time, use null for current time
	 * @throws IfmapErrorResult
	 * @throws IfmapException
	 */
	private void publishTime(Calendar clientTime) throws IfmapErrorResult, IfmapException {
		PublishElement pu;
		PublishRequest pr;
		String date = DateHelpers.getUtcTimeAsIso8601(clientTime);
		Document timeMd = mMetadataFac.createClientTime(date);

		pu = Requests.createPublishUpdate(mDev, timeMd, MetadataLifetime.session);
		pr = Requests.createPublishReq(pu);

		mSsrc.publish(pr);
	}

	/**
	 * Search for the self-published client time on {@link #mDev}.
	 *
	 * @return XML element with client and server timestamp
	 * @throws IfmapErrorResult
	 * @throws IfmapException
	 */
	private Element searchTime() throws IfmapErrorResult, IfmapException {

		SearchRequest sr;
		SearchResult res;
		List<ResultItem> items;
		ResultItem ri;
		List<Document> mlist;
		Node node;

		String resultFilter = IfmapStrings.OP_METADATA_PREFIX + ":client-time[@" + IfmapStrings.PUBLISHER_ID_ATTR
			+ " = \"" + mSsrc.getPublisherId() + "\"]";


		sr = Requests.createSearchReq(null, 0, null, null, resultFilter, mDev);
		sr.addNamespaceDeclaration(IfmapStrings.OP_METADATA_PREFIX,
				IfmapStrings.OP_METADATA_NS_URI);

		res = mSsrc.search(sr);

		items = res.getResultItems();


		if (items.size() > 1) {
			IfmapJLog.warn("time sync: weird result item count: " + items.size());
		}

		if (items.size() == 0) {
			throw new IfmapException("time sync", "No ResultItems for search!");
		}

		ri = items.get(0);

		mlist = ri.getMetadata();

		if (mlist.size() > 1) {
			IfmapJLog.warn("time sync: multiple client-time elements: " + mlist.size());
		}

		if (mlist.size() == 0) {
			throw new IfmapException("time sync", "No client-time metadata!");
		}


		// Take the last one in the list, hoping that it is the most current
		// one.
		Document clientTime = mlist.get(mlist.size() - 1);

		node = clientTime.getFirstChild();

		if (node.getNodeType() != Node.ELEMENT_NODE) {
			throw new IfmapException("time sync", "Metadata is not element");
		}

		return (Element) node;
	}

	/**
	 * Delete all current-time metadata objects attached to the {{@link #mDev}
	 * identifier provided they have "our" publisher-id.
	 *
	 * @throws IfmapErrorResult
	 * @throws IfmapException
	 */
	private void deleteTimes() throws IfmapErrorResult, IfmapException {
		String filter = IfmapStrings.OP_METADATA_PREFIX + ":client-time[@" + IfmapStrings.PUBLISHER_ID_ATTR
			+ " = \"" + mSsrc.getPublisherId() + "\"]";
		PublishDelete pd = Requests.createPublishDelete(mDev, filter);
		pd.addNamespaceDeclaration(IfmapStrings.OP_METADATA_PREFIX,
				IfmapStrings.OP_METADATA_NS_URI);
		PublishRequest pr = Requests.createPublishReq(pd);

		mSsrc.publish(pr);
	}
}
