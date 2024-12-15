package kpn.core.test

import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.json.Json
import org.mongodb.scala.MongoClient
import org.scalatest.exceptions.StackDepthException
import org.scalatest.exceptions.TestFailedException

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

  def assertEqual(object1: Object, object2: Object): Unit = {
    if (object1 != object2) {
      val json1 = jsonWriter.writeValueAsString(object1)
      val json2 = jsonWriter.writeValueAsString(object2)
      throw new TestFailedException(
        (e: StackDepthException) => Some(s"""$json1 did not equal $json2"""),
        None,
        (e: StackDepthException) => 1, // stack depth at which assertXx() is called, used to provide source line of assert in failure message
      )
    }
  }
}
