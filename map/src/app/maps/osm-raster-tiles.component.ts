import { Component } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { Map as MaplibreMap } from 'maplibre-gl';

@Component({
  selector: 'app-osm-raster-tiles',
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
export class OsmRasterTilesComponent implements AfterViewInit, OnDestroy {
  mapInstance: MaplibreMap | null = null;

  ngAfterViewInit(): void {
    this.mapInstance = new MaplibreMap({
      container: 'map',
      style: {
        version: 8,
        sources: {
          'raster-tiles': {
            type: 'raster',
            tiles: ['https://tile.openstreetmap.org/{z}/{x}/{y}.png'],
            tileSize: 256,
            minzoom: 0,
            maxzoom: 19,
          },
        },
        layers: [
          {
            id: 'simple-tiles',
            type: 'raster',
            source: 'raster-tiles',
          },
        ],
      },
      attributionControl: {
        compact: false,
        customAttribution:
          '&copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors',
      },

      center: [4.46839, 51.46774],
      zoom: 14,
    });
  }

  ngOnDestroy(): void {
    if (this.mapInstance) {
      this.mapInstance.remove();
    }
  }
}
