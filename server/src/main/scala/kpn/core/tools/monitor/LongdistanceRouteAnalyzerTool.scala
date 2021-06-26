package kpn.core.tools.monitor

import kpn.api.common.LatLonImpl
import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.monitor.LongdistanceRouteChange
import kpn.api.custom.Relation
import kpn.api.custom.Timestamp
import kpn.core.data.DataBuilder
import kpn.core.db.couch.Couch
import kpn.core.loadOld.Parser
import kpn.core.tools.monitor.LongdistanceRouteAnalyzer.toGeoJson
import kpn.core.tools.monitor.LongdistanceRouteAnalyzer.toRouteSegments
import kpn.core.util.Log
import kpn.server.repository.ChangeSetInfoRepository
import kpn.server.repository.ChangeSetInfoRepositoryImpl
import kpn.server.repository.LongdistanceRouteRepository
import kpn.server.repository.LongdistanceRouteRepositoryImpl
import org.apache.commons.io.FileUtils
import org.locationtech.jts.geom.LineString

import java.io.File
import java.nio.charset.Charset
import scala.xml.XML

object LongdistanceRouteAnalyzerTool {
  def main(args: Array[String]): Unit = {
    Couch.executeIn("kpn-database", "changesets2") { changeSetDatabase =>
      Couch.executeIn("kpn-database", "analysis1") { analysisDatabase =>
        val longdistanceRouteRepository = new LongdistanceRouteRepositoryImpl(analysisDatabase)
        val changeSetInfoRepository = new ChangeSetInfoRepositoryImpl(null, changeSetDatabase, false)
        new LongdistanceRouteAnalyzerTool(longdistanceRouteRepository, changeSetInfoRepository).analyze()
      }
    }
  }
}

class LongdistanceRouteAnalyzerTool(
  longdistanceRouteRepository: LongdistanceRouteRepository,
  changeSetInfoRepository: ChangeSetInfoRepository
) {

  private val routeId = 3121667L
  private val gpxFilename = "GR5_2020-08-01.gpx"
  private val log = Log(classOf[LongdistanceRouteAnalyzerTool])

  def analyze(): Unit = {

    val referenceRelation = readRelation(s"/kpn/wrk/begin/$routeId.xml")
    val referenceRouteSegments = toRouteSegments(referenceRelation)
    val gpxLineString = referenceRouteSegments.head.lineString
    log.info(s"Reference situation: ${toRouteSegments(referenceRelation).size} segments")

    val files = new File("/kpn/wrk").list().filterNot(_ == "begin").toSeq.sorted
    files.zipWithIndex.foreach { case (changeSetId, index) =>
      Log.context(s"${index + 1}/$changeSetId") {
        analyzeChangeSet(changeSetId, gpxLineString)
      }
    }
  }

  private def analyzeChangeSet(changeSetId: String, gpxLineString: LineString): Unit = {

    val beforeRelation = readRelation(s"/kpn/wrk/$changeSetId/$routeId-before.xml")
    val beforeRouteSegments = toRouteSegments(beforeRelation)
    val beforeRoute = LongdistanceRouteAnalyzer.analyze(gpxFilename, gpxLineString, beforeRelation, beforeRouteSegments)

    val afterRelation = readRelation(s"/kpn/wrk/$changeSetId/$routeId-after.xml")
    val afterRouteSegments = toRouteSegments(afterRelation)
    val afterRoute = LongdistanceRouteAnalyzer.analyze(gpxFilename, gpxLineString, afterRelation, afterRouteSegments)

    val wayIdsBefore = beforeRelation.wayMembers.map(_.way.id).toSet
    val wayIdsAfter = afterRelation.wayMembers.map(_.way.id).toSet

    val wayIdsAdded = (wayIdsAfter -- wayIdsBefore).size
    val wayIdsRemoved = (wayIdsBefore -- wayIdsAfter).size

    val wayIdsUpdated = wayIdsAfter.intersect(wayIdsBefore).count { wayId =>
      val wayBefore = beforeRelation.wayMembers.filter(_.way.id == wayId).head.way
      val wayAfter = afterRelation.wayMembers.filter(_.way.id == wayId).head.way
      val latLonsBefore = wayBefore.nodes.map(node => LatLonImpl(node.latitude, node.longitude))
      val latLonsAfter = wayAfter.nodes.map(node => LatLonImpl(node.latitude, node.longitude))
      !latLonsBefore.equals(latLonsAfter)
    }

    if ((wayIdsAdded + wayIdsRemoved + wayIdsUpdated) == 0) {
      log.info("No geometry changes, no further analysis")
    }
    else {
      val beforeGeoJons = beforeRoute.nokSegments.map(_.geoJson)
      val afterGeoJons = afterRoute.nokSegments.map(_.geoJson)

      val newSegments = afterRoute.nokSegments.filterNot(nokSegment => beforeGeoJons.contains(nokSegment.geoJson))
      val resolvedSegments = beforeRoute.nokSegments.filterNot(nokSegment => afterGeoJons.contains(nokSegment.geoJson))

      val message = s"ways=${afterRoute.wayCount} $wayIdsAdded/$wayIdsRemoved/$wayIdsUpdated," ++
        s" osm=${afterRoute.osmDistance}," ++
        s" gpx=${afterRoute.gpxDistance}," ++
        s" osmSegments=${afterRoute.osmSegments.size}," ++
        s" nokSegments=${afterRoute.nokSegments.size}," ++
        s" new=${newSegments.size}," ++
        s" resolved=${resolvedSegments.size}"

      val timestamp = changeSetInfoRepository.get(changeSetId.toLong) match {
        case None => Timestamp(2020, 8, 11, 0, 0, 0)
        case Some(changeSetInfo) => changeSetInfo.createdAt
      }

      val key = ChangeKey(
        1,
        timestamp,
        changeSetId.toLong,
        routeId
      )

      val referenceJson = if (newSegments.nonEmpty || resolvedSegments.nonEmpty) {
        toGeoJson(gpxLineString)
      }
      else {
        ""
      }

      val routeSegments = if (newSegments.nonEmpty || resolvedSegments.nonEmpty) {
        afterRoute.osmSegments
      }
      else {
        Seq()
      }

      val change = LongdistanceRouteChange(
        key,
        None,
        afterRoute.wayCount,
        wayIdsAdded,
        wayIdsRemoved,
        wayIdsUpdated,
        afterRoute.osmDistance,
        afterRoute.gpxDistance,
        afterRoute.gpxFilename.getOrElse(gpxFilename),
        afterRoute.bounds,
        referenceJson,
        afterRoute.osmSegments.size,
        routeSegments,
        newSegments,
        resolvedSegments,
        happy = resolvedSegments.nonEmpty,
        investigate = newSegments.nonEmpty
      )

      longdistanceRouteRepository.saveChange(change)
      log.info(message)
    }
  }

  private def readRelation(filename: String): Relation = {
    val xmlString = FileUtils.readFileToString(new File(filename), Charset.forName("UTF-8"))
    val xml = XML.loadString(xmlString)
    val rawData = new Parser().parse(xml.head)
    new DataBuilder(rawData).data.relations(routeId)
  }

}
