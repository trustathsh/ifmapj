package de.fhhannover.inform.trust.ifmapj.util;

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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import util.Base64;


/**
 * Test the {@link Base64} class that we use (although not written by us).
 * @author ib
 *
 */
public class Base64Test {
	
	String plain1 = "password1";			// ''
	String plain12 = "password12";			// '=='
	String plain123 = "password123";		// '='

	String base1 = "cGFzc3dvcmQx";		// ''
	String base12 = "cGFzc3dvcmQxMg==";	// '=='
	String base123 = "cGFzc3dvcmQxMjM=";	// '='
	
	String empty = "";

	@Test
	public void testEncodeToString(){
		assertEquals(base1, Base64.encodeToString(plain1.getBytes(), true));
		assertEquals(base12, Base64.encodeToString(plain12.getBytes(), true));
		assertEquals(base123, Base64.encodeToString(plain123.getBytes(), true));
		assertEquals(empty, Base64.encodeToString(null, true));
	}
	
	@Test
	public void testEncodeToByte(){
		assertArrayEquals(base1.getBytes(), Base64.encodeToByte(plain1.getBytes(), true));
		assertArrayEquals(base12.getBytes(), Base64.encodeToByte(plain12.getBytes(), true));
		assertArrayEquals(base123.getBytes(), Base64.encodeToByte(plain123.getBytes(), true));
		assertArrayEquals(empty.getBytes(), Base64.encodeToByte(null, true));
	}
	
	@Test
	public void testEncodeToChar(){
		assertEquals(base1, new String(Base64.encodeToChar(plain1.getBytes(), true)));
		assertEquals(base12, new String(Base64.encodeToChar(plain12.getBytes(), true)));
		assertEquals(base123, new String(Base64.encodeToChar(plain123.getBytes(), true)));
		assertEquals(empty, new String(Base64.encodeToChar(empty.getBytes(), true)));
	}
	
	@Test
	public void testDecode(){
		// byte[]
		assertArrayEquals(plain1.getBytes(), Base64.decode(base1.getBytes()));
		assertArrayEquals(plain12.getBytes(), Base64.decode(base12.getBytes()));
		assertArrayEquals(plain123.getBytes(), Base64.decode(base123.getBytes()));
		assertArrayEquals(empty.getBytes(), Base64.decode(empty.getBytes()));
		// char[]
		assertArrayEquals(plain1.getBytes(), Base64.decode(base1.toCharArray()));
		assertArrayEquals(plain12.getBytes(), Base64.decode(base12.toCharArray()));
		assertArrayEquals(plain123.getBytes(), Base64.decode(base123.toCharArray()));
		assertArrayEquals(empty.getBytes(), Base64.decode(empty.toCharArray()));
		// String
		assertArrayEquals(plain1.getBytes(), Base64.decode(base1));
		assertArrayEquals(plain12.getBytes(), Base64.decode(base12));
		assertArrayEquals(plain123.getBytes(), Base64.decode(base123));
		assertArrayEquals(empty.getBytes(), Base64.decode(empty));
	}
	
	@Test
	public void testDecodeFast(){
		// byte[]
		assertArrayEquals(plain1.getBytes(), Base64.decodeFast(base1.getBytes()));
		assertArrayEquals(plain12.getBytes(), Base64.decodeFast(base12.getBytes()));
		assertArrayEquals(plain123.getBytes(), Base64.decodeFast(base123.getBytes()));
		assertArrayEquals(empty.getBytes(), Base64.decodeFast(empty.getBytes()));
		// char[]
		assertArrayEquals(plain1.getBytes(), Base64.decodeFast(base1.toCharArray()));
		assertArrayEquals(plain12.getBytes(), Base64.decodeFast(base12.toCharArray()));
		assertArrayEquals(plain123.getBytes(), Base64.decodeFast(base123.toCharArray()));
		assertArrayEquals(empty.getBytes(), Base64.decodeFast(empty.toCharArray()));
		// String
		assertArrayEquals(plain1.getBytes(), Base64.decodeFast(base1));
		assertArrayEquals(plain12.getBytes(), Base64.decodeFast(base12));
		assertArrayEquals(plain123.getBytes(), Base64.decodeFast(base123));
		assertArrayEquals(empty.getBytes(), Base64.decodeFast(empty));
	}
}
