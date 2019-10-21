package kpn.core.db.couch

import java.util.UUID

import kpn.core.TestObjects
import kpn.core.test.TestSupport.withDatabase
import kpn.core.test.TestSupport.withEnvironment
import org.scalatest.FunSuite
import org.scalatest.Matchers

class DatabaseCreateTest extends FunSuite with Matchers with TestObjects {

  test("create") {
    withDatabase(database => {
      database.exists should equal(true)
    })
  }

  test("create - database already exists") {

    val databaseName = s"test-db-${UUID.randomUUID().toString}"

    withEnvironment((couchConfig, objectMapper) => {
      val database: Database = new DatabaseImpl(DatabaseContext(couchConfig, objectMapper, databaseName))
      database.exists should equal(false)
      database.create()
      database.exists should equal(true)
      try {
        database.create()
        fail("Expected exception not thrown")
      }
      catch {
        case e: IllegalStateException =>
          e.getMessage.contains("Could not create database") should equal(true)
          e.getMessage.contains("(already exists?)") should equal(true)
      }
      finally {
        database.delete()
      }
    })
  }

  test("create - invalid authorization") {

    val databaseName = s"test-db-${UUID.randomUUID().toString}"

    withEnvironment((couchConfig, objectMapper) => {
      val invalidCouchConfig = couchConfig.copy(password = "wrong-password")
      val database: Database = new DatabaseImpl(DatabaseContext(invalidCouchConfig, objectMapper, "databaseName"))
      database.exists should equal(false)
      try {
        database.create()
        fail("Expected exception not thrown")
      }
      catch {
        case e: IllegalStateException =>
          e.getMessage.contains("Could not create database") should equal(true)
          e.getMessage.contains("(invalid user/password?)") should equal(true)
      }
    })
  }
}
