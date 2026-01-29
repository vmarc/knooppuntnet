import { inject } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MapService } from '@app/map/map.service';

@Component({
  selector: 'ui-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <div id="map"></div> `,
  styles: [
    `
      :host {
        width: 100%;
        height: 100%;
      }

      #map {
        width: 100%;
        height: 100%;
      }

      #map:-webkit-full-screen {
        top: 0;
      }

      #map:-ms-fullscreen {
        top: 0;
      }

      #map:fullscreen {
        top: 0;
      }
    `,
  ],
})
export class MapComponent implements AfterViewInit, OnDestroy {
  private readonly mapService = inject(MapService);

  ngAfterViewInit(): void {
    this.mapService.init();
  }

  ngOnDestroy(): void {
    this.mapService.destroy();
  }
}
