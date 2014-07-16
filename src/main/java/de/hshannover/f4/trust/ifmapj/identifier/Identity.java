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
package de.hshannover.f4.trust.ifmapj.identifier;

import de.hshannover.f4.trust.ifmapj.binding.IfmapStrings;
import de.hshannover.f4.trust.ifmapj.exception.MarshalException;

import javax.security.auth.x500.X500Principal;

import util.DomHelpers;
import util.StringHelpers;

/**
 * Represents an IF-MAP identity identifier.
 *
 * @author aw
 * @author jk
 *
 */
public class Identity extends IdentifierWithAd {

	private /* final */ String mName;

	private /* final */ IdentityType mType;

	private /* final */ String mOtherTypeDefinition;

	/**
	 * Package constructor.
	 */
	Identity(IdentityType type, String name, String admDom,
			String otherTypeDef) {
		super(admDom);

		mType = type;
		mOtherTypeDefinition = otherTypeDef;
		setName(name);
	}

	public String getName() {
		return mName;
	}

	@Deprecated
	public final void setName(String name) {
		if (name == null || mType == null) {
			mName = name;
			return;
		}

		// lowercase case-insensitive types
		if (mType.equals(IdentityType.dnsName)
				|| mType.equals(IdentityType.telUri)) {
			mName = name.toLowerCase();
		} else if (mType.equals(IdentityType.emailAddress) && StringHelpers.getStringCharCount(name, '@') == 1) {
			String[] parts = name.split("@");
			mName = parts[0] + "@" + parts[1].toLowerCase();
		} else if (mType.equals(IdentityType.hipHit)) {
			mName = name.toLowerCase();
		} else {
			mName = name;
		}
}

	public IdentityType getType() {
		return mType;
	}

	@Deprecated
	public void setType(IdentityType type) {
		mType = type;
	}

	public String getOtherTypeDefinition() {
		return mOtherTypeDefinition;
	}

	@Deprecated
	public void setOtherTypeDefinition(String otherTypeDefinition) {
		mOtherTypeDefinition = otherTypeDefinition;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String otherTypeDef = "";

		if (getType() == IdentityType.other) {
			otherTypeDef = ", " + getOtherTypeDefinition();
		}

		return String.format("id{%s, %s%s%s}", getName(), getType(),
				otherTypeDef, super.toString());
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mName == null) ? 0 : mName.hashCode());
		result = prime
				* result
				+ ((mOtherTypeDefinition == null) ? 0 : mOtherTypeDefinition
						.hashCode());
		result = prime * result + ((mType == null) ? 0 : mType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Identity other = (Identity) obj;
		if (mName == null) {
			if (other.mName != null)
				return false;
		} else if (!mName.equals(other.mName))
			return false;
		if (mOtherTypeDefinition == null) {
			if (other.mOtherTypeDefinition != null)
				return false;
		} else if (!mOtherTypeDefinition.equals(other.mOtherTypeDefinition))
			return false;
		if (mType != other.mType)
			return false;
		return true;
	}

	/**
	 * Compare two extended identifiers
	 *
	 * @param eid1 Extended identifier XML string
	 * @param eid2 Extended identifier XML string
	 * @return boolean true if both are equal
	 * @since 0.1.5
	 */
	private boolean extendedIdentifierEquals(String eid1, String eid2) {
		try {
			return DomHelpers.compare(DomHelpers.toDocument(eid1, null),
					DomHelpers.toDocument(eid2, null));
		} catch (MarshalException e) {
			return false;
		}
	}

	/**
	 * Compare two DSN
	 *
	 * @param dsn1 Distinguished name (X.500 DSN) string
	 * @param dsn2 Distinguished name (X.500 DSN) string
	 * @return boolean true if both DSN are equal
	 * @since 0.1.5
	 */
	private boolean distinguishedNameEquals(String dsn1, String dsn2) {
		return new X500Principal(dsn1).equals(new X500Principal(dsn2));
	}
}
