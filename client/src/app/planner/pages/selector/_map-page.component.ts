import { ChangeDetectionStrategy } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { MapService } from '@app/components/ol/services';

@Component({
  selector: 'kpn-map-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li i18n="@@breadcrumb.map">Map</li>
    </ul>

    <kpn-page-header subject="planner" i18n="@@planner.map">
      Map
    </kpn-page-header>
    <kpn-icon-buttons>
      <kpn-icon-button
        routerLink="/map/cycling"
        icon="cycling"
        i18n-title="@@network-type.cycling"
        title="Cycling"
      />
      <kpn-icon-button
        routerLink="/map/hiking"
        icon="hiking"
        i18n-title="@@network-type.hiking"
        title="Hiking"
      />
      <kpn-icon-button
        routerLink="/map/horse-riding"
        icon="horse-riding"
        i18n-title="@@network-type.horse-riding"
        title="Horse riding"
      />
      <kpn-icon-button
        routerLink="/map/motorboat"
        icon="motorboat"
        i18n-title="@@network-type.motorboat"
        title="Motorboat"
      />
      <kpn-icon-button
        routerLink="/map/canoe"
        icon="canoe"
        i18n-title="@@network-type.canoe"
        title="Canoe"
      />
      <kpn-icon-button
        routerLink="/map/inline-skating"
        icon="inline-skating"
        i18n-title="@@network-type.inline-skating"
        title="Inline skating"
      />
    </kpn-icon-buttons>
  `,
})
export class MapPageComponent implements OnInit {
  constructor(private mapService: MapService) {}

  ngOnInit(): void {
    this.mapService.nextNetworkType(null);
  }
}
