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
package de.hshannover.f4.trust.ifmapj.identifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Trivial tests for {@link AccessRequest} class.
 *
 * @author ibente
 *
 */
public class AccessRequestTest {
	public String mAd = "de.fhhannover.inform.trust";
	public String mName = "publisherid:number";

	@Test
	public void testAccessRequest() {
		AccessRequest ar = Identifiers.createAr(null);
		assertNotNull(ar);
		assertNull(ar.getAdministrativeDomain());
		assertNull(ar.getName());
		ar = Identifiers.createAr(mName, mAd);
		assertEquals(mAd, ar.getAdministrativeDomain());
		assertEquals(mName, ar.getName());
	}

	@Test
	public void testAccessRequestToString() {
		AccessRequest ar = Identifiers.createArRandom();
		assertTrue(ar.getName().matches("[a-f0-9]{32}"));

		ar = Identifiers.createAr("abc");
		assertEquals("ar{abc}", ar.toString());


		ar = Identifiers.createArRandom("abc");
		assertTrue(ar.getAdministrativeDomain().equals("abc"));
		assertTrue(ar.getName().matches("[a-f0-9]{32}") || ar.getName().matches("[A-Za-z0-9+/=]{24}"));

		ar = Identifiers.createAr("abc", "abc");
		assertEquals("ar{abc, abc}", ar.toString());
	}

	@Test
	public void testAccessRequestWithGeneratedName() {
		AccessRequest ar = Identifiers.createArRandomUuid();
		assertNotNull(ar);
		String regexUuid = "[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}";
		assertTrue(ar.getName().matches(regexUuid));
	}

	@Test
	public void testAccessRequestWithPublisherId() {
		AccessRequest ar = Identifiers.createArPubPrefixed("uid", "publisherid", mAd);
		assertNotNull(ar);
		assertEquals(ar.getName(), "publisherid:uid");

		ar = Identifiers.createArPubPrefixed("uid", "publisherid", null);
		assertNotNull(ar);
		assertEquals(ar.getName(), "publisherid:uid");
		assertNull(ar.getAdministrativeDomain());

		ar = Identifiers.createArPubPrefixed("uid", "publisherid");
		assertNotNull(ar);
		assertEquals(ar.getName(), "publisherid:uid");
		assertNull(ar.getAdministrativeDomain());
}
}
