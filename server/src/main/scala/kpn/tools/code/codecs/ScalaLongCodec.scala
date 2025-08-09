package kpn.tools.code.codecs

import org.bson.BsonReader
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bson.codecs.LongCodec

class ScalaLongCodec extends Codec[java.lang.Long] {
  private val longCodec = new LongCodec()

  override def decode(bsonReader: BsonReader, decoderContext: DecoderContext): java.lang.Long = {
    longCodec.decode(bsonReader, decoderContext)
  }

  override def encode(bsonWriter: BsonWriter, value: java.lang.Long, encoderContext: EncoderContext): Unit = {
    longCodec.encode(bsonWriter, value, encoderContext)
  }

  override def getEncoderClass: Class[java.lang.Long] = {
    java.lang.Long.TYPE
  }
}
