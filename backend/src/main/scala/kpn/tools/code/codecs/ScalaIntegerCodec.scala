package kpn.tools.code.codecs

import org.bson.BsonReader
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bson.codecs.IntegerCodec

class ScalaIntegerCodec extends Codec[java.lang.Integer] {
  private val integerCodec = new IntegerCodec()

  override def decode(bsonReader: BsonReader, decoderContext: DecoderContext): java.lang.Integer = {
    integerCodec.decode(bsonReader, decoderContext)
  }

  override def encode(bsonWriter: BsonWriter, value: java.lang.Integer, encoderContext: EncoderContext): Unit = {
    integerCodec.encode(bsonWriter, value, encoderContext)
  }

  override def getEncoderClass: Class[java.lang.Integer] = {
    java.lang.Integer.TYPE
  }
}
