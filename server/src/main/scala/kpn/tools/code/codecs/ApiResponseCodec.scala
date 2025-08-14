package kpn.tools.code.codecs

import kpn.api.custom.ApiResponse
import kpn.api.custom.Timestamp
import org.bson.BsonReader
import org.bson.BsonType
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bson.codecs.configuration.CodecRegistry

class ApiResponseCodec[T](registry: CodecRegistry) extends Codec[ApiResponse[T]] {

  private val longCodec = registry.get(classOf[Long])
  private val tCodec = registry.get(???)
  private val timestampCodec = registry.get(classOf[Timestamp])

  override def decode(bsonReader: BsonReader, decoderContext: DecoderContext): ApiResponse[T] = {
    bsonReader.readStartDocument()

    var situationOn: Option[Timestamp] = None
    var version: Long = 0
    var result: Option[T] = None

    while (bsonReader.readBsonType != BsonType.END_OF_DOCUMENT) {
      val fieldName = bsonReader.readName
      if (fieldName == "situationOn") {
        situationOn = Some(timestampCodec.decode(bsonReader, decoderContext))
      }
      else if (fieldName == "version") {
        version = longCodec.decode(bsonReader, decoderContext)
      }
      else if (fieldName == "result") {
        result = Some(tCodec.decode(bsonReader, decoderContext))
      }
      else {
        Codecs.log.warn(s"Unknown field name: $fieldName in ApiResponseCodec.decode()")
        bsonReader.skipValue()
      }
    }

    bsonReader.readEndDocument()

    ApiResponse(
      situationOn,
      version,
      result,
    )
  }

  override def encode(bsonWriter: BsonWriter, value: ApiResponse[T], encoderContext: EncoderContext): Unit = {
    bsonWriter.writeStartDocument()

    if (value.situationOn.isDefined) {
      bsonWriter.writeName("situationOn")
      timestampCodec.encode(bsonWriter, value.situationOn.get, encoderContext)
    }

    bsonWriter.writeName("version")
    longCodec.encode(bsonWriter, value.version, encoderContext)

    if (value.result.isDefined) {
      bsonWriter.writeName("result")
      tCodec.encode(bsonWriter, ??? /*value.result.get*/ , encoderContext)
    }

    bsonWriter.writeEndDocument()
  }

  override def getEncoderClass: Class[ApiResponse[T]] = {
    classOf[ApiResponse[T]]
  }
}
