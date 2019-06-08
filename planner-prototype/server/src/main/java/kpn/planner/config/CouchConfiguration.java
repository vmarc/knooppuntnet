package kpn.planner.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "couchdb")
public class CouchConfiguration {

	private String host;
	private String port;
	private String user;
	private String password;

	private Dbname dbname;

	public String getHost() {
		return host;
	}

	public void setHost(final String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(final String port) {
		this.port = port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(final String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public Dbname getDbname() {
		return dbname;
	}

	public void setDbname(final Dbname dbname) {
		this.dbname = dbname;
	}

	public static class Dbname {
		private String main;
		private String pois;

		public String getMain() {
			return main;
		}

		public void setMain(final String main) {
			this.main = main;
		}

		public String getPois() {
			return pois;
		}

		public void setPois(final String pois) {
			this.pois = pois;
		}
	}
}
