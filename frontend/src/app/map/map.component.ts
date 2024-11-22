import { OverlayConfig } from '@angular/cdk/overlay';
import { FlexibleConnectedPositionStrategy } from '@angular/cdk/overlay';
import { OverlayRef } from '@angular/cdk/overlay';
import { Overlay } from '@angular/cdk/overlay';
import { CdkPortal } from '@angular/cdk/portal';
import { ElementRef } from '@angular/core';
import { viewChild } from '@angular/core';
import { inject } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MapRoutePopupRoute } from '@app/state';
import { Coordinate } from 'ol/coordinate';
import { MapRoutePopupComponent } from './popup/map-route-popup.component';
import { MapService } from './map.service';

@Component({
  selector: 'kpn-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div id="main-map" class="main-map" #overlayOrigin></div>

    <ng-template cdkPortal>
      <kpn-map-route-popup />
    </ng-template>
  `,
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
  imports: [CdkPortal, MapRoutePopupComponent],
})
export class MapComponent implements AfterViewInit, OnDestroy {
  private readonly mapService = inject(MapService);

  private readonly overlay = inject(Overlay);
  private readonly elementRef = viewChild<ElementRef<HTMLDivElement>>('overlayOrigin');
  portal = viewChild(CdkPortal);
  overlayRef: OverlayRef;
  positionStrategy: FlexibleConnectedPositionStrategy;

  constructor() {
    this.mapService.xxx((hooverRoutes: Array<MapRoutePopupRoute>, coordinate: Coordinate) => {
      if (this.positionStrategy && coordinate) {
        if (hooverRoutes.length > 0) {
          this.positionStrategy.setOrigin({ x: coordinate[0], y: coordinate[1] }).apply();
        } else {
          this.positionStrategy.setOrigin({ x: 0, y: 0 }).apply();
        }
      }
    });
  }

  ngAfterViewInit(): void {
    setTimeout(() => {
      this.mapService.init();
      this.openModel();
    }, 100);
  }

  ngOnDestroy(): void {
    this.mapService.destroy();
  }

  private openModel() {
    console.log('elementRef', this.elementRef());
    this.positionStrategy = this.overlay
      .position()
      .flexibleConnectedTo(this.elementRef())
      .withDefaultOffsetY(10)
      .withDefaultOffsetX(10)
      .withViewportMargin(10)
      .withPositions([
        {
          originX: 'start',
          originY: 'top',
          overlayX: 'start',
          overlayY: 'top',
          offsetX: 10,
          offsetY: 10,
        },
        {
          originX: 'start',
          originY: 'top',
          overlayX: 'start',
          overlayY: 'bottom',
          offsetX: -10,
          offsetY: -10,
        },
        {
          originX: 'start',
          originY: 'top',
          overlayX: 'end',
          overlayY: 'top',
          offsetX: -10,
          offsetY: 10,
        },
        {
          originX: 'start',
          originY: 'top',
          overlayX: 'end',
          overlayY: 'bottom',
          offsetX: -10,
          offsetY: -10,
        },
      ]);

    const config = new OverlayConfig({
      positionStrategy: this.positionStrategy,
    });
    this.overlayRef = this.overlay.create(config);
    this.overlayRef.attach(this.portal());
  }
}
