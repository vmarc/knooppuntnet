package kpn.core.db.couch

import java.util.UUID

import kpn.core.TestObjects
import kpn.core.test.TestSupport.withEnvironment
import org.scalatest.FunSuite
import org.scalatest.Matchers

class DatabaseDeleteTest extends FunSuite with Matchers with TestObjects {

  test("delete") {
    val databaseName = s"test-db-${UUID.randomUUID().toString}"
    withEnvironment((couchConfig, objectMapper) => {
      val database: Database = new DatabaseImpl(DatabaseContext(couchConfig, objectMapper, databaseName))
      database.create()
      database.exists should equal(true)
      database.delete()
      database.exists should equal(false)
    })
  }

  test("delete - database does not exist") {
    withEnvironment((couchConfig, objectMapper) => {
      val database: Database = new DatabaseImpl(DatabaseContext(couchConfig, objectMapper, "bla"))
      database.exists should equal(false)
      try {
        database.delete()
        fail("Expected exception not thrown")
      }
      catch {
        case e: IllegalStateException =>
          e.getMessage.contains("Could not delete database") should equal(true)
          e.getMessage.contains("(database does not exist?)") should equal(true)
      }
    })
  }

  test("delete - wrong password") {
    withEnvironment((couchConfig, objectMapper) => {
      val databaseName = s"test-db-${UUID.randomUUID().toString}"
      val originalDatabase: Database = new DatabaseImpl(DatabaseContext(couchConfig, objectMapper, databaseName))
      originalDatabase.create()
      try {
        val invalidCouchConfig = couchConfig.copy(password = "wrong-password")
        val database: Database = new DatabaseImpl(DatabaseContext(invalidCouchConfig, objectMapper, databaseName))
        database.exists should equal(true)
        try {
          database.delete()
          fail("Expected exception not thrown")
        }
        catch {
          case e: IllegalStateException =>
            e.getMessage.contains("Could not delete database") should equal(true)
            e.getMessage.contains("(invalid user/password?)") should equal(true)
        }
      }
      finally {
        originalDatabase.delete()
      }
    })
  }
}
