import { AfterViewInit } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Bounds } from '@api/common/bounds';
import { RawNode } from '@api/common/data/raw/raw-node';
import { GeometryDiff } from '@api/common/route/geometry-diff';
import { RouteChangeMapService } from '@app/analysis/components/changes/route/route-change-map.service';

@Component({
  selector: 'kpn-route-change-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div [id]="service.mapId" class="kpn-embedded-map">
      <kpn-layer-switcher
        [layerStates]="service.layerStates$ | async"
        (layerStateChange)="service.layerStateChange($event)"
      />
      <kpn-map-link-menu [map]="service.map" />
    </div>
  `,
  providers: [RouteChangeMapService],
})
export class RouteChangeMapComponent implements AfterViewInit, OnDestroy {
  @Input() geometryDiff: GeometryDiff;
  @Input() nodes: RawNode[];
  @Input() bounds: Bounds;

  constructor(protected service: RouteChangeMapService) {}

  ngAfterViewInit(): void {
    setTimeout(
      () => this.service.init(this.geometryDiff, this.nodes, this.bounds),
      1
    );
  }

  ngOnDestroy(): void {
    this.service.destroy();
  }
}
