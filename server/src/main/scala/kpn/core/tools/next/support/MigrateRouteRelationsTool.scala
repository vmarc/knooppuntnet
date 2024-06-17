package kpn.core.tools.next.support

import kpn.api.common.data.Node
import kpn.api.common.data.NodeMember
import kpn.api.common.data.RelationMember
import kpn.api.common.data.Way
import kpn.api.common.data.WayMember
import kpn.api.custom.Relation
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.tools.next.database.NextDatabase
import kpn.core.tools.next.database.NextDatabaseImpl
import kpn.core.tools.next.domain.NextRouteRelation
import kpn.core.tools.next.domain.OldNode
import kpn.core.tools.next.domain.OldNodeMember
import kpn.core.tools.next.domain.OldRelation
import kpn.core.tools.next.domain.OldRelationMember
import kpn.core.tools.next.domain.OldWay
import kpn.core.tools.next.domain.OldWayMember
import kpn.database.util.Mongo.codecRegistry
import org.mongodb.scala.MongoClient

object MigrateRouteRelationsTool {
  def main(args: Array[String]): Unit = {
    val client = MongoClient("mongodb://localhost:27017")
    try {
      val mongoDatabase = client.getDatabase("kpn-next").withCodecRegistry(codecRegistry)
      val database = new NextDatabaseImpl(mongoDatabase)
      val tool = new MigrateRouteRelationsTool(database)
      tool.migrate()
    } finally {
      client.close()
    }
  }
}

class MigrateRouteRelationsTool(database: NextDatabase) {
  def migrate(): Unit = {
    println("collecting document ids")
    val ids = database.oldRouteRelations.ids()
    println(s"processing ${ids.size} documents")
    ids.zipWithIndex.foreach { case (id, index) =>
      if (((index + 1) % 100) == 0) {
        println(s"${index + 1}/${ids.size}")
      }
      database.oldRouteRelations.findById(id).foreach { oldDocument =>
        val relation = toRelation(oldDocument.relation)
        val newDocument = NextRouteRelation(
          relation.id,
          relation.version,
          relation.timestamp,
          relation.changeSetId,
          relation.tags,
          relation.members
        )
        database.routeRelations.save(newDocument)
      }
    }
  }

  private def toNode(oldNode: OldNode): Node = {
    Node(
      oldNode.raw.id,
      oldNode.raw.latitude,
      oldNode.raw.longitude,
      oldNode.raw.version,
      oldNode.raw.timestamp,
      oldNode.raw.changeSetId,
      oldNode.raw.tags
    )
  }

  private def toWay(oldWay: OldWay): Way = {
    Way(
      oldWay.raw.id,
      oldWay.raw.version,
      oldWay.raw.timestamp,
      oldWay.raw.changeSetId,
      oldWay.raw.tags,
      oldWay.nodes.map(toNode).toVector,
      oldWay.length
    )
  }

  private def toRelation(oldRelation: OldRelation): Relation = {

    val newMembers = oldRelation.members.map {
      case nodeMember: OldNodeMember =>
        NodeMember(
          toNode(nodeMember.node),
          nodeMember.role
        )
      case wayMember: OldWayMember =>
        WayMember(
          toWay(wayMember.way),
          wayMember.role
        )

      case relationMember: OldRelationMember =>
        RelationMember(
          toRelation(relationMember.relation),
          relationMember.role
        )
    }

    Relation(
      oldRelation.raw.id: Long,
      oldRelation.raw.version: Long,
      oldRelation.raw.timestamp: Timestamp,
      oldRelation.raw.changeSetId: Long,
      oldRelation.raw.tags: Tags,
      newMembers
    )
  }
}
