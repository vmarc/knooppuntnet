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
  `,
  styles: `
    #map {
      width: 700px;
      height: 700px;
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
}
