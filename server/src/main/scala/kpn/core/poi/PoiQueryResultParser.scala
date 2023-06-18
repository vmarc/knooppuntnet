package kpn.core.poi

import kpn.api.common.location.Location
import kpn.api.common.poi.Poi
import kpn.api.custom.Tag
import kpn.api.custom.Tags

class PoiQueryResultParser {

  def parse(layer: String, xml: scala.xml.Node): Seq[Poi] = {
    xml.child.toSeq.flatMap { element =>
      element.label match {
        case "node" => Some(node(layer, element))
        case "way" => Some(way(layer, element))
        case "relation" => Some(relation(layer, element))
        case _ => None
      }
    }
  }

  private def node(layer: String, xmlNode: scala.xml.Node): Poi = {
    val latitude = (xmlNode \ "@lat").text
    val longitude = (xmlNode \ "@lon").text
    val nodeId = id(xmlNode)
    Poi(
      s"node:$nodeId",
      "node",
      nodeId,
      latitude,
      longitude,
      Seq(layer),
      tags(xmlNode),
      Location.empty,
      Seq.empty,
      None,
      None,
      link = false,
      image = false
    )
  }

  private def way(layer: String, xmlNode: scala.xml.Node): Poi = {
    val center = xmlNode \ "center"
    val latitude = (center \ "@lat").text
    val longitude = (center \ "@lon").text
    val wayId = id(xmlNode)
    Poi(
      s"way:$wayId",
      "way",
      wayId,
      latitude,
      longitude,
      Seq(layer),
      tags(xmlNode),
      Location.empty,
      Seq.empty,
      None,
      None,
      link = false,
      image = false
    )
  }

  private def relation(layer: String, xmlNode: scala.xml.Node): Poi = {
    val center = xmlNode \ "center"
    val latitude = (center \ "@lat").text
    val longitude = (center \ "@lon").text
    val relationId = id(xmlNode)
    Poi(
      s"relation:$relationId",
      "relation",
      relationId,
      latitude,
      longitude,
      Seq(layer),
      tags(xmlNode),
      Location.empty,
      Seq.empty,
      None,
      None,
      link = false,
      image = false
    )
  }

  private def tags(node: scala.xml.Node): Tags = {
    Tags(
      (node \ "tag").map { tag =>
        val key = (tag \ "@k").text
        val value = (tag \ "@v").text
        Tag(key, value)
      }
    )
  }

  private def id(node: scala.xml.Node): Long = (node \ "@id").text.toLong

}
