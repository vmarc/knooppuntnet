package kpn.core.tools.support

import java.io.File
import java.io.PrintWriter

import kpn.core.analysis.Link
import kpn.core.analysis.LinkType
import kpn.core.obsolete.OldLinkImageBuilder
import kpn.core.report.LinkImageBuilder
import kpn.core.util.Xml

object ImageGenerationTool {

  def main(args: Array[String]): Unit = {
    new ImageGenerationTool().generate()
  }

}

class ImageGenerationTool {

  def generate(): Unit = {

    import kpn.core.analysis.LinkType._

    val links = Seq(FORWARD, BACKWARD, ROUNDABOUT, NONE) flatMap { linkType =>
      Seq(false, true) flatMap { isLoop =>
        Seq(false, true) flatMap { isOnewayLoopForwardPart =>
          Seq(false, true) flatMap { isOnewayLoopBackwardPart =>
            Seq(false, true) flatMap { isOnewayHead =>
              Seq(false, true) flatMap { isOnewayTail =>
                Seq(false, true) flatMap { hasPrev =>
                  Seq(false, true) map { hasNext =>
                    Link(linkType, hasPrev, hasNext, isLoop, isOnewayLoopForwardPart,
                      isOnewayLoopBackwardPart, isOnewayHead, isOnewayTail, invalid = false
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

    val dir = "/tmp/node-network-analysis"
    new File(dir).mkdirs
    new File(dir + "/images").mkdirs
    new File(dir + "/old-images").mkdirs

    val out = new PrintWriter("%s/index.html".format(dir))
    out.println("<html>")

    out.println("<head>")
    out.println( """  <meta http-equiv="content-type" content="text/html; charset=UTF-8">""")
    out.println( """  <link href="styles.css" rel="stylesheet" type="text/css">""")
    out.println("  <title>Test Images</title>")
    out.println("</head>")

    out.println("<body>")
    out.println( """<table style="margin: 30">""")

    allLinks.foreach { link =>
      LinkImageBuilder.build("%s/images/%s.png".format(dir, link.name), link)
      OldLinkImageBuilder.build("%s/old-images/%s.png".format(dir, link.name), link)

      out.println("<tr>")
      out.println( """<td style="padding:0">""")
      out.println( """<img src="images/%s.png"/>""".format(link.name))
      out.println("</td>")
      out.println( """<td style="padding:0">""")
      out.println( """<img src="old-images/%s.png"/>""".format(link.name))
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
