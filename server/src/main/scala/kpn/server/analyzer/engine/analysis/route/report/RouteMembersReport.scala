package kpn.server.analyzer.engine.analysis.route.report

import kpn.core.analysis.RouteMember
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext

object RouteMembersReport {
  def report(context: RouteAnalysisContext): String = {
    new RouteMembersReport(context).report()
  }
}

class RouteMembersReport(context: RouteAnalysisContext) {
  def report(): String = {
    s"""<table>
       |  <tr class="header">
       |    <td>nr</td>
       |    <td></td>
       |    <td>link</td>
       |    <td>id</td>
       |    <td>name</td>
       |    <td>role</td>
       |    <td>accessible</td>
       |  </tr>
       |${routeMemberRows()}
       |</table>
       |""".stripMargin
  }

  private def routeMemberRows(): String = {
    context.routeMembers.get.zipWithIndex.map { case (routeMember, index) =>
      routeMemberRow(routeMember, index)
    }.mkString
  }

  private def routeMemberRow(routeMember: RouteMember, index: Int): String = {
    s"""<tr>
       |  <td>
       |    ${index + 1}
       |  </td>
       |  <td style="padding:0">
       |    <img src="images/${routeMember.linkName}.png"/>
       |  </td>
       |  <td>
       |    <pre>${routeMember.link.map(_.reportString).getOrElse("")}</pre>
       |  </td>
       |  <td>
       |    <a href="https://www.openstreetmap.org/${routeMember.memberType}/${routeMember.id}">${routeMember.id}</a>
       |  </td>
       |  <td>
       |    ${routeMember.name}
       |  </td>
       |  <td>
       |    ${routeMember.role.getOrElse("")}
       |  </td>
       |  <td>
       |    ${if (routeMember.accessible) "" else "no"}
       |  </td>
       |</tr>
       |""".stripMargin
  }
}
