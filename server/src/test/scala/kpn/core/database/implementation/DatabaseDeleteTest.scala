package kpn.core.database.implementation

import java.util.UUID

import kpn.core.TestObjects
import kpn.core.database.Database
import kpn.core.database.DatabaseImpl
import kpn.core.test.TestSupport.withEnvironment
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class DatabaseDeleteTest extends AnyFunSuite with Matchers with TestObjects {

  test("delete") {
    val databaseName = s"test-db-${UUID.randomUUID().toString}"
    withEnvironment { (couchConfig, objectMapper) =>
      val database: Database = new DatabaseImpl(DatabaseContext(couchConfig, objectMapper, databaseName))
      database.create()
      database.exists should equal(true)
      database.delete()
      database.exists should equal(false)
    }
  }

  test("delete - database does not exist") {
    withEnvironment { (couchConfig, objectMapper) =>
      val database: Database = new DatabaseImpl(DatabaseContext(couchConfig, objectMapper, "bla"))
      database.exists should equal(false)
      try {
        database.delete()
        fail("Expected exception not thrown")
      }
      catch {
        case e: IllegalStateException =>
          e.getMessage should include("Could not delete database")
          e.getMessage should include("(database does not exist?)")
      }
    }
  }

  test("delete - wrong password") {
    withEnvironment { (couchConfig, objectMapper) =>
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
            e.getMessage should include("Could not delete database")
            e.getMessage should include("(invalid user/password?)")
        }
      }
      finally {
        originalDatabase.delete()
      }
    }
  }
}
