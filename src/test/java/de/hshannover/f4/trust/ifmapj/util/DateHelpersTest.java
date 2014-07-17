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

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;

import org.junit.Test;

import util.DateHelpers;

/**
 * Simple testcase for class {@link DateHelpers}.
 *
 * @since 0.1.5
 * @author jk
 *
 */
public class DateHelpersTest {

	 	@Test
	public void testGetTimeFromIso8601String() throws DatatypeConfigurationException {
		Calendar actual = DateHelpers.getTimeFromIso8601String("2010-03-23T14:23:29Z");
		Calendar expected = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		expected.setTimeInMillis(1269354209000L);
		assertEquals(expected, actual);
	}

	@Test
	public void testGetCurrentUtcTimeAsIso8601() {
		SimpleDateFormat xmlDateUtc =
			new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		xmlDateUtc.setTimeZone(TimeZone.getTimeZone("UTC"));
		Calendar time = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		String expectedStr = xmlDateUtc.format(time.getTime());
		String actualStr = DateHelpers.getUtcTimeAsIso8601(time);
		assertEquals(expectedStr, actualStr);
	}

	@Test
	public void testGetUtcTimeAsIso8601() {
		SimpleDateFormat xmlDateUtc =
			new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		xmlDateUtc.setTimeZone(TimeZone.getTimeZone("UTC"));
		Calendar desiredDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		desiredDate.set(2010, 10, 17, 22, 45, 32);
		String expectedStr = xmlDateUtc.format(desiredDate.getTime());
		String actualStr = DateHelpers.getUtcTimeAsIso8601(desiredDate);
		assertEquals(expectedStr, actualStr);
	}
}
