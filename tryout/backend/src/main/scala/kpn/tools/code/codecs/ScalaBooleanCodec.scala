package kpn.tools.code.codecs

import org.bson.BsonReader
import org.bson.BsonWriter
import org.bson.codecs.BooleanCodec
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext

class ScalaBooleanCodec extends Codec[java.lang.Boolean] {
  private val booleanCodec = new BooleanCodec()

  override def decode(bsonReader: BsonReader, decoderContext: DecoderContext): java.lang.Boolean = {
    booleanCodec.decode(bsonReader, decoderContext)
  }

  override def encode(bsonWriter: BsonWriter, value: java.lang.Boolean, encoderContext: EncoderContext): Unit = {
    booleanCodec.encode(bsonWriter, value, encoderContext)
  }

  override def getEncoderClass: Class[java.lang.Boolean] = {
    java.lang.Boolean.TYPE
  }
}
