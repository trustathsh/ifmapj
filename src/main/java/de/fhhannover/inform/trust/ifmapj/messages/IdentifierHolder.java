package de.fhhannover.inform.trust.ifmapj.messages;

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

import de.fhhannover.inform.trust.ifmapj.identifier.Identifier;

/**
 * Interface to access {@link Identifier} objects in elements that hold them.
 * 
 * An element containing up to two {@link Identifier} objects, as it is the
 * case for {@link PublishUpdate}, {@link PublishNotify}, {@link PublishDelete}
 * {@link ResultItem}.
 * 
 * @author aw
 *
 */
public interface IdentifierHolder {
	
	/**
	 * @return the first identifier attached to this element.
	 */
	public Identifier getIdentifier1();
	
	/**
	 * @param id the {@link Identifier} to be set.
	 * @throws NullPointerException if the given {@link Identifier} is null.
	 */
	public void setIdentifier1(Identifier id);
	
	/**
	 * @return the second identifier attached to this element.
	 */
	public Identifier getIdentifier2();
	
	/**
	 * @param id the {@link Identifier} to be set.
	 * @throws NullPointerException if the given {@link Identifier} is null.
	 */
	public void setIdentifier2(Identifier id);
	
	/**
	 * @return an array of size 2, containing references to both identifiers,
	 *	 or null entries if appropriate
	 */
	public Identifier[] getIdentifier();
	
	/**
	 * @return true if both identifiers are non-null.
	 */
	public boolean holdsLink();
}
