package kpn.core.tools.next.support

import kpn.api.custom.Tag
import kpn.api.custom.Timestamp
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.tools.config.Dirs
import kpn.core.tools.next.database.NextDatabase
import kpn.core.tools.next.database.NextDatabaseImpl
import kpn.core.tools.next.domain.RouteTagsDoc
import kpn.core.util.Log
import kpn.database.util.Mongo.codecRegistry
import org.apache.commons.io.FileUtils
import org.mongodb.scala.MongoClient

import java.io.File
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import scala.xml.XML

object AllRoutesCreateTagsTool {
  def main(args: Array[String]): Unit = {
    val client = MongoClient("mongodb://localhost:27017")
    try {
      val mongoDatabase = client.getDatabase("kpn-next").withCodecRegistry(codecRegistry)
      val database = new NextDatabaseImpl(mongoDatabase)
      val overpassQueryExecutor = new OverpassQueryExecutorRemoteImpl()
      val tool = new AllRoutesCreateTagsTool(database, overpassQueryExecutor)
      tool.createRouteTags()
    } finally {
      client.close()
    }
  }
}

class AllRoutesCreateTagsTool(database: NextDatabase, overpassQueryExecutor: OverpassQueryExecutor) {

  private val log = Log(classOf[AllRoutesCreateTagsTool])

  def createRouteTags(): Unit = {
    val routeIds = collectRouteIds()
    log.info(s"processing ${routeIds.size} routes")
    val tasks = routeIds.map { routeId =>
      new Callable[String]() {
        def call(): String = {
          loadRoute(routeId)
        }
      }
    }
    val executor = Executors.newFixedThreadPool(20)
    try {
      val futures = tasks.map { task =>
        executor.submit(task)
      }
      var todo = futures.size
      while (todo > 0) {
        Thread.sleep(5000)
        todo = futures.filterNot(_.isDone).size
        println(s"$todo/${futures.size}")
      }
    }
    finally {
      executor.shutdown()
    }
    println("done")
  }

  private def collectRouteIds(): Seq[Long] = {
    val file = new File(Dirs.root, "next/all-route-ids.txt")
    val allRouteIds = FileUtils.readFileToString(file, "UTF-8").split("\n").map(_.toLong)
    val loadedRouteIds = database.allRouteTags.ids()
    (allRouteIds.toSet -- loadedRouteIds.toSet).toSeq.sorted
  }

  private def loadRoute(routeId: Long): String = {
    val query = s"""[date:'${Timestamp.analysisStart.iso}'][timeout:1500][maxsize:24000000000];relation($routeId);out meta;"""
    val xmlString = overpassQueryExecutor.execute(query)
    val xml = XML.loadString(xmlString)
    (xml.head \ "relation").map { relationElem =>
      val tags = (relationElem \ "tag").map { t =>
        val key = (t \ "@k").text
        val value = (t \ "@v").text
        Tag(key, value)
      }
      database.allRouteTags.save(
        RouteTagsDoc(
          routeId,
          tags
        )
      )
    }
    "OK"
  }
}
