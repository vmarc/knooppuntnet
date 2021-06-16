package kpn.core.tools.support

import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.core.db.couch.Couch
import kpn.server.repository.NetworkRepositoryImpl

object FindNetworksWithoutCountryTool {
  def main(args: Array[String]): Unit = {
    Couch.executeIn("kpn-server", "master-test") { database =>
      val repo = new NetworkRepositoryImpl(null, database, false)
      val networks = NetworkType.all.flatMap { networkType =>
        repo.networks(kpn.api.custom.Subset(Country.nl, networkType)).filter(_.country.isEmpty)
      }
      println(networks.map(_.id).mkString(","))
      println(networks.map(_.name).mkString(","))
      println("Done")
    }
  }
}
