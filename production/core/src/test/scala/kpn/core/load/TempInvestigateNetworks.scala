package kpn.core.load

import kpn.core.db.couch.Couch
import kpn.core.repository.NetworkRepositoryImpl
import spray.json.JsString

object TempInvestigateNetworks {
  def main(args: Array[String]): Unit = {
    new TempInvestigateNetworks().run()
  }
}

class TempInvestigateNetworks {

  def run(): Unit = {

    Couch.executeIn("master") { database =>

      val networkRepository = new NetworkRepositoryImpl(database)

      val networkKeys = database.keys("network:0", "network:9999999999", Couch.batchTimeout)
      val networkIds = networkKeys.flatMap {
        case JsString(key) => Some(key.toString.drop("network:".size).toLong)
        case _ => None
      }

      val networks = networkIds.flatMap { networkId => networkRepository.network(networkId, Couch.defaultTimeout) }

      val sortedNetworks = networks.sortWith { (a, b) =>
        val networkType1 = a.attributes.networkType.name
        val networkType2 = b.attributes.networkType.name

        if (networkType1 == networkType2) {
          val country1 = a.attributes.country.map(_.domain).getOrElse("-")
          val country2 = b.attributes.country.map(_.domain).getOrElse("-")

          if (country1 == country2) {
            val name1 = a.attributes.name
            val name2 = b.attributes.name
            name1 < name2
          }
          else {
            country1 < country2
          }
        }
        else {
          networkType1 < networkType2
        }
      }

      sortedNetworks.foreach { network =>
        print(network.id)
        print("\t")
        print(network.attributes.networkType.name)
        print("\t")
        print(network.attributes.country.map(_.domain).getOrElse("-"))
        print("\t")
        print(!network.ignored)
        print("\t")
        print(network.attributes.name)
        println()
      }
    }
  }
}
