package kpn.core.tools

import java.io.File

import kpn.shared.ReplicationId

object VerifyMinuteDiffs {
  def main(args: Array[String]): Unit = {
    ReplicationId.range(ReplicationId(1), ReplicationId(1, 857, 553)).foreach { replicationId =>
      val filename = s"/kpn/replicate/${replicationId.name}.osc.gz"
      if (!new File(filename).exists) {
        val command1 = "wget -O - \"https://planet.osm.org/replication/minute/" + replicationId.name + ".osc.gz\" > " + replicationId.name + ".osc.gz"
        val command2 = "wget -O - \"https://planet.osm.org/replication/minute/" + replicationId.name + ".state.txt\" > " + replicationId.name + ".state.txt"
        println(command1)
        println(command2)
      }
    }
  }
}
