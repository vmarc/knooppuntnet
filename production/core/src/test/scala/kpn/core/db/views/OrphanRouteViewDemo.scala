package kpn.core.db.views

import kpn.core.db.couch.Couch
import kpn.core.repository.OrphanRepositoryImpl
import kpn.core.repository.RouteRepositoryImpl
import kpn.core.repository.TaskRepository
import kpn.core.repository.TaskRepositoryImpl
import kpn.shared.NetworkType
import spray.json.JsString

import scala.io.Source

object OrphanRouteViewDemo {

  def main(args: Array[String]): Unit = {

    Couch.executeIn("master") { database =>
      val repo = new OrphanRepositoryImpl(database)
      val rwnRouteIds = repo.ignoredRouteIds(NetworkType.hiking)
      val rcnRouteIds = repo.ignoredRouteIds(NetworkType.bicycle)

      println("rwnRouteIds.size=" + rwnRouteIds.size)
      println("rcnRouteIds.size=" + rcnRouteIds.size)

      val rwnNodeIds = repo.ignoredNodeIds(NetworkType.hiking)
      val rcnNodeIds = repo.ignoredNodeIds(NetworkType.bicycle)

      println("rwnNodeIds.size=" + rwnNodeIds.size)
      println("rcnNodeIds.size=" + rcnNodeIds.size)
    }


    //    // val changeSetIdsInTasks = readChangeSetIdsInTasks()
    //    val referencedChangeSetIds = readChangeSetIdsInFiles()
    //    val changeSetIds = readChangeSetIds()
    //    val toBeAdded = referencedChangeSetIds -- changeSetIds
    //
    //    println(s"toBeAdded = ${toBeAdded.size}")
    //
    //    addTasks(toBeAdded)
    //
    ////    deleteTasks(toBeDeleted)
    //
    //    // readChangeSetIds()
    //    // readChangeSetIdsInRouteAnalysis()
    //    // readChangeSetIdsInNodeAnalysis()
    //    // readChangeSetIdsInRoutes()
    //    // val ids = readIgnoredRouteIds()
    //    // deleteRouteVersions(ids)
    //
    //    println("Done")
  }

  private def readChangeSetIdsInFiles(): Set[Long] = {
    val routeAnalysisChangeSetIds = readChangeSetIdsInFile("/kpn/logs/routeAnalysisChangeSetIds")
    val nodeAnalysisChangeSetIds = readChangeSetIdsInFile("/kpn/logs/nodeAnalysisChangeSetIds")
    val routeChangeSetIds = readChangeSetIdsInFile("/kpn/logs/routeChangeSetIds")
    val all = routeAnalysisChangeSetIds ++ nodeAnalysisChangeSetIds ++ routeChangeSetIds

    println(s"routeAnalysisChangeSetIds = ${routeAnalysisChangeSetIds.size}")
    println(s"nodeAnalysisChangeSetIds = ${nodeAnalysisChangeSetIds.size}")
    println(s"routeChangeSetIds = ${routeChangeSetIds.size}")
    println(s"all = ${all.size}")

    all
  }

  private def readChangeSetIdsInFile(filename: String): Set[Long] = {
    Source.fromFile(filename).getLines().map(_.toLong).toSet
  }

  private def readChangeSetIdsInTasks(): Set[Long] = {
    var ids: Set[Long] = Set()
    Couch.executeIn("tasks") { database =>
      val repo = new TaskRepositoryImpl(database)
      val taskIds = repo.all(TaskRepository.changeSetInfoTask)
      println(s"task count = ${taskIds.size}")
      ids = taskIds.map(task => task.substring(TaskRepository.changeSetInfoTask.length).toLong).toSet
    }
    ids
  }

  private def readChangeSetIds(): Set[Long] = {
    var ids: Set[Long] = Set()
    Couch.executeIn("changes3") { database =>
      val startKey = "change-set-info:"
      val endKey = startKey + "999999999999999"
      val keys = database.keys(startKey, endKey, Couch.defaultTimeout).flatMap {
        case JsString(key) => Some(key)
        case _ => None
      }
      ids = keys.map(_.toString.substring(startKey.length).toLong).toSet
    }
    println(s"changeSets in database = ${ids.size}")
    ids
  }

  private def readIgnoredRouteIds(): Set[Long] = {

    var ids: Set[Long] = Set()

    Couch.executeIn("master3") { database =>

      val routeRepository = new RouteRepositoryImpl(database)

      val routeSummaries = database.query(AnalyzerDesign, OrphanRouteView, Couch.uiTimeout)(true, true).map(OrphanRouteView.convert)
      ids = routeSummaries.map(_.id).toSet
      println(s"ids.size=${ids.size}")

      //      routeSummaries.foreach { routeSummary =>
      //        routeRepository.routeWithId(routeSummary.id) match {
      //          case None => println(s"${routeSummary.id} ${routeSummary.name}: route details not found")
      //          case Some(routeInfo) =>
      //            println(s"${routeSummary.id} ${routeSummary.name}, facts= ${routeInfo.facts.map(_.name).mkString(",")}")
      //        }
      //      }
    }
    ids
  }

  //    Couch.executeIn("changes3") { database =>
  //      val routeAnalysisRepository = new RouteAnalysisRepositoryImpl(database)
  //      401 to 999 foreach { version =>
  //        println("delete " + version)
  //        routeAnalysisRepository.delete(100366, version)
  //      }
  //    }

  private def deleteTasks(ids: Set[Long]): Unit = {
    Couch.executeIn("tasks") { database =>
      ids.zipWithIndex.foreach { case (id, index) =>
        val key = TaskRepository.changeSetInfoTask + id
        println(s"delete $index/${ids.size} - $key")
        database.delete(key)
      }
    }
  }

  private def addTasks(ids: Set[Long]): Unit = {
    Couch.executeIn("tasks") { database =>
      val repo = new TaskRepositoryImpl(database)
      ids.toSeq.zipWithIndex.foreach { case (id, index) =>
        val task = TaskRepository.changeSetInfoTask + id
        if ((index % 100) == 0) {
          println(s"$index/${ids.size} $task")
        }
        repo.add(task)
      }
    }
  }
}
