package kpn.server.config

import kpn.api.custom.ApiResponse
import kpn.api.custom.Timestamp
import kpn.core.util.Log
import kpn.database.util.Mongo
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bson.json.JsonReader
import org.bson.json.JsonWriter
import org.springframework.http.converter.json.AbstractJsonHttpMessageConverter

import java.io.Reader
import java.io.Writer
import java.lang.reflect.Type

class BsonHttpMessageConverter extends AbstractJsonHttpMessageConverter {

  private val log = Log(classOf[BsonHttpMessageConverter])

  private val DefaultEncoderContext = EncoderContext.builder.build
  private val DefaultDecoderContext = DecoderContext.builder.build

  private val longCodec = Mongo.codecRegistry.get(classOf[Long])
  private val timestampCodec = Mongo.codecRegistry.get(classOf[Timestamp])

  override def readInternal(resolvedType: Type, reader: Reader): AnyRef = {
    val bsonReader = new JsonReader(reader)
    val clazz = resolvedType match {
      case c: Class[_] => c
      case _ => throw new IllegalArgumentException(s"Expected Class type but got: $resolvedType")
    }

    if (clazz == classOf[ApiResponse[_]]) {
      throw new UnsupportedOperationException("ApiResponse decoding not supported")
    }

    val codec = Mongo.codecRegistry.get(clazz)
    codec.decode(bsonReader, DefaultDecoderContext).asInstanceOf[AnyRef]
  }

  override def writeInternal(instance: Any, objectType: Type, writer: Writer): Unit = {
    val bsonWriter = new JsonWriter(writer)

    if (objectType.getTypeName.startsWith("kpn.api.custom.ApiResponse")) {
      val apiResponse = instance.asInstanceOf[ApiResponse[Any]]
      encodeApiResponse(bsonWriter, apiResponse, DefaultEncoderContext)
    }
    else if (objectType.getTypeName.startsWith("java.lang.String")) {
      bsonWriter.writeString(instance.toString)
    }
    else {
      val clazz: Class[Any] = objectType match {
        case c: Class[_] => c.asInstanceOf[Class[Any]]
        case _ =>
          throw new IllegalArgumentException(s"Expected Class type but got: $objectType")
      }
      val codec: Codec[Any] = Mongo.codecRegistry.get(clazz)
      codec.encode(bsonWriter, instance, DefaultEncoderContext)
    }
  }

  private def encodeApiResponse(bsonWriter: BsonWriter, apiResponse: ApiResponse[Any], encoderContext: EncoderContext): Unit = {

    bsonWriter.writeStartDocument()

    if (apiResponse.situationOn.isDefined) {
      bsonWriter.writeName("situationOn")
      timestampCodec.encode(bsonWriter, apiResponse.situationOn.get, encoderContext)
    }

    bsonWriter.writeName("version")
    longCodec.encode(bsonWriter, apiResponse.version, encoderContext)

    apiResponse.result match {
      case None =>
      case Some(result) =>
        val resultClass = result.getClass.asInstanceOf[Class[Any]]
        val codec = Mongo.codecRegistry.get(resultClass)
        bsonWriter.writeName("result")
        codec.encode(bsonWriter, result, encoderContext)
    }

    bsonWriter.writeEndDocument()
  }
}
