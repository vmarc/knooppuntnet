package kpn.tools.code.codecs

import kpn.api.custom.Day
import org.bson.BsonReader
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bson.codecs.configuration.CodecRegistry

class DayCodec(registry: CodecRegistry) extends Codec[Day] {

  private val intCodec = registry.get(classOf[Int])

  override def decode(bsonReader: BsonReader, decoderContext: DecoderContext): Day = {
    Day.fromString(bsonReader.readString()).get
  }

  override def encode(bsonWriter: BsonWriter, value: Day, encoderContext: EncoderContext): Unit = {
    bsonWriter.writeString(value.yyyymmdd)
  }

  override def getEncoderClass: Class[Day] = {
    classOf[Day]
  }
}
