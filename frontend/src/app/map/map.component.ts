import { inject } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MapService } from './map.service';

@Component({
  selector: 'kpn-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <div id="main-map" class="main-map"></div> `,
  styles: [
    `
      :host {
        width: 100%;
        height: 100%;
      }

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
  standalone: true,
})
export class MapComponent implements AfterViewInit, OnDestroy {
  private readonly mapService = inject(MapService);

  ngAfterViewInit(): void {
    setTimeout(() => {
      this.mapService.init();
    }, 100);
  }

  ngOnDestroy(): void {
    this.mapService.destroy();
  }
}
