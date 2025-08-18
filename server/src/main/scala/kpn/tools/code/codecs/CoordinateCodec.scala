package kpn.tools.code.codecs

import org.bson.BsonReader
import org.bson.BsonType
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bson.codecs.configuration.CodecRegistry
import org.locationtech.jts.geom.Coordinate

class CoordinateCodec(registry: CodecRegistry) extends Codec[Coordinate] {
  private val doubleCodec = registry.get(classOf[Double])

  override def decode(bsonReader: BsonReader, decoderContext: DecoderContext): Coordinate = {
    bsonReader.readStartDocument()

    var x: Double = 0
    var y: Double = 0

    while (bsonReader.readBsonType != BsonType.END_OF_DOCUMENT) {
      val fieldName = bsonReader.readName
      if (fieldName == "x") {
        x = doubleCodec.decode(bsonReader, decoderContext)
      }
      else if (fieldName == "y") {
        y = doubleCodec.decode(bsonReader, decoderContext)
      }
      else {
        bsonReader.skipValue()
      }
    }

    bsonReader.readEndDocument()

    new Coordinate(x, y)
  }

  override def encode(bsonWriter: BsonWriter, value: Coordinate, encoderContext: EncoderContext): Unit = {
    bsonWriter.writeStartDocument()

    bsonWriter.writeName("x")
    doubleCodec.encode(bsonWriter, value.x, encoderContext)

    bsonWriter.writeName("y")
    doubleCodec.encode(bsonWriter, value.y, encoderContext)

    bsonWriter.writeEndDocument()
  }

  override def getEncoderClass: Class[Coordinate] = {
    classOf[Coordinate]
  }
}
