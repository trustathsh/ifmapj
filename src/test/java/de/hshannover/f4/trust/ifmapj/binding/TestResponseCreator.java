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
 * This file is part of ifmapj, version 1.0.1, implemented by the Trust@HsH
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
package de.hshannover.f4.trust.ifmapj.binding;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.junit.Ignore;

@Ignore
public class TestResponseCreator {

	public static final String IFMAP_NS_URI =  "http://www.trustedcomputinggroup.org/2010/IFMAP/2";
	public static final String STD_META_NS_URI =  "http://www.trustedcomputinggroup.org/2010/IFMAP-METADATA/2";
	public static final String IFMAP_PREFIX =  "ifmap";
	public static final String SOAP_PREFIX = "env";
	public static final String SOAP_NS_URI =  "http://www.w3.org/2003/05/soap-envelope";

	private static final String ENV_HEAD = "<" + SOAP_PREFIX + ":Envelope"
		+ " xmlns:" + SOAP_PREFIX + "=\"" + SOAP_NS_URI + "\""
		+ " xmlns:" + IFMAP_PREFIX + "=\"" + IFMAP_NS_URI + "\">";

	private static final String BODY_HEAD = "<" + SOAP_PREFIX + ":Body>";
	private static final String RESPONSE_HEAD = "<" + IFMAP_PREFIX + ":response>";
	private static final String RESPONSE_TAIL = "</" + IFMAP_PREFIX + ":response>";

	private static final String BODY_TAIL = "</" + SOAP_PREFIX + ":Body>";
	private static final String ENV_TAIL = "</" + SOAP_PREFIX + ":Envelope>";


	String createProlog() {
		StringBuffer sb = new StringBuffer();
		sb.append(ENV_HEAD);
		sb.append(BODY_HEAD);
		sb.append(RESPONSE_HEAD);
		return sb.toString();
	}

	String createEpilog() {
		StringBuffer sb = new StringBuffer();
		sb.append(RESPONSE_TAIL);
		sb.append(BODY_TAIL);
		sb.append(ENV_TAIL);
		return sb.toString();
	}

	InputStream createSearchResultResponse(String resultItem) {
		List<String> list = new LinkedList<String>();
		list.add(resultItem);
		return createSearchResultResponse(list);
	}

	public String createSearchResult(String name, String resultItem) {
		List<String> list = new LinkedList<String>();
		list.add(resultItem);
		return createSearchResult(name, list);
	}

	InputStream createSearchResultResponse(List<String> resultItems) {
		StringBuffer sb = new StringBuffer();
		sb.append(createProlog());
		sb.append(createSearchResult("searchResult", null, resultItems));
		sb.append(createEpilog());
		return new ByteArrayInputStream(sb.toString().getBytes());
	}

	private String createSearchResult(String type, String name,
			List<String> resultItems) {
		StringBuffer sb = new StringBuffer();
		sb.append("<");
		sb.append(type);
		if (name != null) {
			sb.append(" name=\"");
			sb.append(name);
			sb.append("\"");
		}
		sb.append(">");

		for (String ri : resultItems) {
			sb.append("<resultItem>");
			sb.append(ri);
			sb.append("</resultItem>");
		}

		sb.append("</");
		sb.append(type);
		sb.append(">");
		return sb.toString();
	}


	String createSearchResult(String name, List<String> resultItems) {
		return createSearchResult("searchResult", name , resultItems);
	}

	String createUpdateResult(String name, List<String> resultItems) {
		return createSearchResult("updateResult", name , resultItems);
	}

	String createDeleteResult(String name, List<String> resultItems) {
		return createSearchResult("deleteResult", name , resultItems);
	}

	String createNotifyResult(String name, List<String> resultItems) {
		return createSearchResult("notifyResult", name , resultItems);
	}

	InputStream createPollResultResponse(List<String> results) {
		StringBuffer sb = new StringBuffer();
		sb.append(createProlog());
		sb.append("<pollResult>");

		for (String result : results) {
			sb.append(result);
		}

		sb.append("</pollResult>");
		sb.append(createEpilog());
		return new ByteArrayInputStream(sb.toString().getBytes());
	}

	public InputStream createPollResultResponse(String searchResult) {
		List<String> list = new LinkedList<String>();
		list.add(searchResult);
		return createPollResultResponse(list);
	}

}
