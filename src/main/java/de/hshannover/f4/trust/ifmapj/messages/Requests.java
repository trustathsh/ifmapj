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
 * This file is part of ifmapj, version 2.3.2, implemented by the Trust@HsH
 * research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2010 - 2016 Trust@HsH
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
package de.hshannover.f4.trust.ifmapj.messages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import util.DomHelpers;
import de.hshannover.f4.trust.ifmapj.binding.IfmapStrings;
import de.hshannover.f4.trust.ifmapj.exception.EndSessionException;
import de.hshannover.f4.trust.ifmapj.exception.IfmapErrorCode;
import de.hshannover.f4.trust.ifmapj.exception.IfmapErrorResult;
import de.hshannover.f4.trust.ifmapj.exception.MarshalException;
import de.hshannover.f4.trust.ifmapj.exception.UnmarshalException;
import de.hshannover.f4.trust.ifmapj.identifier.Identifier;
import de.hshannover.f4.trust.ifmapj.identifier.Identifiers;
import de.hshannover.f4.trust.ifmapj.messages.Requests.Helpers;
import de.hshannover.f4.trust.ifmapj.messages.SearchResult.Type;

/**
 * Provides access to {@link Request} associated classes.
 *
 * @since 0.1.4
 * @author aw
 */
public final class Requests {

	private static Map<Class<? extends Request>, RequestHandler<? extends Request>>
			sRequestHandlers;

	@SuppressWarnings("deprecation")
	private static RequestFactory sRequestFactoryInstance;

	// Not to be instanced
	private Requests() { }

	private static void initializeDefaultHandlers() {

		sRequestHandlers = new HashMap<Class<? extends Request>,
			RequestHandler<? extends Request>>();

		registerRequestHandler(new NewSessionRequestHandler());
		registerRequestHandler(new EndSessionRequestHandler());
		registerRequestHandler(new PurgePublisherRequestHandler());
		registerRequestHandler(new RenewSessionRequestHandler());
		registerRequestHandler(new PollRequestHandler());
		registerRequestHandler(new PublishRequestHandler());
		registerRequestHandler(new SubscribeRequestHandler());
		registerRequestHandler(new SearchRequestHandler());
	}

	/**
	 * Register a custom request handler.
	 *
	 * @param rh
	 */
	public static void registerRequestHandler(RequestHandler<? extends Request> rh) {
		if (rh == null) {
			throw new NullPointerException("rh is null");
		}

		if (rh.handles() == null) {
			throw new NullPointerException("rh.handles() returns null");
		}

		if (sRequestHandlers == null) {
			initializeDefaultHandlers();
		}

		if (sRequestHandlers.containsKey(rh.handles())) {
			throw new RuntimeException("RequestHandler already registered for "
					+ rh.handles());
		}

		sRequestHandlers.put(rh.handles(), rh);
	}

	public static RequestHandler<? extends Request> getHandlerFor(Request req) {

		if (sRequestHandlers == null) {
			initializeDefaultHandlers();
		}

		for (Entry<Class<? extends Request>,
				RequestHandler<? extends Request>> entry : sRequestHandlers.entrySet()) {
			if (entry.getKey().isInstance(req)) {
				return entry.getValue();
			}
		}

		return null;
	}


	/**
	 * @return
	 * @deprecated
	 * Don't use. Will be gone soon.
	 */
	@Deprecated
	public static RequestFactory getRequestFactory() {
		if (sRequestFactoryInstance == null) {
			sRequestFactoryInstance = new RequestFactoryImpl();
		}

		return sRequestFactoryInstance;
	}

	// REQUEST CREATION
	/**
	 * Create an empty {@link PublishRequest}
	 *
	 * @return the new {@link PublishRequest}
	 */
	public static PublishRequest createPublishReq() {
		return new PublishRequestImpl();
	}

	/**
	 * Create a new {@link PublishRequest} that contains the given {@link PublishElement}
	 *
	 * @param el the {@link PublishElement} that is added to the new {@link PublishRequest}
	 * @return the new {@link PublishRequest}
	 */
	public static PublishRequest createPublishReq(PublishElement el) {
		if (el == null) {
			throw new NullPointerException("el is not allowed to be null");
		}

		PublishRequest ret = createPublishReq();
		ret.addPublishElement(el);
		return ret;
	}


	/**
	 * Create a new {@link PublishRequest} that contains the given list of
	 * {@link PublishElement} instances.
	 *
	 * @param list the list of {@link PublishElement} instances that are added
	 * 		  to the new {@link PublishRequest}
	 * @return the new {@link PublishRequest}
	 */
	public static PublishRequest createPublishReq(List<PublishElement> list) {
		if (list == null) {
			throw new NullPointerException("list is not allowed to be null");
		}

		PublishRequest ret = createPublishReq();
		for (PublishElement el : list) {
			ret.addPublishElement(el);
		}

		return ret;
	}

	public static NewSessionRequest createNewSessionReq() {
		return new NewSessionRequestImpl();
	}

	public static EndSessionRequest createEndSessionReq() {
		return new EndSessionRequestImpl();
	}

	public static RenewSessionRequest createRenewSessionReq() {
		return new RenewSessionRequestImpl();
	}

	public static PurgePublisherRequest createPurgePublisherReq() {
		return new PurgePublisherRequestImpl();
	}

	public static PollRequest createPollReq() {
		return new PollRequestImpl();
	}

	/**
	 * Create an empty {@link SubscribeRequest}
	 *
	 * @return the new {@link SubscribeRequest} instance
	 */
	public static SubscribeRequest createSubscribeReq() {
		return new SubscribeRequestImpl();
	}

	/**
	 * Create a {@link SubscribeRequest} with one {@link SubscribeElement}.
	 *
	 * @param el the {@link SubscribeElement} that is added to the new
	 * {@link SubscribeRequest}. This can either be {@link SubscribeUpdate} or
	 * {@link SubscribeDelete}.
	 *
	 * @return the new {@link SubscribeRequest}
	 */
	public static SubscribeRequest createSubscribeReq(SubscribeElement el) {
		if (el == null) {
			throw new NullPointerException("el is not allowed to be null");
		}

		SubscribeRequest ret = createSubscribeReq();
		ret.addSubscribeElement(el);
		return ret;
	}

	/**
	 * Create a {@link SubscribeRequest} with a list of {@link SubscribeElement}
	 * instances.
	 *
	 * @param list the list of {@link SubscribeElement} instances that are added
	 * to the new {@link SubscribeRequest}. The {@link SubscribeElement} instances
	 * are either {@link SubscribeUpdate} or {@link SubscribeDelete} instances.
	 * @return the new {@link SubscribeRequest}
	 */
	public static SubscribeRequest createSubscribeReq(List<SubscribeElement> list) {
		if (list == null) {
			throw new NullPointerException("list is not allowed to be null");
		}

		SubscribeRequest ret = createSubscribeReq();
		for (SubscribeElement el : list) {
			ret.addSubscribeElement(el);
		}

		return ret;
	}

	/**
	 * Create a {@link SearchRequest} with default values. You need to set at
	 * least the start {@link Identifier} with
	 * {@link SearchRequest#setStartIdentifier(Identifier)} afterwards!
	 *
	 * @return the new {@link SearchRequest} instance
	 */
	public static SearchRequest createSearchReq() {
		return new SearchRequestImpl(new SearchHolderImpl());
	}

	/**
	 * Create a new {@link SearchRequest} in order to perform an IF-MAP search.
	 *
	 * @param matchLinks the match-links filter (null means match-all, an empty
	 * 		  {@link String} means match-nothing)
	 * @param maxDepth max-depth of the search (default is left out)
	 * 		  types (for example 'identity,device')
	 * @param maxSize max-size of search result (default is left out)
	 * @param resultFilter the result-filter filter (null means match-all, an
	 * 		  empty {@link String} means match-nothing)
	 * @param start the start {@link Identifier}
	 * @return the new {@link SearchRequest} instance
	 */
	public static SearchRequest createSearchReq(String matchLinks, Integer maxDepth,
			String termIdents, Integer maxSize, String resultFilter, Identifier start) {
		SearchRequest sr = createSearchReq();
		fillSearchHolder(sr, matchLinks, maxDepth, termIdents, maxSize, resultFilter, start);
		return sr;
	}

	private static void fillSearchHolder(SearchHolder holder, String matchLinks,
			Integer maxDepth, String termIdents, Integer maxSize,
			String resultFilter, Identifier start) {
		holder.setMatchLinksFilter(matchLinks);
		holder.setMaxDepth(maxDepth);
		holder.setTerminalIdentifierTypes(termIdents);
		holder.setMaxSize(maxSize);
		holder.setResultFilter(resultFilter);
		holder.setStartIdentifier(start);
	}

	/**
	 *
	 * @return the new {@link PublishUpdate} instance
	 */
	public static PublishUpdate createPublishUpdate() {
		return new PublishUpdateImpl();
	}

	/**
	 * Create a new {@link PublishUpdate} instance that is used to publish
	 * metadata to an {@link Identifier}. The {@link MetadataLifetime} of the
	 * new metadata is set to {@link MetadataLifetime#session}.
	 *
	 * @param i1 the {@link Identifier} to which the given metadata is published to
	 * @param md the metadata that shall be published
	 * @return the new {@link PublishUpdate} instance
	 */
	public static PublishUpdate createPublishUpdate(Identifier i1, Document md) {
		return createPublishUpdate(i1, md, MetadataLifetime.session);
	}

	/**
	 * Create a new {@link PublishUpdate} instance that is used to publish
	 * metadata on an {@link Identifier} with a specific {@link MetadataLifetime}.
	 *
	 * @param i1 the {@link Identifier} to which the given metadata is published to
	 * @param md the metadata that shall be published
	 * @param lifetime the lifetime of the new metadata
	 * @return the new {@link PublishUpdate} instance
	 */
	public static PublishUpdate createPublishUpdate(Identifier i1, Document md,
			MetadataLifetime lifetime) {
		return createPublishUpdate(i1, null, md, lifetime);
	}

	/**
	 * Create a new {@link PublishUpdate} instance that is used to publish
	 * metadata on a link between two {@link Identifier} instances. The
	 * {@link MetadataLifetime} of the new metadata is set to {@link MetadataLifetime#session}.
	 *
	 * @param i1 the first {@link Identifier} of the link
	 * @param i2 the second {@link Identifier} of the link
	 * @param md the metadata that shall be published
	 * @return the new {@link PublishUpdate} instance
	 */
	public static PublishUpdate createPublishUpdate(Identifier i1, Identifier i2,
			Document md) {
		return createPublishUpdate(i1, i2, md, MetadataLifetime.session);
	}

	/**
	 * Create a new {@link PublishUpdate} instance that is used to publish
	 * metadata on a link between two {@link Identifier} instances with a specific
	 * {@link MetadataLifetime}.
	 *
	 * @param i1 the first {@link Identifier} of the link
	 * @param i2 the second {@link Identifier} of the link
	 * @param md the metadata that shall be published
	 * @param lifetime the lifetime of the new metadata
	 * @return the new {@link PublishUpdate} instance
	 */
	public static PublishUpdate createPublishUpdate(Identifier i1, Identifier i2,
			Document md, MetadataLifetime lifetime) {
		if (md == null) {
			throw new NullPointerException("md not allowed to be null");
		}

		List<Document> list = new ArrayList<Document>(1);
		list.add(md);

		return createPublishUpdate(i1, i2, list, lifetime);
	}

	/**
	 * Create a new {@link PublishUpdate} instance that is used to publish
	 * a list of metadata instances with a specific {@link MetadataLifetime}.
	 *
	 * @param i1 the {@link Identifier} to which the given metadata is published to
	 * @param mdlist a list of metadata objects
	 * @param lifetime the lifetime of the new metadata
	 * @return the new {@link PublishUpdate} instance
	 */
	public static PublishUpdate createPublishUpdate(Identifier i1,
			Collection<Document> mdlist, MetadataLifetime lifetime) {
		return createPublishUpdate(i1, null, mdlist, lifetime);
	}

	/**
	 * Create a new {@link PublishUpdate} instance that is used to publish
	 * a list of metadata instances. The {@link MetadataLifetime} of the new
	 * metadata is set to {@link MetadataLifetime#session}.
	 *
	 * @param i1 the {@link Identifier} to which the given metadata is published to
	 * @param mdlist a list of metadata objects
	 * @return the new {@link PublishUpdate} instance
	 */
	public static PublishUpdate createPublishUpdate(Identifier i1,
			Collection<Document> mdlist) {
		return createPublishUpdate(i1, null, mdlist);
	}

	/**
	 * Create a new {@link PublishUpdate} instance that is used to publish
	 * a list of metadata on a link between two {@link Identifier} instances.
	 * The {@link MetadataLifetime} of the new metadata is set to
	 * {@link MetadataLifetime#session}.
	 *
	 * @param i1 the first {@link Identifier} of the link
	 * @param i2 the second {@link Identifier} of the link
	 * @param mdlist a list of metadata objects
	 * @return the new {@link PublishUpdate} instance
	 */
	public static PublishUpdate createPublishUpdate(Identifier i1, Identifier i2,
			Collection<Document> mdlist) {
		return createPublishUpdate(i1, i2, mdlist, MetadataLifetime.session);
	}

	/**
	 * Create a new {@link PublishUpdate} instance that is used to publish
	 * a list of metadata on a link between two {@link Identifier} instances
	 * with a specific {@link MetadataLifetime}.
	 *
	 * @param i1 the first {@link Identifier} of the link
	 * @param i2 the second {@link Identifier} of the link
	 * @param mdlist a list of metadata objects
	 * @param lifetime the lifetime of the new metadata
	 * @return the new {@link PublishUpdate} instance
	 */
	public static PublishUpdate createPublishUpdate(Identifier i1, Identifier i2,
			Collection<Document> mdlist, MetadataLifetime lifetime) {
		if (mdlist == null) {
			throw new NullPointerException("mdlist not allowed to be null");
		}
		PublishUpdate pu = createPublishUpdate();
		fillMetadataHolder(pu, i1, i2, mdlist);
		pu.setLifeTime(lifetime);
		return pu;
	}

	/**
	 * Create a new, empty {@link PublishNotify} instance.
	 *
	 * @return the new {@link PublishNotify} instance
	 */
	public static PublishNotify createPublishNotify() {
		return new PublishNotifyImpl();
	}

	/**
	 * Create a new {@link PublishNotify} instance that is used to publish
	 * metadata to an {@link Identifier}.
	 *
	 * @param i1 the {@link Identifier} to which the given metadata is published to
	 * @param md the metadata that shall be published
	 * @return the new {@link PublishNotify} instance
	 */
	public static PublishNotify createPublishNotify(Identifier i1, Document md) {
		return createPublishNotify(i1, null, md);
	}

	/**
	 * Create a new {@link PublishNotify} instance that is used to publish
	 * a list of metadata instances.
	 *
	 * @param i1 the {@link Identifier} to which the given metadata is published to
	 * @param mdlist a list of metadata objects
	 * @return the new {@link PublishNotify} instance
	 */
	public static PublishNotify createPublishNotify(Identifier i1,
			Collection<Document> mdlist) {
		return createPublishNotify(i1, null, mdlist);
	}

	/**
	 * Create a new {@link PublishNotify} instance that is used to publish
	 * metadata on a link between two {@link Identifier} instances.
	 *
	 * @param i1 the first {@link Identifier} of the link
	 * @param i2 the second {@link Identifier} of the link
	 * @param md the metadata that shall be published
	 * @return the new {@link PublishNotify} instance
	 */
	public static PublishNotify createPublishNotify(Identifier i1, Identifier i2,
			Document md) {

		if (md == null) {
			throw new NullPointerException("md is null");
		}

		List<Document> mdlist = new ArrayList<Document>(1);
		mdlist.add(md);

		return createPublishNotify(i1, i2, mdlist);
	}

	/**
	 * Create a new {@link PublishNotify} instance that is used to publish
	 * a list of metadata on a link between two {@link Identifier} instances.
	 * The {@link MetadataLifetime} of the new metadata is set to
	 * {@link MetadataLifetime#session}.
	 *
	 * @param i1 the first {@link Identifier} of the link
	 * @param i2 the second {@link Identifier} of the link
	 * @param mdlist a list of metadata objects
	 * @return the new {@link PublishNotify} instance
	 */
	public static PublishNotify createPublishNotify(Identifier i1, Identifier i2,
			Collection<Document> mdlist) {

		if (mdlist == null) {
			throw new NullPointerException("mdlist is null");
		}

		PublishNotify pn = createPublishNotify();
		fillMetadataHolder(pn, i1, i2, mdlist);
		return pn;
	}

	private static void fillMetadataHolder(MetadataHolder holder, Identifier i1,
			Identifier i2, Collection<Document> mdlist) {

		fillIdentifierHolder(holder, i1, i2);

		if (mdlist != null) {
			for (Document md : mdlist) {
				holder.addMetadata(md);
			}
		}
	}

	private static void fillIdentifierHolder(IdentifierHolder holder, Identifier i1,
			Identifier i2) {
		holder.setIdentifier1(i1);
		holder.setIdentifier2(i2);
	}

	/**
	 * Create a new, empty {@link PublishDelete} instance.
	 *
	 * @return the new {@link PublishDelete} instance
	 */
	public static PublishDelete createPublishDelete() {
		return new PublishDeleteImpl();
	}

	/**
	 * Create a new {@link PublishDelete} instance for a specific {@link Identifier}
	 * in order to delete all of its metadata.
	 *
	 * @param i1 the {@link Identifier} that is the target of the delete request
	 * @return the new {@link PublishDelete} instance
	 */
	public static PublishDelete createPublishDelete(Identifier i1) {
		return createPublishDelete(i1, (String) null);
	}

	/**
	 * Create a new {@link PublishDelete} instance for a link between two
	 * {@link Identifier} instances in order to delete all metadata of the link.
	 *
	 * @param i1 the first {@link Identifier} of the link
	 * @param i2 the second {@link Identifier} of the link
	 * @return the new {@link PublishDelete} instance
	 */
	public static PublishDelete createPublishDelete(Identifier i1, Identifier i2) {
		return createPublishDelete(i1, i2, null);
	}

	/**
	 * Create a new {@link PublishDelete} instance for a specific {@link Identifier}
	 * in order to delete its metadata that matches the given filter.
	 *
	 * @param i1 the {@link Identifier} that is the target of the delete request
	 * @param filter a filter that expresses the metadata that shall be deleted
	 * @return the new {@link PublishDelete} instance
	 */
	public static PublishDelete createPublishDelete(Identifier i1, String filter) {
		return createPublishDelete(i1, null, filter);
	}

	/**
	 * Create a new {@link PublishDelete} instance for a link between two
	 * {@link Identifier} instances in order to delete its metadata that matches
	 * the given filter.
	 *
	 * @param i1 the first {@link Identifier} of the link
	 * @param i2 the second {@link Identifier} of the link
	 * @param filter a filter that expresses the metadata that shall be deleted
	 * @return the new {@link PublishDelete} instance
	 */
	public static PublishDelete createPublishDelete(Identifier i1, Identifier i2,
			String filter) {
		PublishDelete pd = createPublishDelete();
		fillIdentifierHolder(pd, i1, i2);
		pd.setFilter(filter);
		return pd;
	}

	/**
	 * Create a {@link SubscribeUpdate} instance with default values. You need
	 * to add the start {@link Identifier} with
	 * {@link SubscribeUpdate#setStartIdentifier(Identifier)} and the name with
	 * {@link SubscribeUpdate#setName(String)} afterwards!
	 *
	 * @return the new {@link SubscribeUpdate} instance
	 */
	public static SubscribeUpdate createSubscribeUpdate() {
		return new SubscribeUpdateImpl(new SearchHolderImpl());
	}

	/**
	 * Create a {@link SubscribeUpdate} instance with the given parameters.
	 *
	 * @param name the name of the subscription
	 * @param matchLinks the match-links filter (null means match-all, an empty
	 * 		  {@link String} means match-nothing)
	 * @param maxDepth max-depth of the search (default is left out)
	 * 		  types (for example 'identity,device')
	 * @param maxSize max-size of search result (default is left out)
	 * @param resultFilter the result-filter filter (null means match-all, an
	 * 		  empty {@link String} means match-nothing)
	 * @param start the start {@link Identifier}
	 * @return the new {@link SubscribeUpdate} instance
	 */
	public static SubscribeUpdate createSubscribeUpdate(String name,
			String matchLinks, Integer maxDepth, String termIdents,
			Integer maxSize, String resultFilter, Identifier start) {
		SubscribeUpdate su = createSubscribeUpdate();
		fillSearchHolder(su, matchLinks, maxDepth, termIdents, maxSize, resultFilter, start);
		su.setName(name);
		return su;
	}

	/**
	 * Create a {@link SubscribeDelete} instance with default values. You need
	 * to set the name with {@link SubscribeDelete#setName(String)} afterwards!
	 *
	 * @return the new {@link SubscribeDelete} instance
	 */
	public static SubscribeDelete createSubscribeDelete() {
		return new SubscribeDeleteImpl();
	}

	/**
	 * Create a new {@link SubscribeDelete} instance with the given name.
	 *
	 * @param name the name of the subscription that shall be deleted on the
	 * MAPS.
	 * @return the new {@link SubscribeDelete} instance
	 */
	public static SubscribeDelete createSubscribeDelete(String name) {
		SubscribeDelete sd = createSubscribeDelete();
		sd.setName(name);
		return sd;
	}

	/**
	 * This class provides helpers for custom request implementations. It's not
	 * a fixed API!
	 */
	public static final class Helpers {

		private Helpers() { }


		public static String baseNsUri() {
			return IfmapStrings.BASE_NS_URI;
		}

		public static void addSearchInfo(SearchHolder searchInfo, Document doc, Element to)
				throws MarshalException {

			Identifier id = searchInfo.getStartIdentifier();
			String matchLinks = searchInfo.getMatchLinksFilter();
			String resultFilter = searchInfo.getResultFilter();
			Integer maxDepth = searchInfo.getMaxDepth();
			Integer maxSize = searchInfo.getMaxSize();
			String terminalIdentifiers = searchInfo.getTerminalIdentifierTypes();

			if (id == null) {
				throw new MarshalException("No start identifier");
			}

			addIdentifier(id, doc, to);

			if (matchLinks != null) {
				DomHelpers.addAttribute(to, IfmapStrings.SEARCH_MATCH_LINKS_FILTER_ATTR,
						matchLinks);
			}

			if (resultFilter != null) {
				DomHelpers.addAttribute(to, IfmapStrings.SEARCH_RESULT_FILTER_ATTR,
						resultFilter);
			}

			if (terminalIdentifiers != null) {
				DomHelpers.addAttribute(to, IfmapStrings.SEARCH_TERM_IDENT_TYPE,
						terminalIdentifiers);
			}

			if (maxDepth != null) {
				DomHelpers.addAttribute(to, IfmapStrings.SEARCH_MAX_DEPTH_ATTR,
						maxDepth.toString());
			}

			if (maxSize != null) {
				DomHelpers.addAttribute(to, IfmapStrings.SEARCH_MAX_SIZE_ATTR,
						maxSize.toString());
			}

			DomHelpers.addXmlNamespaceDeclarations(searchInfo, to);
		}

		public static void addIdentifiers(IdentifierHolder ih, Document doc, Element to)
				throws MarshalException {

			Identifier i1 = ih.getIdentifier1();
			Identifier i2 = ih.getIdentifier2();

			if (i1 == null && i2 == null) {
				throw new MarshalException("IdentifierHolder with no Identifiers");
			}

			if (i1 != null) {
				addIdentifier(i1, doc, to);
			}

			if (i2 != null) {
				addIdentifier(i2, doc, to);
			}
		}

		public static void addIdentifier(Identifier i, Document doc, Element to)
				throws MarshalException {
			to.appendChild(Identifiers.toElement(i, doc));
		}

		public static void addSessionId(Element el, Request r)
				throws MarshalException {

			if (r.getSessionId() == null || r.getSessionId().length() == 0) {
				throw new MarshalException("sessionId is null");
			}

			DomHelpers.addAttribute(el, IfmapStrings.SESSION_ID_ATTR, r.getSessionId());
		}

		public static SearchResult parseSearchResult(Element sres, Type type)
				throws UnmarshalException {
			List<ResultItem> ritems = new ArrayList<ResultItem>();
			String name = null;

			List<Element> elementChildren = DomHelpers.getChildElements(sres);

			if (elementChildren.size() == 0) {
				throw new UnmarshalException("searchResult with no resultItems");
			}

			for (Element child : elementChildren) {
				if (!DomHelpers.elementMatches(child, IfmapStrings.RESULT_ITEM_EL_NAME)) {
					throw new UnmarshalException("Found non " + IfmapStrings.RESULT_ITEM_EL_NAME + " element in "
							+ "SearchResult: " + child.getLocalName());
				}

				ritems.add(parseResultItem(child));
			}

			/* check if a name is set */
			name = sres.getAttribute(IfmapStrings.SEARCH_RES_NAME_ATTR);
			name = name != null && name.length() > 0 ? name : null;

			return new SearchResultImpl(ritems, name, type);
		}

		public static ResultItem parseResultItem(Element el)
				throws UnmarshalException {
			Identifier ident1 = null;
			Identifier ident2 = null;
			List<Document> mdlist = null;

			List<Element> elementChildren = DomHelpers.getChildElements(el);

			if (elementChildren.size() == 0) {
				throw new UnmarshalException("No elements in resultItem found");
			}

			if (elementChildren.size() > 3) {
				throw new UnmarshalException("Too many elements in resultItem "
						+ "element (" + elementChildren.size() + ")");
			}

			for (Element child : elementChildren) {
				Identifier tmp = Identifiers.tryFromElement(child);

				// If there was no identifier maybe there is some metadata
				if (tmp == null && DomHelpers.elementMatches(child, IfmapStrings.SEARCH_METADATA_EL_NAME)) {

					if (mdlist != null) {
						throw new UnmarshalException("Multiple metadata elements " + " in resultItem found");
					}

					mdlist = parseMetadataList(child);
				} else if (tmp == null) {
					throw new UnmarshalException("Unexpected element in searchResult: "
							+ child.getLocalName());
				}

				if (tmp != null && ident1 == null) {
					ident1 = tmp;
				} else if (tmp != null && ident2 == null) {
					ident2 = tmp;
				} else if (tmp != null) {
					throw new UnmarshalException("Found > 2 identifiers in resultItem");
				}
			}

			if (ident1 == null && ident2 == null) {
				throw new UnmarshalException("No identifier found in searchResult");
			}

			/* in case there was no metadata element, create one */
			if (mdlist == null) {
				mdlist = new LinkedList<Document>();
			}

			return new ResultItemImpl(ident1, ident2, mdlist);
		}

		public static List<Document> parseMetadataList(Element el)
				throws UnmarshalException {

			List<Document> ret = new ArrayList<Document>();
			List<Element> elementChildren = DomHelpers.getChildElements(el);

			for (Element child : elementChildren) {
				ret.add(DomHelpers.deepCopy(child));
			}

			return ret;
		}

		public static IfmapErrorResult parseErrorResult(Element el)
				throws UnmarshalException {
			String errCodeStr;
			String errStr;
			Element errStrElem = null;
			IfmapErrorCode errCode = null;
			String name = null;

			errCodeStr = el.getAttribute(IfmapStrings.ERROR_RES_ATTR_ERRCODE);

			errStrElem = DomHelpers.findElementInChildren(el,
					IfmapStrings.ERROR_RES_ERRSTR_EL_NAME, IfmapStrings.NO_URI);

			errStr = errStrElem == null ? "IfmapJ: errorString not set" : errStrElem.getTextContent();

			try {
				errCode = IfmapErrorCode.valueOf(errCodeStr);
			} catch (IllegalArgumentException e) {
				throw new UnmarshalException("Invalid errorCode received: " + errCodeStr);
			}

			// There might be a name if this errorResult is located in a pollResult
			if (el.getAttributeNode(IfmapStrings.ERROR_RES_ATTR_NAME) != null) {
				name = el.getAttribute(IfmapStrings.ERROR_RES_ATTR_NAME);
			}

			return new IfmapErrorResult(errCode, errStr, name);
		}

		public static void checkSimpleResult(Element el, String expected)
				throws UnmarshalException, IfmapErrorResult {
			Element content = Helpers.getResponseContentErrorCheck(el);

			if (!DomHelpers.elementMatches(content, expected)) {
				throw new UnmarshalException("Wrong Result. Expected " + expected + " got " + content.getLocalName());
			}
		}

		public static Element getResponseContentErrorCheck(Element response)
				throws UnmarshalException, IfmapErrorResult {
			Element content = getResponseContentNoErrorCheck(response);
			checkForError(content);
			return content;
		}

		public static Element getResponseContentNoErrorCheck(Element response)
				throws UnmarshalException, IfmapErrorResult {
			List<Element> children = DomHelpers.getChildElements(response);

			if (children.size() == 0) {
				throw new UnmarshalException("No element in response element found");
			}

			if (children.size() > 1) {
				throw new UnmarshalException("Too many elements in response element "
						+ " found (" + children.size() + ")");
			}

			return children.get(0);
		}

		public static void checkForError(Element el)
				throws UnmarshalException, IfmapErrorResult {
			if (DomHelpers.elementMatches(el, IfmapStrings.ERROR_RES_EL_NAME)) {
				throw parseErrorResult(el);
			}
		}

		public static void checkRequestType(Request req, RequestHandler<? extends Request> rh)
				throws MarshalException {

			Class<? extends Request> clazz = rh.handles();
			if (!clazz.isInstance(req)) {
				throw new MarshalException("Handler for requests of type "
						+ rh.handles() + " got request of type " + req.getClass());
			}
		}
	}

}


// REQUEST HANDLER IMPLEMENTATIONS

class NewSessionRequestHandler implements RequestHandler<NewSessionRequest> {

	@Override
	public Element toElement(Request req, Document doc) throws MarshalException {

		Helpers.checkRequestType(req, this);

		NewSessionRequest nsr = (NewSessionRequest) req;

		Element ret = doc.createElementNS(Helpers.baseNsUri(),
				DomHelpers.makeRequestFqName(
						IfmapStrings.NEW_SESSION_REQ_EL_NAME));

		if (nsr.getMaxPollResultSize() != null) {
			DomHelpers.addAttribute(ret,
					IfmapStrings.NEW_SESSION_REQ_ATTR_MPRS,
					nsr.getMaxPollResultSize().toString());
		}

		return ret;
	}

	@Override
	public Result fromElement(Element res) throws UnmarshalException, IfmapErrorResult {

		Element content = Helpers.getResponseContentErrorCheck(res);

		String sId;
		String pId;
		String mprsStr;
		Integer mprs = null;

		if (!DomHelpers.elementMatches(content, IfmapStrings.NEW_SESSION_RES_EL_NAME)) {
			throw new UnmarshalException("No newSession element found");
		}

		sId = content.getAttribute(IfmapStrings.NEW_SESSION_RES_ATTR_SID);
		pId = content.getAttribute(IfmapStrings.NEW_SESSION_RES_ATTR_PID);
		mprsStr = content.getAttribute(IfmapStrings.NEW_SESSION_RES_ATTR_MPRS);

		if (sId == null || sId.length() == 0) {
			throw new UnmarshalException("No " + IfmapStrings.NEW_SESSION_RES_ATTR_SID + " found");
		}

		if (pId == null || pId.length() == 0) {
			throw new UnmarshalException("No " + IfmapStrings.NEW_SESSION_RES_ATTR_PID + " found");
		}

		if (mprsStr != null && mprsStr.length() > 0) {
			try {
				mprs = Integer.parseInt(mprsStr);
			} catch (NumberFormatException e) {
				throw new UnmarshalException("Failed to parse " + "max-poll-result-size. Was: " + mprsStr);
			}
		}

		return new NewSessionResultImpl(sId, pId, mprs);
	}

	@Override
	public Class<NewSessionRequest> handles() {
		return NewSessionRequest.class;
	}

}

class EndSessionRequestHandler implements RequestHandler<EndSessionRequest> {

	@Override
	public Element toElement(Request req, Document doc) throws MarshalException {

		Helpers.checkRequestType(req, this);

		Element ret = doc.createElementNS(Helpers.baseNsUri(),
				DomHelpers.makeRequestFqName(
						IfmapStrings.END_SESSION_REQ_EL_NAME));

		Helpers.addSessionId(ret, req);

		return ret;
	}

	@Override
	public Result fromElement(Element res) throws UnmarshalException, IfmapErrorResult {
		Helpers.checkSimpleResult(res, IfmapStrings.END_SESSION_RES_EL_NAME);
		return null;
	}

	@Override
	public Class<EndSessionRequest> handles() {
		return EndSessionRequest.class;
	}
}

class PurgePublisherRequestHandler implements RequestHandler<PurgePublisherRequest> {

	@Override
	public Element toElement(Request req, Document doc) throws MarshalException {

		Helpers.checkRequestType(req, this);

		PurgePublisherRequest ppr = (PurgePublisherRequest) req;
		String pId = ppr.getPublisherId();

		if (pId == null || pId.length() == 0) {
			throw new MarshalException("No ifmap-publisher-id for "
					+ IfmapStrings.PURGE_PUBLISHER_REQ_EL_NAME + " set");
		}

		Element ret = doc.createElementNS(Helpers.baseNsUri(),
				DomHelpers.makeRequestFqName(
						IfmapStrings.PURGE_PUBLISHER_REQ_EL_NAME));

		DomHelpers.addAttribute(ret, IfmapStrings.PUBLISHER_ID_ATTR,
				ppr.getPublisherId());

		Helpers.addSessionId(ret, req);

		return ret;
	}

	@Override
	public Result fromElement(Element res) throws UnmarshalException, IfmapErrorResult {
		Helpers.checkSimpleResult(res, IfmapStrings.PURGE_PUBLISHER_RES_EL_NAME);
		return null;
	}

	@Override
	public Class<PurgePublisherRequest> handles() {
		return PurgePublisherRequest.class;
	}
}

class RenewSessionRequestHandler implements RequestHandler<RenewSessionRequest> {

	@Override
	public Element toElement(Request req, Document doc) throws MarshalException {

		Helpers.checkRequestType(req, this);

		Element ret = doc.createElementNS(Helpers.baseNsUri(),
				DomHelpers.makeRequestFqName(
						IfmapStrings.RENEW_SESSION_REQ_EL_NAME));

		Helpers.addSessionId(ret, req);

		return ret;
	}

	@Override
	public Result fromElement(Element res) throws UnmarshalException, IfmapErrorResult {
		Helpers.checkSimpleResult(res, IfmapStrings.RENEW_SESSION_RES_EL_NAME);
		return null;
	}

	@Override
	public Class<RenewSessionRequest> handles() {
		return RenewSessionRequest.class;
	}
}

class PollRequestHandler implements RequestHandler<PollRequest> {

	@Override
	public Element toElement(Request req, Document doc) throws MarshalException {

		Helpers.checkRequestType(req, this);

		Element ret = doc.createElementNS(Helpers.baseNsUri(),
				DomHelpers.makeRequestFqName(
						IfmapStrings.POLL_REQ_EL_NAME));

		Helpers.addSessionId(ret, req);

		return ret;
	}

	@Override
	public Result fromElement(Element res) throws UnmarshalException, IfmapErrorResult {
		return parsePollResultElement(res);
	}

	private Result parsePollResultElement(Element res)
			throws UnmarshalException, IfmapErrorResult {
		Element content = Helpers.getResponseContentNoErrorCheck(res);

		List<SearchResult> sResults = new ArrayList<SearchResult>();
		List<IfmapErrorResult> errorResults = new ArrayList<IfmapErrorResult>();
		Type curType;
		String curName;
		SearchResult curSearchResult;

		if (DomHelpers.elementMatches(content, IfmapStrings.POLL_RES_EL_NAME)) {
			List<Element> elementChildren = DomHelpers.getChildElements(content);

			if (elementChildren.size() == 0) {
				throw new UnmarshalException("No result elements in pollResult");
			}

			for (Element child : elementChildren) {
				curName = null;
				curType = resultType(child);

				if (curType != null) {
					curSearchResult = Helpers.parseSearchResult(child, curType);
					curName = curSearchResult.getName();
					sResults.add(curSearchResult);
				} else /* error result */ {
					IfmapErrorResult err = Helpers.parseErrorResult(child);
					curName = err.getName();
					errorResults.add(err);
				}

				if (curName == null) {
					throw new UnmarshalException("No name set for result in pollResult");
				}

			}
		} else if (DomHelpers.elementMatches(content, IfmapStrings.END_SESSION_RES_EL_NAME)) {
			return new EndSessionException();
		} else {
			Helpers.checkForError(content);
			// if we get here, we didn't find a pollResult, no endSessionResult
			// and no errorResult so something is weird ;)
			throw new UnmarshalException("Bad poll response: " + content.getLocalName());
		}

		return new PollResultImpl(sResults, errorResults);
	}


	private Type resultType(Element child) throws UnmarshalException {
		if (DomHelpers.elementMatches(child, IfmapStrings.POLL_SEARCH_RES_EL_NAME)) {
			return Type.searchResult;
		} else if (DomHelpers.elementMatches(child, IfmapStrings.POLL_UPDATE_RES_EL_NAME)) {
			return Type.updateResult;
		} else if (DomHelpers.elementMatches(child, IfmapStrings.POLL_DELETE_RES_EL_NAME)) {
			return Type.deleteResult;
		} else if (DomHelpers.elementMatches(child, IfmapStrings.POLL_NOTIFY_RES_EL_NAME)) {
			return Type.notifyResult;
		} else if (DomHelpers.elementMatches(child, IfmapStrings.ERROR_RES_EL_NAME)) {
			return null;
		} else {
			throw new UnmarshalException("Unknown element in "
					+ IfmapStrings.POLL_RES_EL_NAME + ": " + child.getLocalName());
		}
	}

	@Override
	public Class<PollRequest> handles() {
		return PollRequest.class;
	}
}

class PublishRequestHandler implements RequestHandler<PublishRequest> {

	@Override
	public Element toElement(Request req, Document doc) throws MarshalException {

		Helpers.checkRequestType(req, this);

		PublishRequest pr = (PublishRequest) req;

		Element ret = doc.createElementNS(Helpers.baseNsUri(),
				DomHelpers.makeRequestFqName(
						IfmapStrings.PUBLISH_REQ_EL_NAME));

		addPublishElements(pr, doc, ret);

		Helpers.addSessionId(ret, req);

		return ret;
	}

	private void addPublishElements(PublishRequest pr, Document doc, Element to)
			throws MarshalException {

		if (pr.getPublishElements().size() == 0) {
			throw new MarshalException("No publish elements");
		}

		for (PublishElement pubEl : pr.getPublishElements()) {
			Element el;

			if (pubEl instanceof PublishUpdate) {
				PublishUpdate pu = (PublishUpdate) pubEl;
				MetadataLifetime lifetime = MetadataLifetime.session;
				el = DomHelpers.createNonNsElement(doc,
						IfmapStrings.PUBLISH_UPDATE_EL_NAME);

				if (pu.getLifeTime() != null) {
					lifetime = pu.getLifeTime();
				}

				DomHelpers.addAttribute(el,
						IfmapStrings.PUBLISH_UPDATE_ATTR_LIFETIME,
						lifetime.toString());

			} else if (pubEl instanceof PublishDelete) {
				PublishDelete pd = (PublishDelete) pubEl;
				el = DomHelpers.createNonNsElement(doc,
						IfmapStrings.PUBLISH_DELETE_EL_NAME);

				if (pd.getFilter() != null) {
					DomHelpers.addAttribute(el,
							IfmapStrings.PUBLISH_DELETE_ATTR_FILTER,
							pd.getFilter());
				}

				DomHelpers.addXmlNamespaceDeclarations(pd, el);

			} else if (pubEl instanceof PublishNotify) {
				el = DomHelpers.createNonNsElement(doc,
						IfmapStrings.PUBLISH_NOTIFY_EL_NAME);
			} else {
				throw new MarshalException("Unknown PublishElement Implementation");
			}

			if (pubEl instanceof IdentifierHolder) {
				Helpers.addIdentifiers((IdentifierHolder) pubEl, doc, el);
			} else {
				throw new MarshalException("No IdentifierHoldingRequest?");
			}

			if (pubEl instanceof MetadataHolder) {
				addMetadataList((MetadataHolder) pubEl, doc, el);
			}

			to.appendChild(el);
		}
	}

	private void addMetadataList(MetadataHolder mh, Document doc,
			Element to) throws MarshalException {

		if (mh.getMetadata().size() == 0) {
			throw new MarshalException("No metadata to add");
		}

		Element mlist = DomHelpers.createNonNsElement(doc,
				IfmapStrings.PUBLISH_METADATA_EL_NAME);

		to.appendChild(mlist);

		// clone Metadata in form of Document instances and append the to
		// the mlist Element.
		for (Document md : mh.getMetadata()) {
			Node node = md.getFirstChild();
			Element toClone;
			Element clone;

			if (node == null) {
				throw new MarshalException("Metadata has no root Element?");
			}

			if (!(node instanceof Element)) {
				throw new MarshalException("Metadata root element not Element?");
			}

			toClone = (Element) node;
			clone = (Element) toClone.cloneNode(true);
			doc.adoptNode(clone);
			mlist.appendChild(clone);
		}
	}

	@Override
	public Result fromElement(Element resp)
			throws UnmarshalException, IfmapErrorResult {
		Helpers.checkSimpleResult(resp, IfmapStrings.PUBLISH_RES_EL_NAME);
		return null;
	}

	@Override
	public Class<PublishRequest> handles() {
		return PublishRequest.class;
	}
}

class SubscribeRequestHandler implements RequestHandler<SubscribeRequest> {

	@Override
	public Element toElement(Request req, Document doc) throws MarshalException {

		Helpers.checkRequestType(req, this);

		SubscribeRequest sr = (SubscribeRequest) req;

		Element ret = doc.createElementNS(Helpers.baseNsUri(),
				DomHelpers.makeRequestFqName(
						IfmapStrings.SUBSCRIBE_REQ_EL_NAME));

		addSubscribeElements(sr, doc, ret);

		Helpers.addSessionId(ret, req);

		return ret;
	}

	private void addSubscribeElements(SubscribeRequest sr, Document doc,
			Element to) throws MarshalException {

		if (sr.getSubscribeElements().size() == 0) {
			throw new MarshalException("No subscribe elements");
		}

		for (SubscribeElement subEl : sr.getSubscribeElements()) {
			Element el;

			if (subEl.getName() == null) {
				throw new MarshalException("subscription name null");
			}

			if (subEl.getName().length() == 0) {
				throw new MarshalException("subscription name empty");
			}

			if (subEl instanceof SubscribeUpdate) {
				el = DomHelpers.createNonNsElement(doc,
						IfmapStrings.SUBSCRIBE_UPDATE_EL_NAME);
			} else if (subEl instanceof SubscribeDelete) {
				el = DomHelpers.createNonNsElement(doc,
						IfmapStrings.SUBSCRIBE_DELETE_EL_NAME);
			} else {
				throw new MarshalException("Unknown SubscribeElement implementation");
			}

			DomHelpers.addAttribute(el,
					IfmapStrings.SUBSCRIBE_SUB_NAME_ATTR, subEl.getName());

			if (subEl instanceof SearchHolder) {
				Helpers.addSearchInfo((SearchHolder) subEl, doc, el);
			}

			to.appendChild(el);
		}
	}

	@Override
	public Result fromElement(Element resp)
			throws UnmarshalException, IfmapErrorResult {
		Helpers.checkSimpleResult(resp, IfmapStrings.SUBSCRIBE_RES_EL_NAME);
		return null;
	}

	@Override
	public Class<SubscribeRequest> handles() {
		return SubscribeRequest.class;
	}
}

class SearchRequestHandler implements RequestHandler<SearchRequest> {

	@Override
	public Element toElement(Request req, Document doc) throws MarshalException {

		Helpers.checkRequestType(req, this);

		SearchRequest sr = (SearchRequest) req;

		Element ret = doc.createElementNS(Helpers.baseNsUri(),
				DomHelpers.makeRequestFqName(
						IfmapStrings.SEARCH_REQ_EL_NAME));

		Helpers.addSessionId(ret, req);

		Helpers.addSearchInfo(sr, doc, ret);

		return ret;
	}

	@Override
	public Result fromElement(Element resp) throws UnmarshalException, IfmapErrorResult {
		Element content = Helpers.getResponseContentErrorCheck(resp);
		SearchResult ret = Helpers.parseSearchResult(content, Type.searchResult);
		if (ret.getName() != null) {
			throw new UnmarshalException("Found name attribute in searchResult after normal search");
		}

		return ret;
	}

	@Override
	public Class<SearchRequest> handles() {
		return SearchRequest.class;
	}
}
