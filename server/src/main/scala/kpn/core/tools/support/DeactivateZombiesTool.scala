package kpn.core.tools.support

import kpn.api.custom.ScopedNetworkType
import kpn.core.database.Database
import kpn.core.database.views.analyzer.DocumentView
import kpn.core.db.couch.Couch
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.core.overpass.QueryNode
import kpn.core.overpass.QueryNodeIds

import scala.xml.XML

object DeactivateZombiesTool {

  def main(args: Array[String]): Unit = {

    if (args.length < 2) {
      println("Usage: DeactivateZombiesTool host masterDbName")
      System.exit(-1)
    }
    val host = args(0)
    val masterDbName = args(1)
    val executor = new OverpassQueryExecutorImpl()
    Couch.executeIn(host, masterDbName) { database =>
      new DeactivateZombiesTool(database, executor).run()
    }
  }
}

class DeactivateZombiesTool(database: Database, executor: OverpassQueryExecutor) {

  def run(): Unit = {

    println("Start")

    val couchdbNodeIds = readCouchdbNodeIds()
    println("couchdbNodeIds.size=" + couchdbNodeIds.size)

    //    val overpassNodeIds = readOverpassNodeIds()
    //    println("overpassNodeIds.size=" + overpassNodeIds.size)
    //
    //    val nonActiveNodeIds = couchdbNodeIds -- overpassNodeIds
    //    println("nonActiveNodeIds.size=" + nonActiveNodeIds.size)
    //
    //    val repo = new NodeRepositoryImpl(database)
    //    nonActiveNodeIds.foreach { nodeId =>
    //      repo.nodeWithId(nodeId, Couch.uiTimeout).foreach { nodeInfo =>
    //        if (nodeInfo.active) {
    //          println("zombie " + nodeInfo)
    //          readOverpassNode(nodeInfo.id)
    //        }
    //      }
    //    }
  }

  private def readCouchdbNodeIds(): Set[Long] = {
    DocumentView.allNodeIds(database).toSet
  }

  private def readOverpassNodeIds(): Set[Long] = {
    ScopedNetworkType.all.flatMap(readOverpassNodeIdsWithTagKey).toSet
  }

  private def readOverpassNodeIdsWithTagKey(scopedNetworkType: ScopedNetworkType): Set[Long] = {
    println(s"read overpass nodes ${scopedNetworkType.key}")
    //    val xmlString = FileUtils.readFileToString(new File(s"/kpn/tmp/${networkType.name}.xml"))
    val query = QueryNodeIds(scopedNetworkType)
    val xmlString = executor.executeQuery(None, query)
    val xml = XML.loadString(xmlString)
    (xml \ "node").map { n => (n \ "@id").text.toLong }.toSet
  }

  private def readOverpassNode(nodeId: Long): Unit = {
    val query = QueryNode(nodeId)
    val xmlString = executor.executeQuery(None, query)
    println(xmlString)
  }

}
