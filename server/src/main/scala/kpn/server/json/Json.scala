package kpn.server.json

import kpn.database.util.Mongo
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bson.json.JsonReader
import org.bson.json.JsonWriter
import org.bson.json.JsonWriterSettings

import java.io.Reader
import java.io.StringReader
import java.io.StringWriter

object Json {

  def pretty(o: Object): String = {
    encode(o, pretty = true)
  }

  def string(o: Object): String = {
    encode(o)
  }

  private def encode(o: Object, pretty: Boolean = false): String = {
    val settings = JsonWriterSettings
      .builder
      .indent(pretty)
      .build
    val stringWriter = new StringWriter()
    val jsonWriter = new JsonWriter(stringWriter, settings)
    val codec = Mongo.codecRegistry.get(o.getClass.asInstanceOf[Class[Any]])
    codec.encode(jsonWriter, o, EncoderContext.builder.build)
    jsonWriter.close()
    stringWriter.toString
  }

  def readValue[T](string: String, valueType: Class[T]): T = {
    val reader = new StringReader(string)
    try {
      val jsonReader = new JsonReader(reader)
      val codec = Mongo.codecRegistry.get(valueType /*o.getClass.asInstanceOf[Class[Any]]*/)
      codec.decode(jsonReader, DecoderContext.builder.build)
    }
    finally {
      reader.close()
    }
  }

  def readValue[T](reader: Reader, valueType: Class[T]): T = {
    val jsonReader = new JsonReader(reader)
    val codec = Mongo.codecRegistry.get(valueType /*o.getClass.asInstanceOf[Class[Any]]*/)
    codec.decode(jsonReader, DecoderContext.builder.build)
  }
}
