package kpn.server.search

import kpn.api.common.Bounds
import kpn.api.common.GeocoderLocation

import scala.xml.XML

object NomatimResponseParser {
  def parse(xmlString: String): Seq[GeocoderLocation] = {
    new NomatimResponseParser().parse(xmlString)
  }
}

class NomatimResponseParser {
  def parse(xmlString: String): Seq[GeocoderLocation] = {
    val strippedXmlString = xmlString.drop("""<?xml version="1.0" encoding="UTF-8" ?>""".size)
    val xml = XML.loadString(strippedXmlString)
    val searchresults = xml.head \\ "searchresults"
    (searchresults.head \\ "place").map { place =>
      val name = (place \ "@display_name").text
      val latitude = (place \ "@lat").text
      val longitude = (place \ "@lon").text
      val boundsString = (place \ "@boundingbox").text
      val Array(xmin, ymin, xmax, ymax) = boundsString.split(",")
      val bounds = Bounds(
        xmin.toDouble,
        ymin.toDouble,
        xmax.toDouble,
        ymax.toDouble,
      )
      GeocoderLocation(
        name,
        latitude,
        longitude,
        bounds
      )
    }
  }
}
