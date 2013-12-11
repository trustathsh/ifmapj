package de.hshannover.f4.trust.ifmapj.messages;

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
 * This file is part of IfmapJ, version 1.0.0, implemented by the Trust@HsH
 * research group at the Hochschule Hannover.
 * 
 * IfmapJ is a lightweight, platform-independent, easy-to-use IF-MAP client
 * library for Java. IF-MAP is an XML based protocol for sharing data across
 * arbitrary components, specified by the Trusted Computing Group. IfmapJ is
 * maintained by the Trust@HsH group at the Hochschule Hannover. IfmapJ
 * was developed within the ESUKOM research project.
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

import java.util.Collection;

import util.Pair;
import de.hshannover.f4.trust.ifmapj.identifier.Identifier;

class SearchHolderImpl implements SearchHolder {

	private Identifier mStartIdentifier;
	
	private String mMatchLinksFilter;
	
	private Integer mMaxDepth;
	
	private String mTerminalIdentifierTypes;
	
	private Integer mMaxSize;
	
	private String mResultFilter;
	
	private final NamespaceDeclarationHolder mNamespaces;
	
	/**
	 * package constructor
	 */
	SearchHolderImpl() {
		mNamespaces = new NamespaceDeclarationHolderImpl();
	}

	@Override
	public Identifier getStartIdentifier() {
		return mStartIdentifier;
	}

	@Override
	public void setStartIdentifier(Identifier startIdentifier) {
		mStartIdentifier = startIdentifier;
	}

	@Override
	public String getMatchLinksFilter() {
		return mMatchLinksFilter;
	}

	@Override
	public void setMatchLinksFilter(String matchLinksFilter) {
		mMatchLinksFilter = matchLinksFilter;
	}

	@Override
	public Integer getMaxDepth() {
		return mMaxDepth;
	}

	@Override
	public void setMaxDepth(Integer maxDepth) {
		mMaxDepth = maxDepth;
	}

	@Override
	public String getTerminalIdentifierTypes() {
		return mTerminalIdentifierTypes;
	}

	@Override
	public void setTerminalIdentifierTypes(String terminalIdentifierTypes) {
		mTerminalIdentifierTypes = terminalIdentifierTypes;
	}

	@Override
	public Integer getMaxSize() {
		return mMaxSize;
	}

	@Override
	public void setMaxSize(Integer maxSize) {
		mMaxSize = maxSize;
	}

	@Override
	public String getResultFilter() {
		return mResultFilter;
	}

	@Override
	public void setResultFilter(String resultFilter) {
		mResultFilter = resultFilter;
	}
	
	/*
	 * We simply delegate those calls, so we can reuse some code...
	 * 
	 * (non-Javadoc)
	 * @see de.fhhannover.inform.trust.ifmapj.request.NamespaceDeclarationHolder#addNamespaceDeclaration(java.lang.String, java.lang.String)
	 */
	@Override
	public void addNamespaceDeclaration(String prefix, String uri) {
		mNamespaces.addNamespaceDeclaration(prefix, uri);
	}

	/*
	 * We simply delegate those calls, so we can reuse some code...
	 * 
	 * (non-Javadoc)
	 * @see de.fhhannover.inform.trust.ifmapj.request.NamespaceDeclarationHolder#getNamespaceDeclarations()
	 */
	@Override
	public Collection<Pair<String, String>> getNamespaceDeclarations() {
		return mNamespaces.getNamespaceDeclarations();
	}
}
