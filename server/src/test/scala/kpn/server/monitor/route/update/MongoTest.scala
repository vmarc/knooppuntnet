package kpn.server.monitor.route.update

import kpn.core.common.Time
import kpn.core.test.TestSupport
import kpn.core.util.UnitTest
import kpn.database.base.Database
import org.mongodb.scala.MongoClient
import org.scalatest.BeforeAndAfterEach

import java.util.concurrent.atomic.AtomicInteger

abstract class MongoTest extends UnitTest with BeforeAndAfterEach {

  private val count = new AtomicInteger(0)

  private var _mongoClient: MongoClient = _
  private var _database: Database = _

  override def beforeEach(): Unit = {
    val (client, db) = TestSupport.newDatabase
    _mongoClient = client
    _database = db
  }

  override def afterEach(): Unit = {
    _database.dropDatabase()
    _mongoClient.close()
    Time.clear()
  }

  def database: Database = _database
}
