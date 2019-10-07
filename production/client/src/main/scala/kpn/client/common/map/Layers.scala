// TODO migrate to Angular
package kpn.client.common.map

import kpn.shared.NetworkType
import kpn.shared.tiles.ZoomLevel.bitmapTileMaxZoom
import kpn.shared.tiles.ZoomLevel.bitmapTileMinZoom
import kpn.shared.tiles.ZoomLevel.vectorTileMaxZoom
import kpn.shared.tiles.ZoomLevel.vectorTileMinZoom

object Layers {

  def osm: ol.layer.Base = {
    val tileLayer = new ol.layer.Tile(
      olx.layer.TileOptions(
        source = new ol.source.XYZ(
          olx.source.XYZOptions(
            url = "https://{a-c}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          )
        )
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
                tileSize = 512,
                minZoom = vectorTileMinZoom.toDouble,
                maxZoom = vectorTileMaxZoom.toDouble
              )
            ),
            tilePixelRatio = vectorTileMaxZoom.toDouble,
            url = s"/tiles/${networkType.name}/{z}/{x}/{y}.mvt"
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
