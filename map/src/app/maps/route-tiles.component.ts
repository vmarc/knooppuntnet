import { Component } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { Map as MaplibreMap } from 'maplibre-gl';

@Component({
  selector: 'app-route-tiles',
  template: `
    <p>Route tiles</p>
    <div id="map"></div>
  `,
  styles: `
    #map {
      width: 500px;
      height: 500px;
    }
  `,
})
export class RouteTilesComponent implements AfterViewInit, OnDestroy {
  mapInstance: MaplibreMap | null = null;

  ngAfterViewInit(): void {
    const map = new MaplibreMap({
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
      zoom: 13,
    });

    this.mapInstance = map;

    map.on('load', () => {
      map.addSource('routes', {
        type: 'vector',
        tiles: ['http://localhost:4200/tiles/hiking/{z}/{x}/{y}.mvt'],
      });
      map.addLayer({
        id: 'node-route',
        type: 'line',
        source: 'routes',
        'source-layer': 'node-route',
        layout: {
          'line-join': 'round',
          'line-cap': 'round',
        },
        paint: {
          'line-color': '#ff69b4',
          'line-width': 3,
        },
      });

      map.loadImage('/assets/arrow.png').then((response) => {
        map.addImage('arrow', response.data);
      });

      map.addLayer({
        id: 'node-route-arrows',
        type: 'symbol',
        source: 'routes',
        'source-layer': 'node-route',
        layout: {
          'symbol-placement': 'line',
          'icon-image': 'arrow',
          'icon-rotation-alignment': 'map',
          'icon-allow-overlap': true,
          'symbol-spacing': 10,
        },
      });

      map.addLayer({
        id: 'node',
        type: 'circle',
        source: 'routes',
        'source-layer': 'node',
        paint: {
          'circle-radius': 10,
          'circle-color': '#ffffff',
          'circle-stroke-width': 3,
          'circle-stroke-color': '#ff69b4',
        },
      });

      map.addLayer({
        id: 'node-name',
        type: 'symbol',
        source: 'routes',
        'source-layer': 'node',
        layout: {
          'text-field': ['get', 'ref'],
          'text-font': ['Roboto Regular'],
          'text-size': 12,
          'text-anchor': 'center',
        },
      });
    });
  }

  ngOnDestroy(): void {
    if (this.mapInstance) {
      this.mapInstance.remove();
    }
  }
}
