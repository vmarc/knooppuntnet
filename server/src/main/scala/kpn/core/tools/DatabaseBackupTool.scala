package kpn.core.tools

import java.io.File

import kpn.core.db.couch.Couch
import kpn.core.db.couch.OldDatabase
import kpn.core.util.GZipFile
import spray.json.JsString

object DatabaseBackupTool {
  def main(args: Array[String]): Unit = {
    //new DatabaseBackupTool().backupMaster()
    new DatabaseBackupTool().restoreMaster()
  }
}

class DatabaseBackupTool {

  def restoreMaster(): Unit = {

    val root = "/tmp/kpn/"
    val dirs = new File(root).listFiles()
    val filenames = dirs.sorted.flatMap { dir =>
      dir.listFiles().map(_.getAbsolutePath).sorted
    }
    Couch.executeIn("testdb") { database =>
      println("count=" + filenames.length)
      filenames.zipWithIndex.foreach { case (filename, index) =>
        val id = filename.drop(root.length + 7).takeWhile(_ != '.')
        val value = GZipFile.read(filename)
        val newValue = "{" + (value.dropWhile(_ != ',').drop(1).dropWhile(_ != ',').drop(1))
        println(s"${index + 1}/${filenames.length} $id $filename ${value.length} bytes")
        // TODO database.saveString(id, newValue)
      }
    }
  }


  def backupMaster(): Unit = {
    Couch.executeIn("master") { database =>
      val keys = Seq(
        "network",
        "network-gpx",
        "network-timestamp",
        "node",
        "route"
      ).flatMap { documentType =>
        collectKeys(database, documentType)
      }

      println("total keys=" + keys.size)

      val root = "/tmp/kpn/"

      keys.zipWithIndex.sliding(1000, 1000).zipWithIndex.foreach { case (subset: Seq[(String, Int)], dirIndex: Int) =>
        val dir = root + (1000000 + dirIndex + 1).toString.drop(1) + "/"
        new File(dir).mkdirs()
        subset.foreach { case (key, index) =>
          val filename = dir + key + ".gz"
          println(s"${index + 1}/${keys.size} $filename")
          val json = database.getJsonString(key)
          GZipFile.write(filename, json)
        }
      }
    }
  }

  private def collectKeys(database: OldDatabase, documentType: String): Seq[String] = {
    val startKey = documentType + ":"
    val endKey = documentType + ":999999999999"
    val keys = database.keys(startKey, endKey, Couch.defaultTimeout).flatMap {
      case JsString(key) => Some(key)
      case _ => None
    }
    println(s"$documentType ${keys.size} documents")
    keys
  }
}
