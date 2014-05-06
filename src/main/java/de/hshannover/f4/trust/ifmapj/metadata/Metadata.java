package de.hshannover.f4.trust.ifmapj.metadata;

/**
 * Interface for common IF-MAP metadata attributes.
 */
public interface Metadata {

	/**
	 * Return the publisher id of this metadata.
	 * If this metadata object has no publisher id the empty string is returned.
	 * If the extraction of the publisher id fails null is returned.
	 *
	 * @return the publisher id, null or the empty string
	 */
	public String getPublisherId();

	/**
	 * Return the publish timestamp of this metadata.
	 * If this metadata object has no publish timestamp the empty string is returned.
	 * If the extraction of the publish timestamp fails null is returned.
	 *
	 * @return the publish timestamp, null or the empty string
	 */
	public String getPublishTimestamp();

	/**
	 * Return the type name of this metadata, including the namespace prefix.
	 * If the extraction of the type name fails null is returned.
	 *
	 * @return the type name or null
	 */
	public String getTypename();

	/**
	 * Return the local type name of this metadata without the namespace prefix.
	 * If the extraction of the type name fails null is returned.
	 *
	 * @return the local type name or null
	 */
	public String getLocalname();

	/**
	 * Return the cardinality of this metadata.
	 * If this metadata object has no cardinality the empty string is returned.
	 * If the extraction of the cardinality fails null is returned.
	 *
	 * @return the cardinality, null or the empty string
	 */
	public String getCardinality();

	/**
	 * Return true if this metadata has 'singleValue' cardinality.
	 *
	 * @return true if cardinality equals 'singleValue'
	 */
	public boolean isSingleValue();

	/**
	 * Return true if this metadata has 'multiValue' cardinality.
	 *
	 * @return true if cardinality equals 'multiValue'
	 */
	public boolean isMultiValue();
}
