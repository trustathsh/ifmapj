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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.fhhannover.inform.trust.ifmapj.exception.IfmapErrorResult;
import de.fhhannover.inform.trust.ifmapj.exception.MarshalException;
import de.fhhannover.inform.trust.ifmapj.exception.UnmarshalException;

/**
 * Test the handler functionality provided by the {@link Requests} class.
 * 
 * @author aw
 *
 */
public class RequestsTest {

	@Test
	public void testQueryDefaultHandlers() {
		
		assertNotNull(Requests.getHandlerFor(Requests.createNewSessionReq()));
		assertNotNull(Requests.getHandlerFor(Requests.createEndSessionReq()));
		assertNotNull(Requests.getHandlerFor(Requests.createRenewSessionReq()));
		assertNotNull(Requests.getHandlerFor(Requests.createPurgePublisherReq()));
		assertNotNull(Requests.getHandlerFor(Requests.createPublishReq()));
		assertNotNull(Requests.getHandlerFor(Requests.createSubscribeReq()));
		assertNotNull(Requests.getHandlerFor(Requests.createSearchReq()));
		assertNotNull(Requests.getHandlerFor(Requests.createPollReq()));
	}
	
	@Test
	public void testQueryUnknownHandler() {
		assertNull(Requests.getHandlerFor(new Request() {
			
			@Override
			public void setSessionId(String sessionId) {
				// anonymous
			}
			
			@Override
			public String getSessionId() {
				// anonymous
				return null;
			}
		}));
	}

	@Test(expected=NullPointerException.class)
	public void testRegisterNullHandler() {
		Requests.registerRequestHandler(null);
	}

	@Test(expected=RuntimeException.class)
	public void testRegisterDefaultHandlerTwice() {
		Requests.registerRequestHandler(new EndSessionRequestHandler());
	}

	@Test(expected=RuntimeException.class)
	public void testRegisterCustomHandlerTwice() {
		
		class CustomRequest implements Request  {

			@Override
			public void setSessionId(String sessionId) {
				// nothing
			}

			@Override
			public String getSessionId() {
				// nothing
				return null;
			}
		}

		class CustomRequestHandler implements RequestHandler<CustomRequest> {

			@Override
			public Element toElement(Request req, Document doc)
					throws MarshalException {
				return null;
			}

			@Override
			public Result fromElement(Element response)
					throws UnmarshalException, IfmapErrorResult {
				return null;
			}

			@Override
			public Class<CustomRequest> handles() {
				return CustomRequest.class;
			}
		}	
		
		Requests.registerRequestHandler(new CustomRequestHandler());
		Requests.registerRequestHandler(new CustomRequestHandler());
	}

	@Test
	public void testRegisterGoodCustomHandler() {
		
		class CustomRequest implements Request  {

			@Override
			public void setSessionId(String sessionId) {
				// nothing
			}

			@Override
			public String getSessionId() {
				// nothing
				return null;
			}
		}
			
		class CustomRequestHandler implements RequestHandler<CustomRequest> {

			@Override
			public Element toElement(Request req, Document doc)
					throws MarshalException {
				return null;
			}

			@Override
			public Result fromElement(Element response)
					throws UnmarshalException, IfmapErrorResult {
				return null;
			}

			@Override
			public Class<CustomRequest> handles() {
				return CustomRequest.class;
			}
		}	
		
		Requests.registerRequestHandler(new CustomRequestHandler());
		assertNotNull(Requests.getHandlerFor(new CustomRequest()));
	}
	
	@Test(expected=NullPointerException.class)
	public void testRegisterHandlesNullHandler() {
	
		class CustomRequest implements Request  {

			@Override
			public void setSessionId(String sessionId) {
				// nothing
			}

			@Override
			public String getSessionId() {
				// nothing
				return null;
			}
		};

		class CustomRequestHandler implements RequestHandler<CustomRequest> {

			@Override
			public Element toElement(Request req, Document doc)
			throws MarshalException {
				return null;
			}

			@Override
			public Result fromElement(Element response)
			throws UnmarshalException, IfmapErrorResult {
				return null;
			}

			@Override
			public Class<CustomRequest> handles() {
				return null;
			}
		}
		
		// will throw NullPointers, because handles() returns null
		Requests.registerRequestHandler(new CustomRequestHandler());
	}
}
