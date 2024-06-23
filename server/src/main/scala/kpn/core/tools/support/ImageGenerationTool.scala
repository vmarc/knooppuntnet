package kpn.core.tools.support

import kpn.core.analysis.Link
import kpn.core.analysis.LinkType
import kpn.core.analysis.LinkType.BACKWARD
import kpn.core.analysis.LinkType.FORWARD
import kpn.core.analysis.LinkType.NONE
import kpn.core.analysis.LinkType.ROUNDABOUT
import kpn.core.report.LinkImageBuilder
import kpn.core.util.Xml

import java.io.File
import java.io.PrintWriter

object ImageGenerationTool {

  def main(args: Array[String]): Unit = {
    new ImageGenerationTool().generate()
  }
}

class ImageGenerationTool {

  def generate(): Unit = {
    val links = Seq(FORWARD, BACKWARD, ROUNDABOUT, NONE) flatMap { linkType =>
      Seq(false, true) flatMap { isLoop =>
        Seq(false, true) flatMap { isOnewayLoopForwardPart =>
          Seq(false, true) flatMap { isOnewayLoopBackwardPart =>
            Seq(false, true) flatMap { isOnewayHead =>
              Seq(false, true) flatMap { isOnewayTail =>
                Seq(false, true) flatMap { hasPrev =>
                  Seq(false, true) map { hasNext =>
                    Link(
                      linkType,
                      hasPrev,
                      hasNext,
                      isLoop,
                      isOnewayLoopForwardPart,
                      isOnewayLoopBackwardPart,
                      isOnewayHead,
                      isOnewayTail,
                      invalid = false
                    )
                  }
                }
              }
            }
          }
        }
      }
    }
    val allLinks = links :+ Link(
      LinkType.NONE,
      hasPrev = false,
      hasNext = false,
      isLoop = false,
      isOnewayLoopForwardPart = false,
      isOnewayLoopBackwardPart = false,
      isOnewayHead = false,
      isOnewayTail = false,
      invalid = true
    )

    val dir = "/Users/marc/tmp/node-network-analysis"
    new File(dir).mkdirs
    new File(dir + "/images").mkdirs

    val out = new PrintWriter("%s/index.html".format(dir))
    out.println("<html>")

    out.println("<head>")
    out.println("""  <meta http-equiv="content-type" content="text/html; charset=UTF-8">""")
    out.println("""  <link href="styles.css" rel="stylesheet" type="text/css">""")
    out.println("  <title>Test Images</title>")
    out.println("  <style>")
    out.println("  table, th, td {")
    out.println("    border: 1px solid gray;")
    out.println("    border-collapse: collapse;")
    out.println("  }")
    out.println("  ")
    out.println("  </style>")
    out.println("</head>")

    out.println("<body>")
    out.println("""<table style="margin: 30">""")

    allLinks.foreach { link =>
      LinkImageBuilder.build("%s/images/%s.png".format(dir, link.name), link)
      out.println("<tr>")
      out.println("""<td style="padding:0">""")
      out.println("""<img src="images/%s.png"/>""".format(link.name))
      out.println("</td>")
      out.println("<td>")
      out.println(link.name)
      out.println("</td>")
      out.println("<td>")
      out.println(Xml.escape(link.description))
      out.println("</td>")
      out.println("</tr>")
    }

    out.println("</table>")
    out.println("</body>")
    out.println("</html>")
    out.close()

    println("ready")
  }
}
