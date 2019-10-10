package kpn.shared.network

/***

When estimating the completeness of a network, we should not only look at
the number of nodes (compare against estimated number of nodes), but also
at the number of routes connecting those nodes. When only there is only an
estimate for the number of nodes, the estimated number of routes can be
calculated as follows:

  expectedRoutes = (expectedNodes - 2) * 2 + 1

  completePercentage = 100 * (nodes + routes) / (expectedNodes + expectedRoutes)

100 * 237 / 339 = 70%

****/

case class NetworkSizeEstimate(nodeCount: Int)

object NetworkSizeEstimates {

  // data collected 2014-04-11

  // TODO match to the names as used in OSM:

  val size = Map(
    "Wandelnetwerk Antwerpse Kempen - Kempens Landgoed" -> NetworkSizeEstimate(297),
    "Wandelnetwerk Antwerpse Kempen - Kempense Beemden" -> NetworkSizeEstimate(308),
    "Wandelnetwerk Antwerpse Kempen - Kempense Heide" -> NetworkSizeEstimate(339),
    "Wandelnetwerk Antwerpse Kempen - Kempense Heuvelrug" -> NetworkSizeEstimate(207),
    "Wandelnetwerk Antwerpse Kempen - Kempense Hoven" -> NetworkSizeEstimate(346),
    "Wandelnetwerk Antwerpse Kempen - Kempense Kolonie" -> NetworkSizeEstimate(336), // official name: Kempense KolonieS
    "Wandelnetwerk Antwerpse Kempen - Kempense Landduinen" -> NetworkSizeEstimate(316),
    "Wandelnetwerk Antwerpse Kempen - Kempense Meren" -> NetworkSizeEstimate(232),
    "Wandelnetwerk Antwerpse Kempen - Kempense Netevallei" -> NetworkSizeEstimate(317),
    "Wandelnetwerk Bulskampveld" -> NetworkSizeEstimate(120),
    "Wandelnetwerk De Merode" -> NetworkSizeEstimate(413), // official name: de Merode (first letter lowercase)
    "Wandelnetwerk Hagelandse Heuvels" -> NetworkSizeEstimate(220),
    "Wandelnetwerk Heuvelland" -> NetworkSizeEstimate(272),
    "Wandelnetwerk Ieperboog" -> NetworkSizeEstimate(75),
    "Wandelnetwerk Kust" -> NetworkSizeEstimate(53), // officiele naam: "Kustwandelroute"
    "Wandelnetwerk Land van Mortagne" -> NetworkSizeEstimate(121),
    "Wandelnetwerk Pajottenland" -> NetworkSizeEstimate(266),
    "Wandelnetwerk Rivierenland" -> NetworkSizeEstimate(289),
    "Wandelnetwerk Vlaamse Ardennen" -> NetworkSizeEstimate(161), // official name: "Getuigenheuvels Vlaamse Ardennen" (if this is the same)
    "Wandelnetwerk Westkust" -> NetworkSizeEstimate(105),
    "Wandelnetwerk Zwin" -> NetworkSizeEstimate(24)
  )
}
