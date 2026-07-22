package kpn.tools.code.codecs

import kpn.core.tools.translations.PoeTranslations
import org.bson.BsonReader
import org.bson.BsonType
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bson.codecs.configuration.CodecRegistry

class PoeTranslationsCodec(registry: CodecRegistry) extends Codec[PoeTranslations] {

  private val stringCodec = registry.get(classOf[String])

  override def decode(bsonReader: BsonReader, decoderContext: DecoderContext): PoeTranslations = {
    bsonReader.readStartDocument()

    val mapBuilder = Map.newBuilder[String, String]

    while (bsonReader.readBsonType != BsonType.END_OF_DOCUMENT) {
      val fieldName = bsonReader.readName
      val value = stringCodec.decode(bsonReader, decoderContext)
      mapBuilder.addOne((fieldName, value))
    }

    bsonReader.readEndDocument()

    PoeTranslations(
      mapBuilder.result()
    )
  }

  override def encode(bsonWriter: BsonWriter, value: PoeTranslations, encoderContext: EncoderContext): Unit = {
    bsonWriter.writeStartDocument()
    val sortedTranslations = value.translations.toSeq.sortBy { case (key, _) => key }
    sortedTranslations.foreach { case (key, translation) =>
      bsonWriter.writeName(key)
      stringCodec.encode(bsonWriter, translation, encoderContext)
    }
    bsonWriter.writeEndDocument()
  }

  override def getEncoderClass: Class[PoeTranslations] = {
    classOf[PoeTranslations]
  }
}
