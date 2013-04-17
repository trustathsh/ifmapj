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
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Trivial tests for {@link Device} class.
 * 
 * @author ibente
 *
 */
public class DeviceTest {
	public String name = "publisherid:number";

	@Test
	public void testDevice() {
		Device dev = Identifiers.createDev(null);
		assertNotNull(dev);
		assertNull(dev.getName());
		dev = Identifiers.createDev(name);
		assertEquals(name, dev.getName());
	}

	@Test
	public void testDeviceToString() {
		Device dev = Identifiers.createDevRandom();
		assertTrue(dev.getName().matches("[a-f0-9]{32}") ||
				dev.getName().matches("[A-Za-z0-9+/=]{24}"));
		dev = Identifiers.createDev("abc");
		assertEquals("dev{abc}", dev.toString());
	}
	
	@Test
	public void testDeviceWithRandomUUID() {
		Device dev = Identifiers.createDevRandomUUID();
		assertNotNull(dev);
		String regex_uuid = ("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}");
		assertTrue(dev.getName().matches(regex_uuid));
	}
	
	@Test
	public void testDeviceWithPublisherId() {
		Device dev = Identifiers.createDevPubPrefixed("uid", "publisherid");
		assertNotNull(dev);
		assertEquals("publisherid:uid", dev.getName());
	}
}
