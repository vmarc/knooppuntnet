package kpn.server.analyzer.engine.analysis.route.analyzers.detail

object RouteRoleAnalyzer {

  def isPoiRole(role: Option[String]): Boolean = {
    val roles = role match {
      case None => Seq.empty
      case Some(value) =>
        if (value.contains(";")) {
          value.split(";").toSeq
        }
        else {
          Seq(value)
        }
    }

    roles.exists(role => poiRoles.contains(role) || role.startsWith("stop:"))
  }

  val knownRoles: Seq[String] = Seq(
    "forward",
    "backward",
    // --
    "alternate",
    "alternative",
    "both", // e.g. 12854917
    "connection", // e.g. 9134296
    "detour", // e.g. 368092
    "east", // e.g. 2905073
    "end", // e.g. 1730662
    "excursion", // e.g. 3587317
    "first",
    "left", // e.g. 6934901
    "link", // e.g. 1113500
    "main",
    "north", // e.g. 9906893 16486590
    "shortcut", // e.g. 5867631
    "south", // e.g. 16486590
    "start", // e.g. 1029576
    "west", // e.g. 16293870
  )

  val poiRoles: Seq[String] = Seq(
    "accommodation", // e.g. 11781131
    "alternative_terminal",
    "approach", // e.g. 1243132
    "artwork", // e.g. 10987381
    "attraction",
    "attraction",
    "barrier", // e.g. 15874004 ??????
    "cabin", // e.g. 5471716, 5471717
    "camp_site", // e.g. 6850995
    "chapel", //  2951823
    "checkpoint",
    "church", // e.g. 2951823
    "fountain", // e.g. 15892530
    "guidepost",
    "historic", // e.g. 976514, 2420067
    "information",
    "infrastructure", // e.g. 15297807
    "marker", // e.g. 15673309
    "nightstop",
    "parking", // e.g. parking:XX 2614842
    "place",
    "place_locality",
    "place_of_worship", // e.g. 13777123
    "platform", // e.g. 570335
    "playground", // e.g. 11948939
    "poi",
    "portage", // TODO make accesility analysis look at this for canoe routes (e.g. 569426, 569449)
    "proposed", // e.g. 2261588
    "pub",
    "put_in", // e.g. 11034917, 11151367
    "resupply", // e.g. 11025812
    "role",
    "route", // e.g. 1028991
    "sculpture",
    "services", // e.g. 11781131
    "shelter",
    "site", // e.g. 13534147
    "station", // e.g. 2667614
    "stop",
    "substation", // e.g. 13635962
    "supermarket", // e.g. 11781131
    "toilets", // e.g. 5472302
    "tower", // e.g. 2951823
    "trailhead",
    "viewpoint",
  )
}

class RouteRoleAnalyzer
