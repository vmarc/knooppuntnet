import { inject } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { Component } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { input } from '@angular/core';
import { Bounds } from '@api/common';
import { GeometryDiff } from '@api/common/route';
import { RouteNodeChange } from '@api/common/route/route-node-change';
import { MAP_SERVICE_TOKEN } from '@app/ol/services';
import { RouteChangeMapService } from './route-change-map.service';

@Component({
  selector: 'kpn-route-change-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <div [id]="service.mapId" class="kpn-embedded-map"></div> `,
  providers: [
    RouteChangeMapService,
    {
      provide: MAP_SERVICE_TOKEN,
      useExisting: RouteChangeMapService,
    },
  ],
})
export class RouteChangeMapComponent implements AfterViewInit, OnDestroy {
  geometryDiff = input.required<GeometryDiff>();
  bounds = input.required<Bounds>();
  nodeChanges = input.required<RouteNodeChange[]>();

  protected readonly service = inject(RouteChangeMapService);

  ngAfterViewInit(): void {
    setTimeout(() => this.service.init(this.geometryDiff(), this.nodeChanges(), this.bounds()), 1);
  }

  ngOnDestroy(): void {
    this.service.destroy();
  }
}
