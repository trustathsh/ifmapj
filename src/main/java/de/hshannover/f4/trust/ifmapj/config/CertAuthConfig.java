package de.hshannover.f4.trust.ifmapj.config;

/**
 * Configuration for certificate based SSRC authentication.
 */
public class CertAuthConfig {

	public final String url;
	public final String keyStorePath;
	public final String keyStorePassword;
	public final String trustStorePath;
	public final String trustStorePassword;

	public final boolean threadSafe;

	/**
	 * Create a new {@link CertAuthConfig} object with the given configuration
	 * parameter.
	 *
	 * @param url the URL to connect to
	 * @param keyStorePath path to keyStore file
	 * @param keyStorePassword password for keyStore
	 * @param trustStorePath path to trustStore
	 * @param trustStorePassword password for trustStore
	 * @param threadSafe true if the SSRC should be thread safe
	 */
	public CertAuthConfig(
			String url,
			String keyStorePath,
			String keyStorePassword,
			String trustStorePath,
			String trustStorePassword,
			boolean threadSafe) {
		super();
		this.url = url;
		this.keyStorePath = keyStorePath;
		this.keyStorePassword = keyStorePassword;
		this.trustStorePath = trustStorePath;
		this.trustStorePassword = trustStorePassword;
		this.threadSafe = threadSafe;
	}
}
