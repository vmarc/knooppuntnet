package kpn.server.analyzer.engine.poi

import kpn.api.common.ReplicationId
import kpn.api.common.poi.Poi
import kpn.api.custom.Timestamp
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.analysis.location.LocationAnalyzerImpl
import kpn.server.analyzer.engine.changes.changes.OsmChange
import kpn.server.analyzer.engine.changes.OsmChangeRepository
import kpn.server.analyzer.engine.changes.changes.OsmChangeParser
import kpn.server.analyzer.engine.tile.OldTileCalculatorImpl
import kpn.server.api.analysis.pages.poi.MasterPoiAnalyzerImpl
import kpn.server.repository.PoiRepositoryImpl
import kpn.server.repository.TaskRepository

import scala.xml.InputSource
import scala.xml.XML

class OsmChangeRepositoryTestImpl extends OsmChangeRepository {

  def get(replicationId: ReplicationId): OsmChange = {
    val filename = s"/case-studies/minute-diff-5719419.xml"
    val stream = getClass.getResourceAsStream(filename)
    val inputSource = new InputSource(stream)
    val xml = XML.load(inputSource)
    new OsmChangeParser().parse(xml)
  }

  def timestamp(replicationId: ReplicationId): Timestamp = {
    throw new RuntimeException("Not implemented")
  }
}

class KnownPoiCacheTestImpl extends KnownPoiCache {

  private var knownPois = KnownPois()

  override def contains(poiRef: PoiRef): Boolean = {
    poiRef.elementType match {
      case "node" => knownPois.nodeIds.contains(poiRef.elementId)
      case "way" => knownPois.wayIds.contains(poiRef.elementId)
      case "relation" => knownPois.relationIds.contains(poiRef.elementId)
    }
  }

  override def add(poiRef: PoiRef): Unit = {
    knownPois = poiRef.elementType match {
      case "node" => knownPois.copy(nodeIds = knownPois.nodeIds + poiRef.elementId)
      case "way" => knownPois.copy(wayIds = knownPois.wayIds + poiRef.elementId)
      case "relation" => knownPois.copy(relationIds = knownPois.relationIds + poiRef.elementId)
    }
  }

  override def delete(poiRef: PoiRef): Unit = {
    knownPois = poiRef.elementType match {
      case "node" => knownPois.copy(nodeIds = knownPois.nodeIds - poiRef.elementId)
      case "way" => knownPois.copy(wayIds = knownPois.wayIds - poiRef.elementId)
      case "relation" => knownPois.copy(relationIds = knownPois.relationIds - poiRef.elementId)
    }
  }
}

class PoiRepositoryTestImpl(database: Database) extends PoiRepositoryImpl(database) {

  private val log = Log(classOf[PoiRepositoryTestImpl])

  override def save(poi: Poi): Unit = {
    log.info(s"save poi ${poi._id}")
  }

  override def delete(poiRef: PoiRef): Unit = {
    log.info(s"delete poi ${poiRef.toId}")
  }
}

class TaskRepositoryTestImpl() extends TaskRepository {

  private val tasks = scala.collection.mutable.ListBuffer[String]()

  override def add(id: String): Unit = {
    tasks += id
  }

  override def delete(id: String): Unit = {
    throw new RuntimeException("Not implemented")
  }

  override def exists(id: String): Boolean = {
    throw new RuntimeException("Not implemented")
  }

  override def all(prefix: String): Seq[String] = {
    throw new RuntimeException("Not implemented")
  }

  def taskCount: Int = tasks.size
}

class OverpassQueryExecutorTestImpl extends OverpassQueryExecutorRemoteImpl {
  var requestCount = 0

  override def execute(queryString: String): String = {
    requestCount = requestCount + 1
    super.execute(queryString)
  }
}

object PoiChangeAnalyzerPerformanceTool {
  private val log = Log(classOf[PoiChangeAnalyzerPerformanceTool])

  def main(args: Array[String]): Unit = {

    // Configurator.setRootLevel(Level.DEBUG)
    // Configurator.setLevel("kpn.core.overpass.OverpassQueryExecutorRemoteImpl", Level.DEBUG)

    Mongo.executeIn("kpn-prod") { database =>
      val overpassQueryExecutor = new OverpassQueryExecutorTestImpl()
      val osmChangeRepository = new OsmChangeRepositoryTestImpl()
      val knownPoiCache = new KnownPoiCacheTestImpl()
      val poiRepository = new PoiRepositoryTestImpl(database)
      val tileCalculator = new OldTileCalculatorImpl()
      val taskRepository = new TaskRepositoryTestImpl()
      val poiQueryExecutor = new PoiQueryExecutorImpl(overpassQueryExecutor)
      val locationAnalyzer = new LocationAnalyzerImpl(analyzerEnabled = true)
      val poiScopeAnalyzer = new PoiScopeAnalyzerImpl(locationAnalyzer)
      val masterPoiAnalyzer = new MasterPoiAnalyzerImpl()
      val poiChangeAnalyzer = new PoiChangeAnalyzerImpl(
        knownPoiCache,
        poiRepository,
        tileCalculator,
        taskRepository,
        poiScopeAnalyzer,
        poiQueryExecutor,
        locationAnalyzer,
        masterPoiAnalyzer
      )

      new PoiChangeAnalyzerPerformanceTool(
        osmChangeRepository,
        poiChangeAnalyzer
      ).analyze(ReplicationId("005/719/419"))

      log.info(s"${taskRepository.taskCount} tasks added")
      log.info(s"${overpassQueryExecutor.requestCount} overpass requests")
    }
  }
}

class PoiChangeAnalyzerPerformanceTool(
  osmChangeRepository: OsmChangeRepository,
  poiChangeAnalyzer: PoiChangeAnalyzer
) {

  def analyze(replicationId: ReplicationId): Unit = {
    val osmChange = readOsmChange(replicationId)
    PoiChangeAnalyzerPerformanceTool.log.infoElapsed {
      poiChangeAnalyzer.analyze(osmChange)
      ("analysis", ())
    }
  }

  private def readOsmChange(replicationId: ReplicationId): OsmChange = {
    val osmChange = osmChangeRepository.get(replicationId)
    val actionCount = osmChange.actions.size
    val changeSetCount = osmChange.allChangeSetIds.size
    val elements = osmChange.actions.flatMap(_.elements)
    val elementCount = elements.size
    val nodeCount = elements.count(_.isNode)
    val wayCount = elements.count(_.isWay)
    val relationCount = elements.count(_.isRelation)
    PoiChangeAnalyzerPerformanceTool.log.info(s"${replicationId.name} $actionCount actions, $changeSetCount changesets, $elementCount elements, $nodeCount nodes, $wayCount ways, $relationCount relations")
    osmChange
  }
}
