package kpn.server.opendata.flanders

import kpn.api.common.LatLonImpl
import kpn.server.opendata.flanders.FlandersUtil.lambertToLatLon

import scala.xml.Node

class FlandersRouteParser {

  def parse(xml: Node, tag: String): Seq[FlandersRoute] = {
    val featureMembers = xml \ "featureMembers"
    val routes = featureMembers \ tag
    routes.map(parseRoute)
  }

  private def parseRoute(route: Node): FlandersRoute = {
    val _id = (route \ "pid").text
    val fromNodeId = (route \ "begin_geoid").text
    val toNodeId = (route \ "end_geoid").text
    val owner = (route \ "eigenaar").text
    val network = (route \ "naam").text
    val contact = (route \ "meldpunt").text
    val updated = (route \ "updatedate").text.replaceAll("Z", "")
    val coordinates = parseCoordinates(route)
    FlandersRoute(
      _id,
      fromNodeId,
      toNodeId,
      coordinates,
      owner,
      network,
      updated,
      contact
    )
  }

  private def parseCoordinates(route: Node): Seq[LatLonImpl] = {
    val geom = route \ "geom"
    val lineString = geom \ "LineString"
    val posList = (lineString \ "posList").text
    val positionCoordinates = posList.split(" ")
    positionCoordinates.sliding(2, 2).toSeq.map { case Array(x, y) =>
      lambertToLatLon(x, y)
    }
  }
}
