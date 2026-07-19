package kpn.server.search

import kpn.api.common.Bounds
import kpn.api.common.GeocoderLocation
import kpn.core.util.UnitTest

class NomatimResponseParserTest extends UnitTest {

  test("parse nomatim response string") {

    val locations = NomatimResponseParser.parse(responseString)
    assertEqual(
      locations,
      Seq(
        GeocoderLocation(
          name = "Essen, North Rhine-Westphalia, Germany",
          latitude = "51.4582235",
          longitude = "7.0158171",
          bounds = Bounds(
            51.3475714,
            51.5342269,
            6.8943442,
            7.1376500
          )
        ),
        GeocoderLocation(
          name = "Essen, Antwerp, Flanders, 2910, Belgium",
          latitude = "51.4679229",
          longitude = "4.4698256",
          bounds = Bounds(
            51.4154863,
            51.4823977,
            4.3793170,
            4.5481608
          )
        ),
        GeocoderLocation(
          name = "Essen (Oldenburg), Cloppenburg district, Lower Saxony, 49632, Germany",
          latitude = "52.7224880",
          longitude = "7.9352306",
          bounds = Bounds(
            52.6797506,
            52.7664132,
            7.8318507,
            8.0777375
          )
        )
      )
    )
  }

  private def responseString: String = {
    """<?xml version="1.0" encoding="UTF-8" ?>
      |<searchresults
      |    timestamp="Mon, 18 Nov 2024 16:55:45 +00:00"
      |    attribution="Data © OpenStreetMap contributors, ODbL 1.0. http://osm.org/copyright"
      |    querystring="Essen"
      |    more_url="https://nominatim.openstreetmap.org/search?q=Essen&amp;addressdetails=1&amp;limit=20&amp;exclude_place_ids=103710423%2C97001130%2C139910255&amp;format=xml"
      |    exclude_place_ids="103710423,97001130,139910255"
      |>
      |  <place
      |      place_id="103710423"
      |      osm_type="relation"
      |      osm_id="62713"
      |      ref="Essen"
      |      lat="51.4582235"
      |      lon="7.0158171"
      |      boundingbox="51.3475714,51.5342269,6.8943442,7.1376500"
      |      place_rank="12"
      |      address_rank="16"
      |      display_name="Essen, North Rhine-Westphalia, Germany"
      |      class="boundary"
      |      type="administrative"
      |      importance="0.6728980498138706"
      |  />
      |  <place
      |      place_id="97001130"
      |      osm_type="relation"
      |      osm_id="964003"
      |      ref="Essen"
      |      lat="51.4679229"
      |      lon="4.4698256"
      |      boundingbox="51.4154863,51.4823977,4.3793170,4.5481608"
      |      place_rank="14"
      |      address_rank="16"
      |      display_name="Essen, Antwerp, Flanders, 2910, Belgium"
      |      class="boundary"
      |      type="administrative"
      |      importance="0.4911576156520829"
      |  />
      |  <place
      |      place_id="139910255"
      |      osm_type="relation"
      |      osm_id="1101377"
      |      ref="Essen (Oldenburg)"
      |      lat="52.7224880"
      |      lon="7.9352306"
      |      boundingbox="52.6797506,52.7664132,7.8318507,8.0777375"
      |      place_rank="16"
      |      address_rank="16"
      |      display_name="Essen (Oldenburg), Cloppenburg district, Lower Saxony, 49632, Germany"
      |      class="boundary"
      |      type="administrative"
      |      importance="0.4024478844199759"
      |  />
      |</searchresults>
      |""".stripMargin
  }
}
