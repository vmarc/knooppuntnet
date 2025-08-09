package kpn.core.tools.monitor.support

import kpn.database.base.Database
import kpn.database.base.DatabaseCollection
import kpn.database.base.MongoAggregates.equal
import kpn.database.util.Mongo

object MonitorRouteCopyTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-prod") { sourceDatabase =>
      Mongo.executeIn("kpn-experimental") { targetDatabase =>
        new MonitorRouteCopyTool(sourceDatabase, targetDatabase).copy()
      }
    }
  }

  class MonitorRouteCopyTool(sourceDatabase: Database, targetDatabase: Database) {

    def copy(): Unit = {

      targetDatabase.monitorGroups.drop()
      targetDatabase.monitorRoutes.drop()
      targetDatabase.monitorReferences.drop()
      targetDatabase.monitorStates.drop()
      targetDatabase.monitorRouteChanges.drop()

      copyCollection("groups", sourceDatabase.monitorGroups, targetDatabase.monitorGroups)
      copyCollection("routes", sourceDatabase.monitorRoutes, targetDatabase.monitorRoutes)
      copyCollection("route-references", sourceDatabase.monitorReferences, targetDatabase.monitorReferences)
      copyCollection("route-states", sourceDatabase.monitorStates, targetDatabase.monitorStates)
      copyCollection("route-changes", sourceDatabase.monitorRouteChanges, targetDatabase.monitorRouteChanges)
    }

    private def copyCollection[T](name: String, source: DatabaseCollection[T], target: DatabaseCollection[T]): Unit = {
      println(s"Collecting ids: $name")
      val ids = source.objectIds()
      val idsSize = ids.size
      ids.zipWithIndex.foreach { case (id, index) =>
        println(s"  $name ${index + 1}/$idsSize")
        if (target.countFilteredDocuments(equal("_id", id.raw)) == 0L) {
          source.findByObjectId(id).foreach { document =>
            target.save(document)
          }
        }
      }
    }
  }
}
