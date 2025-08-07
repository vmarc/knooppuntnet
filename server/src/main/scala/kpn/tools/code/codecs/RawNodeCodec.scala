package kpn.tools.code.codecs

import kpn.api.common.data.raw.RawNode
import kpn.api.custom.Tag
import kpn.api.custom.Timestamp
import org.bson.BsonReader
import org.bson.BsonType
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bson.codecs.configuration.CodecRegistry

class RawNodeCodec(registry: CodecRegistry) extends Codec[RawNode] {

  private val stringCodec = registry.get(classOf[String])
  private val timestampCodec = registry.get(classOf[Timestamp])
  private val tagCodec = registry.get(classOf[Tag])
  private val longCodec = registry.get(classOf[Long])

  override def decode(bsonReader: BsonReader, decoderContext: DecoderContext): RawNode = {
    bsonReader.readStartDocument()

    var id: Long = 0
    var latitude: String = ""
    var longitude: String = ""
    var version: Long = 0
    var timestamp: Timestamp = null
    var changeSetId: Long = 0
    var tags: Seq[Tag] = Seq.empty

    while (bsonReader.readBsonType != BsonType.END_OF_DOCUMENT) {
      val fieldName = bsonReader.readName
      if (fieldName == "id") {
        id = longCodec.decode(bsonReader, decoderContext)
      }
      else if (fieldName == "lattitude") {
        latitude = stringCodec.decode(bsonReader, decoderContext)
      }
      else if (fieldName == "longitude") {
        longitude = stringCodec.decode(bsonReader, decoderContext)
      }
      else if (fieldName == "version") {
        version = longCodec.decode(bsonReader, decoderContext)
      }
      else if (fieldName == "timestamp") {
        timestamp = timestampCodec.decode(bsonReader, decoderContext)
      }
      else if (fieldName == "changeSetId") {
        changeSetId = longCodec.decode(bsonReader, decoderContext)
      }
      else if (fieldName == "tags") {
        bsonReader.readStartArray()
        val tagBuffer = scala.collection.mutable.Buffer[Tag]()
        while (bsonReader.readBsonType != BsonType.END_OF_DOCUMENT) {
          tagBuffer += tagCodec.decode(bsonReader, decoderContext)
        }
        bsonReader.readEndArray()
        tags = tagBuffer.toSeq
      } else {
        //throw new IllegalArgumentException(s"unknown fieldName $fieldName")
        bsonReader.skipValue()
      }
    }
    bsonReader.readEndDocument()
    RawNode(
      id,
      latitude,
      longitude,
      version,
      timestamp,
      changeSetId,
      tags,
    )
  }

  override def encode(bsonWriter: BsonWriter, value: RawNode, encoderContext: EncoderContext): Unit = {
    bsonWriter.writeStartDocument()

    bsonWriter.writeName("id")
    longCodec.encode(bsonWriter, value.id, encoderContext)

    bsonWriter.writeName("latitude")
    stringCodec.encode(bsonWriter, value.latitude, encoderContext)

    bsonWriter.writeName("longitude")
    stringCodec.encode(bsonWriter, value.longitude, encoderContext)

    bsonWriter.writeName("version")
    longCodec.encode(bsonWriter, value.version, encoderContext)

    bsonWriter.writeName("timestamp")
    timestampCodec.encode(bsonWriter, value.timestamp, encoderContext)

    bsonWriter.writeName("changeSetId")
    longCodec.encode(bsonWriter, value.changeSetId, encoderContext)

    bsonWriter.writeName("tags")
    bsonWriter.writeStartArray()
    value.tags.foreach(tag => tagCodec.encode(bsonWriter, tag, encoderContext))
    bsonWriter.writeEndArray()

    bsonWriter.writeEndDocument()
  }

  override def getEncoderClass: Class[RawNode] = {
    classOf[RawNode]
  }
}
