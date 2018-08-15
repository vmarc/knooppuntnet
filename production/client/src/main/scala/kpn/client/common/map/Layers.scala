package kpn.client.common.map

import ol.Ol.TileUrlFunctionType
import kpn.shared.NetworkType
import kpn.shared.tiles.ZoomLevel.bitmapTileMaxZoom
import kpn.shared.tiles.ZoomLevel.bitmapTileMinZoom
import kpn.shared.tiles.ZoomLevel.vectorTileMaxOverZoom
import kpn.shared.tiles.ZoomLevel.vectorTileMaxZoom
import kpn.shared.tiles.ZoomLevel.vectorTileMinZoom

object Layers {

  def osm: ol.layer.Base = {
    val tileLayer = new ol.layer.Tile(
      olx.layer.TileOptions(
        source = new ol.source.OSM()
      )
    )
    tileLayer.set("name", "OpenStreetMap")
    tileLayer
  }

  def debug: ol.layer.Base = new ol.layer.Tile(
    olx.layer.TileOptions(
      source = new ol.source.TileDebug(
        olx.source.TileDebugOptions(
          projection = ol.ProjectionLike("EPSG:3857"),
          tileGrid = new ol.source.OSM().getTileGrid()
        )
      )
    )
  )

  def vectorTileLayer(networkType: NetworkType): ol.layer.VectorTile = {

    val scalaFun: (ol.Ol.TileCoord, Double, ol.proj.Projection) => String = {
      (tileCoord: ol.Ol.TileCoord, something: Double, projection: ol.proj.Projection) => {
        val zIn = tileCoord._1.toInt
        val xIn = tileCoord._2.toInt
        val yIn = tileCoord._3.toInt
        var z = if (zIn >= vectorTileMaxZoom) vectorTileMaxZoom else zIn
        var x = xIn
        var y = -yIn - 1
        s"/tiles/${networkType.name}/$z/$x/$y.mvt"
      }
    }

    val jsFun: TileUrlFunctionType = scalaFun

    new ol.layer.VectorTile(
      olx.layer.VectorTileOptions(
        renderMode = "vector",
        source = new ol.source.VectorTile(
          olx.source.VectorTileOptions(
            format = new ol.format.MVT(
              olx.format.MVTOptions(
                featureClass = ol.Feature
              )
            ),
            tileGrid = ol.tilegrid.Tilegrid.createXYZ(
              olx.tilegrid.XYZOptions(
                minZoom = vectorTileMinZoom.toDouble,
                maxZoom = vectorTileMaxOverZoom.toDouble
              )
            ),
            tilePixelRatio = vectorTileMaxZoom.toDouble,
            tileUrlFunction = jsFun
          )
        )
      )
    )
  }

  def bitmapTileLayer(networkType: NetworkType): ol.layer.Tile = {
    new ol.layer.Tile(
      olx.layer.TileOptions(
        source = new ol.source.XYZ(
          olx.source.XYZOptions(
            minZoom = bitmapTileMinZoom.toDouble,
            maxZoom = bitmapTileMaxZoom.toDouble,
            url = s"/tiles/${networkType.name}/{z}/{x}/{y}.png"
          )
        )
      )
    )
  }
}
