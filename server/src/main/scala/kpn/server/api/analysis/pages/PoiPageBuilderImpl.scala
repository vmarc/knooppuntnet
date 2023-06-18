package kpn.server.api.analysis.pages

import kpn.api.common.PoiPage
import kpn.api.common.poi.Poi
import kpn.api.custom.Tags
import kpn.core.poi.PoiConfiguration
import kpn.core.util.Log
import kpn.server.analyzer.engine.poi.PoiRef
import kpn.server.api.analysis.pages.poi.MasterPoiAnalyzer
import kpn.server.repository.PoiRepository
import org.springframework.stereotype.Component

@Component
class PoiPageBuilderImpl(poiRepository: PoiRepository, masterPoiAnalyzer: MasterPoiAnalyzer) extends PoiPageBuilder {

  private val log = Log(classOf[PoiPageBuilderImpl])

  def build(poiRef: PoiRef): Option[PoiPage] = {

    poiRepository.get(poiRef).map { poi =>

      var interpretedTagKeys: Set[String] = Set()

      interpretedTagKeys = interpretedTagKeys ++ PoiConfiguration.instance.tagKeys(poi.layers.head)

      val context = masterPoiAnalyzer.analyze(poi)

      val ignoredTags = Tags(poi.tags.tags.filter(t => context.ignoredTagKeys.contains(t.key)))

      val filteredTags = Tags(
        poi.tags.tags.filterNot(t =>
          context.ignoredTagKeys.contains(t.key)
            || context.processedTagKeys.contains(t.key)
            || interpretedTagKeys.contains(t.key)
            || context.ignoredTagKeyValues.contains(t)
        )
      )

      val interpretedTags = Tags(poi.tags.tags.filter(t => interpretedTagKeys.contains(t.key)))
      val processedTags = Tags(poi.tags.tags.filter(t => context.processedTagKeys.contains(t.key)))
      val processedTagKeyValues = Tags(poi.tags.tags.filter(t => context.ignoredTagKeyValues.contains(t)))

      logPoi(poi, filteredTags, interpretedTags, processedTags, ignoredTags ++ processedTagKeyValues)

      PoiPage(
        elementType = poi.elementType,
        elementId = poi.elementId,
        latitude = poi.latitude,
        longitude = poi.longitude,
        context.analysis.copy(mainTags = filteredTags)
      )
    }
  }

  private def logPoi(poi: Poi, filteredTags: Tags, interpretedTags: Tags, processedTags: Tags, ignoredTags: Tags): Unit = {
    val header = s"""${poi.elementType}:${poi.elementId} ${poi.layers.mkString(", ")}"""

    val tags = format("Reported", filteredTags) +
      format("Interpreted", interpretedTags) +
      format("Processed", processedTags) +
      format("Ignored", ignoredTags)

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
