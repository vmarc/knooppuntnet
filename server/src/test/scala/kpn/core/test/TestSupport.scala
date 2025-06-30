package kpn.core.test

import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.json.Json
import org.mongodb.scala.MongoClient

import java.util.concurrent.atomic.AtomicInteger

object TestSupport {

  private val count = new AtomicInteger(0)
  private val jsonWriter = Json.objectMapper.writerWithDefaultPrettyPrinter()

  /**
   * Perform given function with a freshly created database. The database is deleted
   * afterwards.
   */
  def withDatabase(f: Database => Unit): Unit = {
    withDatabase()(f)
  }

  /**
   * Perform given function with a freshly created database.
   */
  def withDatabase(keepDatabaseAfterTest: Boolean = false)(f: Database => Unit): Unit = {

    val databaseName = s"unit-testdb-${count.incrementAndGet()}"
    val mongoClient = MongoClient()
    try {
      val database = Mongo.database(mongoClient, databaseName)
      database.dropDatabase()
      try {
        f(database)
      } finally {
        if (!keepDatabaseAfterTest) {
          database.dropDatabase()
        }
      }
    }
    finally {
      mongoClient.close()
    }
  }

  def newDatabase: (MongoClient, Database) = {
    val databaseName = s"unit-testdb-${count.incrementAndGet()}"
    val mongoClient = MongoClient()
    val database = Mongo.database(mongoClient, databaseName)
    database.dropDatabase()
    (mongoClient, database)
  }
}
