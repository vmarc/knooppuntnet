package kpn.core.tools.support

import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.util.Mongo

object FindChangesInMultipleMinuteDiffsTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-prod") { database =>
      new FindChangesInMultipleMinuteDiffsTool(database).report()
    }
  }
}

class FindChangesInMultipleMinuteDiffsTool(database: Database) {

  private val log = Log(classOf[FindChangesInMultipleMinuteDiffsTool])

  def report(): Unit = {
    log.info("Collecting changes ids")
    val ids = database.changes.stringIds().sorted
    log.info(s"Evaluating ${ids.size} changes")
    val idMap = ids.map { id =>
      val splitted = id.split(":")
      splitted.head -> splitted(1)
    }.groupBy(_._1).filter(_._2.sizeIs > 5).map(entry => entry._1 -> entry._2.map(_._2))
    val mostRecentId = idMap.keys.toSeq.sorted.reverse.head
    println(s"$mostRecentId: ${idMap(mostRecentId).mkString(", ")}")
  }
}
