package util;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

/**
 * @since 0.1.5
 * @author jk
 */
public class DateHelpers {
	
	/**
	 * Formats the current date and time to an ISO-8601 compliant string
	 * without milliseconds.
	 * 
	 * @return current UTC date and time as ISO-8601 string representation.
	 */
	public static String getUtcTimeAsIso8601() {
		return getUtcTimeAsIso8601(null);
	}

	/**
	 * Formats the given Calendar to an ISO-8601 compliant string
	 * without milliseconds.
	 * 
	 * @param cal the java.util.Calendar to convert
	 * @return date and time as UTC ISO-8601 string representation.
	 */
	public static String getUtcTimeAsIso8601(Calendar cal) {
		try {
			if (cal == null)
				return (DatatypeFactory.newInstance().newXMLGregorianCalendar(
						new GregorianCalendar(TimeZone.getTimeZone("UTC"))))
						.toXMLFormat().replaceAll("\\.[0-9]{3}", "");
			
			GregorianCalendar suppliedDateCalendar =
					new GregorianCalendar(TimeZone.getTimeZone("UTC"));
			suppliedDateCalendar.setTimeInMillis(cal.getTimeInMillis());

			return (DatatypeFactory.newInstance().newXMLGregorianCalendar(
					suppliedDateCalendar)).toXMLFormat().replaceAll("\\.[0-9]{3}", "");
		} catch (DatatypeConfigurationException e) {
			SimpleDateFormat XmlDateUtc = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			XmlDateUtc.setTimeZone(TimeZone.getTimeZone("UTC"));
			return XmlDateUtc.format(Calendar.getInstance());
		}
	}
	
	/**
	 * Converts an ISO-8601 date/time string to a Calendar
	 * 
	 * @param isoDateStr an ISO-8601 compliant date/time string
	 * @return Calendar representing date and time in UTC from given string
	 * @throws DatatypeConfigurationException
	 */
	public static Calendar getTimeFromIso8601String(String isoDateStr)
			throws DatatypeConfigurationException {
		Calendar retCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		retCal.setTime(DatatypeFactory.newInstance()
				.newXMLGregorianCalendar(isoDateStr)
				.toGregorianCalendar().getTime());
		return retCal;
	}
}
