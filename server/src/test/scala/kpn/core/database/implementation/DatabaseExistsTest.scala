package kpn.core.database.implementation

import kpn.core.database.Database
import kpn.core.database.DatabaseImpl
import kpn.core.test.TestSupport.withDatabase
import kpn.core.test.TestSupport.withEnvironment
import kpn.core.util.UnitTest

class DatabaseExistsTest extends UnitTest {

  test("exists - database exists") {
    withDatabase { database =>
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
