package kpn.planner.util;

import java.net.MalformedURLException;

import org.ektorp.CouchDbConnector;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConnectorProducer {

	@Autowired
	private CouchConfiguration couchConfiguration;

	public CouchDbConnector getCouchDbConnectorKnooppunt() {
		StdCouchDbInstance db = new StdCouchDbInstance(getClient());
		return db.createConnector(couchConfiguration.getDbname().getMain(), false);
	}

	public CouchDbConnector getCouchDbConnectorPoi() {
		StdCouchDbInstance db = new StdCouchDbInstance(getClient());
		return db.createConnector(couchConfiguration.getDbname().getPois(), false);
	}

	private HttpClient getClient() {
		try {
			String url = "http://" + couchConfiguration.getHost() + ":" + couchConfiguration.getPort() + "/";
			return new StdHttpClient.Builder().url(url).username(couchConfiguration.getUser()).password(couchConfiguration.getPassword()).build();
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
}
