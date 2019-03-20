package kpn.core.gpx

import java.io.PrintWriter
import java.io.StringWriter

class GpxWriter(file: GpxFile) {

  private val sw = new StringWriter
  private val out = new PrintWriter(sw)

  out.println("<?xml version=\"1.0\"?>")
  out.print("<gpx creator=\"OSM node network analyzer\" version=\"1.0\" ")
  out.print("xmlns=\"http://www.topografix.com/GPX/1/0\" ")
  out.print("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ")
  out.print("xsi:schemaLocation=\"http://www.topografix.com/GPX/1/0 ")
  out.println("http://www.topografix.com/GPX/1/0/gpx.xsd\">")

  file.wayPoints.foreach { wpt =>
    out.printf("  <wpt lat=\"%s\" lon=\"%s\">\n", wpt.lat, wpt.lon)
    out.printf("    <name>%s</name>\n", wpt.name)
    out.printf("  </wpt>\n")
  }

  out.printf("  <trk>\n")
  file.trackSegments foreach { segment =>
    out.printf("    <trkseg>\n")
    segment.trackPoints foreach { trkpt =>
      out.printf("      <trkpt lat=\"%s\" lon=\"%s\"/>\n", trkpt.lat, trkpt.lon)
    }
    out.printf("    </trkseg>\n")
  }
  out.printf("  </trk>\n")

  out.println("</gpx>")
  out.close()

  val string: String = sw.toString
}
