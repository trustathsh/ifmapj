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
 * This file is part of ifmapj, version 2.0.1, implemented by the Trust@HsH
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
package de.hshannover.f4.trust.ifmapj.messages;

import java.util.Collection;
import java.util.List;

import org.w3c.dom.Document;

import de.hshannover.f4.trust.ifmapj.identifier.Identifier;

/**
 * Factory in order to create any IF-MAP request message.
 * <br/>
 * For PublishUpdate: If no lifetime is given, it's session.
 * <br/>
 * For PublishDelete: If no filter string is given, it is left out --> match all
 * <br/>
 * For Search/Subscribe: Setting same parameters to NULL leaves those parameters out.
 *
 * @author aw
 * @author ib
 * @deprecated
 * Use {@link Requests} directly to create differen {@link Request} implementations.
 *
 */
@Deprecated
public interface RequestFactory {

	/**
	 * Create an empty {@link PublishRequest}
	 *
	 * @return the new {@link PublishRequest}
	 */
	PublishRequest createPublishReq();

	/**
	 * Create a new {@link PublishRequest} that contains the given {@link PublishElement}
	 *
	 * @param el the {@link PublishElement} that is added to the new {@link PublishRequest}
	 * @return the new {@link PublishRequest}
	 */
	PublishRequest createPublishReq(PublishElement el);

	/**
	 * Create a new {@link PublishRequest} that contains the given list of
	 * {@link PublishElement} instances.
	 *
	 * @param list the list of {@link PublishElement} instances that are added
	 * 		  to the new {@link PublishRequest}
	 * @return the new {@link PublishRequest}
	 */
	PublishRequest createPublishReq(Collection<PublishElement> list);

	/**
	 * Create a new, empty {@link PublishUpdate} instance.
	 *
	 * @return the new {@link PublishUpdate} instance
	 */
	PublishUpdate createPublishUpdate();

	/**
	 * Create a new {@link PublishUpdate} instance that is used to publish
	 * metadata to an {@link Identifier}. The {@link MetadataLifetime} of the
	 * new metadata is set to {@link MetadataLifetime#session}.
	 *
	 * @param i1 the {@link Identifier} to which the given metadata is published to
	 * @param md the metadata that shall be published
	 * @return the new {@link PublishUpdate} instance
	 */
	PublishUpdate createPublishUpdate(Identifier i1, Document md);

	/**
	 * Create a new {@link PublishUpdate} instance that is used to publish
	 * metadata on an {@link Identifier} with a specific {@link MetadataLifetime}.
	 *
	 * @param i1 the {@link Identifier} to which the given metadata is published to
	 * @param md the metadata that shall be published
	 * @param lifetime the lifetime of the new metadata
	 * @return the new {@link PublishUpdate} instance
	 */
	PublishUpdate createPublishUpdate(Identifier i1, Document md,
			MetadataLifetime lifetime);

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
	PublishUpdate createPublishUpdate(Identifier i1, Identifier i2, Document md);

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
	PublishUpdate createPublishUpdate(Identifier i1, Identifier i2,
			Document md, MetadataLifetime lifetime);

	/**
	 * Create a new {@link PublishUpdate} instance that is used to publish
	 * a list of metadata instances. The {@link MetadataLifetime} of the new
	 * metadata is set to {@link MetadataLifetime#session}.
	 *
	 * @param i1 the {@link Identifier} to which the given metadata is published to
	 * @param mdlist a list of metadata objects
	 * @return the new {@link PublishUpdate} instance
	 */
	PublishUpdate createPublishUpdate(Identifier i1,
			Collection<Document> mdlist);

	/**
	 * Create a new {@link PublishUpdate} instance that is used to publish
	 * a list of metadata instances with a specific {@link MetadataLifetime}.
	 *
	 * @param i1 the {@link Identifier} to which the given metadata is published to
	 * @param mdlist a list of metadata objects
	 * @param lifetime the lifetime of the new metadata
	 * @return the new {@link PublishUpdate} instance
	 */
	PublishUpdate createPublishUpdate(Identifier i1,
			Collection<Document> mdlist, MetadataLifetime lifetime);

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
	PublishUpdate createPublishUpdate(Identifier i1, Identifier i2,
			Collection<Document> mdlist);

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
	PublishUpdate createPublishUpdate(Identifier i1, Identifier i2,
			Collection<Document> mdlist, MetadataLifetime lifetime);

	/**
	 * Create a new, empty {@link PublishDelete} instance.
	 *
	 * @return the new {@link PublishDelete} instance
	 */
	PublishDelete createPublishDelete();

	/**
	 * Create a new {@link PublishDelete} instance for a specific {@link Identifier}
	 * in order to delete all of its metadata.
	 *
	 * @param i1 the {@link Identifier} that is the target of the delete request
	 * @return the new {@link PublishDelete} instance
	 */
	PublishDelete createPublishDelete(Identifier i1);

	/**
	 * Create a new {@link PublishDelete} instance for a link between two
	 * {@link Identifier} instances in order to delete all metadata of the link.
	 *
	 * @param i1 the first {@link Identifier} of the link
	 * @param i2 the second {@link Identifier} of the link
	 * @return the new {@link PublishDelete} instance
	 */
	PublishDelete createPublishDelete(Identifier i1, Identifier i2);

	/**
	 * Create a new {@link PublishDelete} instance for a specific {@link Identifier}
	 * in order to delete its metadata that matches the given filter.
	 *
	 * @param i1 the {@link Identifier} that is the target of the delete request
	 * @param filter a filter that expresses the metadata that shall be deleted
	 * @return the new {@link PublishDelete} instance
	 */
	PublishDelete createPublishDelete(Identifier i1, String filter);

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
	PublishDelete createPublishDelete(Identifier i1, Identifier i2, String filter);

	/**
	 * Create a new, empty {@link PublishNotify} instance.
	 *
	 * @return the new {@link PublishNotify} instance
	 */
	PublishNotify createPublishNotify();

	/**
	 * Create a new {@link PublishNotify} instance that is used to publish
	 * metadata to an {@link Identifier}.
	 *
	 * @param i1 the {@link Identifier} to which the given metadata is published to
	 * @param md the metadata that shall be published
	 * @return the new {@link PublishNotify} instance
	 */
	PublishNotify createPublishNotify(Identifier i1, Document md);

	/**
	 * Create a new {@link PublishNotify} instance that is used to publish
	 * metadata on a link between two {@link Identifier} instances.
	 *
	 * @param i1 the first {@link Identifier} of the link
	 * @param i2 the second {@link Identifier} of the link
	 * @param md the metadata that shall be published
	 * @return the new {@link PublishNotify} instance
	 */
	PublishNotify createPublishNotify(Identifier i1, Identifier i2, Document md);

	/**
	 * Create a new {@link PublishNotify} instance that is used to publish
	 * a list of metadata instances.
	 *
	 * @param i1 the {@link Identifier} to which the given metadata is published to
	 * @param mdlist a list of metadata objects
	 * @return the new {@link PublishNotify} instance
	 */
	PublishNotify createPublishNotify(Identifier i1,
			Collection<Document> mdlist);

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
	PublishNotify createPublishNotify(Identifier i1, Identifier i2,
			Collection<Document> mdlist);

	/**
	 * Create a {@link SearchRequest} with default values. You need to set at
	 * least the start {@link Identifier} with
	 * {@link SearchRequest#setStartIdentifier(Identifier)} afterwards!
	 *
	 * @return the new {@link SearchRequest} instance
	 */
	SearchRequest createSearchRequest();

	/**
	 * Create a new {@link SearchRequest} in order to perform an IF-MAP search.
	 *
	 * @param matchLinks the match-links filter (null means match-all, an empty
	 * 		  {@link String} means match-nothing)
	 * @param maxDepth max-depth of the search (default is left out)
	 * @param terminalIdentifiers comma separated list of terminal identifier
	 * 		  types (for example 'identity,device')
	 * @param maxSize max-size of search result (default is left out)
	 * @param resultFilter the result-filter filter (null means match-all, an
	 * 		  empty {@link String} means match-nothing)
	 * @param start the start {@link Identifier}
	 * @return the new {@link SearchRequest} instance
	 */
	SearchRequest createSearchRequest(String matchLinks, Integer maxDepth,
			String terminalIdentifiers, Integer maxSize, String resultFilter,
			Identifier start);

	/**
	 * Create an empty {@link SubscribeRequest}
	 *
	 * @return the new {@link SubscribeRequest} instance
	 */
	SubscribeRequest createSubscribeReq();

	/**
	 * Create a {@link SubscribeRequest} with one {@link SubscribeElement}.
	 *
	 * @param el the {@link SubscribeElement} that is added to the new
	 * {@link SubscribeRequest}. This can either be {@link SubscribeUpdate} or
	 * {@link SubscribeDelete}.
	 *
	 * @return the new {@link SubscribeRequest}
	 */
	SubscribeRequest createSubscribeReq(SubscribeElement el);

	/**
	 * Create a {@link SubscribeRequest} with a list of {@link SubscribeElement}
	 * instances.
	 *
	 * @param list the list of {@link SubscribeElement} instances that are added
	 * to the new {@link SubscribeRequest}. The {@link SubscribeElement} instances
	 * are either {@link SubscribeUpdate} or {@link SubscribeDelete} instances.
	 * @return the new {@link SubscribeRequest}
	 */
	SubscribeRequest createSubscribeReq(List<SubscribeElement> list);

	/**
	 * Create a {@link SubscribeUpdate} instance with default values. You need
	 * to add the start {@link Identifier} with
	 * {@link SubscribeUpdate#setStartIdentifier(Identifier)} and the name with
	 * {@link SubscribeUpdate#setName(String)} afterwards!
	 *
	 * @return the new {@link SubscribeUpdate} instance
	 */
	SubscribeUpdate createSubscribeUpdate();

	/**
	 * Create a {@link SubscribeUpdate} instance with the given parameters.
	 *
	 * @param name the name of the subscription
	 * @param matchLinks the match-links filter (null means match-all, an empty
	 * 		  {@link String} means match-nothing)
	 * @param maxDepth max-depth of the search (default is left out)
	 * @param terminalIdentifiers comma separated list of terminal identifier
	 * 		  types (for example 'identity,device')
	 * @param maxSize max-size of search result (default is left out)
	 * @param resultFilter the result-filter filter (null means match-all, an
	 * 		  empty {@link String} means match-nothing)
	 * @param start the start {@link Identifier}
	 * @return the new {@link SubscribeUpdate} instance
	 */
	SubscribeUpdate createSubscribeUpdate(String name, String matchLinks,
			Integer maxDepth, String terminalIdentifiers, Integer maxSize,
			String resultFilter, Identifier start);

	/**
	 * Create a {@link SubscribeDelete} instance with default values. You need
	 * to set the name with {@link SubscribeDelete#setName(String)} afterwards!
	 *
	 * @return the new {@link SubscribeDelete} instance
	 */
	SubscribeDelete createSubscribeDelete();

	/**
	 * Create a new {@link SubscribeDelete} instance with the given name.
	 *
	 * @param name the name of the subscription that shall be deleted on the
	 * MAPS.
	 * @return the new {@link SubscribeDelete} instance
	 */
	SubscribeDelete createSubscribeDelete(String name);


}
