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
 * This file is part of ifmapj, version 2.1.0, implemented by the Trust@HsH
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
package de.hshannover.f4.trust.ifmapj.metadata;

import javax.xml.namespace.NamespaceContext;

/**
 * Interface for common IF-MAP metadata attributes.
 */
public interface Metadata {

	/**
	 * Return the publisher id of this metadata.
	 * If this metadata object has no publisher id the empty string is returned.
	 * If the extraction of the publisher id fails null is returned.
	 *
	 * @return the publisher id, null or the empty string
	 */
	public String getPublisherId();

	/**
	 * Return the publish timestamp of this metadata.
	 * If this metadata object has no publish timestamp the empty string is returned.
	 * If the extraction of the publish timestamp fails null is returned.
	 *
	 * @return the publish timestamp, null or the empty string
	 */
	public String getPublishTimestamp();

	/**
	 * Return the type name of this metadata, including the namespace prefix.
	 * If the extraction of the type name fails null is returned.
	 *
	 * @return the type name or null
	 */
	public String getTypename();

	/**
	 * Return the local type name of this metadata without the namespace prefix.
	 * If the extraction of the type name fails null is returned.
	 *
	 * @return the local type name or null
	 */
	public String getLocalname();

	/**
	 * Return the cardinality of this metadata.
	 * If this metadata object has no cardinality the empty string is returned.
	 * If the extraction of the cardinality fails null is returned.
	 *
	 * @return the cardinality, null or the empty string
	 */
	public String getCardinality();

	/**
	 * Return true if this metadata has 'singleValue' cardinality.
	 *
	 * @return true if cardinality equals 'singleValue'
	 */
	public boolean isSingleValue();

	/**
	 * Return true if this metadata has 'multiValue' cardinality.
	 *
	 * @return true if cardinality equals 'multiValue'
	 */
	public boolean isMultiValue();

	/**
	 * Evaluate the given XPath expression on this metadata document and return
	 * the result string. If this metadata object does not match the given
	 * expression the empty string is returned. If the evaluation of the
	 * expression fails null is returned.
	 *
	 * @param xPathExpression the XPath expression
	 * @return the result string, null or the empty string
	 */
	public String getValueForXpathExpression(String xPathExpression);

	/**
	 * Evaluate the given XPath expression on this metadata document and return
	 * the result string. If the evaluation fails the given default value will
	 * be returned.
	 *
	 * @param xPathExpression the XPath expression
	 * @param defaultValue the default value
	 * @return the result string or the default value
	 */
	public String getValueForXpathExpressionOrElse(String xPathExpression, String defaultValue);

	/**
	 * Returns a formatted XML string representation of this metadata document.
	 *
	 * @return formatted XML string
	 */
	public String toFormattedString();

	/**
	 * Set the given {@link NamespaceContext} as the namespace context for
	 * XPath queries.
	 *
	 * @param context the new namespace context
	 */
	public void setNamespaceContext(NamespaceContext context);
}
