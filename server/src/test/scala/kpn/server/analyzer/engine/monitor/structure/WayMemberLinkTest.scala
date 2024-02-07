package kpn.server.analyzer.engine.monitor.structure

import kpn.core.util.UnitTest

class WayMemberLinkTest extends UnitTest {

  test("links") {
    val setup = new StructureTestSetupBuilder() {
      memberWay(11, "")
      memberWay(12, "")
      memberWay(13, "")
      memberWay(14, "")
    }.build
    val wayMembers = setup.data.relations(1).wayMembers

    val links = WayMemberLink.from(wayMembers)
    links.map(_.wayMember.way.id).shouldMatchTo(Seq(11, 12, 13, 14))
    links.map(_.next.map(_.wayMember.way.id)).shouldMatchTo(Seq(Some(12), Some(13), Some(14), None))
  }

  test("empty links") {
    val links = WayMemberLink.from(Seq.empty)
    links.shouldMatchTo(Seq.empty)
  }

  test("single link") {
    val setup = new StructureTestSetupBuilder() {
      memberWay(11, "")
    }.build
    val wayMembers = setup.data.relations(1).wayMembers

    val links = WayMemberLink.from(wayMembers)
    links.map(_.wayMember.way.id).shouldMatchTo(Seq(11))
    links.map(_.next.map(_.wayMember.way.id)).shouldMatchTo(Seq(None))
  }
}
