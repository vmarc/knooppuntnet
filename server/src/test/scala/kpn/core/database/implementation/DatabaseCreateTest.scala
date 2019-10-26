package kpn.core.database.implementation

import java.util.UUID

import kpn.core.TestObjects
import kpn.core.database.{Database, DatabaseImpl}
import kpn.core.test.TestSupport.{withDatabase, withEnvironment}
import org.scalatest.{FunSuite, Matchers}

class DatabaseCreateTest extends FunSuite with Matchers with TestObjects {

  test("create") {
    withDatabase(database => {
      database.exists should equal(true)
    })
  }

  test("create - database already exists") {

    val databaseName = s"test-db-${UUID.randomUUID().toString}"

    withEnvironment((tempCouch, couchConfig, objectMapper) => {
      val database: Database = new DatabaseImpl(DatabaseContext(tempCouch, couchConfig, objectMapper, databaseName))
      database.exists should equal(false)
      database.create()
      database.exists should equal(true)
      try {
        database.create()
        fail("Expected exception not thrown")
      }
      catch {
        case e: IllegalStateException =>
          e.getMessage should include("Could not create database")
          e.getMessage should include("(already exists?)")
      }
      finally {
        database.delete()
      }
    })
  }

  test("create - invalid authorization") {

    val databaseName = s"test-db-${UUID.randomUUID().toString}"

    withEnvironment((tempCouch, couchConfig, objectMapper) => {
      val invalidCouchConfig = couchConfig.copy(password = "wrong-password")
      val database: Database = new DatabaseImpl(DatabaseContext(tempCouch, invalidCouchConfig, objectMapper, databaseName))
      database.exists should equal(false)
      try {
        database.create()
        fail("Expected exception not thrown")
      }
      catch {
        case e: IllegalStateException =>
          e.getMessage should include("Could not create database")
          e.getMessage should include("(invalid user/password?)")
      }
    })
  }
}
