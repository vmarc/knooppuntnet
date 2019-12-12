package kpn.server.analyzer.engine.poi

import kpn.api.common.Poi
import kpn.api.common.changes.ChangeAction.Create
import kpn.api.common.changes.ChangeAction.Delete
import kpn.api.common.changes.ChangeAction.Modify
import kpn.api.common.data.raw.RawElement
import kpn.api.common.data.raw.RawNode
import kpn.api.common.data.raw.RawRelation
import kpn.api.common.data.raw.RawWay
import kpn.api.common.tiles.ZoomLevel
import kpn.api.custom.Tags
import kpn.core.poi.PoiConfiguration
import kpn.core.poi.PoiDefinition
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzer
import kpn.server.analyzer.engine.changes.changes.OsmChange
import kpn.server.analyzer.engine.tile.TileCalculator
import kpn.server.repository.PoiRepository
import kpn.server.repository.TaskRepository
import org.springframework.stereotype.Component

@Component
class PoiChangeAnalyzerImpl(
  knownPoiCache: KnownPoiCache,
  poiRepository: PoiRepository,
  countryAnalyzer: CountryAnalyzer,
  tileCalculator: TileCalculator,
  taskRepository: TaskRepository,
  poiUpdateProcessor: PoiUpdateProcessor,
  poiDeleteProcessor: PoiDeleteProcessor
) extends PoiChangeAnalyzer {

  private val log = Log(classOf[PoiChangeAnalyzerImpl])

  override def analyze(osmChange: OsmChange): Unit = {
    osmChange.actions.foreach { change =>
      change.action match {
        case Create => processElements(change.elements)
        case Modify => processElements(change.elements)
        case Delete => processDelete(change.elements)
      }
    }
  }

  private def processElements(elements: Seq[RawElement]): Unit = {
    elements.foreach { element =>

    }
  }


  private def processDelete(elements: Seq[RawElement]): Unit = {
    elements.foreach { element =>
      poiDeleteProcessor.delete(PoiRef.of(element))
    }
  }


}
