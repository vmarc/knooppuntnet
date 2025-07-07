package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.monitor.MonitorAction
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.custom.Timestamp
import kpn.core.loadOld.Parser
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.monitor.route.update.MonitorRouteRelationRepository
import kpn.server.monitor.route.update.MonitorRouteStructureLoader
import kpn.server.monitor.route.update.MonitorUpdateContext
import kpn.server.monitor.route.update.MonitorUpdateReporterLogger
import kpn.server.monitor.route.update.MonitorUpdaterConfiguration

import scala.xml.InputSource
import scala.xml.XML

case class TaunusKlubRoute(id: Long, name: String, description: String)

object TaunusKlubTool {
  private val routeInfos = Seq(
    TaunusKlubRoute(20745, "112", "Grenzstein-Rundwanderweg (Siegfried-Rumbler-Weg)"),
    TaunusKlubRoute(63178, "Lim", "Limeswanderweg [Taunus]"),
    TaunusKlubRoute(6390768, "OR1", "Oberurseler Rundwanderweg 1"),
    TaunusKlubRoute(6390703, "OR2", "Oberurseler Rundwanderweg 2"),
    TaunusKlubRoute(6390845, "OR3", "Oberurseler Rundwanderweg 3"),
    TaunusKlubRoute(7721870, "R1", "Rund um den Rettershof: Weg 1"),
    TaunusKlubRoute(5857060, "R2", "Rund um den Rettershof: Weg 2"),
    TaunusKlubRoute(5857061, "R4", "Rund um den Rettershof: Weg 4"),
    TaunusKlubRoute(5857062, "R5", "Rund um den Rettershof: Weg 5"),
    TaunusKlubRoute(5857063, "R7", "Rund um den Rettershof: Weg 7"),
    TaunusKlubRoute(2871772, "R8", "Rund um den Rettershof: Weg 8"),
    TaunusKlubRoute(2411600, "R9", "Rund um den Rettershof: Weg 9"),
    TaunusKlubRoute(11264988, "SSW", "Saar-Schlesien-Weg (Hessen)"),
    TaunusKlubRoute(154303, "001", "Taunusklub Route 001 | Taunushöhenweg T"),
    TaunusKlubRoute(555578, "002", "Taunusklub Route 002 | Lahnhöhenweg"),
    TaunusKlubRoute(398922, "005", "Taunusklub Route 005"),
    TaunusKlubRoute(222651, "006", "Taunusklub Route 006"),
    TaunusKlubRoute(226790, "007", "Taunusklub Route 007"),
    TaunusKlubRoute(161396, "008", "Taunusklub Route 008"),
    TaunusKlubRoute(15961186, "008-UWR", "Taunusklub Route 008 UWR"),
    TaunusKlubRoute(929838, "009", "Taunusklub Route 009"),
    TaunusKlubRoute(899356, "010", "Taunusklub Route 010"),
    TaunusKlubRoute(194521, "011", "Taunusklub Route 011"),
    TaunusKlubRoute(215744, "012", "Taunusklub Route 012"),
    TaunusKlubRoute(84380, "013", "Taunusklub Route 013"),
    TaunusKlubRoute(571673, "014", "Taunusklub Route 014"),
    TaunusKlubRoute(611160, "015", "Taunusklub Route 015"),
    TaunusKlubRoute(543538, "016", "Taunusklub Route 016"),
    TaunusKlubRoute(543537, "017", "Taunusklub Route 017"),
    TaunusKlubRoute(543541, "018", "Taunusklub Route 018"),
    TaunusKlubRoute(543527, "019", "Taunusklub Route 019"),
    TaunusKlubRoute(149386, "020", "Taunusklub Route 020"),
    TaunusKlubRoute(611158, "021", "Taunusklub Route 021"),
    TaunusKlubRoute(543506, "022", "Taunusklub Route 022"),
    TaunusKlubRoute(543507, "023", "Taunusklub Route 023"),
    TaunusKlubRoute(398924, "024", "Taunusklub Route 024"),
    TaunusKlubRoute(543505, "025", "Taunusklub Route 025"),
    TaunusKlubRoute(398921, "026", "Taunusklub Route 026"),
    TaunusKlubRoute(543462, "027", "Taunusklub Route 027"),
    TaunusKlubRoute(70547, "028", "Taunusklub Route 028"),
    TaunusKlubRoute(398923, "029", "Taunusklub Route 029"),
    TaunusKlubRoute(403933, "030", "Taunusklub Route 030"),
    TaunusKlubRoute(913388, "031", "Taunusklub Route 031"),
    TaunusKlubRoute(15669358, "032", "Taunusklub Route 032 | Jubiläumsweg"),
    TaunusKlubRoute(918878, "035", "Taunusklub Route 035"),
    TaunusKlubRoute(919789, "036", "Taunusklub Route 036"),
    TaunusKlubRoute(70543, "037", "Taunusklub Route 037"),
    TaunusKlubRoute(557624, "038", "Taunusklub Route 038"),
    TaunusKlubRoute(70546, "039", "Taunusklub Route 039"),
    TaunusKlubRoute(563050, "040", "Taunusklub Route 040"),
    TaunusKlubRoute(919878, "042", "Taunusklub Route 042"),
    TaunusKlubRoute(918695, "043", "Taunusklub Route 043"),
    TaunusKlubRoute(918694, "044", "Taunusklub Route 044"),
    TaunusKlubRoute(70544, "045", "Taunusklub Route 045"),
    TaunusKlubRoute(70560, "046", "Taunusklub Route 046"),
    TaunusKlubRoute(70558, "047", "Taunusklub Route 047"),
    TaunusKlubRoute(55658, "048", "Taunusklub Route 048"),
    TaunusKlubRoute(153821, "049", "Taunusklub Route 049"),
    TaunusKlubRoute(153819, "050", "Taunusklub Route 050"),
    TaunusKlubRoute(55672, "051", "Taunusklub Route 051"),
    TaunusKlubRoute(192848, "052", "Taunusklub Route 052"),
    TaunusKlubRoute(55669, "053", "Taunusklub Route 053"),
    TaunusKlubRoute(900563, "054", "Taunusklub Route 054"),
    TaunusKlubRoute(116064, "055", "Taunusklub Route 055"),
    TaunusKlubRoute(918417, "056", "Taunusklub Route 056"),
    TaunusKlubRoute(112770, "057", "Taunusklub Route 057"),
    TaunusKlubRoute(939809, "059", "Taunusklub Route 059"),
    TaunusKlubRoute(918416, "060", "Taunusklub Route 060"),
    TaunusKlubRoute(611159, "061", "Taunusklub Route 061"),
    TaunusKlubRoute(1067641, "062", "Taunusklub Route 062"),
    TaunusKlubRoute(359951, "063", "Taunusklub Route 063"),
    TaunusKlubRoute(112769, "064", "Taunusklub Route 064"),
    TaunusKlubRoute(934499, "065", "Taunusklub Route 065"),
    TaunusKlubRoute(383503, "066", "Taunusklub Route 066"),
    TaunusKlubRoute(135584, "067", "Taunusklub Route 067"),
    TaunusKlubRoute(194464, "068", "Taunusklub Route 068"),
    TaunusKlubRoute(194463, "069", "Taunusklub Route 069"),
    TaunusKlubRoute(194439, "070", "Taunusklub Route 070"),
    TaunusKlubRoute(1069612, "070A", "Taunusklub Route 070A Weidenhausweg"),
    TaunusKlubRoute(194438, "071", "Taunusklub Route 071"),
    TaunusKlubRoute(1070558, "071A", "Taunusklub Route 071A Kuhkopfweg"),
    TaunusKlubRoute(194268, "072", "Taunusklub Route 072"),
    TaunusKlubRoute(135585, "073", "Taunusklub Route 073"),
    TaunusKlubRoute(194555, "074", "Taunusklub Route 074"),
    TaunusKlubRoute(194554, "075", "Taunusklub Route 075"),
    TaunusKlubRoute(936628, "076", "Taunusklub Route 076"),
    TaunusKlubRoute(403699, "077", "Taunusklub Route 077"),
    TaunusKlubRoute(403680, "078", "Taunusklub Route 078"),
    TaunusKlubRoute(79322, "079", "Taunusklub Route 079"),
    TaunusKlubRoute(79601, "080", "Taunusklub Route 080"),
    TaunusKlubRoute(134234, "082", "Taunusklub Route 082"),
    TaunusKlubRoute(79115, "083", "Taunusklub Route 083"),
    TaunusKlubRoute(79116, "084", "Taunusklub Route 084"),
    TaunusKlubRoute(79293, "085", "Taunusklub Route 085"),
    TaunusKlubRoute(240227, "086", "Taunusklub Route 086"),
    TaunusKlubRoute(403587, "088", "Taunusklub Route 088"),
    TaunusKlubRoute(222970, "090", "Taunusklub Route 090"),
    TaunusKlubRoute(84298, "091", "Taunusklub Route 091"),
    TaunusKlubRoute(902246, "092", "Taunusklub Route 092"),
    TaunusKlubRoute(934389, "093", "Taunusklub Route 093"),
    TaunusKlubRoute(934390, "097", "Taunusklub Route 097"),
    TaunusKlubRoute(550247, "098", "Taunusklub Route 098"),
    TaunusKlubRoute(917978, "099", "Taunusklub Route 099"),
    TaunusKlubRoute(919879, "100", "Taunusklub Route 100"),
    TaunusKlubRoute(556848, "102", "Taunusklub Route 102"),
    TaunusKlubRoute(11112369, "103", "Taunusklub Route 103"),
    TaunusKlubRoute(936397, "104", "Taunusklub Route 104"),
    TaunusKlubRoute(905544, "106", "Taunusklub Route 106"),
    TaunusKlubRoute(555579, "107", "Taunusklub Route 107"),
    TaunusKlubRoute(1103158, "108", "Taunusklub Route 108"),
    TaunusKlubRoute(905543, "109", "Taunusklub Route 109"),
    TaunusKlubRoute(13360841, "RMV1", "Taunusklub Route RMV1"),
    TaunusKlubRoute(17023725, "RMV1-ZBG", "Taunusklub Route RMV1 Zubringer"),
    TaunusKlubRoute(13363574, "RMV2", "Taunusklub Route RMV2"),
    TaunusKlubRoute(17024490, "RMV2-ZBG", "Taunusklub Route RMV2 Zubringer"),
    TaunusKlubRoute(13364026, "RMV3", "Taunusklub Route RMV3"),
    TaunusKlubRoute(10328867, "RMV4", "Taunusklub Route RMV4"),
    TaunusKlubRoute(13366905, "RMV5", "Taunusklub Route RMV5"),
    TaunusKlubRoute(13369802, "RMV6", "Taunusklub Route RMV6"),
    TaunusKlubRoute(13371057, "RMV7", "Taunusklub Route RMV7"),
    TaunusKlubRoute(13364055, "RMV8", "Taunusklub Route RMV8"),
  )

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-prod") { database =>
      val tool = new TaunusKlubTool(configuration(database))
      // tool.listRoutes()
      tool.createRoutes(routeInfos)
    }
  }

  private def configuration(database: Database): MonitorUpdaterConfiguration = {
    val overpassQueryExecutor = new OverpassQueryExecutorRemoteImpl()
    val monitorRouteRelationRepository = new MonitorRouteRelationRepository(overpassQueryExecutor)
    val monitorRouteStructureLoader = new MonitorRouteStructureLoader(overpassQueryExecutor)
    new MonitorUpdaterConfiguration(
      database,
      monitorRouteRelationRepository,
      monitorRouteStructureLoader
    )
  }
}

class TaunusKlubTool(configuration: MonitorUpdaterConfiguration) {
  def createRoutes(routeInfos: Seq[TaunusKlubRoute]): Unit = {
    routeInfos.drop(1).foreach { routeInfo =>
      configuration.monitorRouteUpdateExecutor.execute(
        MonitorUpdateContext(
          "create-taunusklub-routes",
          new MonitorUpdateReporterLogger(),
          MonitorRouteUpdate(
            action = MonitorAction.add,
            groupName = "de-tc",
            routeName = routeInfo.name,
            referenceType = MonitorReferenceType.osm,
            referenceTimestamp = Some(Timestamp(2024, 2, 1)),
            description = Some(routeInfo.description),
            comment = None,
            relationId = Some(routeInfo.id),
          )
        )
      )
    }
  }

  def listRoutes(): Unit = {
    val stream = getClass.getResourceAsStream("/case-studies/taunus-klub.xml")
    val inputSource = new InputSource(stream)
    val xml = XML.load(inputSource)
    val rawData = new Parser(includeMetadata = false).parse(xml)
    val infos: Seq[Seq[String]] = rawData.relations.map { relation =>
      Seq(relation.tagValue("name").get, relation.id.toString, relation.tagValue("ref").toString)
    }
    infos.sortBy(_.head).foreach { info =>
      println(s"""TaunusKlubRoute(${info(1)}, "${info(2)}", "${info.head}"),""")
    }
  }
}
