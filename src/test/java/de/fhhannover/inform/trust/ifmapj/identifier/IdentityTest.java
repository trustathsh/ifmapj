package de.fhhannover.inform.trust.ifmapj.identifier;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Trivial tests for {@link Identity} class.
 * 
 * @author ibente
 *
 */
public class IdentityTest {
	public String ad = "de.fhhannover.inform.trust";
	public String name = "publisherid:number";
	public String otd = "vendorid:definition";

	@Test
	public void testIdentity() {
		Identity id = Identifiers.createIdentity(null, null);
		
		assertNotNull(id);
		assertNull(id.getAdministrativeDomain());
		assertNull(id.getName());
		assertNull(id.getOtherTypeDefinition());
		assertNull(id.getType());
		
		id = Identifiers.createIdentity(IdentityType.userName, name, ad, otd);
		
		assertEquals(ad, id.getAdministrativeDomain());
		assertEquals(name, id.getName());
		assertEquals(otd, id.getOtherTypeDefinition());
		assertEquals(IdentityType.userName, id.getType());
	}

	@Test
	public void testIdentityToString() {
		
		Identity id = Identifiers.createIdentity(null, null);
		
		assertEquals("id{null, null}", id.toString());
		
		id = Identifiers.createIdentity(IdentityType.aikName, null);
		assertEquals("id{null, aik-name}", id.toString());
	
		id = Identifiers.createIdentity(IdentityType.dnsName, null, "abc");
		assertEquals("id{null, dns-name, abc}", id.toString());
		

		id = Identifiers.createIdentity(IdentityType.dnsName,
				"www.google.com", "abc");
		assertEquals("id{www.google.com, dns-name, abc}", id.toString());
		

		id = Identifiers.createIdentity(IdentityType.other,
				"abc", "abc", null);
		assertEquals("id{abc, other, null, abc}", id.toString());
		
		id = Identifiers.createIdentity(IdentityType.other,
				"abc", "", null);
		assertEquals("id{abc, other, null}", id.toString());
		
		id = Identifiers.createIdentity(IdentityType.other,
				"abc", "", "the-other-guy");
		assertEquals("id{abc, other, the-other-guy}", id.toString());
		
		id = Identifiers.createIdentity(IdentityType.other,
				"abc", "the-other-side", "the-other-guy");
		assertEquals("id{abc, other, the-other-guy, the-other-side}",
				id.toString());
	}
}
