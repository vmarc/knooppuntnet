import {Component, OnInit,AfterViewInit} from '@angular/core';

import * as ol from 'openlayers';

@Component({
  selector: 'kpn-map-page',
  templateUrl: './map-page.component.html',
  styleUrls: ['./map-page.component.css']
})
export class MapPageComponent implements OnInit, AfterViewInit {

  mainMap: ol.Map;

  constructor() {
  }

  ngOnInit() {

    const tileLayer = new ol.layer.Tile(
      {
        source: new ol.source.OSM()
      }
    );
    tileLayer.set("name", "OpenStreetMap");

    const mapOptions = {
      target: 'map',
      layers: [
        tileLayer
      ],
      view: new ol.View({
        minZoom: 6,
        maxZoom: 15
      })
    };

    this.mainMap = new ol.Map(mapOptions);
  }

  ngAfterViewInit(): void {
    const a: ol.Coordinate = ol.proj.fromLonLat([2.24, 50.16]);
    const b: ol.Coordinate = ol.proj.fromLonLat([10.56, 54.09]);
    const extent: ol.Extent = [a[0], a[1], b[0], b[1]];
    this.mainMap.getView().fit(extent);
  }

}
