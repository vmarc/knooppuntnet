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
      view: new ol.View({
        minZoom: 6,
        maxZoom: 15
      })
    };

    this.map = new Map(mapOptions);
  }

  ngAfterViewInit(): void {
    const a: ol.Coordinate = ol.proj.fromLonLat([2.24, 50.16]);
    const b: ol.Coordinate = ol.proj.fromLonLat([10.56, 54.09]);
    const extent: ol.Extent = [a[0], a[1], b[0], b[1]];
    this.mainMap.getView().fit(extent);
  }

}
