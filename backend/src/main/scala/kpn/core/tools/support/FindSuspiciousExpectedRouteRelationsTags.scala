package kpn.core.tools.support

import kpn.api.common.Country
import kpn.api.common.data.Tagable
import kpn.api.custom.ScopedRouteType
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

  private val all = ScopedRouteType.all
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
    val a = routeTypesInNodeNames(node)
    val b = routeTypesInExpectedTags(node)
    (b -- a).nonEmpty
  }

  private def report(nodes: Seq[NodeDoc]): Unit = {
    Country.values.foreach { country =>
      println(s"** ${country.entryName} ***")
      val countryNodes = nodes.filter(_.base.country.contains(country))
      val countryNodesSize = countryNodes.size
      countryNodes.zipWithIndex.foreach { case (node, index) =>
        println(s"${country.entryName} ${index + 1}/$countryNodesSize [${node.base.name}](http://localhost:4000/analysis/node/${node._id})")
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

  private def routeTypesInNodeNames(tagable: Tagable): Set[String] = {
    val from = "proposed:".length
    (routeTypesInTags(nodeNameTags, 0, 3, tagable) ++
      routeTypesInTags(proposedNameTags, from, from + 3, tagable)).toSet
  }

  private def routeTypesInExpectedTags(tagable: Tagable): Set[String] = {
    val from = "expected_".length
    routeTypesInTags(expectedTags, from, from + 3, tagable).toSet
  }

  private def routeTypesInTags(tagKeys: Seq[String], from: Int, until: Int, tagable: Tagable) = {
    tagKeys.filter(tag => tagable.hasTag(tag)).map(tag => tag.slice(from, until))
  }
}
