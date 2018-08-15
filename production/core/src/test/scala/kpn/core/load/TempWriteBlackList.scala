package kpn.core.load

import kpn.core.db.couch.Couch
import kpn.core.repository.BlackListRepositoryImpl
import kpn.core.engine.changes.data.BlackList
import kpn.core.engine.changes.data.BlackListEntry

object TempWriteBlackList extends App {

  val blackList = BlackList(
    Seq(

      BlackListEntry(33216, "Radverkehrsnetz NRW", "network collection"),
      BlackListEntry(1574277, "Radverkehrsnetz Schleswig-Holstein", "network collection"),
      BlackListEntry(2313383, "Radverkehrsnetz Bayern", "network collection"),
      BlackListEntry(6534926, "NRW", "network collection"),
      BlackListEntry(3208117, "21 Radrundwege Mecklenburg-Vorpommern", "network collection"),
      BlackListEntry(5406832, "NiederRheinroute", "not a node network"),
      BlackListEntry(226721, "R-Radwegenetz NRW", "network collection"),
      BlackListEntry(1764005, "Radverkehrsnetz Kreis Nordfriesland", "not a node network"),
      BlackListEntry(1753500, "Radverkehrsnetz NRW, Stadt Dortmund", "not a node network"),
      BlackListEntry(215044, "Route der Industriekultur per Rad", "not a node network"),
      BlackListEntry(6408300, "Rundroutennetz Radregion Münsterland", "network collection"),
      BlackListEntry(3927908, "Römer-Lippe-Route mit Schleifen", "not a node network"),
      BlackListEntry(6583919, "Hauptradverkehrsnetz Wien", "Foreign country"),
      BlackListEntry(6639104, "Seven Waters Bike Trail", "Foreign country"),

      BlackListEntry(536151, "Radverkehrsnetz BW", "network collection"),
      BlackListEntry(43701120, "Württembergischer Weinwanderweg", "not a node network")
    ),
    Seq(),
    Seq()
  )

  Couch.executeIn("master") { database =>
    val repository = new BlackListRepositoryImpl(database)
    repository.save(blackList)
  }
}
