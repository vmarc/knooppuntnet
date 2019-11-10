package kpn.core.history

import kpn.api.common.data.Node
import kpn.core.data.Data
import kpn.core.util.Haversine

class OsmDataDiffAnalyzer(before: Data, after: Data) {

  def analyze(): Unit = {

    println("Nodes removed:")
    val nodesRemoved = (nodeIdsBefore -- nodeIdsAfter).map(before.nodes)
    nodesRemoved.foreach { node =>
      print("  ")
      printNode(node)
      printRefs(nodeRefs(before, node.id))
    }

    println("Nodes added:")
    val nodesAdded = (nodeIdsAfter -- nodeIdsBefore).map(after.nodes)
    nodesAdded.foreach { node =>
      print("  ")
      printNode(node)
      val refs = nodeRefs(after, node.id)
      printRefs(refs)
    }

    println("Nodes updated:")
    val nodesUpdatedIds = nodeIdsCommon.filter(nodeId => before.nodes(nodeId) != after.nodes(nodeId))
    nodesUpdatedIds.foreach { nodeId =>
      val nodeBefore = before.nodes(nodeId)
      val nodeAfter = after.nodes(nodeId)

      print("  node " + nodeId)
      if (nodeBefore.latitude != nodeAfter.latitude || nodeBefore.longitude != nodeAfter.longitude) {
        println(" moved " + Haversine.meters(Seq(nodeBefore.raw, nodeAfter.raw)) + " meters")
      }
      else {
        println("")
      }

      print("    before=")
      printNode(before.nodes(nodeId))
      printRefs(nodeRefs(before, nodeId))
      print("    after=")
      printNode(after.nodes(nodeId))
      printRefs(nodeRefs(after, nodeId))
    }

    // *****************


    // *****************
    println("Ways removed:")
    val waysRemoved = (wayIdsBefore -- wayIdsAfter).map(before.ways)
    waysRemoved.foreach { way =>
      println("  " + way)
      printRefs(wayRefs(before, way.id))
    }

    println("Ways added:")
    val waysAdded = (wayIdsAfter -- wayIdsBefore).map(after.ways)
    waysAdded.foreach { way =>
      println("  " + way)
      printRefs(wayRefs(after, way.id))
    }

    println("Ways updated:")
    val waysUpdatedIds = wayIdsCommon.filter(wayId => before.ways(wayId) != after.ways(wayId))
    waysUpdatedIds.foreach { wayId =>
      println("  way " + wayId)
      println("    before=" + before.ways(wayId))
      printRefs(wayRefs(before, wayId))
      println("    after=" + after.ways(wayId))
      printRefs(wayRefs(after, wayId))
    }

    // *****************

    println("Relations removed:")
    val relationsRemoved = (relationIdsBefore -- relationIdsAfter).map(before.relations)
    relationsRemoved.foreach { relation =>
      println("  " + relation)
      printRefs(relationRefs(before, relation.id))
    }

    println("Relations added:")
    val relationsAdded = (relationIdsAfter -- relationIdsBefore).map(after.relations)
    relationsAdded.foreach { relation =>
      println("  " + relation)
      printRefs(relationRefs(after, relation.id))
    }

    println("Relations updated:")
    val relationsUpdatedIds = relationIdsCommon.filter(relationId => before.relations(relationId) != after.relations(relationId))
    relationsUpdatedIds.foreach { relationId =>
      println("  relation " + relationId)
      println("    before=" + before.relations(relationId))
      printRefs(relationRefs(before, relationId))
      println("    after=" + after.relations(relationId))
      printRefs(relationRefs(after, relationId))
    }
  }

  private def nodeRefs(data: Data, nodeId: Long): Seq[String] = {

    val waysWithNode = data.ways.values.filter(way => way.nodes.exists(n => n.id == nodeId)).map(_.id)

    val relationsWithNode = data.relations.values.filter(relation => relation.nodeMembers.exists(m => m.node.id == nodeId)).map(_.id)

    val w = waysWithNode.toSeq.flatMap { wayId =>
      val xx = "> way " + wayId
      val refs = wayRefs(data, wayId)
      if (refs.isEmpty) {
        Seq(xx)
      }
      else {
        refs.map { ref =>
          xx + " " + ref
        }
      }
    }

    val r = relationsWithNode.toSeq.flatMap { relationId =>
      val xx = "> relation " + relationId
      val refs = relationRefs(data, relationId)
      if (refs.isEmpty) {
        Seq(xx)
      }
      else {
        refs.map { ref =>
          xx + " " + ref
        }
      }
    }

    w ++ r
  }

  private def wayRefs(data: Data, wayId: Long): Seq[String] = {
    val relationsWithWay = data.relations.values.filter(relation => relation.wayMembers.exists(m => m.way.id == wayId)).map(_.id)
    relationsWithWay.toSeq.flatMap { relId =>
      val refs = relationRefs(data, relId)
      if (refs.isEmpty) {
        Seq("> relation " + relId)
      }
      else {
        refs.map(r => "> relation " + relId + " " + r)
      }
    }
  }

  private def relationRefs(data: Data, relationId: Long): Seq[String] = {
    val relationsWithRelation = data.relations.values.filter(relation => relation.relationMembers.exists(m => m.relation.id == relationId)).map(_.id)
    relationsWithRelation.toSeq.map { relId =>
      "> relation " + relId
    }
  }

  private def printNode(node: Node): Unit = {
    println(s"Node id=${node.id}, latitude=${node.latitude}, longitude=${node.longitude}, version=${node.version}, timestamp=${node.timestamp}, changeset=${node.changeSetId}, tags=${node.tags.tagString}")
  }

  private def printRefs(refs: Seq[String]): Unit = {
    if (refs.isEmpty) {
      println("    no refs")
    }
    else {
      refs.foreach { ref =>
        println("     " + ref)
      }
    }
  }

  private def nodeIdsAfter: Set[Long] = after.nodes.keys.toSet

  private def nodeIdsBefore: Set[Long] = before.nodes.keys.toSet

  private def nodeIdsCommon: Set[Long] = nodeIdsBefore intersect nodeIdsAfter

  private def wayIdsAfter: Set[Long] = after.ways.keys.toSet

  private def wayIdsBefore: Set[Long] = before.ways.keys.toSet

  private def wayIdsCommon: Set[Long] = wayIdsBefore intersect wayIdsAfter

  private def relationIdsAfter: Set[Long] = after.relations.keys.toSet

  private def relationIdsBefore: Set[Long] = before.relations.keys.toSet

  private def relationIdsCommon: Set[Long] = relationIdsBefore intersect relationIdsAfter

}
