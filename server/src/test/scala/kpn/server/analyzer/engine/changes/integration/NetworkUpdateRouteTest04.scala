package kpn.server.analyzer.engine.changes.integration

import kpn.core.test.TestSupport.withDatabase

class NetworkUpdateRouteTest04 extends AbstractTest {

  test("delete route that is part of network") {

    // TODO CHANGE assert does not become an orphan or ignored route
    // TODO CHANGE assert gets delete from the database ?? or better: do we keep for future reference as link from changeset histories ???

    pending

    withDatabase { database =>
    }
  }

}
