package kpn.tools.code.codecs

import org.bson.BsonReader
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.DoubleCodec
import org.bson.codecs.EncoderContext

class ScalaDoubleCodec extends Codec[java.lang.Double] {
  private val doubleCodec = new DoubleCodec()

  override def decode(bsonReader: BsonReader, decoderContext: DecoderContext): java.lang.Double = {
    doubleCodec.decode(bsonReader, decoderContext)
  }

  override def encode(bsonWriter: BsonWriter, value: java.lang.Double, encoderContext: EncoderContext): Unit = {
    doubleCodec.encode(bsonWriter, value, encoderContext)
  }

  override def getEncoderClass: Class[java.lang.Double] = {
    java.lang.Double.TYPE
  }
}
