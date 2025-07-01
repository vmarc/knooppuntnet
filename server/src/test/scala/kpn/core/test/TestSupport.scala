package kpn.core.test

import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.json.Json
import org.mongodb.scala.MongoClient

import java.util.concurrent.atomic.AtomicInteger

object TestSupport {

  private val count = new AtomicInteger(0)
  private val jsonWriter = Json.objectMapper.writerWithDefaultPrettyPrinter()

  def newDatabase: (MongoClient, Database) = {
    val databaseName = s"unit-testdb-${count.incrementAndGet()}"
    val mongoClient = MongoClient()
    val database = Mongo.database(mongoClient, databaseName)
    database.dropDatabase()
    (mongoClient, database)
  }
}
