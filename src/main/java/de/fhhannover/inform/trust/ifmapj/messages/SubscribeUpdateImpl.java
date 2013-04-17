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

import java.util.Collection;

import util.Pair;
import de.fhhannover.inform.trust.ifmapj.identifier.Identifier;


/**
 * Implements {@link SubscribeUpdate} by simply delegating to SearchHolder;
 * 
 * @author aw
 *
 */
class SubscribeUpdateImpl extends SubscribeElementImpl implements SubscribeUpdate {
	
	private final SearchHolder mSearchHolder;
	
	SubscribeUpdateImpl(SearchHolder searchHolder) {
		if (searchHolder == null)
			throw new NullPointerException("searchHolder is null");
		
		mSearchHolder = searchHolder;
	}

	@Override
	public void addNamespaceDeclaration(String prefix, String uri) {
		mSearchHolder.addNamespaceDeclaration(prefix, uri);
	}

	@Override
	public Collection<Pair<String, String>> getNamespaceDeclarations() {
		return mSearchHolder.getNamespaceDeclarations();
	}

	@Override
	public Identifier getStartIdentifier() {
		return mSearchHolder.getStartIdentifier();
	}

	@Override
	public void setStartIdentifier(Identifier startIdentifier) {
		mSearchHolder.setStartIdentifier(startIdentifier);
	}

	@Override
	public String getMatchLinksFilter() {
		return mSearchHolder.getMatchLinksFilter();
	}

	@Override
	public void setMatchLinksFilter(String matchLinksFilter) {
		mSearchHolder.setMatchLinksFilter(matchLinksFilter);
	}

	@Override
	public Integer getMaxDepth() {
		return mSearchHolder.getMaxDepth();
	}

	@Override
	public void setMaxDepth(Integer maxDepth) {
		mSearchHolder.setMaxDepth(maxDepth);
	}

	@Override
	public String getTerminalIdentifierTypes() {
		return mSearchHolder.getTerminalIdentifierTypes();
	}

	@Override
	public void setTerminalIdentifierTypes(String terminalIdentifierTypes) {
		mSearchHolder.setTerminalIdentifierTypes(terminalIdentifierTypes);
	}

	@Override
	public Integer getMaxSize() {
		return mSearchHolder.getMaxSize();
	}

	@Override
	public void setMaxSize(Integer maxSize) {
		mSearchHolder.setMaxSize(maxSize);
	}

	@Override
	public String getResultFilter() {
		return mSearchHolder.getResultFilter();
	}

	@Override
	public void setResultFilter(String resultFilter) {
		mSearchHolder.setResultFilter(resultFilter);
	}
}