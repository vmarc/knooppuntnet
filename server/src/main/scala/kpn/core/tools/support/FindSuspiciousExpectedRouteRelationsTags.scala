package kpn.core.tools.support

import kpn.api.common.Country
import kpn.api.common.data.Tagable
import kpn.api.custom.ScopedNetworkType
import kpn.core.doc.NodeDoc
import kpn.database.base.Database
import kpn.database.util.Mongo

object FindSuspiciousExpectedRouteRelationsTags {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-laptop") { database =>
      new FindSuspiciousExpectedRouteRelationsTags(database).explore()
    }
  }
}

class FindSuspiciousExpectedRouteRelationsTags(database: Database) {

  private val all = ScopedNetworkType.all
  private val nodeNameTags = all.map(_.nodeRefTagKey) ++ all.map(_.nodeNameTagKey)
  private val proposedNameTags = all.map(_.proposedNodeRefTagKey) ++ all.map(_.proposedNodeNameTagKey)
  private val expectedTags = all.map(_.expectedRouteRelationsTag)

  def explore(): Unit = {
    println("loading nodes")
    val nodes = database.nodes.findAll()
    println(s"analyzing ${nodes.size} nodes")

    val suspiciousNodes = nodes.filter(isSuspicious)

    report(suspiciousNodes)
    println(s"\n${suspiciousNodes.size} nodes")
  }

  private def isSuspicious(node: NodeDoc): Boolean = {
    val a = networkTypesInNodeNames(node)
    val b = networkTypesInExpectedTags(node)
    (b -- a).nonEmpty
  }

  private def report(nodes: Seq[NodeDoc]): Unit = {
    Country.values.foreach { country =>
      println(s"** ${country.entryName} ***")
      val countryNodes = nodes.filter(_.country.contains(country))
      val countryNodesSize = countryNodes.size
      countryNodes.zipWithIndex.foreach { case (node, index) =>
        println(s"${country.entryName} ${index + 1}/$countryNodesSize [${node.name}](http://localhost:4000/analysis/node/${node._id})")
        reportNode(node)
      }
    }
  }

  private def reportNode(node: NodeDoc): Unit = {
    println()
    println("|key|value|")
    println("|---|-----|")
    node.tags.foreach { tag =>
      println(s"|${tag.key}|${tag.value}|")
    }
    println()
  }

  private def networkTypesInNodeNames(tagable: Tagable): Set[String] = {
    val from = "proposed:".length
    (networkTypesInTags(nodeNameTags, 0, 3, tagable) ++
      networkTypesInTags(proposedNameTags, from, from + 3, tagable)).toSet
  }

  private def networkTypesInExpectedTags(tagable: Tagable): Set[String] = {
    val from = "expected_".length
    networkTypesInTags(expectedTags, from, from + 3, tagable).toSet
  }

  private def networkTypesInTags(tagKeys: Seq[String], from: Int, until: Int, tagable: Tagable) = {
    tagKeys.filter(tag => tagable.hasTag(tag)).map(tag => tag.slice(from, until))
  }
}
