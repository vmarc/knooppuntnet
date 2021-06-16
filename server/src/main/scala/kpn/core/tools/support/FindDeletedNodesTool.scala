package kpn.core.tools.support

import kpn.core.data.DataBuilder
import kpn.core.database.Database
import kpn.core.db.couch.Couch
import kpn.core.loadOld.Parser
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.core.overpass.QueryNodes
import kpn.server.repository.NodeRepositoryImpl
import org.xml.sax.SAXParseException

import scala.xml.XML

object FindDeletedNodesTool {

  def main(args: Array[String]): Unit = {

    if (args.length < 2) {
      println("Usage: FindDeletedNodesTool host analysisDatabaseName")
      System.exit(-1)
    }
    val host = args(0)
    val analysisDatabaseName = args(1)
    val executor = new OverpassQueryExecutorImpl()
    Couch.executeIn(host, analysisDatabaseName) { database =>
      new FindDeletedNodesTool(database, executor).report()
    }
  }
}

class FindDeletedNodesTool(
  database: Database,
  executor: OverpassQueryExecutor
) {

  private val nodeRepository = new NodeRepositoryImpl(null, database, false)

  def report(): Unit = {
    println("Collecting node ids")
    val nodeIds = nodeRepository.allNodeIds()
    println(s"${nodeIds.size} nodes")
    nodeIds.sliding(50, 50).zipWithIndex.foreach { case (ids, index) =>
      println(s"${index * 50}/${nodeIds.size}")
      val nodes = nodeRepository.nodesWithIds(ids)
      val activeNodeIds = nodes.filter(_.active).map(_.id)
      val xmlString = executor.executeQuery(None, QueryNodes("nodes", activeNodeIds))
      val xml = try {
        XML.loadString(xmlString)
      }
      catch {
        case e: SAXParseException =>
          val msg = s"Could not load nodes [ids=$activeNodeIds]\n$xmlString"
          throw new RuntimeException(msg, e)
      }

      val rawData = new Parser().parse(xml.head)
      val data = new DataBuilder(rawData).data
      val foundNodeIds = data.nodes.keys
      val deletedNodeIds = (activeNodeIds.toSet -- foundNodeIds.toSet).toSeq.sorted
      if (deletedNodeIds.nonEmpty) {
        println("deleted nodeIds: " + deletedNodeIds)
      }
    }
    println("done")
  }
}
