package kpn.core.poi

import kpn.shared.data.Tag
import kpn.shared.data.Tags

class PoiQueryResultParser {

  def parse(layer: String, xml: scala.xml.Node): Seq[Poi] = {
    xml.child.flatMap { element =>
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
    Poi("node", id(xmlNode), latitude, longitude, Seq(layer), tags(xmlNode))
  }

  private def way(layer: String, xmlNode: scala.xml.Node): Poi = {
    val center = xmlNode \ "center"
    val latitude = (center \ "@lat").text
    val longitude = (center \ "@lon").text
    Poi("way", id(xmlNode), latitude, longitude, Seq(layer), tags(xmlNode))
  }

  private def relation(layer: String, xmlNode: scala.xml.Node): Poi = {
    val center = xmlNode \ "center"
    val latitude = (center \ "@lat").text
    val longitude = (center \ "@lon").text
    Poi("relation", id(xmlNode), latitude, longitude, Seq(layer), tags(xmlNode))
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
