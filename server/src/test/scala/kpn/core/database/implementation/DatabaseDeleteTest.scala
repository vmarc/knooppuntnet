package kpn.core.database.implementation

import java.util.UUID

import kpn.core.TestObjects
import kpn.core.database.Database
import kpn.core.database.DatabaseImpl
import kpn.core.test.TestSupport.withEnvironment
import kpn.core.util.UnitTest

class DatabaseDeleteTest extends UnitTest with TestObjects {

  test("delete") {
    val databaseName = s"test-db-${UUID.randomUUID().toString}"
    withEnvironment { (couchConfig, objectMapper) =>
      val database = new DatabaseImpl(DatabaseContextImpl(couchConfig, objectMapper, databaseName))
      database.create()
      assert(database.exists)
      database.delete()
      assert(!database.exists)
    }
  }

  test("delete - database does not exist") {
    withEnvironment { (couchConfig, objectMapper) =>
      val database = new DatabaseImpl(DatabaseContextImpl(couchConfig, objectMapper, "bla"))
      assert(!database.exists)
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
      val originalDatabase = new DatabaseImpl(DatabaseContextImpl(couchConfig, objectMapper, databaseName))
      originalDatabase.create()
      try {
        val invalidCouchConfig = couchConfig.copy(password = "wrong-password")
        val database = new DatabaseImpl(DatabaseContextImpl(invalidCouchConfig, objectMapper, databaseName))
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
