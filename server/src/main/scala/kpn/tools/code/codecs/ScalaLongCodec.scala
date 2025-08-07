package kpn.tools.code.codecs

import org.bson.BsonReader
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bson.codecs.configuration.CodecRegistry

class ScalaLongCodec(registry: CodecRegistry) extends Codec[Long] {
  private val stringCodec = registry.get(classOf[String])

  override def decode(bsonReader: BsonReader, decoderContext: DecoderContext): Long = {
    bsonReader.readInt64()
  }

  override def encode(bsonWriter: BsonWriter, value: Long, encoderContext: EncoderContext): Unit = {
    bsonWriter.writeInt64(value)
  }

  override def getEncoderClass: Class[Long] = {
    classOf[Long]
  }
}
