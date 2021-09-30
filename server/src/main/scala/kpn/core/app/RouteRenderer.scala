package kpn.core.app

import kpn.core.doc.RouteDoc

class RouteRenderer(route: RouteDoc, language: String) {

  val drawBoundingBox = true

  val width = 300
  val height = 600
  val linex = 40
  val memberHeight = 50

  def svg: String = {
    header + contents + trailer
  }

  private def header: String = s"""<svg width="$width" height="$height">"""

  private def trailer: String = "</svg>"

  private def contents: String = {
    boundingBox + structure + members
  }

  private def boundingBox: String = {
    if (drawBoundingBox) {
      s"""<rect class="svg-bounding-box" x="0" y="1" width="$width" height="${height - 1}"/>"""
    }
    else {
      ""
    }
  }

  private def members: String = {
    route.analysis.members.zipWithIndex.map { case (member, index) =>
      val y = 30 + (1 + index) * memberHeight
      val memberType = s"""<text x="50" y="$y">${member.memberType}</text>"""
      val name = if (member.isWay) {
        s"""<text x="100" y="${30 + (1 + index) * memberHeight}">${member.description}</text>"""
      }
      else {
        s"""<circle cx="$linex" cy="$y" r="5" fill="blue"/>"""
      }

      val nodes = member.nodes.map { node =>
        s"""
           |<a xlink:href="/$language/node/${node.id}">
           |  <text x="${linex - 10}" y="$y" style="stroke: blue;stroke-width:0.2;" text-anchor="end">${node.alternateName}</text>
           |</a>
         """.stripMargin
      }.mkString

      memberType + name + nodes
    }.mkString
  }

  private def structure: String = {

    val verticalLine = s"""<line x1="$linex" y1="1" x2="$linex" y2="$height"/>"""

    val firstMemberSeparator = s"""<line x1="${linex - 3}" y1="${30 + memberHeight - (memberHeight / 2)}" x2="${linex + 3}" y2="${30 + memberHeight - (memberHeight / 2)}" style="stroke:rgb(255,0,0);stroke-width:1" />"""

    val memberSeparators = route.analysis.members.zipWithIndex.map { case (member, index) =>
      val y = 30 + (1 + index) * memberHeight
      s"""<line x1="${linex - 3}" y1="${y + (memberHeight / 2)}" x2="${linex + 3}" y2="${y + (memberHeight / 2)}" style="stroke:rgb(255,0,0);stroke-width:1" />"""
    }
    """<g class="svg-structure">""" + verticalLine + firstMemberSeparator + memberSeparators + "</g>"
  }
}
