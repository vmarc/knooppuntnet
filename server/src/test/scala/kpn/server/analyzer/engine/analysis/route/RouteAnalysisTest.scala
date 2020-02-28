package kpn.server.analyzer.engine.analysis.route

import kpn.api.custom.NetworkType
import kpn.api.custom.Fact.RouteBroken
import kpn.api.custom.Fact.RouteNotBackward
import kpn.api.custom.Fact.RouteOneWay
import kpn.api.custom.Fact.RouteNotOneWay
import kpn.api.custom.Fact.RouteNotContinious
import kpn.api.custom.Fact.RouteNotForward
import kpn.api.custom.Fact.RouteUnusedSegments
import kpn.api.custom.Tags
import org.scalatest.FunSuite
import org.scalatest.Matchers

class RouteAnalysisTest extends FunSuite with Matchers {

  test("single way route") {

    val d = new RouteTestData("01-02") {
      node(1, "01")
      node(4, "02")
      memberNode(1)
      memberWay(10, "", 1, 2, 3, 4)
      memberNode(4)
    }

    new RouteAnalysisInspector() {

      startNode(1)
      endNode(4)

      forward(1, 2, 3, 4)
      backward(4, 3, 2, 1)

      structure("forward=(01-02 via +<01-02 10>)")
      structure("backward=(02-01 via -<01-02 10>)")

    }.analyze(d)
  }

  test("simple route") {

    val d = new RouteTestData("01-02") {

      node(1, "01")
      node(6, "02")

      memberNode(1)
      memberWay(10, "", 1, 2, 3)
      memberWay(11, "", 3, 4, 5)
      memberWay(12, "", 5, 6)
      memberNode(6)
    }

    new RouteAnalysisInspector() {

      startNode(1)
      endNode(6)

      forward(1, 2, 3, 4, 5, 6)
      backward(6, 5, 4, 3, 2, 1)

      structure("forward=(01-02 via +<01- 10>+<11>+<-02 12>)")
      structure("backward=(02-01 via -<-02 12>-<11>-<01- 10>)")

    }.analyze(d)
  }

  /*
   * TODO:
   *
   *  loops
   *  tentacle(s) at start
   *  tentacle(s) at end
   *
   *  route broken in (multiple) tentacles
   *
   * - start route with way that has "forward" role     --> firstNode logic is different from role="" || role="backward"
   * - start route with way that has "backward" role
   *      ==> role is needed for tentacles at the start!?
   *
   *
   *
   *
   */

  test("tentacle at start with network node at start and end of first way (similar to 3095938)") {

    val d = new RouteTestData("01-02") {
      node(1, "01")
      node(4, "01")
      node(6, "02")

      memberWay(10, "", 1, 2, 3, 4)
      memberWay(11, "", 4, 5)
      memberWay(12, "", 5, 6)
    }

    new RouteAnalysisInspector() {

      startTentacleNode(1)
      startNode(4)
      endNode(6)

      tentacle(4, 3, 2, 1)
      forward(4, 5, 6)
      backward(6, 5, 4)

      structure("forward=(01.a-02 via +<01.a- 11>+<-02 12>)")
      structure("backward=(02-01.a via -<-02 12>-<01.a- 11>)")
      structure("startTentacles=(01.a-01.b via -<01.b-01.a 10>)")

    }.analyze(d)
  }

  test("tentacle at start") {

    val d = new RouteTestData("01-02") {
      node(1, "01")
      node(4, "01")
      node(6, "02")

      memberWay(10, "", 1, 2, 3)
      memberWay(11, "", 3, 4)
      memberWay(12, "", 4, 5)
      memberWay(13, "", 5, 6)
    }

    new RouteAnalysisInspector() {

      startTentacleNode(1)
      startNode(4)
      endNode(6)

      tentacle(4, 3, 2, 1)
      forward(4, 5, 6)
      backward(6, 5, 4)

      structure("forward=(01.a-02 via +<01.a- 12>+<-02 13>)")
      structure("backward=(02-01.a via -<-02 13>-<01.a- 12>)")
      structure("startTentacles=(01.a-01.b via -<-01.a 11>-<01.b- 10>)")

    }.analyze(d)
  }

  test("tentacle at end") {

    val d = new RouteTestData("01-02") {
      node(1, "01")
      node(5, "02")
      node(7, "02")

      memberWay(10, "", 1, 2) // 01
      memberWay(11, "", 2, 3)
      memberWay(12, "", 3, 4)
      memberWay(13, "", 4, 5) // first 02
      memberWay(14, "", 5, 6)
      memberWay(15, "", 6, 7) // second 02
    }

    new RouteAnalysisInspector() {

      startNode(1)
      endNode(5)
      endTentacleNode(7)

      forward(1, 2, 3, 4, 5)
      backward(5, 4, 3, 2, 1)
      tentacle(5, 6, 7)

      structure("forward=(01-02.a via +<01- 10>+<11>+<12>+<-02.a 13>)")
      structure("backward=(02.a-01 via -<-02.a 13>-<12>-<11>-<01- 10>)")
      structure("endTentacles=(02.a-02.b via +<02.a- 14>+<-02.b 15>)")

    }.analyze(d)
  }

  test("multiple tentacles stacked at end") {

    val d = new RouteTestData("01-02") {
      node(1, "01")
      node(5, "02")
      node(7, "02")
      node(9, "02")

      memberWay(10, "", 1, 2) // 01
      memberWay(11, "", 2, 3)
      memberWay(12, "", 3, 4)
      memberWay(13, "", 4, 5) // first 02
      memberWay(14, "", 5, 6)
      memberWay(15, "", 6, 7) // second 02
      memberWay(16, "", 7, 8)
      memberWay(17, "", 8, 9) // third 02
    }

    new RouteAnalysisInspector() {

      startNode(1)
      endNode(5)

      endTentacleNode(7)
      endTentacleNode(9)

      forward(1, 2, 3, 4, 5)
      backward(5, 4, 3, 2, 1)
      tentacle(5, 6, 7)
      tentacle(7, 8, 9)

      structure("forward=(01-02.a via +<01- 10>+<11>+<12>+<-02.a 13>)")
      structure("backward=(02.a-01 via -<-02.a 13>-<12>-<11>-<01- 10>)")
      structure("endTentacles=(02.a-02.b via +<02.a- 14>+<-02.b 15>,02.b-02.c via +<02.b- 16>+<-02.c 17>)")

    }.analyze(d)

  }

  test("multiple forked tentacles at end") {

    val d = new RouteTestData("01-02") {
      node(1, "01")
      node(5, "02")
      node(7, "02")
      node(9, "02")

      memberWay(10, "", 1, 2) // 01
      memberWay(11, "", 2, 3)
      memberWay(12, "", 3, 4)
      memberWay(13, "", 4, 5) // first 02
      memberWay(14, "forward", 5, 6)
      memberWay(15, "forward", 6, 7) // second 02
      memberWay(16, "backward", 5, 8)
      memberWay(17, "backward", 8, 9) // third 02
    }

    new RouteAnalysisInspector() {

      startNode(1)
      endNode(5)
      endTentacleNode(7)
      endTentacleNode(9)

      forward(1, 2, 3, 4, 5)
      backward(5, 4, 3, 2, 1)
      tentacle(5, 6, 7) // TODO would be more logical if nodes within tentacle were in reversed order?
      tentacle(9, 8, 5)

      structure("forward=(01-02.a via +<01- 10>+<11>+<12>+<-02.a 13>)")
      structure("backward=(02.a-01 via -<-02.a 13>-<12>-<11>-<01- 10>)")
      structure("endTentacles=(02.a-02.b via +>02.a- 14>+>-02.b 15>,02.c-02.a via -<-02.c 17<-<02.a- 16<)")

    }.analyze(d)
  }

  test("split way with forward and backward roles") {

    val d = new RouteTestData("01-02") {

      node(1, "01")
      node(10, "02")

      memberWay(101, "", 1, 2)
      memberWay(102, "", 2, 3)
      memberWay(103, "forward", 3, 4)
      memberWay(104, "forward", 4, 5)
      memberWay(105, "forward", 5, 6)
      memberWay(106, "backward", 3, 7)
      memberWay(107, "backward", 7, 8)
      memberWay(108, "backward", 8, 6)
      memberWay(109, "", 6, 9)
      memberWay(110, "", 9, 10)
    }

    new RouteAnalysisInspector() {

      startNode(1)
      endNode(10)

      forward(1, 2, 3, 4, 5, 6, 9, 10)
      backward(10, 9, 6, 8, 7, 3, 2, 1)

      structure("forward=(01-02 via +<01- 101>+<102>+>103>+>104>+>105>+<109>+<-02 110>)")
      structure("backward=(02-01 via -<-02 110>-<109>-<108<-<107<-<106<-<102>-<01- 101>)")

    }.analyze(d)
  }

  test("split way with forward and forward roles") {

    val d = new RouteTestData("01-02") {

      node(1, "01")
      node(10, "02")

      memberWay(101, "", 1, 2)
      memberWay(102, "", 2, 3)
      memberWay(103, "forward", 3, 4)
      memberWay(104, "forward", 4, 5)
      memberWay(105, "forward", 5, 6)
      memberWay(106, "forward", 7, 3)
      memberWay(107, "forward", 8, 7)
      memberWay(108, "forward", 6, 8)
      memberWay(109, "", 6, 9)
      memberWay(110, "", 9, 10)
    }

    new RouteAnalysisInspector() {

      startNode(1)
      endNode(10)

      forward(1, 2, 3, 4, 5, 6, 9, 10)
      backward(10, 9, 6, 8, 7, 3, 2, 1)

      structure("forward=(01-02 via +<01- 101>+<102>+>103>+>104>+>105>+<109>+<-02 110>)")
      structure("backward=(02-01 via -<-02 110>-<109>+>108>+>107>+>106>-<102>-<01- 101>)")

    }.analyze(d)

  }

  ignore("roundabout") {

    val d = new RouteTestData("01-02") {

      node(1, "01")
      node(10, "02")

      memberWay(10, "", 1, 2, 4)
      memberWay(11, roundAboutTags, "", 3, 4, 5, 6, 7, 8, 3)
      memberWay(12, "", 7, 9, 10)
    }

    new RouteAnalysisInspector() {

      startNode(1)
      endNode(10)

      forward(1, 2, 4, 5, 6, 7, 9, 10)
      backward(10, 9, 7, 8, 3, 4, 2, 1)

      structure("forward=(01-02 via +<01- 10>+<11(4-5-6-7)>+<-02 12>)")
      structure("backward=(02-01 via -<-02 12>+<11(7-8-3)>+<11(3-4)>-<01- 10>)")

    }.analyze(d)
  }

  test("broken in simple route") {

    val d = new RouteTestData("01-02") {

      node(1, "01")
      node(6, "02")

      memberNode(1)
      memberWay(10, "", 1, 2, 3) // broken
      memberWay(11, "", 4, 5)
      memberWay(12, "", 5, 6)
      memberNode(6)
    }

    new RouteAnalysisInspector() {

      fact(RouteBroken)
      fact(RouteNotForward)
      fact(RouteNotBackward)
      fact(RouteNotContinious)

      startNode(1)
      endNode(6)

      forward(1, 2, 3)
      backward(6, 5, 4)

      structure("forward=(01-None [broken] via +<01- 10>)")
      structure("backward=(02-None [broken] via -<-02 12>-<11>)")

    }.analyze(d)
  }

  test("broken in way that 'overshoots'") {

    val d = new RouteTestData("01-02") {

      node(1, "01")
      node(6, "02")

      memberWay(10, "", 1, 2)
      memberWay(11, "", 2, 3, 4, 5) // overshoot
      memberWay(12, "", 3, 6)
    }

    new RouteAnalysisInspector() {

      fact(RouteBroken)
      fact(RouteUnusedSegments)

      startNode(1)
      endNode(6)

      forward(1, 2, 3, 6)
      backward(6, 3, 2, 1)
      // TODO test unused here?
      // TODO analysis.forwardBreakPoint should equal(Some(12 -> 3))
      // TODO analysis.backwardBreakPoint should equal(Some(12 -> 3))

      structure("forward=(01-02 via +<01- 10>+<11(2-3)>+<-02 12>)")
      structure("backward=(02-01 via -<-02 12>-<11(2-3)>-<01- 10>)")
      structure("unused=(+<11(3-4-5)>)")

    }.analyze(d)
  }

  test("broken in split way forward segment") {

    val d = new RouteTestData("01-02") {

      node(1, "01")
      node(10, "02")

      memberWay(101, "", 1, 2)
      memberWay(102, "", 2, 3)
      memberWay(103, "forward", 3, 4)
      memberWay(104, "forward", 4, 11) // broken
      memberWay(105, "forward", 5, 6)
      memberWay(106, "backward", 3, 7)
      memberWay(107, "backward", 7, 8)
      memberWay(108, "backward", 8, 6)
      memberWay(109, "", 6, 9)
      memberWay(110, "", 9, 10)
    }

    new RouteAnalysisInspector() {

      fact(RouteBroken)
      fact(RouteNotForward)
      fact(RouteNotContinious)

      startNode(1)
      endNode(10)

      forward(1, 2, 3, 4, 11)
      backward(10, 9, 6, 8, 7, 3, 2, 1)

      structure("forward=(01-None [broken] via +<01- 101>+<102>+>103>+>104>)")
      structure("backward=(02-01 via -<-02 110>-<109>-<108<-<107<-<106<-<102>-<01- 101>)")
      structure("unused=(+>105>)")

    }.analyze(d)
  }

  test("broken in split way backward segment") {

    val d = new RouteTestData("01-02") {

      node(1, "01")
      node(10, "02")

      memberWay(101, "", 1, 2)
      memberWay(102, "", 2, 3)
      memberWay(103, "forward", 3, 4)
      memberWay(104, "forward", 4, 5)
      memberWay(105, "forward", 5, 6)
      memberWay(106, "backward", 3, 7)
      memberWay(107, "backward", 7, 11) // broken
      memberWay(108, "backward", 8, 6)
      memberWay(109, "", 6, 9)
      memberWay(110, "", 9, 10)
    }

    new RouteAnalysisInspector() {

      fact(RouteBroken)
      fact(RouteNotBackward)
      fact(RouteNotContinious)

      startNode(1)
      endNode(10)

      forward(1, 2, 3, 4, 5, 6, 9, 10)
      backward(10, 9, 6, 8)

      structure("forward=(01-02 via +<01- 101>+<102>+>103>+>104>+>105>+<109>+<-02 110>)")
      structure("backward=(02-None [broken] via -<-02 110>-<109>-<108<)")
      structure("unused=(+<106<+<107<)")

    }.analyze(d)
  }

  test("broken at roundabout") {

    val d = new RouteTestData("01-02") {

      node(1, "01")
      node(10, "02")

      memberWay(10, "", 1, 2)
      memberWay(11, roundAboutTags, "", 3, 4, 5, 6, 7, 8)
      memberWay(12, "", 7, 9, 10)
    }

    new RouteAnalysisInspector() {

      fact(RouteBroken)
      fact(RouteNotBackward)
      fact(RouteNotForward)
      fact(RouteNotContinious)

      startNode(1)
      endNode(10)

      forward(1, 2)
      backward(10, 9, 7, 8)

      structure("forward=(01-None [broken] via +<01- 10>)")
      structure("backward=(02-None [broken] via -<-02 12>+<11(7-8)>)")

      // TODO analysis.forwardBreakPoint should equal(Some(10 -> 2))
      // TODO analysis.backwardBreakPoint should equal(Some(10 -> 2))

    }.analyze(d)
  }

  test("broken after roundabout") {

    val d = new RouteTestData("01-02") {

      node(1, "01")
      node(10, "02")

      memberWay(10, "", 1, 2, 4)
      memberWay(11, roundAboutTags, "", 3, 4, 5, 6, 7, 8)
      memberWay(12, "", 9, 10)
    }

    new RouteAnalysisInspector() {

      fact(RouteBroken)
      fact(RouteNotBackward)
      fact(RouteNotForward)
      fact(RouteNotContinious)

      startNode(1)
      endNode(10)

      forward(1, 2, 4, 5, 6, 7, 8)
      backward(10, 9)

      // TODO analysis.forwardBreakPoint should equal(Some(12 -> 9))
      // TODO analysis.backwardBreakPoint should equal(Some(12 -> 9))

      structure("forward=(01-None [broken] via +<01- 10>+<11(4-5-6-7-8)>)")
      structure("backward=(02-None [broken] via -<-02 12>)")

    }.analyze(d)
  }

  test("oneway route -> oneway=yes") {
    val d = new RouteTestData("01-02", NetworkType.cycling, Tags.from("oneway" -> "yes")) {

      node(1, "01")
      node(3, "02")

      memberWay(10, Tags.from("highway" -> "road", "oneway" -> "yes"), "", 1, 2, 3)
    }

    new RouteAnalysisInspector() {

      fact(RouteOneWay)

      startNode(1)
      endNode(3)

      forward(1, 2, 3)
      backward()

      structure("forward=(01-02 via +<01-02 10>)")

    }.analyze(d)
  }

  test("oneway route -> comment indicates oneway") {
    val d = new RouteTestData("01-02", NetworkType.cycling, Tags.from("comment" -> "to be used in one direction")) {

      node(1, "01")
      node(3, "02")

      memberWay(10, Tags.from("highway" -> "road", "oneway" -> "yes"), "", 1, 2, 3)
    }

    new RouteAnalysisInspector() {

      fact(RouteOneWay)

      startNode(1)
      endNode(3)

      forward(1, 2, 3)
      backward()

      structure("forward=(01-02 via +<01-02 10>)")

    }.analyze(d)
  }

  test("oneway route -> direction=forward") {
    val d = new RouteTestData("01-02", NetworkType.cycling, Tags.from("direction" -> "forward")) {

      node(1, "01")
      node(3, "02")

      memberWay(10, Tags.from("highway" -> "road", "oneway" -> "yes"), "", 1, 2, 3)
    }

    new RouteAnalysisInspector() {

      fact(RouteOneWay)

      startNode(1)
      endNode(3)

      forward(1, 2, 3)
      backward()

      structure("forward=(01-02 via +<01-02 10>)")

    }.analyze(d)
  }

  test("oneway route -> direction=backward") {
    val d = new RouteTestData("01-02", NetworkType.cycling, Tags.from("direction" -> "backward")) {

      node(1, "01")
      node(3, "02")

      memberWay(10, Tags.from("highway" -> "road", "oneway" -> "yes"), "", 3, 2, 1)
    }

    new RouteAnalysisInspector() {

      fact(RouteOneWay)

      startNode(1)
      endNode(3)

      forward()
      backward(3, 2, 1)

      structure("backward=(02-01 via +<02-01 10>)")

    }.analyze(d)
  }

  test("not a oneway route if both directions ok") {
    val d = new RouteTestData("01-02", NetworkType.cycling, Tags.from("oneway" -> "yes")) {

      node(1, "01")
      node(2, "02")

      memberWay(10, "", 1, 2)
    }

    new RouteAnalysisInspector() {

      fact(RouteNotOneWay)

      startNode(1)
      endNode(2)

      forward(1, 2)
      backward(2, 1)

      structure("forward=(01-02 via +<01-02 10>)")
      structure("backward=(02-01 via -<01-02 10>)")

    }.analyze(d)
  }

  test("route with direction=forward, but forward not ok") {
    val d = new RouteTestData("01-02", NetworkType.cycling, Tags.from("direction" -> "forward")) {

      node(1, "01")
      node(3, "02")

      memberWay(10, Tags.from("highway" -> "road", "oneway" -> "yes"), "", 3, 2, 1)
    }

    new RouteAnalysisInspector() {

      fact(RouteNotForward)
      fact(RouteNotContinious)
      fact(RouteBroken)

      startNode(1)
      endNode(3)

      forward()
      backward(3, 2, 1)

      structure("backward=(02-01 via +<02-01 10>)")

    }.analyze(d)
  }

  test("route with direction=backward, but backward not ok") {
    val d = new RouteTestData("01-02", NetworkType.cycling, Tags.from("direction" -> "backward")) {

      node(1, "01")
      node(3, "02")

      memberWay(10, Tags.from("highway" -> "road", "oneway" -> "yes"), "", 1, 2, 3)
    }

    new RouteAnalysisInspector() {

      fact(RouteNotBackward)
      fact(RouteNotContinious)
      fact(RouteBroken)

      startNode(1)
      endNode(3)

      forward(1, 2, 3)
      backward()

      structure("forward=(01-02 via +<01-02 10>)")

    }.analyze(d)
  }

  ignore("route without nodes, but with state=connection") {

    val d = new RouteTestData("01-02", routeTags = Tags.from("state" -> "connection")) {
      memberWay(10, "", 1, 2)
      memberWay(11, "", 2, 3)
      memberWay(12, "", 3, 4)
    }

    new RouteAnalysisInspector() {
    }.analyze(d)
  }

  ignore("route without nodes, but with state=connection, with wrong sorting order") {

    val d = new RouteTestData("01-02", routeTags = Tags.from("state" -> "connection")) {
      memberWay(10, "", 1, 2)
      memberWay(12, "", 3, 4)
      memberWay(11, "", 2, 3)
    }

    new RouteAnalysisInspector() {
    }.analyze(d)
  }

  private def roundAboutTags = Tags.from("highway" -> "road", "junction" -> "roundabout")
}
