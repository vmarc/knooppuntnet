import {AfterViewInit, Component, OnInit} from '@angular/core';

import Map from 'ol/Map';
import View from 'ol/View';
import Coordinate from 'ol/View';
import Extent from 'ol/View';
import TileLayer from 'ol/layer/Tile';
import OSM from 'ol/source/OSM';
import XYZ from 'ol/source/XYZ';
import TileDebug from 'ol/source/TileDebug';
import MVT from 'ol/format/MVT';
import VectorTileLayer from 'ol/layer/VectorTile';
import VectorTile from 'ol/source/VectorTile';
import Style from 'ol/style/Style';
import Fill from 'ol/style/Fill';
import Stroke from 'ol/style/Stroke';
import {fromLonLat} from 'ol/proj';
import {createXYZ} from 'ol/tilegrid';

import {ZoomLevel} from "../zoom-level";

@Component({
  selector: 'kpn-map-page',
  templateUrl: './map-page.component.html',
  styleUrls: ['./map-page.component.css']
})
export class MapPageComponent implements OnInit, AfterViewInit {

  map: Map;
  bitmapTileLayer = this.buildBitmapTileLayer();
  vectorTileLayer = this.buildVectorTileLayer();

  constructor() {
  }

  ngOnInit() {
    const mapOptions = {
      target: 'map',
      layers: [
        this.osmLayer(), this.bitmapTileLayer, this.vectorTileLayer, this.debugLayer()
      ],
      view: new View({
        minZoom: ZoomLevel.minZoom,
        maxZoom: ZoomLevel.maxZoom
      })
    };

    this.map = new Map(mapOptions);
  }

  ngAfterViewInit(): void {
    const a: Coordinate = fromLonLat([2.24, 50.16]);
    const b: Coordinate = fromLonLat([10.56, 54.09]);
    const extent: Extent = [a[0], a[1], b[0], b[1]];
    const view = this.map.getView();
    view.fit(extent);
    view.on("change:resolution", () => this.zoom(view.getZoom()));
  }

  private zoom(zoomLevel: number) {
    const zoom = Math.round(zoomLevel);
    if (zoom <= ZoomLevel.bitmapTileMaxZoom) {
      this.bitmapTileLayer.setVisible(true);
      this.vectorTileLayer.setVisible(false);
    }
    else if (zoom >= ZoomLevel.vectorTileMinZoom) {
      this.bitmapTileLayer.setVisible(false);
      this.vectorTileLayer.setVisible(true);
    }
    return true;
  }

  private osmLayer() {
    const osmLayer = new TileLayer({
      source: new OSM()
    });
    osmLayer.set("name", "OpenStreetMap");
    return osmLayer;
  }

  private buildBitmapTileLayer() {
    return new TileLayer({
      source: new XYZ({
        minZoom: ZoomLevel.bitmapTileMinZoom,
        maxZoom: ZoomLevel.bitmapTileMaxZoom,
        url: "/tiles/rcn/{z}/{x}/{y}.png"
      })
    });
  }

  private debugLayer() {
    return new TileLayer({
      source: new TileDebug({
        projection: "EPSG:3857",
        tileGrid: new OSM().getTileGrid()
      })
    });
  }

  private buildVectorTileLayer() {

    const format = new MVT();

    const urlFunction = function(tileCoord, pixelRatio, projection) {
      const zIn = tileCoord[0];
      const xIn = tileCoord[1];
      const yIn = tileCoord[2];

      const z = zIn >= ZoomLevel.vectorTileMaxZoom ? ZoomLevel.vectorTileMaxZoom : zIn;
      const x = xIn;
      const y = -yIn - 1;
      return "/tiles/rcn/" + z + "/" + x + "/" + y + ".mvt"
    };

    const tileGrid = createXYZ({
      // minZoom: ZoomLevel.vectorTileMinZoom,
      // maxZoom: ZoomLevel.vectorTileMaxOverZoom
    });

    const source = new VectorTile({
      format: format,
      tileGrid: tileGrid,
      tileUrlFunction: urlFunction
    });

    const layer = new VectorTileLayer({
      source: source
    });

    layer.setStyle(function (feature, resolution) {
      const properties = feature.getProperties();
      console.log("feature " + JSON.stringify(properties, null, 2));
      return new Style({
        fill: new Fill({
          color: 'black'
        }),
        stroke: new Stroke({
          width: 3,
          color: 'red'
        })
      });
    });
    return layer;
  }

}
