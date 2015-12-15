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
 * This file is part of ifmapj, version 2.3.1, implemented by the Trust@HsH
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
package de.hshannover.f4.trust.ifmapj.messages;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.hshannover.f4.trust.ifmapj.exception.IfmapErrorResult;
import de.hshannover.f4.trust.ifmapj.exception.MarshalException;
import de.hshannover.f4.trust.ifmapj.exception.UnmarshalException;

/**
 * Provides functionality to generically handle {@link Request} instances and
 * response parsing.
 *
 * @author aw
 * @since 0.1.4
 */
public interface RequestHandler<T extends Request> {

	/**
	 * Marshals a given {@link Request} instance to XML form and returns the
	 * {@link Element} to be attached to the soap:Body element.
	 *
	 * @param req the {@link Request} instance to be marshalled.
	 * @param doc a {@link Document} instance to be used to create new
	 *	{@link Element} instances.
	 * @return an {@link Element} instance to be attached to the soap:Body.
	 * @throws MarshalException
	 */
	/**
	 * @throws MarshalException
	 */
	Element toElement(Request req, Document doc) throws MarshalException;

	/**
	 * Unmarshals a given response in XML form to a {@link Result} instance.
	 * The given {@link Element} instance is the ifmap:response element. It
	 * might have an errorResult element attached. An implementation has to
	 * check for this condition and throw an appropriate
	 * {@link IfmapErrorResult}.
	 *
	 * @see {@link Requests.Helpers#getResponseContentErrorCheck(Element)}.
	 *
	 * @param response the {@link Element} under <ifmap:response>
	 * @return an appropriate {@link Result} implementation or null if there is
	 *	 no real result, but everything was good.
	 * @throws UnmarshalException
	 * @throws {@link IfmapErrorResult} if an errorResult was found
	 */
	Result fromElement(Element response) throws UnmarshalException, IfmapErrorResult;

	/**
	 * @return the {@link Class} object associated with the {@link Request}
	 *	 instance this {@link RequestHandler} is able to cope with.
	 */
	Class<T> handles();
}
