package kpn.server.api.analysis.pages

import kpn.api.common.PoiPage
import kpn.api.common.poi.Poi
import kpn.api.custom.Tag
import kpn.core.poi.PoiConfiguration
import kpn.core.util.Log
import kpn.server.analyzer.engine.poi.PoiRef
import kpn.server.api.analysis.pages.poi.MasterPoiAnalyzer
import kpn.server.repository.PoiRepository
import org.springframework.stereotype.Component

@Component
class PoiPageBuilder(poiRepository: PoiRepository, masterPoiAnalyzer: MasterPoiAnalyzer) {

  private val log = Log(classOf[PoiPageBuilder])

  def build(poiRef: PoiRef): Option[PoiPage] = {

    poiRepository.get(poiRef).map { poi =>

      var interpretedTagKeys: Set[String] = Set()

      interpretedTagKeys = interpretedTagKeys ++ PoiConfiguration.instance.tagKeys(poi.layers.head)

      val context = masterPoiAnalyzer.analyze(poi)

      val ignoredTags = poi.tags.filter(t => context.ignoredTagKeys.contains(t.key))

      val filteredTags = poi.tags.filterNot(t =>
        context.ignoredTagKeys.contains(t.key)
          || context.processedTagKeys.contains(t.key)
          || interpretedTagKeys.contains(t.key)
          || context.ignoredTagKeyValues.contains(t)
      )

      val interpretedTags = poi.tags.filter(t => interpretedTagKeys.contains(t.key))
      val processedTags = poi.tags.filter(t => context.processedTagKeys.contains(t.key))
      val processedTagKeyValues = poi.tags.filter(t => context.ignoredTagKeyValues.contains(t))

      logPoi(
        poi,
        filteredTags,
        interpretedTags,
        processedTags,
        ignoredTags ++ processedTagKeyValues
      )

      PoiPage(
        elementType = poi.elementType,
        elementId = poi.elementId,
        latitude = poi.latitude,
        longitude = poi.longitude,
        context.analysis.copy(mainTags = filteredTags)
      )
    }
  }

  private def logPoi(poi: Poi, filteredTags: Seq[Tag], interpretedTags: Seq[Tag], processedTags: Seq[Tag], ignoredTags: Seq[Tag]): Unit = {
    val header = s"""${poi.elementType}:${poi.elementId} ${poi.layers.mkString(", ")}"""

    val tags = s"${format("Reported", filteredTags)}${format("Interpreted", interpretedTags)}${format("Processed", processedTags)}${format("Ignored", ignoredTags)}"

    val message = s"$header$tags"
    log.info(message)
  }

  private def format(title: String, tags: Seq[Tag]): String = {
    if (tags.nonEmpty) {
      s"""
  $title tags:${tags.map(tag => s"${tag.key}=${tag.value}").mkString("\n    ", "\n    ", "")}"""
    }
    else {
      ""
    }
  }
}
