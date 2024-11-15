import { inject } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouteControlComponent } from '@app/ol/components';
import { MapLinkMenuComponent } from '@app/ol/components';
import { LayerSwitcherComponent } from '@app/ol/components';
import { GeolocationControlComponent } from '../planner/pages/planner/geolocation/geolocation-control.component';
import { PoiMenuComponent } from '../planner/pages/planner/poi/poi-menu.component';
import { MapService } from './map.service';

@Component({
  selector: 'kpn-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <div id="main-map" class="main-map"></div> `,
  styles: [
    `
      .main-map {
        width: 100%;
        height: 100%;
      }

      .main-map:-webkit-full-screen {
        top: 0;
      }

      .main-map:-ms-fullscreen {
        top: 0;
      }

      .main-map:fullscreen {
        top: 0;
      }
    `,
  ],
  providers: [MapService],
  standalone: true,
})
export class MapComponent implements AfterViewInit, OnDestroy {
  private readonly mapService = inject(MapService);

  constructor() {}

  ngAfterViewInit(): void {
    this.mapService.init();
  }

  ngOnDestroy(): void {
    this.mapService.destroy();
  }
}
