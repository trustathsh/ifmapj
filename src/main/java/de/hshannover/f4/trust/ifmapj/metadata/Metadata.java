package de.hshannover.f4.trust.ifmapj.metadata;

/**
 * Interface for common IF-MAP metadata attributes.
 */
public interface Metadata {

	/**
	 * Return the publisher id of this metadata.
	 *
	 * @return the publisher id
	 */
	public String getPublisherId();

	/**
	 * Return the publish timestamp of this metadata.
	 *
	 * @return the publish timestamp
	 */
	public String getPublishTimestamp();

	/**
	 * Return the type name of this metadata, including the namespace prefix.
	 *
	 * @return the type name
	 */
	public String getTypename();

	/**
	 * Return the local type name of this metadata without the namespace prefix.
	 *
	 * @return the local type name
	 */
	public String getLocalname();

	/**
	 * Return the cardinality of this metadata.
	 *
	 * @return the cardinality
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
