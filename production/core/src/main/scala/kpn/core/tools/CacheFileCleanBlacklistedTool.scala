package kpn.core.tools

import java.io.File

import kpn.core.app.ActorSystemConfig
import kpn.core.db.couch.Couch
import kpn.core.db.couch.DatabaseImpl
import kpn.core.engine.changes.data.BlackList
import kpn.core.repository.BlackListRepositoryImpl

object CacheFileCleanBlacklistedTool {
  def main(args: Array[String]): Unit = {
    val blackList = {
      val system = ActorSystemConfig.actorSystem()
      val couchConfig = Couch.config
      val couch = new Couch(system, couchConfig)
      val database = new DatabaseImpl(couch, "master")
      val blackListRepository = new BlackListRepositoryImpl(database)
      val bl = blackListRepository.get
      system.terminate()
      bl
    }

    new CacheFileCleanBlacklistedTool(blackList, "/kpn/cache/").processDirs()
  }
}

class CacheFileCleanBlacklistedTool(blackList: BlackList, sourceDir: String) {

  private val blackListedRelationIds: Set[String] = {
    val routeIds = blackList.routes.map(_.id.toString)
    val networkIds = blackList.networks.map(_.id.toString)
    (routeIds ++ networkIds).toSet
  }

  def processDirs(): Unit = {
    processSubDirs(new File(sourceDir), 0)
  }

  private def processSubDirs(dir: File, level: Int): Unit = {
    if (level == 2) {
      val relativeDirName = dir.getAbsolutePath.substring(sourceDir.length)
      println(s"# $relativeDirName")
    }
    if (level == 6) {
      processLeafDir(dir)
    }
    else {
      val files = dir.listFiles.toSeq.sorted
      files.foreach { file =>
        if (file.isDirectory) {
          processSubDirs(file, level + 1)
        }
      }
    }
  }

  private def processLeafDir(dir: File): Unit = {
    val files = dir.listFiles.toSeq.sorted
    files.foreach { file =>
      if (isBlackListed(file.getName)) {
        val relativeDirName = file.getAbsolutePath.substring(sourceDir.length)
        println(s"rm $relativeDirName")
      }
    }
  }

  private def isBlackListed(filename: String): Boolean = {
    if (filename.startsWith("relation-") && filename.endsWith(".xml.gz")) {
      val relationId = filename.substring("relation-".length).dropRight(".xml.gz".length)
      blackListedRelationIds.contains(relationId)
    }
    else {
      false
    }
  }

}
