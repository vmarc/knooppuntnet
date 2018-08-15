package kpn.core.engine.analysis.route

import kpn.core.engine.analysis.NodeUtil

/**
 * Analysis of the route name.
 *
 * The regular expected pattern is two times 2 digits separated with a dash. The first two digits
 * represent the start node, the second 2 digits the end node. The start node is expected to have
 * a lower value than the end node.
 *
 * If the start node has a higher value than the end node, the route name considered to be 'reversed'.
 */
class ZzzzObsoleteRouteNameAnalyzer() {

  def analyze(nameOption: Option[String]): RouteNameAnalysis = {

    nameOption match {

      case None => RouteNameAnalysis()

      case Some(name) =>

        val namePattern = """(.*)-(.*)""".r

        val nodeNames: (Option[String], Option[String]) = namePattern.findFirstMatchIn(name) match {
          case None => (None, None)
          case Some(m) =>
            if (m.group(1).contains("-") || m.group(2).contains("-")) {
              (None, None)
            }
            else if (m.group(1).endsWith("-")) {
              (Some(m.group(2)), None)
            }
            else {
              val n1 = if (m.group(1).nonEmpty) Some(m.group(1)) else None
              val n2 = if (m.group(2).nonEmpty) Some(m.group(2)) else None
              (n1, n2)
            }
        }

        val number1 = nodeNames._1 match {
          case Some(nodeName) => NodeUtil.toNumber(nodeName)
          case _ => None
        }

        val number2 = nodeNames._2 match {
          case Some(nodeName) => NodeUtil.toNumber(nodeName)
          case _ => None
        }

        val reversed: Boolean = if (number1.isDefined && number2.isDefined) {
          // only compare if both node names consist of digits only
          number1.get > number2.get
        }
        else {
          false
        }

        val startNodeName: Option[String] = if (reversed) nodeNames._2 else nodeNames._1

        val endNodeName: Option[String] = if (reversed) nodeNames._1 else nodeNames._2

        RouteNameAnalysis(nameOption, startNodeName, endNodeName, reversed)
    }
  }
}
