package kpn.server.analyzer.engine.monitor

import kpn.api.custom.Relation
import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.segment.FragmentAnalyzer
import kpn.server.analyzer.engine.analysis.route.segment.SegmentBuilder
import org.apache.commons.io.FileUtils

import java.io.File
import java.nio.charset.Charset
import scala.xml.XML

object MonitorChangeProcessorDemo {
  def main(args: Array[String]): Unit = {
    new MonitorChangeProcessorDemo().analyze()
  }
}

class MonitorChangeProcessorDemo {

  private val routeId = 2067765L
  private val log = Log(classOf[MonitorChangeProcessorDemo])

  def analyze(): Unit = {
    val relation = log.infoElapsed {
      ("read relation from file", loadRelation())
    }

    val fragmentMap = new FragmentAnalyzer(Seq.empty, relation.wayMembers).fragmentMap

    log.infoElapsed {
      ("segment builder", new SegmentBuilder(fragmentMap).segments(fragmentMap.ids))
    }
    log.infoElapsed {
      ("segment builder", new SegmentBuilder(fragmentMap).segments(fragmentMap.ids))
    }
    val segments = log.infoElapsed {
      ("segment builder", new SegmentBuilder(fragmentMap).segments(fragmentMap.ids))
    }

    log.info("segments.size=" + segments.size)

  }

  private def loadRelation(): Relation = {
    val filename = s"/kpn/wrk/begin/$routeId.xml"
    val xmlString = FileUtils.readFileToString(new File(filename), Charset.forName("UTF-8"))
    val xml = XML.loadString(xmlString)
    val rawData = new Parser().parse(xml.head)
    new DataBuilder(rawData).data.relations(routeId)
  }

}

