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
package de.hshannover.f4.trust.ifmapj.identifier;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.hshannover.f4.trust.ifmapj.exception.MarshalException;
import de.hshannover.f4.trust.ifmapj.exception.UnmarshalException;

/**
 * Provides generic access to marshal and unmarshal a {@link Identifier}
 * implementation from or to {@link Element} representation.
 *
 * @author aw
 *
 */
public interface IdentifierHandler<T extends Identifier> {

	/**
	 * Create a {@link Element} instance representing the given {@link Identifier}.
	 *
	 * @param i the {@link Identifier}
	 * @param doc the root {@link Document} where the element is to be added.
	 *		An implementation <b>must not</b> add the element to the given {@link Document}.
	 * @return the {@link Element} representation of the given {@link Identifier}.
	 * @throws MarshalException If some constraints for the {@link Identifier} are not fullfilled.
	 */
	Element toElement(Identifier i, Document doc) throws MarshalException;

	/**
	 * Create a {@link Identifier} instance from the given {@link Element}.
	 *
	 * @param el the {@link Element} instance
	 * @return an {@link Identifier} instance representation of the given {@link Element},
	 *	 or null if this {@link IdentifierHandler} was not able to parse
	 *	 the given {@link Element}.
	 * @throws UnmarshalException If some constraint of the {@link Identifier} was
	 *	 not fulfilled. <b>Note, if this handler is not responsible for the
	 *	 given XML, null has to be returned.</b>
	 */
	T fromElement(Element el) throws UnmarshalException;

	/**
	 * @return the {@link Class} object for the {@link Identifier} implementation
	 *	 this implementation is able to handle.
	 */
	Class<T> handles();
}
