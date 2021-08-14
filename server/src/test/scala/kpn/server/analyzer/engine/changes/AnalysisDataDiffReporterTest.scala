package kpn.server.analyzer.engine.changes

import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.changes.changes.ElementIds
import kpn.server.analyzer.engine.changes.data.Watched

class AnalysisDataDiffReporterTest extends UnitTest {

  test("No differences") {
    val left = Watched()
    val right = Watched()
    val report = new AnalysisDataDiffReporter().report(left: Watched, right: Watched).mkString("\n")
    report should equal("No differences")
  }

  test("Network differences") {

    pending

    val left = Watched()
    val right = Watched()

    val leftElementIds = ElementIds(
      nodeIds = Set(1011, 1012),
      wayIds = Set(1021, 1022),
      relationIds = Set(1031, 1032)
    )

    val rightElementIds = ElementIds(
      nodeIds = Set(1011, 1013),
      wayIds = Set(1021, 1023),
      relationIds = Set(1031, 1033)
    )

    left.networks.add(10)
    left.networks.add(11)
    left.networks.add(12)
    right.networks.add(10)
    right.networks.add(11)
    right.networks.add(13)

    val report = new AnalysisDataDiffReporter().report(left: Watched, right: Watched).mkString("\n")

    val expected =
      """|Network differences:
         |  watched:
         |    leftOnlyNetworks = 12
         |    rightOnlyNetworks = 13
         |    network 10
         |      nodeIdsLeftOnly = 1012
         |      nodeIdsRightOnly = 1013
         |      wayIdsLeftOnly = 1022
         |      wayIdsRightOnly = 1023
         |      relationIdsLeftOnly = 1032
         |      relationIdsRightOnly = 1033""".stripMargin

    report should equal(expected)
  }

  test("Orphan route differences") {

    pending


    val left = Watched()
    val right = Watched()

    val leftElementIds = ElementIds(
      nodeIds = Set(1011, 1012),
      wayIds = Set(1021, 1022),
      relationIds = Set(1031, 1032)
    )

    val rightElementIds = ElementIds(
      nodeIds = Set(1011, 1013),
      wayIds = Set(1021, 1023),
      relationIds = Set(1031, 1033)
    )

    left.routes.add(10, leftElementIds)
    left.routes.add(11, ElementIds())
    left.routes.add(12, ElementIds())
    left.routes.add(13, ElementIds())

    right.routes.add(10, rightElementIds)
    right.routes.add(11, ElementIds())
    right.routes.add(14, ElementIds())
    right.routes.add(15, ElementIds())

    val report = new AnalysisDataDiffReporter().report(left: Watched, right: Watched).mkString("\n")

    val expected =
      """|Orphan route differences:
         |  watched:
         |    leftOnly = 12, 13
         |    rightOnly = 14, 15
         |    route 10
         |      left = RouteSummary(10,Some(Country(nl,The Netherlands)),rwn,A,0,false,0,Timestamp(2016,8,11,0,0,0),user,List(),,false)
         |      right = RouteSummary(10,Some(Country(nl,The Netherlands)),rwn,B,0,false,0,Timestamp(2016,8,11,0,0,0),user,List(),,false)
         |      nodeIdsLeftOnly = 1012
         |      nodeIdsRightOnly = 1013
         |      wayIdsLeftOnly = 1022
         |      wayIdsRightOnly = 1023
         |      relationIdsLeftOnly = 1032
         |      relationIdsRightOnly = 1033""".stripMargin

    report should equal(expected)
  }

  test("Orphan node differences") {

    pending

    val left = Watched()
    val right = Watched()

    left.nodes.add(10)
    left.nodes.add(11)
    left.nodes.add(12)
    left.nodes.add(13)

    right.nodes.add(10)
    right.nodes.add(11)
    right.nodes.add(14)
    right.nodes.add(15)

    val report = new AnalysisDataDiffReporter().report(left: Watched, right: Watched).mkString("\n")

    val expected =
      """|Orphan node differences:
         |  watched:
         |    leftOnly = 12, 13
         |    rightOnly = 14, 15
         |    orphan node 10
         |      left = LoadedNode(10,Subset(Country(nl,The Netherlands),rwn),A,Node(RawNode(10,,,0,Timestamp(2016,11,8,0,0,0),1,1,user,Tags())))
         |      right = LoadedNode(10,Subset(Country(nl,The Netherlands),rwn),B,Node(RawNode(10,,,0,Timestamp(2016,11,8,0,0,0),1,1,user,Tags())))""".stripMargin

    report should equal(expected)
  }

}
