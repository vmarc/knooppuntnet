package kpn.tools.code.codecs

import kpn.server.analyzer.engine.tiles.domain.CoordinateArray
import org.bson.BsonReader
import org.bson.BsonType
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bson.codecs.configuration.CodecRegistry
import org.locationtech.jts.geom.Coordinate

class CoordinateArrayCodec(registry: CodecRegistry) extends Codec[CoordinateArray] {

  private val doubleCodec = registry.get(classOf[Double])

  override def decode(bsonReader: BsonReader, decoderContext: DecoderContext): CoordinateArray = {
    val valueBuffer = scala.collection.mutable.Buffer[Coordinate]()

    bsonReader.readStartArray()
    while (bsonReader.readBsonType != BsonType.END_OF_DOCUMENT) {
      bsonReader.readStartArray()
      val x = doubleCodec.decode(bsonReader, decoderContext)
      val y = doubleCodec.decode(bsonReader, decoderContext)
      bsonReader.readEndArray()
      valueBuffer += new Coordinate(x, y)
    }
    bsonReader.readEndArray()

    CoordinateArray(valueBuffer.toArray)


    //    val string = bsonReader.readString()
    //
    //    string.drop(1).dropRight(1).split("")
    //
    //    val valueBuffer = scala.collection.mutable.Buffer[Coordinate]()

    //    while (bsonReader.readBsonType != BsonType.END_OF_DOCUMENT) {
    //      val fieldName = bsonReader.readName
    //      if (fieldName == "coordinates") {
    //        bsonReader.readStartArray()
    //        while (bsonReader.readBsonType != BsonType.END_OF_DOCUMENT) {
    //          valueBuffer += coordinateCodec.decode(bsonReader, decoderContext)
    //        }
    //        bsonReader.readEndArray()
    //        coordinates = valueBuffer.toArray
    //      }
    //      else {
    //        Codecs.log.warn(s"Unknown field name: $fieldName in CoordinateArrayCodec.decode()")
    //        bsonReader.skipValue()
    //      }
    //    }
    //
    //    bsonReader.readEndDocument()
    //
    //    CoordinateArray(
    //      valueBuffer.toArray,
    //    )
  }

  override def encode(bsonWriter: BsonWriter, value: CoordinateArray, encoderContext: EncoderContext): Unit = {
    val string = value.coordinates.map(c => s"[${c.x},${c.y}]").mkString("[", ",", "]")
    bsonWriter.writeString(string)
    //    bsonWriter.writeStartArray()
    //    value.coordinates.foreach { coordinate =>
    //      bsonWriter.writeStartArray()
    //      doubleCodec.encode(bsonWriter, coordinate.x, encoderContext)
    //      doubleCodec.encode(bsonWriter, coordinate.y, encoderContext)
    //      bsonWriter.writeEndArray()
    //    }
    //    bsonWriter.writeEndArray()
  }

  override def getEncoderClass: Class[CoordinateArray] = {
    classOf[CoordinateArray]
  }
}
