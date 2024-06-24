package kpn.core.tools.support

import kpn.core.analysis.Link
import kpn.core.analysis.LinkType
import kpn.core.report.LinkImageBuilder
import kpn.core.tools.config.Dirs
import kpn.core.util.Xml

import java.io.File
import java.io.PrintWriter

object ImageGenerationTool {

  def main(args: Array[String]): Unit = {
    new ImageGenerationTool().generate()
  }
}

class ImageGenerationTool {

  private val dir = new File(Dirs.root, "routes")
  private val out = {
    dir.mkdirs
    new File(dir, "images").mkdirs
    new PrintWriter(new File(dir, "index.html"))
  }

  def generate(): Unit = {
    val allLinks = buildAllLinks()

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
      LinkImageBuilder.build(s"${dir.getAbsolutePath}/images/${link.name}.png", link)
      processLink(link.name, link.description)
    }
    LinkImageBuilder.buildNode(s"${dir.getAbsolutePath}/images/n.png")
    processLink("n", "node")

    out.println("</table>")
    out.println("</body>")
    out.println("</html>")
    out.close()

    println("ready")
  }

  private def processLink(name: String, description: String): Unit = {
    out.println("<tr>")
    out.println("""<td style="padding:0">""")
    out.println("""<img src="images/%s.png"/>""".format(name))
    out.println("</td>")
    out.println("<td>")
    out.println(name)
    out.println("</td>")
    out.println("<td>")
    out.println(Xml.escape(description))
    out.println("</td>")
    out.println("</tr>")
  }

  private def buildAllLinks(): Seq[Link] = {
    Seq(LinkType.Forward, LinkType.Backward, LinkType.RoundaboutRight, LinkType.All) flatMap { linkType =>
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
                      isOnewayTail
                    )
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
