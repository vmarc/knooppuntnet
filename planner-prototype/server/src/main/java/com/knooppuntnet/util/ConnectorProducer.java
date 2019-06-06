package com.knooppuntnet.util;

import org.ektorp.CouchDbConnector;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

import java.net.MalformedURLException;

public class ConnectorProducer {
    private static final String URL = "http://xxx:5984/";
    private static final String USERNAME = "xxx";
    private static final String PASSWORD = "xxx";
    private static final String DBNAMEKNOOPPUNT = "ben2";
	private static final String DBNAMEPOI = "pois";

    public static CouchDbConnector getCouchDbConnectorKnooppunt() {
		StdCouchDbInstance db = new StdCouchDbInstance(getClient());

		return db.createConnector(DBNAMEKNOOPPUNT, false);
	}

	public static CouchDbConnector getCouchDbConnectorPoi() {
		StdCouchDbInstance db = new StdCouchDbInstance(getClient());

		return db.createConnector(DBNAMEPOI, false);
	}

	private static HttpClient getClient() {
		try {
    	return  new StdHttpClient.Builder()
				.url(URL)
//				.username(USERNAME)
//				.password(PASSWORD)
				.build();
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
}
