package kpn.tools.code.codecs

import kpn.api.custom.Timestamp
import kpn.api.time.TimestampUtil
import org.bson.BsonReader
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bson.codecs.configuration.CodecRegistry

class TimestampCodec(registry: CodecRegistry) extends Codec[Timestamp] {
  private val stringCodec = registry.get(classOf[String])

  override def decode(bsonReader: BsonReader, decoderContext: DecoderContext): Timestamp = {
    val value = bsonReader.readString()
    TimestampUtil.parseIso(value)
  }

  override def encode(bsonWriter: BsonWriter, value: Timestamp, encoderContext: EncoderContext): Unit = {
    bsonWriter.writeString(value.iso)
  }

  override def getEncoderClass: Class[Timestamp] = {
    classOf[Timestamp]
  }
}
