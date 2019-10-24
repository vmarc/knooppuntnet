package kpn.core.tools

import kpn.core.db.couch.Couch
import kpn.core.db.couch.Database
import kpn.core.db.views.ViewRow
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.core.overpass.QueryNode
import kpn.core.overpass.QueryNodeIds
import kpn.shared.NetworkType
import spray.json.JsValue

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
    def toNodeId(row: JsValue): Long = {
      val docId = new ViewRow(row).id.toString
      val routeId = docId.drop("node:".length + 1).dropRight(1)
      routeId.toLong
    }

    val request = """_design/AnalyzerDesign/_view/DocumentView?startkey="node"&endkey="node:a"&reduce=false&stale=ok"""
    database.old.getRows(request).map(toNodeId).toSet
  }


  private def readOverpassNodeIds(): Set[Long] = {
    NetworkType.all.flatMap(readOverpassNodeIdsWithNetworkType).toSet
  }

  private def readOverpassNodeIdsWithNetworkType(networkType: NetworkType): Set[Long] = {
    println(s"read overpass nodes ${networkType.name}")
    //    val xmlString = FileUtils.readFileToString(new File(s"/kpn/tmp/${networkType.name}.xml"))
    val query = QueryNodeIds(networkType)
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
