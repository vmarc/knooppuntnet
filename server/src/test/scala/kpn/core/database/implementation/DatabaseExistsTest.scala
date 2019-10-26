package kpn.core.database.implementation

import kpn.core.database.{Database, DatabaseImpl}
import kpn.core.test.TestSupport.{withDatabase, withEnvironment}
import org.scalatest.{FunSuite, Matchers}

class DatabaseExistsTest extends FunSuite with Matchers {

  test("exists - database exists") {
    withDatabase(database => {
      database.exists should equal(true)
    })
  }

  test("exists - database does not exist") {
    withEnvironment((tempCouch, couchConfig, objectMapper) => {
      val database: Database = new DatabaseImpl(DatabaseContext(tempCouch, couchConfig, objectMapper, "bla"))
      database.exists should equal(false)
    })
  }

}
