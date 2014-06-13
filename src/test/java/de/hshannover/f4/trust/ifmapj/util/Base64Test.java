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
 * This file is part of ifmapj, version 2.1.0, implemented by the Trust@HsH
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

	String mPlain1 = "password1";			// ''
	String mPlain12 = "password12";			// '=='
	String mPlain123 = "password123";		// '='

	String mBase1 = "cGFzc3dvcmQx";		// ''
	String mBase12 = "cGFzc3dvcmQxMg==";	// '=='
	String mBase123 = "cGFzc3dvcmQxMjM=";	// '='

	String mEmpty = "";

	@Test
	public void testEncodeToString() {
		assertEquals(mBase1, Base64.encodeToString(mPlain1.getBytes(), true));
		assertEquals(mBase12, Base64.encodeToString(mPlain12.getBytes(), true));
		assertEquals(mBase123, Base64.encodeToString(mPlain123.getBytes(), true));
		assertEquals(mEmpty, Base64.encodeToString(null, true));
	}

	@Test
	public void testEncodeToByte() {
		assertArrayEquals(mBase1.getBytes(), Base64.encodeToByte(mPlain1.getBytes(), true));
		assertArrayEquals(mBase12.getBytes(), Base64.encodeToByte(mPlain12.getBytes(), true));
		assertArrayEquals(mBase123.getBytes(), Base64.encodeToByte(mPlain123.getBytes(), true));
		assertArrayEquals(mEmpty.getBytes(), Base64.encodeToByte(null, true));
	}

	@Test
	public void testEncodeToChar() {
		assertEquals(mBase1, new String(Base64.encodeToChar(mPlain1.getBytes(), true)));
		assertEquals(mBase12, new String(Base64.encodeToChar(mPlain12.getBytes(), true)));
		assertEquals(mBase123, new String(Base64.encodeToChar(mPlain123.getBytes(), true)));
		assertEquals(mEmpty, new String(Base64.encodeToChar(mEmpty.getBytes(), true)));
	}

	@Test
	public void testDecode() {
		// byte[]
		assertArrayEquals(mPlain1.getBytes(), Base64.decode(mBase1.getBytes()));
		assertArrayEquals(mPlain12.getBytes(), Base64.decode(mBase12.getBytes()));
		assertArrayEquals(mPlain123.getBytes(), Base64.decode(mBase123.getBytes()));
		assertArrayEquals(mEmpty.getBytes(), Base64.decode(mEmpty.getBytes()));
		// char[]
		assertArrayEquals(mPlain1.getBytes(), Base64.decode(mBase1.toCharArray()));
		assertArrayEquals(mPlain12.getBytes(), Base64.decode(mBase12.toCharArray()));
		assertArrayEquals(mPlain123.getBytes(), Base64.decode(mBase123.toCharArray()));
		assertArrayEquals(mEmpty.getBytes(), Base64.decode(mEmpty.toCharArray()));
		// String
		assertArrayEquals(mPlain1.getBytes(), Base64.decode(mBase1));
		assertArrayEquals(mPlain12.getBytes(), Base64.decode(mBase12));
		assertArrayEquals(mPlain123.getBytes(), Base64.decode(mBase123));
		assertArrayEquals(mEmpty.getBytes(), Base64.decode(mEmpty));
	}

	@Test
	public void testDecodeFast() {
		// byte[]
		assertArrayEquals(mPlain1.getBytes(), Base64.decodeFast(mBase1.getBytes()));
		assertArrayEquals(mPlain12.getBytes(), Base64.decodeFast(mBase12.getBytes()));
		assertArrayEquals(mPlain123.getBytes(), Base64.decodeFast(mBase123.getBytes()));
		assertArrayEquals(mEmpty.getBytes(), Base64.decodeFast(mEmpty.getBytes()));
		// char[]
		assertArrayEquals(mPlain1.getBytes(), Base64.decodeFast(mBase1.toCharArray()));
		assertArrayEquals(mPlain12.getBytes(), Base64.decodeFast(mBase12.toCharArray()));
		assertArrayEquals(mPlain123.getBytes(), Base64.decodeFast(mBase123.toCharArray()));
		assertArrayEquals(mEmpty.getBytes(), Base64.decodeFast(mEmpty.toCharArray()));
		// String
		assertArrayEquals(mPlain1.getBytes(), Base64.decodeFast(mBase1));
		assertArrayEquals(mPlain12.getBytes(), Base64.decodeFast(mBase12));
		assertArrayEquals(mPlain123.getBytes(), Base64.decodeFast(mBase123));
		assertArrayEquals(mEmpty.getBytes(), Base64.decodeFast(mEmpty));
	}
}
