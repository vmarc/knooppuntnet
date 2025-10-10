package kpn.tools.code.codecs

import it.unimi.dsi.fastutil.longs.LongArrayList
import it.unimi.dsi.fastutil.longs.LongOpenHashSet
import it.unimi.dsi.fastutil.longs.LongSet
import it.unimi.dsi.fastutil.longs.LongSets
import org.bson.BsonReader
import org.bson.BsonType
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bson.codecs.configuration.CodecRegistry

class LongSetCodec(registry: CodecRegistry) extends Codec[LongSet] {
  private val longCodec = registry.get(classOf[Long])

  override def decode(bsonReader: BsonReader, decoderContext: DecoderContext): LongSet = {
    bsonReader.readStartArray()
    val valueBuffer = new LongArrayList()
    while (bsonReader.readBsonType != BsonType.END_OF_DOCUMENT) {
      valueBuffer.add(longCodec.decode(bsonReader, decoderContext))
    }
    bsonReader.readEndArray()
    LongSets.synchronize(new LongOpenHashSet(valueBuffer))
  }

  override def encode(bsonWriter: BsonWriter, value: LongSet, encoderContext: EncoderContext): Unit = {
    bsonWriter.writeStartArray()
    val i = value.iterator
    while (i.hasNext) {
      longCodec.encode(bsonWriter, i.nextLong, encoderContext)
    }
    bsonWriter.writeEndArray()
  }

  override def getEncoderClass: Class[LongSet] = {
    classOf[LongSet]
  }
}
