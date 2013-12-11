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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.w3c.dom.Document;

import de.hshannover.f4.trust.ifmapj.identifier.Identifier;

/**
 * Implementation of interface {@link RequestFactory}
 * 
 * @author aw
 * @deprecated
 * Will be gone soon
 */
class RequestFactoryImpl implements RequestFactoryInternal, RequestFactory {

	@Override
	public PublishRequest createPublishReq() {
		return Requests.createPublishReq();
	}
	
	@Override
	public PublishRequest createPublishReq(PublishElement el) {
		return Requests.createPublishReq(el);
	}

	@Override
	public PublishRequest createPublishReq(Collection<PublishElement> list) {
		return Requests.createPublishReq(new ArrayList<PublishElement>(list));
	}

	@Override
	public NewSessionRequest createNewSessionReq() {
		return Requests.createNewSessionReq();
	}

	@Override
	public EndSessionRequest createEndSessionReq() {
		return Requests.createEndSessionReq();
	}

	@Override
	public RenewSessionRequest createRenewSessionReq() {
		return Requests.createRenewSessionReq();
	}

	@Override
	public PurgePublisherRequest createPurgePublisherReq() {
		return createPurgePublisherReq();
	}

	@Override
	public PollRequest createPollReq() {
		return Requests.createPollReq();
	}

	@Override
	public SubscribeRequest createSubscribeReq() {
		return Requests.createSubscribeReq();
	}
	
	@Override
	public SubscribeRequest createSubscribeReq(SubscribeElement el) {
		return Requests.createSubscribeReq(el);
	}

	@Override
	public SubscribeRequest createSubscribeReq(List<SubscribeElement> list) {
		return Requests.createSubscribeReq(list);
	}

	@Override
	public SearchRequest createSearchRequest() {
		return Requests.createSearchReq();
	}

	@Override
	public SearchRequest createSearchRequest(String matchLinks, Integer maxDepth,
			String termIdents, Integer maxSize, String resultFilter, Identifier start) {
		
		return Requests.createSearchReq(matchLinks, maxDepth, termIdents,
				maxSize, resultFilter, start);
	}

	@Override
	public PublishUpdate createPublishUpdate() {
		return Requests.createPublishUpdate();
	}

	@Override
	public PublishUpdate createPublishUpdate(Identifier i1, Document md) {
		return Requests.createPublishUpdate(i1, md);
	}
	
	@Override
	public PublishUpdate createPublishUpdate(Identifier i1, Document md,
			MetadataLifetime lifetime) {
		return Requests.createPublishUpdate(i1, null, md, lifetime);
	}

	@Override
	public PublishUpdate createPublishUpdate(Identifier i1,
			Collection<Document> mdlist, MetadataLifetime lifetime) {
		return Requests.createPublishUpdate(i1, null, mdlist, lifetime);
	}

	@Override
	public PublishUpdate createPublishUpdate(Identifier i1, Identifier i2,
			Document md) {
		return Requests.createPublishUpdate(i1, i2, md, MetadataLifetime.session);
	}

	@Override
	public PublishUpdate createPublishUpdate(Identifier i1, Identifier i2,
			Document md, MetadataLifetime lifetime) {
		return Requests.createPublishUpdate(i1, i2, md);
	}

	@Override
	public PublishUpdate createPublishUpdate(Identifier i1,
			Collection<Document> mdlist) {
		return Requests.createPublishUpdate(i1, null, mdlist);
	}

	@Override
	public PublishUpdate createPublishUpdate(Identifier i1, Identifier i2,
			Collection<Document> mdlist) {
		return Requests.createPublishUpdate(i1, i2, mdlist, MetadataLifetime.session);
	}

	@Override
	public PublishUpdate createPublishUpdate(Identifier i1, Identifier i2,
			Collection<Document> mdlist, MetadataLifetime lifetime) {
		return Requests.createPublishUpdate(i1, i2, mdlist, lifetime);
	}
	
	@Override
	public PublishNotify createPublishNotify() {
		return Requests.createPublishNotify();
	}

	@Override
	public PublishNotify createPublishNotify(Identifier i1, Document md) {
		return Requests.createPublishNotify(i1, null, md);
	}


	@Override
	public PublishNotify createPublishNotify(Identifier i1,
			Collection<Document> md) {
		return Requests.createPublishNotify(i1, null, md);
	}

	@Override
	public PublishNotify createPublishNotify(Identifier i1, Identifier i2,
			Document md) {
		return Requests.createPublishNotify(i1, i2, md);
	}

	@Override
	public PublishNotify createPublishNotify(Identifier i1, Identifier i2,
			Collection<Document> mdlist) {
		return Requests.createPublishNotify(i1, i2, mdlist);
	}
	
	@Override
	public PublishDelete createPublishDelete() {
		return Requests.createPublishDelete();
	}

	@Override
	public PublishDelete createPublishDelete(Identifier i1) {
		return Requests.createPublishDelete(i1, (String)null);
	}

	@Override
	public PublishDelete createPublishDelete(Identifier i1, Identifier i2) {
		return Requests.createPublishDelete(i1, i2, null);
	}

	@Override
	public PublishDelete createPublishDelete(Identifier i1, String filter) {
		return Requests.createPublishDelete(i1, null, filter);
	}

	@Override
	public PublishDelete createPublishDelete(Identifier i1, Identifier i2,
			String filter) {
		return Requests.createPublishDelete(i1, i2, filter);
	}

	@Override
	public SubscribeUpdate createSubscribeUpdate() {
		return Requests.createSubscribeUpdate();
	}
	
	@Override
	public SubscribeUpdate createSubscribeUpdate(String name,
			String matchLinks, Integer maxDepth, String termIdents,
			Integer maxSize, String resultFilter, Identifier start) {
		return Requests.createSubscribeUpdate(name, matchLinks, maxDepth,
				termIdents, maxSize, resultFilter, start);
	}

	@Override
	public SubscribeDelete createSubscribeDelete() {
		return Requests.createSubscribeDelete();
	}

	@Override
	public SubscribeDelete createSubscribeDelete(String name) {
		return Requests.createSubscribeDelete(name);
	}
}
