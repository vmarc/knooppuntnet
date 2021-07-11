package kpn.core.tools.support

import kpn.core.database.Database
import kpn.core.db.couch.Couch
import kpn.server.repository.NodeRepositoryImpl

object DeactivateNodesTool {
  def main(args: Array[String]): Unit = {
    Couch.executeIn("kpn-database", "attic-analysis") { database =>
      new DeactivateNodesTool(database).delete()
    }
  }
}

class DeactivateNodesTool(database: Database) {

  private val nodeRepository = new NodeRepositoryImpl(null, database, false)

  def delete(): Unit = {
    println("Start")
    deletedNodeIds().foreach { nodeId =>
      nodeRepository.nodeWithId(nodeId) match {
        case None => println("Node not found " + nodeId)
        case Some(nodeInfo) =>
          if (nodeInfo.active) {
            nodeRepository.save(nodeInfo.copy(active = false))
          }
      }
    }
    println("Done")
  }

  private def deletedNodeIds(): Seq[Long] = {
    Seq(
      1015553600L,
      1069126190L,
      1079550304L,
      1091801299L,
      1116648353L,
      1229663673L,
      1300803984L,
      1437507756L,
      1550619438L,
      1562417659L,
      1589998354L,
      1907524431L,
      1958482331L,
      2083085730L,
      2390428852L,
      2467944970L,
      2467944977L,
      247743093L,
      282519456L,
      288913899L,
      295612796L,
      299554924L,
      3040050638L,
      304810916L,
      3152816171L,
      317296390L,
      3251067775L,
      379736577L,
      393446185L,
      417027370L,
      4182906318L,
      42263958L,
      43000695L,
      43166010L,
      43398756L,
      44849641L,
      47831214L,
      47904321L,
      48153404L,
      48178247L,
      487466419L,
      492094570L,
      492493149L,
      495469737L,
      501919469L,
      506079097L,
      5087863050L,
      5554969611L,
      664040428L,
      683086398L,
      727291652L,
      7340651923L,
      7340651924L,
      739277514L,
      793717909L,
      8078748268L,
      8163931867L,
      8460255625L,
      8731919671L,
      972574220L,
      997744665L,

      2971262034L,
      44094860L,
      8196079417L,
      8416361330L,
      8813977624L,
    )
  }
}
