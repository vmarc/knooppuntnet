package kpn.planner.config;

import java.net.MalformedURLException;

import org.ektorp.CouchDbConnector;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;
import org.springframework.stereotype.Component;

@Component
public class Databases {

	private final CouchDbConnector main;
	private final CouchDbConnector pois;

	public Databases(CouchConfiguration configuration) {
		StdCouchDbInstance db = new StdCouchDbInstance(getClient(configuration));
		main = db.createConnector(configuration.getDbname().getMain(), false);
		pois = db.createConnector(configuration.getDbname().getPois(), false);
	}

	public CouchDbConnector getMain() {
		return main;
	}

	public CouchDbConnector getPois() {
		return pois;
	}

	private HttpClient getClient(CouchConfiguration configuration) {
		try {
			String url = "http://" + configuration.getHost() + ":" + configuration.getPort() + "/";
			return new StdHttpClient.Builder().url(url).username(configuration.getUser()).password(configuration.getPassword()).build();
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
}
