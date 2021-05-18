package kpn.core.mongo

import kpn.server.json.Json
import org.bson.BsonReader
import org.bson.BsonWriter
import org.bson.RawBsonDocument
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bson.codecs.RawBsonDocumentCodec

object JacksonCodec {
  private val rawBsonDocumentCodec = new RawBsonDocumentCodec()
}

class JacksonCodec[T](clazz: Class[T]) extends Codec[T] {

  override def getEncoderClass: Class[T] = clazz

  override def decode(reader: BsonReader, decoderContext: DecoderContext): T = {
    val document = JacksonCodec.rawBsonDocumentCodec.decode(reader, decoderContext)
    val json = document.toJson
    Json.objectMapper.readValue(json, clazz)
  }

  override def encode(writer: BsonWriter, value: T, encoderContext: EncoderContext): Unit = {
    val json = Json.objectMapper.writeValueAsString(value)
    val doc = RawBsonDocument.parse(json)
    JacksonCodec.rawBsonDocumentCodec.encode(writer, doc, encoderContext)
  }
}
