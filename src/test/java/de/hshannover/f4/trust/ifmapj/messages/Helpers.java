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
 * This file is part of ifmapj, version 2.3.2, implemented by the Trust@HsH
 * research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2010 - 2016 Trust@HsH
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
package de.hshannover.f4.trust.ifmapj.messages;

import javax.xml.parsers.DocumentBuilder;

import org.junit.Ignore;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.hshannover.f4.trust.ifmapj.identifier.Identifier;
import de.hshannover.f4.trust.ifmapj.identifier.Identifiers;
import de.hshannover.f4.trust.ifmapj.identifier.IpAddress;
import de.hshannover.f4.trust.ifmapj.identifier.IpAddressType;
import de.hshannover.f4.trust.ifmapj.identifier.MacAddress;

@Ignore
class TestHelpers {

	private static final String IFMAP_URI = "http://www.trustedcomputinggroup.org/2010/IFMAP/2";

	static Element makeResponse(Document doc) {
		return doc.createElementNS(IFMAP_URI, "ifmap:response");
	}

	static Element addResultToResponse(String resName, Document doc, Element resp) {
		Element ret = doc.createElementNS(null, resName);
		resp.appendChild(ret);
		return ret;
	}

	static Element makeResult(String name, Document doc, Element... elements) {
		Element ret = doc.createElementNS(null, name);
		addElements(ret, elements);
		return ret;
	}

	static Element resultItem(Document doc, Element... elements) {
		Element ret = doc.createElementNS(null, "resultItem");
		addElements(ret, elements);
		return ret;
	}

	private static void addElements(Element ret, Element[] elements) {
		for (Element el : elements) {
			if (el != null) {
				ret.appendChild(el);
			}
		}

	}

	static Element searchResult(Document doc, Element... ris) {
		return makeResult("searchResult", doc, ris);
	}

	static Element notifyResult(Document doc, Element... ris) {
		return makeResult("notifyResult", doc, ris);
	}

	static Element deleteResult(Document doc, Element... ris) {
		return makeResult("deleteResult", doc, ris);
	}

	static Element updateResult(Document doc, Element... ris) {
		return makeResult("updateResult", doc, ris);
	}

	static Element ipElement(Document doc, String value, String type, String ad) {
		Element ret = doc.createElementNS(null, "ip-address");

		if (value != null) {
			ret.setAttribute("value", value);
		}

		if (type != null) {
			ret.setAttribute("type", type);
		}

		if (ad != null) {
			addAd(ret, ad);
		}

		return ret;
	}

	static Element macElement(Document doc, String value, String ad) {
		Element ret = doc.createElementNS(null, "mac-address");

		if (value != null) {
			ret.setAttribute("value", value);
		}

		if (ad != null) {
			addAd(ret, ad);
		}

		return ret;

	}

	private static void addAd(Element ret, String ad) {
		ret.setAttribute("administrative-domain", ad);
	}

	static void addGoodSingleResultItemSearchResult(Document doc, Element to) {
		Element i1 = TestHelpers.ipElement(doc, "192.168.0.1", "IPv4", null);
		Element ri = TestHelpers.resultItem(doc, i1);
		Element resEl = TestHelpers.makeResult("searchResult", doc, ri);
		to.appendChild(resEl);
	}

	static void addGoodSingleResultItemResult(String elName, Document doc, Element to) {
		Element i1 = TestHelpers.ipElement(doc, "192.168.0.1", "IPv4", null);
		Element ri = TestHelpers.resultItem(doc, i1);
		Element resEl = TestHelpers.makeResult(elName, doc, ri);
		resEl.setAttribute("name", "mysub");
		to.appendChild(resEl);
	}

	static Element makeErrorResult(Document doc, String errCode, String errStr) {
		Element err = makeResult("errorResult", doc);
		if (errCode != null) {
			err.setAttribute("errorCode", errCode);
		}

		if (errStr != null) {
			Element errStrEl = doc.createElementNS(null, "errorString");
			errStrEl.setTextContent(errStr);
			err.appendChild(errStrEl);
		}

		return err;
	}

	static Element appendErrorResult(Document doc, Element to,
			String errCode, String errStr, String name) {

		Element err = makeErrorResult(doc, errCode, errStr);

		if (name != null) {
			err.setAttribute("name", name);
		}

		to.appendChild(err);

		return err;
	}

	static IpAddress ip(String val, IpAddressType type, String ad) {
		return Identifiers.createIp(type, val, ad);
	}

	static MacAddress mac(String val, String ad) {
		return Identifiers.createMac(val, ad);
	}

	static Element metadata(Document doc, Element... md) {
		Element ret = doc.createElementNS(null, "metadata");
		addElements(ret, md);
		return ret;
	}

	public static Document simpleMetadata(DocumentBuilder db, String name, String cardinality) {
		Document ret = db.newDocument();
		Element el = ret.createElementNS(null, name);
		el.setAttribute("ifmap-cardinality", cardinality);
		ret.appendChild(el);
		return ret;
	}

	public static PublishElement publishDelete(Identifier i1, Identifier i2,
			String filter) {
		PublishDelete ret = new PublishDeleteImpl();
		ret.setIdentifier1(i1);
		ret.setIdentifier2(i2);
		ret.setFilter(filter);
		return ret;
	}

	public static PublishElement publishUpdate(Identifier i1, Identifier i2,
			Document... md) {
		PublishUpdate ret = new PublishUpdateImpl();
		ret.setIdentifier1(i1);
		ret.setIdentifier2(i2);

		for (Document d : md) {
			ret.addMetadata(d);
		}

		return ret;
	}

	public static PublishElement publishNotify(Identifier i1, Identifier i2,
			Document... md) {
		PublishNotify ret = new PublishNotifyImpl();
		ret.setIdentifier1(i1);
		ret.setIdentifier2(i2);

		for (Document d : md) {
			ret.addMetadata(d);
		}

		return ret;
	}

	public static SubscribeElement subscribeDelete(String name) {
		SubscribeDelete ret = new SubscribeDeleteImpl();
		if (name != null) {
			ret.setName(name);
		}
		return ret;
	}

	public static SubscribeElement subscribeUpdate(String name) {
		SearchHolder sh = new SearchHolderImpl();
		sh.setStartIdentifier(
				TestHelpers.ip("192.168.0.1", IpAddressType.IPv4, null));
		SubscribeUpdateImpl sui = new SubscribeUpdateImpl(sh);

		if (name != null) {
			sui.setName(name);
		}

		return sui;
	}
}
