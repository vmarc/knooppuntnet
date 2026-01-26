import { Component } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { Map as MaplibreMap } from 'maplibre-gl';

@Component({
  selector: 'app-globe',
  template: `
    <p>Globe</p>
    <div id="map"></div>
  `,
  styles: `
    #map {
      width: 500px;
      height: 500px;
    }
  `,
})
export class GlobeComponent implements AfterViewInit, OnDestroy {
  mapInstance: MaplibreMap | null = null;

  ngAfterViewInit(): void {
    this.mapInstance = new MaplibreMap({
      container: 'map',
      style: 'https://demotiles.maplibre.org/globe.json',
      center: [0, 0],
      zoom: 2,
    });
  }

  ngOnDestroy(): void {
    if (this.mapInstance) {
      this.mapInstance.remove();
    }
  }
}
