package kpn.server.api.analysis.pages

import kpn.api.common.PoiPage
import kpn.api.custom.Tags
import kpn.core.poi.PoiConfiguration
import kpn.server.analyzer.engine.poi.PoiRef
import kpn.server.repository.PoiRepository
import org.springframework.stereotype.Component

@Component
class PoiPageBuilderImpl(poiRepository: PoiRepository) extends PoiPageBuilder {

  private val processedTagKeys = Seq(
    "name",
    "description",
    "addr:city",
    "addr:postcode",
    "addr:street",
    "addr:housenumber",
    "addr:country",
    "website",
    "image"
  )

  private val extraTagKeys = Seq(
    "source",
    "source:date",
    "ref:bag",
    "ref:rce",
    "mapillary",
    "building=yes",
    "start_date"
  )


  def build(poiRef: PoiRef): Option[PoiPage] = {

    poiRepository.poi(poiRef).map { poi =>

      var interpretedTagKeys: Set[String] = Set()

      interpretedTagKeys = interpretedTagKeys ++ PoiConfiguration.instance.tagKeys(poi.layers.head)

      val city = poi.tags("addr:city")
      val postcode = poi.tags("addr:postcode")
      val street = poi.tags("addr:street")
      val housenumber = poi.tags("addr:housenumber")


      val addressLine1: Option[String] = street match {
        case Some(s) =>
          housenumber match {
            case Some(n) => Some(s + " " + n)
            case None => street
          }
        case None => None
      }

      val addressLine2: Option[String] = postcode match {
        case Some(p) =>
          city match {
            case Some(c) => Some(p + " " + c)
            case None => postcode
          }
        case None => city
      }

      val website = poi.tags("website")
      val image = poi.tags("image")

      val extraTags = Tags(poi.tags.tags.filter(t => extraTagKeys.contains(t.key)))

      val filteredTags = Tags(
        poi.tags.tags.filterNot(t =>
          extraTagKeys.contains(t.key)
            || processedTagKeys.contains(t.key)
            || interpretedTagKeys.contains(t.key)
        )
      )

      PoiPage(
        elementType = poi.elementType,
        elementId = poi.elementId,
        latitude = poi.latitude,
        longitude = poi.longitude,
        layers = poi.layers,
        mainTags = filteredTags,
        extraTags = extraTags,
        name = poi.tags("name"),
        subject = None,
        description = poi.tags("description"),
        addressLine1 = addressLine1,
        addressLine2 = addressLine2,
        website = website,
        image = image
      )
    }
  }

}
