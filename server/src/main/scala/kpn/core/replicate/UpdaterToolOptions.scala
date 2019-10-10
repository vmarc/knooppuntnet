package kpn.core.replicate

import java.io.File

case class UpdaterToolOptions(rootDir: File = new File("/kpn")) {

  def databaseDir: File = new File(rootDir, "database")

  def replicateIdFile: File = new File(rootDir, "database/replicate_id")

  def replicateDir: File = new File(rootDir, "replicate")

  def changesDir: File = new File(rootDir, "changes")

  def tmpDir: File = new File(rootDir, "tmp")

  def overpassQuery: File = new File(rootDir, "overpass/bin/osm3s_query")

  def overpassUpdate: File = new File(rootDir, "overpass/bin/update_from_dir")

}

object UpdaterToolOptions {

  def parse(args: Array[String]): Option[UpdaterToolOptions] = {
    optionParser.parse(args, UpdaterToolOptions())
  }

  private def optionParser: scopt.OptionParser[UpdaterToolOptions] = {
    new scopt.OptionParser[UpdaterToolOptions]("UpdaterTool") {
      opt[File]("rootDir") valueName "root-directory" action { (x, c) =>
        c.copy(rootDir = x)
      } text "root directory"
    }
  }
}
