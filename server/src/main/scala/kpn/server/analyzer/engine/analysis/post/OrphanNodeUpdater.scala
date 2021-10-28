package kpn.server.analyzer.engine.analysis.post

import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.util.Mongo
import org.springframework.stereotype.Component

object OrphanNodeUpdater {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-test") { database =>
      new OrphanNodeUpdater(database).update()
    }
  }
}

@Component
class OrphanNodeUpdater(database: Database) {

  private val log = Log(classOf[OrphanNodeUpdater])

  def update(): Unit = {
    log.debugElapsed {
      val allNodeIds = findAllNodeIds()
      val nodeIdsReferencedInRoutes = findNodeIdsReferencedInRoutes()
      val nodeIdsReferencedInNetworks = findNodeIdsReferencedInNetworks()
      val nodeIds = (allNodeIds.toSet -- nodeIdsReferencedInRoutes -- nodeIdsReferencedInNetworks).toSeq.sorted
      updateOrphanNodes(nodeIds)
      ("done", ())
    }
  }

  private def findAllNodeIds(): Seq[Long] = {
    new OrphanNodeUpdater_AllNodeIds(database, log).execute()
  }

  private def findNodeIdsReferencedInRoutes(): Seq[Long] = {
    new OrphanNodeUpdater_ReferencesInRoutes(database, log).execute()
  }

  private def findNodeIdsReferencedInNetworks(): Seq[Long] = {
    new OrphanNodeUpdater_ReferencesInNetworks(database, log).execute()
  }

  private def updateOrphanNodes(nodeIds: Seq[Long]): Unit = {
    new OrphanNodeUpdater_Update(database, log).execute(nodeIds)
  }
}
