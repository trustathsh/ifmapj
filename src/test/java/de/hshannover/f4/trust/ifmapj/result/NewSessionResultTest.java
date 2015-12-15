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
package de.hshannover.f4.trust.ifmapj.result;

/*
 * Trivial tests for class {@link NewSessionResult}.
 *
 * @author ibente
 *
 *

public class NewSessionResultTest {
	private ResultFactory rF = new ResultFactoryImpl();

	@Test (expected=NullPointerException.class)
	public void testNewSessionResultNull() {
		@SuppressWarnings("unused")
		NewSessionResult nsr = rF.createNewSessionRes(null, null);
	}

	@Test (expected=NullPointerException.class)
	public void testNewSessionResultNull2() {
		@SuppressWarnings("unused")
		NewSessionResult nsr = rF.createNewSessionRes(null, null, null);
	}

	@Test
	public void testNewSessionResult() {
		String sid = "0123456789";
		String pid = "publisher:0123456789";
		Integer mprs = new Integer(10000);
		NewSessionResult nsr;

		nsr = rF.createNewSessionRes(sid, pid);
		assertEquals(pid, nsr.getPublisherId());
		assertEquals(sid, nsr.getSessionId());
		assertNull(nsr.getMaxPollResultSize());

		nsr = rF.createNewSessionRes(sid, pid, mprs);
		assertEquals(pid, nsr.getPublisherId());
		assertEquals(sid, nsr.getSessionId());
		assertEquals(mprs, nsr.getMaxPollResultSize());
	}
}
*/
