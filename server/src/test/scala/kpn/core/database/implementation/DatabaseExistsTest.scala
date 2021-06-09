package kpn.core.database.implementation

import kpn.core.database.Database
import kpn.core.database.DatabaseImpl
import kpn.core.test.TestSupport.withCouchDatabase
import kpn.core.test.TestSupport.withEnvironment
import kpn.core.util.UnitTest

class DatabaseExistsTest extends UnitTest {

  test("exists - database exists") {
    withCouchDatabase { database =>
      assert(database.exists)
    }
  }

  test("exists - database does not exist") {
    withEnvironment { (couchConfig, objectMapper) =>
      val database: Database = new DatabaseImpl(DatabaseContextImpl(couchConfig, objectMapper, "bla"))
      assert(!database.exists)
    }
  }

}
