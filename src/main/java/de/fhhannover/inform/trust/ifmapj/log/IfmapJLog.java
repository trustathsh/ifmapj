package de.fhhannover.inform.trust.ifmapj.log;

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

/**
 * Remove this as soon as possible, it's just here to provide a bit more
 * logging than System.out.println and I didn't want to include log4j...
 * 
 * @author aw
 *
 */
public class IfmapJLog {
	
	IfmapJLog() { }
	
	private static IfmapJLogger mLoggerInstance = new IfmapJLogger() {
			
			@Override
			public void trace(String msg) {
				System.out.println("TRACE: " + msg);
				System.out.flush();
				
			}

			@Override
			public void debug(String msg) {
				System.out.println("DEBUG: " + msg);
				System.out.flush();
			}
			
			@Override
			public void info(String msg) {
				System.out.println("INFO: " + msg);
				System.out.flush();
			}

			@Override
			public void warn(String msg) {
				System.out.println("WARN: " + msg);
				System.out.flush();
			}
			
			@Override
			public void error(String msg) {
				System.err.println("ERROR: " + msg);
				System.err.flush();
			}

		};
	
	public static void setLogger(IfmapJLogger logger) {
		mLoggerInstance = logger;
	}

	public static void trace(String msg) {
		if (mLoggerInstance != null)
			mLoggerInstance.trace(msg);
	}
	
	public static void debug(String msg) {
		if (mLoggerInstance != null)
			mLoggerInstance.debug(msg);
	}
	
	public static void info(String msg) {
		if (mLoggerInstance != null)
			mLoggerInstance.info(msg);
	}

	public static void warn(String msg) {
		if (mLoggerInstance != null)
			mLoggerInstance.warn(msg);
	}
	
	public static void error(String msg) {
		if (mLoggerInstance != null)
			mLoggerInstance.error(msg);
	}
}
