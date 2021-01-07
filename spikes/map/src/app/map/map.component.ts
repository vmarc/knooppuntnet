import {AfterViewInit} from '@angular/core';
import {Component} from '@angular/core';
import {Map} from 'ol';
import {View} from 'ol';
import apply from 'ol-mapbox-style';
import BaseLayer from 'ol/layer/Base';
import TileLayer from 'ol/layer/Tile';
import {fromLonLat} from 'ol/proj';
import OSM from 'ol/source/OSM';
import {OsmLibertyStyle} from './osm-liberty-style';


@Component({
  selector: 'app-map',
  template: `
    <div class="side-bar">
      <div class="zoom">
        zoom = {{zoom}}
      </div>
      <button (click)="osmVisible(true)">OSM visible</button>
      <button (click)="osmVisible(false)">OSM invisible</button>
      <button (click)="backgroundVisible(true)">Background visible</button>
      <button (click)="backgroundVisible(false)">Background invisible</button>
    </div>
    <div id="test-map" class="test-map">
    </div>
  `,
  styles: [`

    .side-bar {
      position: fixed;
      top: 0;
      left: 0;
      bottom: 0;
      width: calc(12em - 1px);
      border-right: 1px solid lightgray;
    }

    .test-map {
      position: fixed;
      top: 0;
      left: 12em;
      bottom: 0;
      right: 0;
    }

    button {
      display: block;
      width: 12em;
      margin: 1em;
    }

    .zoom {
      margin: 1em;
    }
  `]
})
export class MapComponent implements AfterViewInit {

  map: Map;
  zoom: number;

  private osmLayer = new TileLayer({
    source: new OSM(),
    visible: false
  });

  backgroundLayer: BaseLayer;

  ngAfterViewInit(): void {

    this.map = new Map({
      target: 'test-map',
      view: new View({
        zoom: 14,
        minZoom: 4,
        maxZoom: 20,
        center: fromLonLat([4.46839, 51.46774])
      })
    });

    apply(this.map, OsmLibertyStyle.osmLibertyStyle);
    this.backgroundLayer = this.map.getLayers().item(0);

    this.map.addLayer(this.osmLayer);

    this.map.getView().on('change:resolution', () => this.updateZoom());
    setTimeout(() => this.updateZoom(), 250);
  }

  private updateZoom(): void {
    this.zoom = this.map.getView().getZoom();
  }

  osmVisible(visible: boolean): void {
    this.osmLayer.setVisible(visible);
  }

  backgroundVisible(visible: boolean): void {
    this.backgroundLayer.setVisible(visible);
  }
}
