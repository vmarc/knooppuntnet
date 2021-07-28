package kpn.core.database.implementation

import kpn.core.TestObjects
import kpn.core.database.doc.CouchNodeDoc
import kpn.core.test.TestSupport.withCouchDatabase
import kpn.core.util.UnitTest

class DatabaseAllIdsTest extends UnitTest with TestObjects {

  test("allIds") {
    withCouchDatabase { database =>
      database.save(CouchNodeDoc("node:1001", newNodeInfo(1001), None))
      database.save(CouchNodeDoc("node:1002", newNodeInfo(1002), None))
      val ids = database.allIds(stale = false).filter(_.startsWith("node:"))
      ids should equal(Seq("node:1001", "node:1002"))
    }
  }
}
