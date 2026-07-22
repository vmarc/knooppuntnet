package kpn.core.tools.location

import kpn.api.common.Language.DE
import kpn.api.common.Language.EN
import kpn.api.common.Language.FR
import kpn.api.common.Language.NL
import kpn.core.doc.LocationName
import kpn.core.util.Log
import kpn.server.json.Json

import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.zip.GZIPInputStream

class LocationBuilderPoland(dir: String) {

  private val countryFilename = s"$dir/pl-level-2.geojson.gz"
  private val locationDatas = new LocationDatas()

  def build(): Seq[LocationData] = {
    Log.context("dk") {
      buildCountry()
      locationDatas.toSeq
    }
  }

  private def buildCountry(): Unit = {
    Log.context("country") {
      val gzippedInputStream = new FileInputStream(countryFilename)
      val ungzippedInputStream = new GZIPInputStream(gzippedInputStream)
      val fileReader = new InputStreamReader(ungzippedInputStream, "UTF-8")
      val locationJsons = Json.readValue(fileReader, classOf[LocationsJson]).features
      val locationJson = locationJsons.head
      val relationId = Math.abs(locationJson.properties.osm_id)

      locationDatas.add(
        LocationData(
          "pl",
          relationId,
          "Poland",
          Seq(
            LocationName(EN, "Poland"),
            LocationName(NL, "Polen"),
            LocationName(DE, "Polen"),
            LocationName(FR, "Pologne")
          ),
          Seq.empty,
          LocationGeometry(locationJson.geometry)
        )
      )
    }
  }
}
