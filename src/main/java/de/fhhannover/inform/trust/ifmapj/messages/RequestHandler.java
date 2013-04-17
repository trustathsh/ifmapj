package de.fhhannover.inform.trust.ifmapj.messages;

/*
 * #%L
 * ====================================================
 *   _____                _     ____  _____ _   _ _   _
 *  |_   _|_ __ _   _ ___| |_  / __ \|  ___| | | | | | |
 *    | | | '__| | | / __| __|/ / _` | |_  | |_| | |_| |
 *    | | | |  | |_| \__ \ |_| | (_| |  _| |  _  |  _  |
 *    |_| |_|   \__,_|___/\__|\ \__,_|_|   |_| |_|_| |_|
 *                             \____/
 * 
 * =====================================================
 * 
 * Fachhochschule Hannover 
 * (University of Applied Sciences and Arts, Hannover)
 * Faculty IV, Dept. of Computer Science
 * Ricklinger Stadtweg 118, 30459 Hannover, Germany
 * 
 * Email: trust@f4-i.fh-hannover.de
 * Website: http://trust.inform.fh-hannover.de/
 * 
 * This file is part of IfmapJ, version 0.1.5, implemented by the Trust@FHH 
 * research group at the Fachhochschule Hannover.
 * 
 * IfmapJ is a lightweight, platform-independent, easy-to-use IF-MAP client
 * library for Java. IF-MAP is an XML based protocol for sharing data across
 * arbitrary components, specified by the Trusted Computing Group. IfmapJ is
 * maintained by the Trust@FHH group at the Fachhochschule Hannover. IfmapJ 
 * was developed within the ESUKOM research project.
 * %%
 * Copyright (C) 2010 - 2013 Trust@FHH
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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.fhhannover.inform.trust.ifmapj.exception.IfmapErrorResult;
import de.fhhannover.inform.trust.ifmapj.exception.MarshalException;
import de.fhhannover.inform.trust.ifmapj.exception.UnmarshalException;

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
