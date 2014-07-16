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
package de.hshannover.f4.trust.ifmapj.identifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import util.DomHelpers;
import de.hshannover.f4.trust.ifmapj.exception.MarshalException;
import de.hshannover.f4.trust.ifmapj.exception.UnmarshalException;

@RunWith(Parameterized.class)
public class IdentityTypeTest {
	private static DocumentBuilder sDocBuilder = DomHelpers.newDocumentBuilder();
	private static IdentityHandler sIdentityHandler = new IdentityHandler();

	private final IdentityType mType;
	private final String mString;

	@SuppressWarnings("rawtypes")
	@Parameters
	public static Collection testParams() {
		return Arrays.asList(new Object[][] {
				{IdentityType.aikName, "aik-name"},
				{IdentityType.distinguishedName, "distinguished-name"},
				{IdentityType.dnsName, "dns-name"},
				{IdentityType.emailAddress, "email-address"},
				{IdentityType.hipHit, "hip-hit"},
				{IdentityType.kerberosPrincipal, "kerberos-principal"},
				{IdentityType.userName, "username"},
				{IdentityType.sipUri, "sip-uri"},
				{IdentityType.telUri, "tel-uri"},
				{IdentityType.other, "other"}
		});
	}

	public IdentityTypeTest(IdentityType type, String expectedTypeString) {

		mString = expectedTypeString;
		mType = type;
	}


	@Test
	public void fromElementTypeTest() throws UnmarshalException {

		Document doc = sDocBuilder.newDocument();
		Element xmlId = doc.createElementNS(null, "identity");

		xmlId.setAttribute("name", "USER100");
		xmlId.setAttribute("type", mString);

		// otherwise parsing fails
		if (mString.equals("other")) {
			xmlId.setAttribute("other-type-definition", "OTHER_TYPE_DEF");
		}

		Identity i = sIdentityHandler.fromElement(xmlId);
		assertNotNull(i);
		assertEquals(mType, i.getType());
	}

	@Test
	public void toElementTypeTest() throws MarshalException {

		Document doc = sDocBuilder.newDocument();
		Identity id;

		if (mType == IdentityType.other) {
			id = Identifiers.createOtherIdentity("USER100", null, "OTHER_TYPE_DEF");
		} else {
			id = Identifiers.createIdentity(mType, "USER100");
		}



		Element el = sIdentityHandler.toElement(id, doc);
		assertNotNull(el);
		assertEquals("identity", el.getLocalName());
		assertEquals(mString, el.getAttribute("type"));
	}
}
