package de.hshannover.f4.trust.ifmapj.config;

/**
 * Configuration for basic SSRC authentication.
 */
public class BasicAuthConfig {

	public final String url;
	public final String username;
	public final String password;
	public final String trustStorePath;
	public final String trustStorePassword;

	public final boolean threadSafe;

	/**
	 * Create a new {@link BasicAuthConfig} object with the given configuration
	 * parameter.
	 *
	 * @param url the URL to connect to
	 * @param username basic authentication user
	 * @param password basic authentication password
	 * @param trustStorePath path to trustStore
	 * @param trustStorePassword password for trustStore
	 * @param threadSafe true if the SSRC should be thread safe
	 */
	public BasicAuthConfig(
			String url,
			String username,
			String password,
			String trustStorePath,
			String trustStorePassword,
			boolean threadSafe) {
		super();
		this.url = url;
		this.username = username;
		this.password = password;
		this.trustStorePath = trustStorePath;
		this.trustStorePassword = trustStorePassword;
		this.threadSafe = threadSafe;
	}
}
