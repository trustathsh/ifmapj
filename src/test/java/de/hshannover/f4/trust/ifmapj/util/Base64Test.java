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
 * Website: http://trust.f4.hs-hannover.de
 * 
 * This file is part of ifmapj, version 1.0.0, implemented by the Trust@HsH
 * research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2010 - 2013 Trust@HsH
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
package de.hshannover.f4.trust.ifmapj.util;

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
