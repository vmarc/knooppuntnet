package kpn.tools.code.codecs

import org.bson.BsonReader
import org.bson.BsonType
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bson.codecs.configuration.CodecRegistry

class TestObjectCodec(registry: CodecRegistry) extends Codec[TestObject[_]] {

  override def decode(bsonReader: BsonReader, decoderContext: DecoderContext): TestObject[_] = {

    var obj: Object = null

    bsonReader.readStartDocument()
    while (bsonReader.readBsonType != BsonType.END_OF_DOCUMENT) {
      val fieldName = bsonReader.readName
      if (fieldName == "obj") {
        val tt = bsonReader.readBsonType()

        println()
        //      id = longCodec.decode(bsonReader, decoderContext)
      }
    }
    //doubleCodec.decode(bsonReader, decoderContext)
    bsonReader.readEndDocument()

    TestObject(
      obj
    )
  }

  override def encode(bsonWriter: BsonWriter, value: TestObject[_], encoderContext: EncoderContext): Unit = {
    bsonWriter.writeStartDocument()
    bsonWriter.writeName("obj")
    val clazz: Class[Any] = value.obj.getClass.asInstanceOf[Class[Any]]
    val codec = registry.get(clazz)
    codec.encode(bsonWriter, value.obj, encoderContext)
    bsonWriter.writeEndDocument()
  }

  override def getEncoderClass: Class[TestObject[_]] = {
    classOf[TestObject[_]]
  }
}
