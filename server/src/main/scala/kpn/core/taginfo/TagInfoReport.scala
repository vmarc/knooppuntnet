package kpn.core.taginfo

import kpn.api.custom.ScopedNetworkType
import kpn.core.poi.PoiConfiguration
import kpn.core.poi.PoiDefinition
import kpn.core.poi.PoiGroupDefinition
import kpn.core.poi.tags.And
import kpn.core.poi.tags.HasTag
import kpn.core.poi.tags.Or
import kpn.core.poi.tags.TagContains
import kpn.core.poi.tags.TagExpression
import kpn.server.analyzer.engine.analysis.route.segment.SurfaceAnalyzer
import kpn.server.json.Json

object TagInfoReport {
  def main(args: Array[String]): Unit = {
    new TagInfoReport().print()
  }
}

class TagInfoReport {

  def print(): Unit = {
    val tags = allTags()
    val tagInfo = TagInfo(tags = tags)
    println(Json.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(tagInfo))
    println(s"tagCount: ${tags.size}")
  }

  private def allTags(): Seq[TagInfoTag] = {
    Seq(
      analysisTags(),
      nodeAnalysisTags(),
      networkAnalysisTags(),
      oneWayAnalysisTags(),
      accessibilityAnalysisTags(),
      surfaceAnalysisTags(),
      poiTags()
    ).flatten
  }

  private def analysisTags(): Seq[TagInfoTag] = {
    Seq(
      TagInfoTag("network:type", Some("node_network"), "Used in node network analysis", Some(Seq("node", "relation"))),
      TagInfoTag("type", Some("network"), "Used in node network analysis: network definition", Some(Seq("relation"))),
      TagInfoTag("type", Some("route"), "Used in node network analysis: route definition", Some(Seq("relation"))),
      TagInfoTag("state", Some("proposed"), "Used in node network analysis node definition", Some(Seq("node"))),
      TagInfoTag("tourism", Some("information"), "Used in node network analysis node definition", Some(Seq("node"))),
      TagInfoTag("information", Some("map"), "Used in node network analysis node definition", Some(Seq("node"))),
      TagInfoTag("information", Some("guidepost"), "Used in node network analysis node definition", Some(Seq("node"))),
      TagInfoTag("source", Some("survey"), "Used in node network analysis node definition", Some(Seq("node", "relation"))),
      TagInfoTag("name", None, "Used in node network analysis", Some(Seq("node", "relation"))),
      TagInfoTag("ref", None, "Used in node network analysis", Some(Seq("node", "relation"))),
      TagInfoTag("state", Some("connection"), "Used in node network analysis", Some(Seq("node", "relation"))),
      TagInfoTag("state", Some("alternate"), "Used in node network analysis", Some(Seq("node", "relation"))),
      TagInfoTag("fixmetodo", None, "Used in node network analysis", None),
      TagInfoTag("fixme", Some("incomplete"), "Used in node network analysis", Some(Seq("relation"))),
      TagInfoTag("direction", Some("forward"), "Used in node network analysis", Some(Seq("relation"))),
      TagInfoTag("direction", Some("backward"), "Used in node network analysis", Some(Seq("relation"))),
    )
  }

  private def nodeAnalysisTags(): Seq[TagInfoTag] = {
    ScopedNetworkType.all.flatMap { scopedNetworkType =>
      Seq(
        scopedNetworkType.nodeRefTagKey,
        scopedNetworkType.nodeNameTagKey,
        scopedNetworkType.proposedNodeRefTagKey,
        scopedNetworkType.proposedNodeNameTagKey,
        scopedNetworkType.expectedRouteRelationsTag
      ).map { tagKey =>
        TagInfoTag(
          tagKey,
          None,
          "Used in node network analysis node definition"
        )
      }
    }
  }


  private def networkAnalysisTags(): Seq[TagInfoTag] = {
    ScopedNetworkType.all.map { scopedNetworkType =>
      TagInfoTag(
        "network",
        Some(scopedNetworkType.key),
        s"Used in node network analysis - ${scopedNetworkType.networkScope.name} ${scopedNetworkType.networkType.name} network"
      )
    }
  }

  private def oneWayAnalysisTags(): Seq[TagInfoTag] = {
    Seq(
      "oneway" -> Seq("yes"),
      "oneway:bicycle" -> Seq("no", "0", "false", "yes", "1", "true", "-1", "reverse"),
      "bicyle:oneway" -> Seq("no", "0", "false", "yes", "1", "true", "-1", "reverse"),
      "cycleway" -> Seq("opposite", "opposite_lane", "opposite_track"),
      "cycleway:left" -> Seq("lane", "track", "opposite_lane", "opposite_track"),
      "cycleway:right" -> Seq("lane", "track", "opposite_lane", "opposite_track"),
    ).flatMap { keyValues =>
      keyValues._2.map { value =>
        TagInfoTag(
          keyValues._1,
          Some(value),
          "Used in oneway analysis",
          Some(Seq("way")
          )
        )
      }
    }
  }

  private def accessibilityAnalysisTags(): Seq[TagInfoTag] = {
    Seq(
      "highway" -> None,
      "highway:virtual" -> None,
      "waterway" -> None,
      "waterway:virtual" -> None,
      "route" -> Some("ferry"),
      "foot" -> Some("yes"),
      "foot" -> Some("no"),
      "horse" -> Some("yes"),
      "horse" -> Some("no"),
      "canoe" -> Some("portage"),
      "canoe" -> Some("yes"),
      "canoe" -> Some("no"),
      "inline_skates" -> Some("yes"),
      "inline_skates" -> Some("no"),
    ).map { keyValue =>
      TagInfoTag(
        keyValue._1,
        keyValue._2,
        "Used in accessibility analysis",
        Some(Seq("way")
        )
      )
    }
  }

  private def surfaceAnalysisTags(): Seq[TagInfoTag] = {
    Seq(
      (SurfaceAnalyzer.wikiSurfacePaved ++ SurfaceAnalyzer.wikiSurfaceUnpaved).map { value =>
        "surface" -> value
      },
      (SurfaceAnalyzer.wikiTracktypePaved ++ SurfaceAnalyzer.wikiTracktypeUnpaved).map { value =>
        "tracktype" -> value
      },
      SurfaceAnalyzer.wikiUnpavedSmoothness.map { value =>
        "smoothness" -> value
      },
      SurfaceAnalyzer.highwayUnpaved.map { value =>
        "highway" -> value
      }
    ).flatten.map { keyValue =>
      TagInfoTag(
        keyValue._1,
        Some(keyValue._2),
        "surface analysis",
        Some(Seq("way")
        )
      )
    }
  }

  private def poiTags(): Seq[TagInfoTag] = {
    PoiConfiguration.instance.groupDefinitions.flatMap { poiGroupDefinition =>
      poiGroupDefinition.definitions.flatMap { poiDefinition =>
        new PoiTagInfoBuilder(poiGroupDefinition, poiDefinition).tags()
      }
    }
  }
}

class PoiTagInfoBuilder(poiGroupDefinition: PoiGroupDefinition, poiDefinition: PoiDefinition) {

  private val description = s"Point of interest ${poiGroupDefinition.name}/${poiDefinition.name}"

  def tags(): Seq[TagInfoTag] = {
    tagExpressionTags(poiDefinition.expression)
  }

  private def tagExpressionTags(tagExpression: TagExpression): Seq[TagInfoTag] = {
    tagExpression match {
      case hasTag: HasTag => hasTagTags(hasTag)
      case tagContains: TagContains => tagContainsTags(tagContains)
      case or: Or => orTags(or)
      case and: And => andTags(and)
      case _ => Seq.empty
    }
  }

  private def hasTagTags(hasTag: HasTag): Seq[TagInfoTag] = {
    hasTag.allowedValues.map { value =>
      TagInfoTag(hasTag.tagKey, Some(value), description)
    }
  }

  private def tagContainsTags(tagContains: TagContains): Seq[TagInfoTag] = {
    tagContains.tagValues.map { value =>
      TagInfoTag(tagContains.tagKey, Some(value), description)
    }
  }

  private def orTags(or: Or): Seq[TagInfoTag] = {
    tagExpressionTags(or.left) ++ tagExpressionTags(or.right)
  }

  private def andTags(and: And): Seq[TagInfoTag] = {
    tagExpressionTags(and.left) ++ tagExpressionTags(and.right)
  }
}
