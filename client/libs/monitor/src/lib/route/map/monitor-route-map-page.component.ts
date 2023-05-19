import { NgIf } from '@angular/common';
import { AfterViewInit } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MonitorRouteMapPage } from '@api/common/monitor';
import { NavService } from '@app/components/shared';
import { MonitorRoutePageHeaderComponent } from '../components/monitor-route-page-header.component';
import { MonitorRouteMapPageService } from './monitor-route-map-page.service';
import { MonitorRouteMapComponent } from './monitor-route-map.component';
import { MonitorRouteMapService } from './monitor-route-map.service';

@Component({
  selector: 'kpn-monitor-route-map-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-monitor-route-page-header
      pageName="map"
      [groupName]="service.groupName()"
      [routeName]="service.routeName()"
      [routeDescription]="service.routeDescription()"
    />
    <div *ngIf="mapService.page() !== null">
      <div *ngIf="canDisplayMap(mapService.page()); then map; else noMap"></div>
      <ng-template #noMap>
        <p i18n="@@monitor.route.map.no-map">No map</p>
      </ng-template>
      <ng-template #map>
        <kpn-monitor-route-map />
      </ng-template>
    </div>
  `,
  providers: [MonitorRouteMapPageService, NavService],
  standalone: true,
  imports: [MonitorRouteMapComponent, MonitorRoutePageHeaderComponent, NgIf],
})
export class MonitorRouteMapPageComponent implements OnInit, AfterViewInit {
  constructor(
    protected service: MonitorRouteMapPageService,
    protected mapService: MonitorRouteMapService
  ) {}

  ngOnInit(): void {
    console.log('MonitorRouteMapPageComponent.ngOnInit()');
    // console.log('MonitorRouteMapPageComponent.ngAfterContentChecked()');
    this.service.init();
  }

  ngAfterViewInit(): void {
    console.log('MonitorRouteMapPageComponent.ngAfterViewInit()');
  }

  canDisplayMap(page: MonitorRouteMapPage): boolean {
    return (
      !!page &&
      (page.bounds.minLat !== 0 ||
        page.bounds.minLon !== 0 ||
        page.bounds.maxLat !== 0 ||
        page.bounds.maxLon !== 0)
    );
  }
}
