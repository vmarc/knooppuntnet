package kpn.server.api.analysis.pages

import kpn.api.common.Poi
import kpn.api.common.PoiPage
import kpn.api.custom.Tag
import kpn.api.custom.Tags
import kpn.core.poi.PoiConfiguration
import kpn.core.util.Log
import kpn.server.analyzer.engine.poi.PoiRef
import kpn.server.repository.PoiRepository
import org.springframework.stereotype.Component

@Component
class PoiPageBuilderImpl(poiRepository: PoiRepository) extends PoiPageBuilder {

  private val log = Log(classOf[PoiPageBuilderImpl])

  private val processedTagKeys = Seq(
    "name",
    "description",
    "addr:city",
    "addr:postcode",
    "addr:street",
    "addr:housenumber",
    "addr:country",
    "website",
    "wikidata",
    "wikipedia",
    "contact:website",
    "contact:phone",
    "contact:email",
    "url",
    "image",
    "wheelchair"
  )

  private val ignoredTagKeys = Seq(
    "source",
    "source:date",
    "ref:bag",
    "ref:rce",
    "start_date",
    "addr:housename"
  )

  private val ignoredTagKeyValues = Seq(
    Tag("building", "yes")
  )

  def build(poiRef: PoiRef): Option[PoiPage] = {

    poiRepository.get(poiRef).map { poi =>

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

      val phone = poi.tags("contact:phone")
      val email = poi.tags("contact:email")
      val wikidata = poi.tags("wikidata").map { id =>
        "https://www.wikidata.org/wiki/" + id
      }
      val wikipedia = poi.tags("wikipedia").map { tagValue =>
        if (tagValue.startsWith("nl:")) {
          val id = tagValue.substring(3)
          "https://nl.wikipedia.org/wiki/" + id.replaceAll(" ", "_")
        }
        else if (tagValue.startsWith("de:")) {
          val id = tagValue.substring(3)
          "https://de.wikipedia.org/wiki/" + id.replaceAll(" ", "_")
        }
        else if (tagValue.startsWith("fr:")) {
          val id = tagValue.substring(3)
          "https://fr.wikipedia.org/wiki/" + id.replaceAll(" ", "_")
        }
        else if (tagValue.startsWith("en:")) {
          val id = tagValue.substring(3)
          "https://en.wikipedia.org/wiki/" + id.replaceAll(" ", "_")
        }
        else {
          tagValue
        }
      }

      val website: Option[String] = {
        Seq(
          poi.tags("website"),
          poi.tags("contact:website"),
          poi.tags("url")
        ).flatten.headOption.map { url =>
          if (url.startsWith("http://") || url.startsWith("https://")) {
            url
          }
          else {
            "http://" + url
          }
        }
      }

      val image = poi.tags("image")

      val ignoredTags = Tags(poi.tags.tags.filter(t => ignoredTagKeys.contains(t.key)))

      val filteredTags = Tags(
        poi.tags.tags.filterNot(t =>
          ignoredTagKeys.contains(t.key)
            || processedTagKeys.contains(t.key)
            || interpretedTagKeys.contains(t.key)
            || ignoredTagKeyValues.contains(t)
        )
      )

      val interpretedTags = Tags(poi.tags.tags.filter(t => interpretedTagKeys.contains(t.key)))
      val processedTags = Tags(poi.tags.tags.filter(t => processedTagKeys.contains(t.key)))
      val processedTagKeyValues = Tags(poi.tags.tags.filter(t => ignoredTagKeyValues.contains(t)))

      logPoi(poi, filteredTags, interpretedTags, processedTags, ignoredTags ++ processedTagKeyValues)

      val wheelchair = poi.tags("wheelchair")

      PoiPage(
        elementType = poi.elementType,
        elementId = poi.elementId,
        latitude = poi.latitude,
        longitude = poi.longitude,
        layers = poi.layers,
        mainTags = filteredTags,
        extraTags = Tags.empty,
        name = poi.tags("name"),
        subject = None,
        description = poi.tags("description"),
        addressLine1 = addressLine1,
        addressLine2 = addressLine2,
        phone,
        email,
        website = website,
        wikidata = wikidata,
        wikipedia = wikipedia,
        image = image,
        wheelchair = wheelchair
      )
    }
  }

  private def logPoi(poi: Poi, filteredTags: Tags, interpretedTags: Tags, processedTags: Tags, ignoredTags: Tags): Unit = {
    val header = s"""${poi.elementType}:${poi.elementId} ${poi.layers.mkString(", ")}"""

    val tags = format("Reported", filteredTags) +
      format("Interpreted", interpretedTags) +
      format("Processed", processedTags) +
      format("Ingored", ignoredTags)

    val message = s"$header$tags"
    log.info(message)
  }

  private def format(title: String, tags: Tags): String = {
    if (tags.nonEmpty) {
      s"\n  $title tags:" +
        tags.tags.map(tag => s"${tag.key}=${tag.value}").mkString("\n    ", "\n    ", "")
    }
    else {
      ""
    }
  }
}
