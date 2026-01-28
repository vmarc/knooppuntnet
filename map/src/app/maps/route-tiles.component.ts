import { Component } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { inject } from '@angular/core';
import { NewMapService } from './new-map.service';

@Component({
  selector: 'app-route-tiles',
  template: `
    <p>Route tiles</p>
    <div id="map"></div>
    <div>
      <button (click)="selectRoute1()">route 01-02</button>
      <button (click)="selectRoute2()">route 02-92</button>
      <button (click)="resetRouteSelection()">reset route selection</button>
    </div>
    <div>
      <button (click)="initRouteType('hiking')">hiking</button>
      <button (click)="initRouteType('cycling')">cycling</button>
      <button (click)="initRouteType('horse-riding')">horse riding</button>
      <button (click)="initRouteType('canoe')">canoe</button>
      <button (click)="initRouteType('motorboat')">motorboat</button>
      <button (click)="initRouteType('inline-skating')">inline skating</button>
      <button (click)="initRouteType('mtb')">mtb</button>
    </div>
    <div>
      <span>node route layer:</span>
      <button (click)="hideNodeRouteLayer()">hide</button>
      <button (click)="showNodeRouteLayer()">show</button>
    </div>
    <div>
      <span>non node route layer:</span>
      <button (click)="hideRouteLayer()">hide</button>
      <button (click)="showRouteLayer()">show</button>
    </div>
  `,
  styles: `
    #map {
      width: 700px;
      height: 700px;
    }

    button {
      margin: 5px;
    }
  `,
})
export class RouteTilesComponent implements AfterViewInit, OnDestroy {
  private service = inject(NewMapService);

  ngAfterViewInit(): void {
    this.service.init();
  }

  ngOnDestroy(): void {
    this.service.destroy();
  }

  selectRoute1(): void {
    this.service.selectRoutes(['13844575']);
  }

  selectRoute2(): void {
    this.service.selectRoutes(['3665081']);
  }

  resetRouteSelection(): void {
    this.service.resetRouteSelection();
  }

  initRouteType(routeType: string): void {
    this.service.initRouteType(routeType);
  }

  hideRouteLayer(): void {
    this.service.hideRouteLayer();
  }

  showRouteLayer(): void {
    this.service.showRouteLayer();
  }

  hideNodeRouteLayer(): void {
    this.service.hideNodeRouteLayer();
  }

  showNodeRouteLayer(): void {
    this.service.showNodeRouteLayer();
  }
}
