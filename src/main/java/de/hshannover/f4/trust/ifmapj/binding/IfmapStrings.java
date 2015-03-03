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
 * This file is part of ifmapj, version 2.2.0, implemented by the Trust@HsH
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
package de.hshannover.f4.trust.ifmapj.binding;

/**
 * Constants to be used for everything in the schema...
 *
 * @author aw
 *
 */
public class IfmapStrings {

	/* General stuff */
	public static final String BASE_PREFIX = "ifmap";
	public static final String BASE_NS_URI =  "http://www.trustedcomputinggroup.org/2010/IFMAP/2";

	public static final String STD_METADATA_PREFIX =  "meta";
	public static final String STD_METADATA_NS_URI =  "http://www.trustedcomputinggroup.org/2010/IFMAP-METADATA/2";

	public static final String OP_METADATA_PREFIX =  "op-meta";
	public static final String OP_METADATA_NS_URI =  "http://www.trustedcomputinggroup.org/2012/IFMAP-OPERATIONAL-METADATA/1";

	public static final String SOAP_ENV_PREFIX = "soap";
	public static final String SOAP_ENV_NS_URI =  "http://www.w3.org/2003/05/soap-envelope";

	public static final String SOAP_ENV_EL_NAME = "Envelope";
	public static final String SOAP_BODY_EL_NAME = "Body";

	public static final String SOAP_PREFIXED_ENV_EL_NAME = SOAP_ENV_PREFIX + ":" + SOAP_ENV_EL_NAME;
	public static final String SOAP_PREFIXED_BODY_EL_NAME = SOAP_ENV_PREFIX + ":" + SOAP_BODY_EL_NAME;

	public static final String EMPTY_VALUE = "";
	public static final String NO_URI = null;

	/* Requests and Responses */
	public static final String RESPONSE_EL_NAME = "response";

	public static final String NEW_SESSION_REQ_EL_NAME = "newSession";
	public static final String NEW_SESSION_REQ_ATTR_MPRS = "max-poll-result-size";
	public static final String NEW_SESSION_RES_EL_NAME = "newSessionResult";
	public static final String NEW_SESSION_RES_ATTR_SID = "session-id";
	public static final String NEW_SESSION_RES_ATTR_PID = "ifmap-publisher-id";
	public static final String NEW_SESSION_RES_ATTR_MPRS = "max-poll-result-size";

	public static final String ERROR_RES_EL_NAME = "errorResult";
	public static final String ERROR_RES_ATTR_ERRCODE = "errorCode";
	public static final String ERROR_RES_ERRSTR_EL_NAME = "errorString";
	public static final String ERROR_RES_ATTR_NAME = "name";


	public static final String SESSION_ID_ATTR = NEW_SESSION_RES_ATTR_SID;
	public static final String PUBLISHER_ID_ATTR = NEW_SESSION_RES_ATTR_PID;

	public static final String END_SESSION_REQ_EL_NAME = "endSession";
	public static final String END_SESSION_RES_EL_NAME = "endSessionResult";

	public static final String PURGE_PUBLISHER_REQ_EL_NAME = "purgePublisher";
	public static final String PURGE_PUBLISHER_RES_EL_NAME = "purgePublisherReceived";

	public static final String RENEW_SESSION_REQ_EL_NAME = "renewSession";
	public static final String RENEW_SESSION_RES_EL_NAME = "renewSessionResult";

	public static final String POLL_REQ_EL_NAME = "poll";
	public static final String POLL_RES_EL_NAME = "pollResult";
	public static final String POLL_SEARCH_RES_EL_NAME = "searchResult";
	public static final String POLL_UPDATE_RES_EL_NAME = "updateResult";
	public static final String POLL_DELETE_RES_EL_NAME = "deleteResult";
	public static final String POLL_NOTIFY_RES_EL_NAME = "notifyResult";

	public static final String PUBLISH_REQ_EL_NAME = "publish";
	public static final String PUBLISH_RES_EL_NAME = "publishReceived";
	public static final String PUBLISH_UPDATE_EL_NAME = "update";
	public static final String PUBLISH_UPDATE_ATTR_LIFETIME = "lifetime";
	public static final String PUBLISH_DELETE_EL_NAME = "delete";
	public static final String PUBLISH_DELETE_ATTR_FILTER = "filter";
	public static final String PUBLISH_NOTIFY_EL_NAME = "notify";
	public static final String PUBLISH_METADATA_EL_NAME = "metadata";

	public static final String SEARCH_REQ_EL_NAME = "search";
	public static final String SEARCH_MATCH_LINKS_FILTER_ATTR = "match-links";
	public static final String SEARCH_RESULT_FILTER_ATTR = "result-filter";
	public static final String SEARCH_TERM_IDENT_TYPE = "terminal-identifier-type";
	public static final String SEARCH_MAX_DEPTH_ATTR = "max-depth";
	public static final String SEARCH_MAX_SIZE_ATTR = "max-size";

	public static final String SEARCH_RES_EL_NAME = "searchResult";
	public static final String SEARCH_RES_NAME_ATTR = "name";
	public static final String SEARCH_METADATA_EL_NAME = "metadata";

	public static final String RESULT_ITEM_EL_NAME = "resultItem";

	public static final String SUBSCRIBE_REQ_EL_NAME = "subscribe";
	public static final String SUBSCRIBE_RES_EL_NAME = "subscribeReceived";
	public static final String SUBSCRIBE_UPDATE_EL_NAME = "update";
	public static final String SUBSCRIBE_DELETE_EL_NAME = "delete";
	public static final String SUBSCRIBE_SUB_NAME_ATTR = "name";

	/* Identifier stuff */
	public static final String IDENTIFIER_ATTR_ADMIN_DOMAIN = "administrative-domain";

	public static final String ACCESS_REQUEST_EL_NAME = "access-request";
	public static final String ACCESS_REQUEST_ATTR_NAME = "name";

	public static final String DEVICE_EL_NAME = "device";
	public static final String DEVICE_NAME_EL_NAME = "name";

	public static final String IDENTITY_EL_NAME = "identity";
	public static final String IDENTITY_ATTR_NAME = "name";
	public static final String IDENTITY_ATTR_TYPE = "type";
	public static final String IDENTITY_ATTR_OTHER_TYPE_DEF = "other-type-definition";

	public static final String IP_ADDRESS_EL_NAME = "ip-address";
	public static final String IP_ADDRESS_ATTR_VALUE = "value";
	public static final String IP_ADDRESS_ATTR_TYPE = "type";

	public static final String MAC_ADDRESS_EL_NAME = "mac-address";
	public static final String MAC_ADDRESS_ATTR_VALUE = "value";

	public static final String OTHER_TYPE_EXTENDED_IDENTIFIER = "extended";
}
