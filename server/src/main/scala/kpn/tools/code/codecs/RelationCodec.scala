package kpn.tools.code.codecs

import kpn.api.common.data.Member
import kpn.api.common.data.Node
import kpn.api.common.data.NodeMember
import kpn.api.common.data.RelationIdMember
import kpn.api.common.data.RelationMember
import kpn.api.common.data.Way
import kpn.api.common.data.WayMember
import kpn.api.custom.Relation
import kpn.api.custom.Tag
import kpn.api.custom.Timestamp
import org.bson.BsonReader
import org.bson.BsonType
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bson.codecs.configuration.CodecRegistry

class RelationCodec(registry: CodecRegistry) extends Codec[Relation] {

  private val longCodec = registry.get(classOf[Long])
  private val stringCodec = registry.get(classOf[String])
  private val memberCodec = registry.get(classOf[Member])
  private val tagCodec = registry.get(classOf[Tag])
  private val timestampCodec = registry.get(classOf[Timestamp])
  private val relationIdMemberCodec = registry.get(classOf[RelationIdMember])
  private val nodeMemberCodec = registry.get(classOf[NodeMember])
  private val wayMemberCodec = registry.get(classOf[WayMember])
  private val relationMemberCodec = registry.get(classOf[RelationMember])

  private val nodeCodec = registry.get(classOf[Node])
  private val wayCodec = registry.get(classOf[Way])
  private val relationCodec = registry.get(classOf[Relation])

  override def decode(bsonReader: BsonReader, decoderContext: DecoderContext): Relation = {
    bsonReader.readStartDocument()

    var id: Long = 0
    var version: Long = 0
    var timestamp: Timestamp = null
    var changeSetId: Long = 0
    var tags: Seq[Tag] = null
    var members: Seq[Member] = null

    while (bsonReader.readBsonType != BsonType.END_OF_DOCUMENT) {
      val fieldName = bsonReader.readName
      if (fieldName == "id") {
        id = longCodec.decode(bsonReader, decoderContext)
      }
      else if (fieldName == "version") {
        version = longCodec.decode(bsonReader, decoderContext)
      }
      else if (fieldName == "timestamp") {
        timestamp = timestampCodec.decode(bsonReader, decoderContext)
      }
      else if (fieldName == "changeSetId") {
        changeSetId = longCodec.decode(bsonReader, decoderContext)
      }
      else if (fieldName == "tags") {
        bsonReader.readStartArray()
        val valueBuffer = scala.collection.mutable.Buffer[Tag]()
        while (bsonReader.readBsonType != BsonType.END_OF_DOCUMENT) {
          valueBuffer += tagCodec.decode(bsonReader, decoderContext)
        }
        bsonReader.readEndArray()
        tags = valueBuffer.toSeq
      }
      else if (fieldName == "members") {
        bsonReader.readStartArray()

        val valueBuffer = scala.collection.mutable.Buffer[Member]()
        while (bsonReader.readBsonType != BsonType.END_OF_DOCUMENT) {

          bsonReader.readStartDocument()

          var role: Option[String] = None
          var node: Node = null
          var way: Way = null
          var relation: Relation = null
          var relationId: Long = 0

          while (bsonReader.readBsonType != BsonType.END_OF_DOCUMENT) {
            val memberFieldName = bsonReader.readName
            if (memberFieldName == "role") {
              role = Some(stringCodec.decode(bsonReader, decoderContext))
            }
            else if (memberFieldName == "node") {
              node = nodeCodec.decode(bsonReader, decoderContext)
            }
            else if (memberFieldName == "way") {
              way = wayCodec.decode(bsonReader, decoderContext)
            }
            else if (memberFieldName == "relation") {
              relation = relationCodec.decode(bsonReader, decoderContext)
            }
            else if (memberFieldName == "relationId") {
              relationId = longCodec.decode(bsonReader, decoderContext)
            }
            else {
              Codecs.log.warn(s"Unknown field name: $memberFieldName in NodeMemberCodec.decode()")
              bsonReader.skipValue()
            }
          }

          if (node != null) {
            valueBuffer += NodeMember(node, role)
          }
          else if (way != null) {
            valueBuffer += WayMember(way, role)
          }
          else if (relation != null) {
            valueBuffer += RelationMember(relation, role)
          }
          else if (relationId > 0) {
            valueBuffer += RelationIdMember(relationId, role)
          }
          else {
            Codecs.log.warn(s"Unknown member type in RelationCodec.decode()")
          }
        }

        bsonReader.readEndArray()
        members = valueBuffer.toSeq
      }
      else {
        Codecs.log.warn(s"Unknown field name: $fieldName in RelationCodec.decode()")
        bsonReader.skipValue()
      }
    }

    bsonReader.readEndDocument()

    Relation(
      id,
      version,
      timestamp,
      changeSetId,
      tags,
      members,
    )
  }

  override def encode(bsonWriter: BsonWriter, value: Relation, encoderContext: EncoderContext): Unit = {
    bsonWriter.writeStartDocument()

    bsonWriter.writeName("id")
    longCodec.encode(bsonWriter, value.id, encoderContext)

    bsonWriter.writeName("version")
    longCodec.encode(bsonWriter, value.version, encoderContext)

    bsonWriter.writeName("timestamp")
    timestampCodec.encode(bsonWriter, value.timestamp, encoderContext)

    bsonWriter.writeName("changeSetId")
    longCodec.encode(bsonWriter, value.changeSetId, encoderContext)

    bsonWriter.writeName("tags")
    bsonWriter.writeStartArray()
    value.tags.foreach(v => tagCodec.encode(bsonWriter, v, encoderContext))
    bsonWriter.writeEndArray()

    bsonWriter.writeName("members")
    bsonWriter.writeStartArray()

    value.members.foreach {
      case relationIdMember: RelationIdMember => relationIdMemberCodec.encode(bsonWriter, relationIdMember, encoderContext)
      case nodeMember: NodeMember => nodeMemberCodec.encode(bsonWriter, nodeMember, encoderContext)
      case wayMember: WayMember => wayMemberCodec.encode(bsonWriter, wayMember, encoderContext)
      case relationMember: RelationMember => relationMemberCodec.encode(bsonWriter, relationMember, encoderContext)
      case _ =>
    }

    bsonWriter.writeEndArray()

    bsonWriter.writeEndDocument()
  }

  override def getEncoderClass: Class[Relation] = {
    classOf[Relation]
  }
}

