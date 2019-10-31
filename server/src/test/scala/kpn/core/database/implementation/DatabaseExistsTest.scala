package kpn.core.database.implementation

import kpn.core.database.Database
import kpn.core.database.DatabaseImpl
import kpn.core.test.TestSupport.withDatabase
import kpn.core.test.TestSupport.withEnvironment
import org.scalatest.FunSuite
import org.scalatest.Matchers

class DatabaseExistsTest extends FunSuite with Matchers {

  test("exists - database exists") {
    withDatabase { database =>
      database.exists should equal(true)
    }
  }

  test("exists - database does not exist") {
    withEnvironment { (couchConfig, objectMapper) =>
      val database: Database = new DatabaseImpl(DatabaseContext(couchConfig, objectMapper, "bla"))
      database.exists should equal(false)
    }
  }

}
