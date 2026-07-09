package kpn.tools.code.codecs

import kpn.core.tools.translations.Translations
import org.bson.BsonReader
import org.bson.BsonType
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bson.codecs.configuration.CodecRegistry

class TranslationsCodec(registry: CodecRegistry) extends Codec[Translations] {

  private val stringCodec = registry.get(classOf[String])

  override def decode(bsonReader: BsonReader, decoderContext: DecoderContext): Translations = {
    bsonReader.readStartDocument()

    var locale: String = ""
    val mapBuilder = Map.newBuilder[String, String]

    while (bsonReader.readBsonType != BsonType.END_OF_DOCUMENT) {
      val fieldName = bsonReader.readName
      if (fieldName == "locale") {
        locale = stringCodec.decode(bsonReader, decoderContext)
      }
      else if (fieldName == "translations") {
        bsonReader.readStartDocument()
        while (bsonReader.readBsonType != BsonType.END_OF_DOCUMENT) {
          val fieldName = bsonReader.readName
          val value = stringCodec.decode(bsonReader, decoderContext)
          mapBuilder.addOne((fieldName, value))
        }
        bsonReader.readEndDocument()
      }
      else {
        Codecs.log.warn(s"Unknown field name: $fieldName in TranslationsCodec.decode()")
        bsonReader.skipValue()
      }
    }

    bsonReader.readEndDocument()

    Translations(
      locale,
      mapBuilder.result()
    )
  }

  override def encode(bsonWriter: BsonWriter, value: Translations, encoderContext: EncoderContext): Unit = {
    bsonWriter.writeStartDocument()

    bsonWriter.writeName("locale")
    stringCodec.encode(bsonWriter, value.locale, encoderContext)

    bsonWriter.writeName("translations")
    bsonWriter.writeStartDocument()
    val sortedTranslations = value.translations.toSeq.sortBy { case (key, _) => key }
    sortedTranslations.foreach { case (key, translation) =>
      bsonWriter.writeName(key)
      stringCodec.encode(bsonWriter, translation, encoderContext)
    }
    bsonWriter.writeEndDocument()
    bsonWriter.writeEndDocument()
  }

  override def getEncoderClass: Class[Translations] = {
    classOf[Translations]
  }
}
