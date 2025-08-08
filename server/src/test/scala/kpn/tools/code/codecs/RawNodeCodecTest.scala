package kpn.tools.code.codecs

import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClients
import kpn.api.common.data.raw.RawNode
import kpn.api.custom.Tag
import kpn.api.custom.Timestamp
import kpn.core.util.UnitTest
import kpn.tools.code.codecs.generated._Provider
import org.bson.codecs.StringCodec
import org.bson.codecs.configuration.CodecRegistries

import scala.collection.convert.ImplicitConversions.`iterable AsScalaIterable`

class RawNodeCodecTest extends UnitTest {

  test("write and read RawNode to mongo") {
    val uri = "mongodb://localhost:27017"
    try {
      val mongoClient = MongoClients.create(uri)
      try {
        val codecRegistry = CodecRegistries.fromRegistries(
          CodecRegistries.fromCodecs(
            new StringCodec(),
          ),
          CodecRegistries.fromProviders(new _Provider),
          MongoClientSettings.getDefaultCodecRegistry
        )
        val database = mongoClient.getDatabase("test").withCodecRegistry(codecRegistry)
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

        val nodes = collection.find().toSeq

        println(nodes)
      } finally {
        if (mongoClient != null) {
          mongoClient.close()
        }
      }
    }
  }
}
