package kpn.tools.code.codecs

import kpn.api.custom.Tag
import org.bson.BsonReader
import org.bson.BsonType
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bson.codecs.configuration.CodecRegistry

class TagCodec(registry: CodecRegistry) extends Codec[Tag] {
  private val stringCodec = registry.get(classOf[String])

  override def decode(bsonReader: BsonReader, decoderContext: DecoderContext): Tag = {
    bsonReader.readStartDocument()

    var key: String = ""
    var value: String = ""

    while (bsonReader.readBsonType != BsonType.END_OF_DOCUMENT) {
      val fieldName = bsonReader.readName
      if (fieldName == "key") {
        key = stringCodec.decode(bsonReader, decoderContext)
      }
      else if (fieldName == "value") {
        value = stringCodec.decode(bsonReader, decoderContext)
      } else {
        throw new IllegalArgumentException(s"unknown fieldName $fieldName")
      }
    }
    bsonReader.readEndDocument()
    Tag(
      key,
      value,
    )
  }

  override def encode(bsonWriter: BsonWriter, value: Tag, encoderContext: EncoderContext): Unit = {
    bsonWriter.writeStartDocument();
    bsonWriter.writeName("key");
    stringCodec.encode(bsonWriter, value.key, encoderContext);
    bsonWriter.writeName("value");
    stringCodec.encode(bsonWriter, value.value, encoderContext);
    bsonWriter.writeEndDocument();
  }

  override def getEncoderClass: Class[Tag] = {
    classOf[Tag]
  }
}
