package kpn.core.tools.country

import java.io._

import com.vividsolutions.jts.geom.LinearRing

/*
  Writes an html file with a leaflet map showing given ring outline (for debugging purposes).
 */
class RingWriter {

  def write(filename: String, ring: LinearRing): Unit = {

    val cs = ring.getCoordinates
    val f = new PrintWriter(new FileWriter(filename))
    try {
      f.println(prefix)

      cs.foreach { p =>
        f.println(s"points.push([${p.x}, ${p.y}]);")
      }

      val xmin = cs.map(_.x).min
      val xmax = cs.map(_.x).max
      val ymin = cs.map(_.y).min
      val ymax = cs.map(_.y).max
      f.println(suffix.format(xmin, ymin, xmax, ymax))
    }
    finally {
      f.close()
    }
  }

  private val prefix =
    """
      |<!DOCTYPE html>
      |<html lang="en">
      |<head><title>Test</title>
      |    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
      |    <link rel="stylesheet" href="stylesheets/styles.css">
      |    <link rel="stylesheet" href="stylesheets/leaflet.css">
      |    <script src="javascripts/leaflet.js" type="text/javascript"></script>
      |    <script src="javascripts/leaflet.label.js" type="text/javascript"></script>
      |</head>
      |<body>
      |
      |    <div id="route-map" style="width: 600px; height: 400px"></div>
      |
      |    <script>
      |
      |var map = L.map('route-map');
      |var osm = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(map);
      |var cycle = L.tileLayer('https://{s}.tile.opencyclemap.org/cycle/{z}/{x}/{y}.png');
      |var countryBoundaries = L.layerGroup().addTo(map);
      |
      |var tileLayers = {
      |"OpenStreetMap": osm,
      |"OpenCycleMap": cycle,
      |"None": ''
      |};
      |
      |var overlays = {
      |"Nodes": countryBoundaries
      |};
      |
      |L.control.layers(tileLayers, overlays).addTo(map);
      |
      |var points = [];
    """.stripMargin

  private val suffix =
    """
      |L.polyline(points,{weight: '3', opacity: '1', color: 'red'}).addTo(countryBoundaries);
      |
      |map.fitBounds([
      |[%s, %s],
      |[%s, %s]
      |]);
      |
      |map.on('click', function(e) {
      |    alert("Lat, Lon : " + e.latlng.lat + ", " + e.latlng.lng)
      |});
      |
      |</script>
      |</body>
      |</html>
    """.stripMargin
}
