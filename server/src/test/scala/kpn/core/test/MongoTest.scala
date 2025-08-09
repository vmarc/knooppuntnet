package kpn.core.test

import com.mongodb.client.MongoClient
import kpn.core.common.Time
import kpn.core.util.UnitTest
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.json.Json
import org.scalatest.BeforeAndAfterEach

import java.util.concurrent.atomic.AtomicInteger

object MongoTest {
  private val count = new AtomicInteger(0)
  private val jsonWriter = Json.objectMapper.writerWithDefaultPrettyPrinter()
}

abstract class MongoTest extends UnitTest with BeforeAndAfterEach {

  private var _mongoClient: MongoClient = _
  private var _database: Database = _

  override def beforeEach(): Unit = {
    val databaseName = s"unit-testdb-${MongoTest.count.incrementAndGet()}"
    _mongoClient = Mongo.client
    _database = Mongo.database(_mongoClient, databaseName)
    database.dropDatabase()
  }

  override def afterEach(): Unit = {
    _database.dropDatabase()
    _mongoClient.close()
    Time.clear()
  }

  def database: Database = _database
}
