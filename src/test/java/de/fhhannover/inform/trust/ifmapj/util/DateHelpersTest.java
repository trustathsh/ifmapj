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

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
	public void testGetTimeFromIso8601String() throws DatatypeConfigurationException{
		Calendar actual = DateHelpers.getTimeFromIso8601String("2010-03-23T14:23:29Z");
		Calendar expected = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		expected.setTimeInMillis(1269354209000l);
		assertEquals(expected, actual);
	}

	@Test
	public void testGetCurrentUtcTimeAsIso8601(){
		SimpleDateFormat XmlDateUtc =
			new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		XmlDateUtc.setTimeZone(TimeZone.getTimeZone("UTC"));
		String expectedStr = XmlDateUtc.format(
			Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime());
		String actualStr = DateHelpers.getUtcTimeAsIso8601();
		assertEquals(expectedStr, actualStr);
	}

	@Test
	public void testGetUtcTimeAsIso8601(){
		SimpleDateFormat XmlDateUtc =
			new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		XmlDateUtc.setTimeZone(TimeZone.getTimeZone("UTC"));
		Calendar desiredDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		desiredDate.set(2010, 10, 17, 22, 45, 32);
		String expectedStr = XmlDateUtc.format(desiredDate.getTime());
		String actualStr = DateHelpers.getUtcTimeAsIso8601(desiredDate);
		assertEquals(expectedStr, actualStr);
	}
}
