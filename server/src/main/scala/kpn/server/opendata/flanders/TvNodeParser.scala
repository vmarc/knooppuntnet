package kpn.server.opendata.flanders

import kpn.api.common.LatLonImpl
import kpn.server.opendata.flanders.TvUtil.lambertToLatLon

import scala.xml.Node

class TvNodeParser {

  def parse(xml: Node): Seq[TvNode] = {
    val featureMembers = xml \ "featureMembers"
    val nodes = featureMembers \ "knoop_wandel"
    nodes.map(parseNode)
  }

  private def parseNode(node: Node): TvNode = {
    val _id = (node \ "geoid").text
    val name = (node \ "knoopnr").text
    val owner = (node \ "eigenaar").text
    val network = (node \ "naam").text
    val contact = (node \ "meldpunt").text
    val updated = (node \ "updatedate").text.replaceAll("Z", "")
    val latLon = parsePosition(node)
    TvNode(
      _id,
      name,
      latLon.latitude,
      latLon.longitude,
      owner,
      network,
      updated,
      contact
    )
  }

  private def parsePosition(node: Node): LatLonImpl = {
    val geom = node \ "geom"
    val point = geom \ "Point"
    val pos = (point \ "pos").text
    val coordinates = pos.split(" ")
    lambertToLatLon(coordinates(0), coordinates(1))
  }
}
