package kpn.server.json

import kpn.database.util.Mongo
import org.bson.AbstractBsonWriter.State
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bson.json.JsonReader
import org.bson.json.JsonWriter
import org.bson.json.JsonWriterSettings

import java.io.Reader
import java.io.StringReader
import java.io.StringWriter
import scala.jdk.CollectionConverters.IterableHasAsJava

object Json {

  def pretty(o: Object): String = {
    encode(o, pretty = true)
  }

  def string(o: Object): String = {
    encode(o)
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

  private def encode(o: Object, pretty: Boolean = false): String = {
    val stringWriter = new StringWriter()
    o match {
      case Nil =>
        encodeEmptySeq(pretty, stringWriter)
      case seq: Seq[_] =>
        encodeSeq(pretty, stringWriter, seq)
      case _ =>
        encodeObject(o, pretty, stringWriter)
    }
    stringWriter.toString
  }

  private def encodeObject(o: Object, pretty: Boolean, stringWriter: StringWriter): Unit = {
    val jsonWriter = buildJsonWriter(pretty, stringWriter)
    val codec = Mongo.codecRegistry.get(o.getClass.asInstanceOf[Class[Any]])
    codec.encode(jsonWriter, o, EncoderContext.builder.build)
    jsonWriter.close()
  }

  private def encodeSeq(pretty: Boolean, stringWriter: StringWriter, seq: Seq[_]): Unit = {
    val jsonWriter = buildValueJsonWriter(pretty, stringWriter)
    val javaList = seq.asJava
    val codec = Mongo.codecRegistry.get(javaList.getClass.asInstanceOf[Class[Any]])
    codec.encode(jsonWriter, javaList, EncoderContext.builder.build)
    jsonWriter.close()
  }

  private def encodeEmptySeq(pretty: Boolean, stringWriter: StringWriter): Unit = {
    val jsonWriter: JsonWriter = buildValueJsonWriter(pretty, stringWriter)
    jsonWriter.writeStartArray()
    jsonWriter.writeEndArray()
    jsonWriter.close()
  }

  private def buildValueJsonWriter(pretty: Boolean, stringWriter: StringWriter): JsonWriter = {
    val settings: JsonWriterSettings = buildJsonWriterSettings(pretty)
    new JsonWriter(stringWriter, settings) {
      setState(State.VALUE)
    }
  }

  private def buildJsonWriter(pretty: Boolean, stringWriter: StringWriter): JsonWriter = {
    val settings = buildJsonWriterSettings(pretty)
    new JsonWriter(stringWriter, settings)
  }

  private def buildJsonWriterSettings(pretty: Boolean) = {
    JsonWriterSettings
      .builder
      .indent(pretty)
      .build
  }
}
