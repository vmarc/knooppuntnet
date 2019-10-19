package kpn.core.db.couch

import kpn.core.test.TestSupport.withEnvironment
import org.scalatest.FunSuite
import org.scalatest.Matchers

class DatabaseTest extends FunSuite with Matchers {

  test("exists") {

    withEnvironment((couchConfig, objectMapper) => {
      val database: Database = new DatabaseImpl(couchConfig, objectMapper, "bla")
      database.exists should equal(false)
    })

    // TODO create database

    withEnvironment((couchConfig, objectMapper) => {
      val database: Database = new DatabaseImpl(couchConfig, objectMapper, "unit-testdb-91")
      database.exists should equal(true)
    })

    // TODO delete database
  }

}
