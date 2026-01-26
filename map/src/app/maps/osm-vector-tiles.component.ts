import { Component } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { Map as MaplibreMap } from 'maplibre-gl';

@Component({
  selector: 'app-osm-vector-tiles',
  template: `
    <p>Raster tiles</p>
    <div id="map"></div>
  `,
  styles: `
    #map {
      width: 500px;
      height: 500px;
    }
  `,
})
export class OsmVectorTilesComponent implements AfterViewInit, OnDestroy {
  mapInstance: MaplibreMap | null = null;

  ngAfterViewInit(): void {
    this.mapInstance = new MaplibreMap({
      container: 'map',
      style: '/assets/osm-liberty-style.json',
      center: [4.46839, 51.46774],
      zoom: 10,
    });
  }

  ngOnDestroy(): void {
    if (this.mapInstance) {
      this.mapInstance.remove();
    }
  }
}
