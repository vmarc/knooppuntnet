package kpn.tools.code.codecs

import com.mongodb.client.MongoClients
import kpn.api.common.data.raw.RawNode
import kpn.api.custom.Tag
import kpn.api.custom.Timestamp
import kpn.core.util.UnitTest
import kpn.database.actions.statistics.ChangeSetCount2
import kpn.database.util.Mongo

import scala.jdk.CollectionConverters.IterableHasAsScala

class RawNodeCodecTest extends UnitTest {

  test("write and read ChangeSetCount2 to mongo") {
    val uri = "mongodb://localhost:27017"
    try {
      val mongoClient = MongoClients.create(uri)
      try {
        val database = mongoClient.getDatabase("test").withCodecRegistry(Mongo.codecRegistry)
        val collection = database.getCollection("counts", classOf[ChangeSetCount2])

        val value = ChangeSetCount2(
          year = 11,
          month = 22,
          day = 33,
          impact = 44,
          total = 55
        )

        collection.insertOne(value)

        val values = collection.find().asScala.toSeq

        println(values)
      } finally {
        if (mongoClient != null) {
          mongoClient.close()
        }
      }
    }
  }

  test("write and read RawNode to mongo") {
    val uri = "mongodb://localhost:27017"
    try {
      val mongoClient = MongoClients.create(uri)
      try {
        val database = mongoClient.getDatabase("test").withCodecRegistry(Mongo.codecRegistry)
        val collection = database.getCollection("raw-nodes", classOf[RawNode])

        val node = RawNode(
          id = 123,
          latitude = "1111",
          longitude = "2222",
          version = 3,
          timestamp = Timestamp(2025, 8, 11, 12, 30, 5),
          changeSetId = 5,
          tags = Seq(
            Tag("key1", "value1"),
            Tag("key2", "value2")
          )
        )

        collection.insertOne(node)

        val nodes = collection.find().asScala.toSeq

        println(nodes)
      } finally {
        if (mongoClient != null) {
          mongoClient.close()
        }
      }
    }
  }
}
