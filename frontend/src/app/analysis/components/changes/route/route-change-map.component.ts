import { inject } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Bounds } from '@api/common';
import { RawNode } from '@api/common/data/raw';
import { GeometryDiff } from '@api/common/route';
import { MapLinkMenuComponent } from '@app/ol/components';
import { LayerSwitcherComponent } from '@app/ol/components';
import { MAP_SERVICE_TOKEN } from '@app/ol/services';
import { RouteChangeMapService } from './route-change-map.service';

@Component({
  selector: 'kpn-route-change-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div [id]="service.mapId" class="kpn-embedded-map">
      <kpn-layer-switcher />
      <kpn-map-link-menu />
    </div>
  `,
  providers: [
    RouteChangeMapService,
    {
      provide: MAP_SERVICE_TOKEN,
      useExisting: RouteChangeMapService,
    },
  ],
  standalone: true,
  imports: [LayerSwitcherComponent, MapLinkMenuComponent],
})
export class RouteChangeMapComponent implements AfterViewInit, OnDestroy {
  @Input() geometryDiff: GeometryDiff;
  @Input() nodes: RawNode[];
  @Input() bounds: Bounds;

  protected readonly service = inject(RouteChangeMapService);

  ngAfterViewInit(): void {
    setTimeout(() => this.service.init(this.geometryDiff, this.nodes, this.bounds), 1);
  }

  ngOnDestroy(): void {
    this.service.destroy();
  }
}
