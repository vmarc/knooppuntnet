package kpn.core.tools.support

import kpn.api.common.route.LinkInfo
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
    val allLinks = LinkInfo.all.map(_.link)

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
    out.println(s"<img src=\"images/$name.png\"/>")
    out.println("</td>")
    out.println("<td>")
    out.println(name)
    out.println("</td>")
    out.println("<td>")
    out.println(Xml.escape(description))
    out.println("</td>")
    out.println("</tr>")
  }
}
