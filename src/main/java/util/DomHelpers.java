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
package util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import de.hshannover.f4.trust.ifmapj.binding.IfmapStrings;
import de.hshannover.f4.trust.ifmapj.exception.MarshalException;
import de.hshannover.f4.trust.ifmapj.log.IfmapJLog;
import de.hshannover.f4.trust.ifmapj.messages.NamespaceDeclarationHolder;

/**
 * Some helpers to handle javax.xml and org.w3c.* stuff...
 *
 * @since 0.1.4
 * @author aw
 * @author jk
 */
public final class DomHelpers {

	private static final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY;
	private static final DocumentBuilder DOCUMENT_BUILDER;
	private static final TransformerFactory TRANSFORMER_FACTORY;

	static {
		TRANSFORMER_FACTORY = TransformerFactory.newInstance();
		DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();
		DOCUMENT_BUILDER_FACTORY.setNamespaceAware(true);
		DOCUMENT_BUILDER = newDocumentBuilder();
	}

	private DomHelpers() { }

	public static DocumentBuilder newDocumentBuilder() {
		try {
			return DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			IfmapJLog.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}

	public static Element createNonNsElement(Document doc, String name) {
		return doc.createElementNS(null, name);
	}

	public static void addAttribute(Element el, String name, String val) {
		el.setAttributeNS(null, name, val);
	}

	public static void addXmlNamespaceDeclarations(NamespaceDeclarationHolder nsh,
			Element to) {

		for (Pair<String, String> nsDecl : nsh.getNamespaceDeclarations()) {
			String name = "xmlns";
			String value;
			if (nsDecl.mFirst.length() > 0) {
				name += ":" + nsDecl.mFirst;
			}
			value = nsDecl.mSecond;
			to.setAttributeNS("http://www.w3.org/2000/xmlns/", name, value);
		}
	}

	public static String makeRequestFqName(String name) {
		return IfmapStrings.BASE_PREFIX + ":" + name;
	}

	public static boolean elementMatches(Element el, String elname) {
		return elementMatches(el, elname, IfmapStrings.NO_URI);
	}

	public static boolean elementMatches(Element e, String name, String uri) {
		// i'm sorry
		return name.equals(e.getLocalName())
				&& (uri == null && e.getNamespaceURI() == null
				|| uri != null && uri.equals(e.getNamespaceURI()));
	}

	/**
	 * Create a new list containing only the child nodes of e of type
	 * {@link Element}.
	 * @param e
	 * @return
	 */
	public static List<Element> getChildElements(Element e) {

		List<Element> ret = new ArrayList<Element>();
		NodeList children = e.getChildNodes();

		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getNodeType()  == Node.ELEMENT_NODE) {
				ret.add((Element) child);
			}
		}

		return ret;
	}

	public static Element findElementInChildren(Node node, String name,
			String uri) {
		NodeList children = node.getChildNodes();
		Element ret = null;
		for (int i = 0; i < children.getLength(); i++) {
			Node n = children.item(i);

			// our interest is in element nodes only
			if (!(n.getNodeType() == Node.ELEMENT_NODE)) {
				continue;
			}

			if (elementMatches((Element) n, name, uri)) {
				ret = (Element) n;
				break;
			}
		}
		return ret;
	}

	/**
	 * Makes a deep-copy of an {@link Element} instance and appends it to a
	 * new {@link Document} instance as child.
	 *
	 * @param child the {@link Element} instnace to be deep-copied
	 * @return a new {@link Document} instance with a deep-copied child appended
	 */
	public static Document deepCopy(Element child) {
		Document mdDoc = DOCUMENT_BUILDER.newDocument();
		Element el = (Element) mdDoc.importNode(child, true);
		mdDoc.appendChild(el);
		return mdDoc;
	}


	/**
	 * Marshal a {@link Document} to {@link InputStream}.
	 *
	 * @param doc
	 * @return
	 * @throws MarshalException
	 */
	public static InputStream toInputStream(Document doc) throws MarshalException {

		Transformer trans;

		try {
			trans = TRANSFORMER_FACTORY.newTransformer();
		} catch (TransformerConfigurationException e) {
			IfmapJLog.error("Oh oh.... [" + e.getMessage() + "]");
			throw new MarshalException(e.getMessage());
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Result result = new StreamResult(baos);
		byte[] marshalled = null;

		doc.setXmlStandalone(true);
		Source source = new DOMSource(doc);

		try {
			trans.transform(source, result);
		} catch (TransformerException e) {
			IfmapJLog.error("Oh oh.... [" + e.getMessage() + "]");
			throw new MarshalException(e.getMessage());
		}
		try {
			baos.flush();
		} catch (IOException e) {
			IfmapJLog.error("Oh oh.... [" + e.getMessage() + "]");
			throw new MarshalException(e.getMessage());
		}

		marshalled = baos.toByteArray();

		return new ByteArrayInputStream(marshalled);
	}

	/**
	 * Prepare an extended identifier for publish by removing
	 * namespace added by XSL transformation and encoding all
	 * relevant XML entities.
	 *
	 * @since 0.1.5
	 * @param str
	 * @return namespace ns0 stripped and encoded XML string
	 * @throws MarshalException
	 */
	public static String prepareExtendedIdentifier(Document doc) throws MarshalException {

		Transformer tf = null;
		String res = null;

		try {
			tf = TRANSFORMER_FACTORY.newTransformer();
		} catch (TransformerConfigurationException e) {
			IfmapJLog.error("Oh oh.... [" + e.getMessage() + "]");
			throw new MarshalException(e.getMessage());
		}

		fixupNamespace(doc);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DOMSource domSource = new DOMSource(doc.getFirstChild());
		Result result = new StreamResult(baos);

		tf.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		tf.setOutputProperty(OutputKeys.INDENT, "no");
		tf.setOutputProperty(OutputKeys.METHOD, "xml");

		try {
			tf.transform(domSource, result);
		} catch (TransformerException e) {
			IfmapJLog.error("Oh oh.... [" + e.getMessage() + "]");
			throw new MarshalException(e.getMessage());
		}

		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		InputSource inputSource = new InputSource(bais);

		CanonicalXML cxml = new CanonicalXML();
		XMLReader reader = null;
		try {
			reader = XMLReaderFactory.createXMLReader();
		} catch (SAXException e) {
			IfmapJLog.error("Oh oh.... [" + e.getMessage() + "]");
			throw new MarshalException(e.getMessage());
		}

		try {
			res = cxml.toCanonicalXml2(reader, inputSource, true);
		} catch (Exception e) {
			// hmm... toCanonicalXML throws Exception...
			IfmapJLog.error("Oh oh.... [" + e.getMessage() + "]");
			throw new MarshalException(e.getMessage());
		}

		return escapeXml(res);
	}

	private static String escapeXml(String input) {

		String ret = input;

		String []unwanted =  {"&",     "<",    ">",   "\"",    "'"};
		String []replaceBy = {"&amp;", "&lt;", "&gt;", "&quot;", "&apos;"};

		for (int i = 0; i < unwanted.length; i++) {
			ret = ret.replace(unwanted[i], replaceBy[i]);
		}

		return ret;
	}

	/**
	 * If the top-level element has a prefix associated with it, drop it.
	 * Go recursively down and remove all prefixes. If we come across
	 * a different prefix, throw a {@link MarshalException}...
	 *
	 * @param doc
	 * @throws MarshalException
	 */
	private static void fixupNamespace(Document doc) throws MarshalException {

		Node n;
		Element el;
		String prefix;

		n = doc.getFirstChild();

		if (n.getNodeType() != Node.ELEMENT_NODE) {
			throw new RuntimeException("No element");
		}

		el = (Element) n;

		prefix = el.getPrefix();

		if (prefix != null && prefix.length() > 0) {
			el.setPrefix(null);
		} else {
			prefix = "";
		}

		dropNamespaceDecls(el);

		removePrefixFromChildren(el, prefix);
	}

	private static void dropNamespaceDecls(Element el) {
		NamedNodeMap nnm = el.getAttributes();
		List<Attr> toDrop = new ArrayList<Attr>();

		for (int i = 0; i < nnm.getLength(); i++) {
			Attr attr = (Attr) nnm.item(i);
			if (attr.getName().startsWith("xmlns:")) {
				toDrop.add(attr);
			}
		}

		for (Attr attr : toDrop) {
			nnm.removeNamedItemNS(attr.getNamespaceURI(), attr.getLocalName());
		}
	}

	/**
	 * If any child of el has prefix as prefix, remove it. drop all namespace
	 * decls on the way. If we find an element with a different prefix, go
	 * crazy.
	 *
	 * @param el
	 * @param prefix
	 * @throws MarshalException
	 */
	private static void removePrefixFromChildren(Element el, String prefix)
			throws MarshalException {

		NodeList nl = el.getChildNodes();
		String localPrefix = null;
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);

			if (n.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}

			localPrefix = n.getPrefix();

			if (localPrefix != null && localPrefix.length() > 0) {

				if (!localPrefix.equals(prefix)) {
					IfmapJLog.warn("Extended Identifier: Multiple namespaces in extended identifer used."
							+ "IfmapJ thinks this is not a wise idea. Sorry!");
					throw new MarshalException("Extended Identifier: Multiple namespaces in extended identifer used."
							+ "IfmapJ thinks this is not a wise idea. Sorry!");
				}

				n.setPrefix(null);
			}

			removePrefixFromChildren((Element) n, prefix);
			dropNamespaceDecls((Element) n);

		}
	}

	/**
	 * Marshal a {@link Document} to {@link InputStream}
	 *
	 * @param is the InputStream containing XML data
	 * @return {@link Document} containg the XML data
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Document toDocument(InputStream is) throws MarshalException {
		try {
			return newDocumentBuilder().parse(is);
		} catch (SAXException e) {
			IfmapJLog.error(e.getMessage());
			throw new MarshalException(e.getMessage());
		} catch (IOException e) {
			IfmapJLog.error(e.getMessage());
			throw new MarshalException(e.getMessage());
		}
	}

	/**
	 * Marshal a {@link Document} from {@link String} with XML data
	 *
	 * @param s string containing XML data
	 * @param c charset used for encoding the string
	 * @return {@link Document} containg the XML data
	 * @throws MarshalException
	 */
	public static Document toDocument(String s, Charset c) throws MarshalException {

		byte[] bytes = c == null ? s.getBytes() : s.getBytes(c);
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

		return toDocument(bais);
	}

	/**
	 * Compare two DOM documents
	 *
	 * @param d1 First DOM document
	 * @param d2 Second DOM document
	 * @return true if both are equal
	 * @throws MarshalException
	 */
	public static boolean compare(Document d1, Document d2) throws MarshalException {
		d1.normalize();
		d2.normalize();
		return d1.isEqualNode(d2);
	}
}
