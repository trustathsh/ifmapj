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
import static org.junit.Assert.assertNull;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.hshannover.f4.trust.ifmapj.exception.MarshalException;

//TODO: check <a/> --> <a></a>

public class ExtendedIdentifierTest {

	static DocumentBuilderFactory dbf;
	static {
		dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
	}

	@Test
	public void testDocumentSimple() throws MarshalException, ParserConfigurationException {
		Document doc = dbf.newDocumentBuilder().newDocument();
		// <elname xmlns:urn://myuri/>
		String expected = "&lt;elname xmlns=&quot;urn://myuri&quot;&gt;&lt;/elname&gt;";

		Element el = doc.createElementNS("urn://myuri", "my:elname");
		doc.appendChild(el);

		Identity id = Identifiers.createExtendedIdentity(doc);

		assertNotNull(id);

		if (!expected.equals(id.getName())) {
			System.out.println("ex=\"" + expected + "\"");
			System.out.println("is=\"" + id.getName() + "\"");
		}
		assertEquals(expected, id.getName());
	}

	@Test
	public void testWithAttr() throws ParserConfigurationException, MarshalException {
		Document doc = dbf.newDocumentBuilder().newDocument();
		String expected = "&lt;elname xmlns=&quot;urn://myuri&quot; val=&quot;attr-val&quot;&gt;&lt;/elname&gt;";

		Element el = doc.createElementNS("urn://myuri", "my:elname");
		el.setAttributeNS(null, "val", "attr-val");
		doc.appendChild(el);

		Identity id = Identifiers.createExtendedIdentity(doc);
		assertNotNull(id);

		if (!expected.equals(id.getName())) {
			System.out.println("ex=\"" + expected + "\"");
			System.out.println("is=\"" + id.getName() + "\"");
		}
		assertEquals(expected, id.getName());

	}

	@Test
	public void testWithMultiAttr() throws ParserConfigurationException, MarshalException {
		Document doc = dbf.newDocumentBuilder().newDocument();
		String expected = "&lt;elname xmlns=&quot;urn://myuri&quot; ad=&quot;advalue&quot; "
				+ "val=&quot;attr-val&quot;&gt;&lt;/elname&gt;";

		Element el = doc.createElementNS("urn://myuri", "my:elname");
		el.setAttributeNS(null, "val", "attr-val");
		el.setAttributeNS(null, "ad", "advalue");
		doc.appendChild(el);

		Identity id = Identifiers.createExtendedIdentity(doc);
		assertNotNull(id);

		if (!expected.equals(id.getName())) {
			System.out.println("ex=\"" + expected + "\"");
			System.out.println("is=\"" + id.getName() + "\"");
		}

		assertEquals(expected, id.getName());
	}

	@Test
	public void testFromStrSimple() throws MarshalException {
		String expected = "&lt;network blub=&quot;blubblub&quot;&gt;&lt;/network&gt;";

		Identity id = Identifiers.createExtendedIdentity("<network blub=\"blubblub\"/>");
		assertNotNull(id);

		if (!expected.equals(id.getName())) {
			System.out.println("ex=\"" + expected + "\"");
			System.out.println("is=\"" + id.getName() + "\"");
		}

		assertEquals(expected, id.getName());
	}

	@Test
	public void testTransformFromStrNs() throws MarshalException {
		String expected = "&lt;network xmlns=&quot;http://uri&quot;&gt;&lt;/network&gt;";

		Identity id = Identifiers.createExtendedIdentity("<nsxx:network xmlns:nsxx=\"http://uri\"/>");
		assertNotNull(id);

		if (!expected.equals(id.getName())) {
			System.out.println("ex=\"" + expected + "\"");
			System.out.println("is=\"" + id.getName() + "\"");
		}

		assertEquals(expected, id.getName());
	}

	@Test
	public void testChildElement() throws MarshalException {
		String val = "<network blub=\"blubblub\"><child x=\"y\"/></network>";
		String expected = "&lt;network blub=&quot;blubblub&quot;&gt;&lt;child x=&quot;y&quot;&gt;&lt;/child&gt;"
					+ "&lt;/network&gt;";

		Identity id = Identifiers.createExtendedIdentity(val);
		assertNotNull(id);

		if (!expected.equals(id.getName())) {
			System.out.println("bad subelement");
			System.out.println("ex=\"" + expected + "\"");
			System.out.println("is=\"" + id.getName() + "\"");
		}

		assertEquals(expected, id.getName());
	}

	@Test
	public void testWhiteSpace() throws MarshalException {
		String val = "<network    blub=\"blubblub\">     <child     x  = \"y\"/>    </network>";
		String expected = "&lt;network blub=&quot;blubblub&quot;&gt;&lt;child x=&quot;y&quot;&gt;&lt;/child&gt;"
					+ "&lt;/network&gt;";

		Identity id = Identifiers.createExtendedIdentity(val);

		if (!expected.equals(id.getName())) {
			System.out.println("multi space bad");
			System.out.println("ex=\"" + expected + "\"");
			System.out.println("is=\"" + id.getName() + "\"");
		}

		assertEquals(expected, id.getName());
	}

	@Test
	public void testTabNewline() throws MarshalException {
		String val = "<network blub=\"blubblub\">\n\t<child x=\"y\"/>\t\n\t</network>";
		String expected = "&lt;network blub=&quot;blubblub&quot;&gt;&lt;child x=&quot;y&quot;&gt;&lt;/child&gt;"
					+ "&lt;/network&gt;";

		Identity id = Identifiers.createExtendedIdentity(val);

		if (!expected.equals(id.getName())) {
			System.out.println("tab newline bad");
			System.out.println("ex=\"" + expected + "\"");
			System.out.println("is=\"" + id.getName() + "\"");
		}

		assertEquals(expected, id.getName());
	}

	@Test
	public void testUnusedNs() throws MarshalException {
		String val = "<xx:el xmlns:xx=\"http://uri\" xmlns:yy=\"http://unused\"/>";
		String expected = "&lt;el xmlns=&quot;http://uri&quot;&gt;&lt;/el&gt;";

		Identity id = Identifiers.createExtendedIdentity(val);

		if (!expected.equals(id.getName())) {
			System.out.println("tab newline bad");
			System.out.println("ex=\"" + expected + "\"");
			System.out.println("is=\"" + id.getName() + "\"");
		}

		assertEquals(expected, id.getName());
	}

	@Test(expected = MarshalException.class)
	public void testMultiNs() throws MarshalException {
		// So, I don't really know what to expect here. The implementation
		// currently throws a RuntimeException. That's what we test here.
		// I don't really see the point in multiple namespaces in a extended
		// identifer.

		String val = "<xx:el xmlns:xx=\"http://uri\" xmlns:yy=\"http://used\"><yy:child/></xx:el>";

		Identity id = Identifiers.createExtendedIdentity(val);
		System.out.println(id.getName());
		assertNull(id);
	}
}
