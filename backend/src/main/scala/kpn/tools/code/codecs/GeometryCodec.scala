package kpn.tools.code.codecs

import org.bson.BsonReader
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bson.codecs.configuration.CodecRegistry
import org.bson.json.JsonWriter
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.io.geojson.GeoJsonWriter

class GeometryCodec(registry: CodecRegistry) extends Codec[Geometry] {

  private val stringCodec = registry.get(classOf[String])

  override def decode(bsonReader: BsonReader, decoderContext: DecoderContext): Geometry = {
    ???
  }

  override def encode(bsonWriter: BsonWriter, value: Geometry, encoderContext: EncoderContext): Unit = {
    val json = new GeoJsonWriter().write(value)
    val jsonWriter: JsonWriter = bsonWriter.asInstanceOf[JsonWriter]
    jsonWriter.getWriter.write(json)
  }

  override def getEncoderClass: Class[Geometry] = {
    classOf[Geometry]
  }
}
