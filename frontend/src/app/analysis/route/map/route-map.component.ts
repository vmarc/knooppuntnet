import { OnDestroy } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { AfterViewInit, Component } from '@angular/core';
import { MapLinkMenuComponent } from '@app/components/ol/components';
import { LayerSwitcherComponent } from '@app/components/ol/components';
import { MAP_SERVICE_TOKEN } from '@app/components/ol/services';
import { Store } from '@ngrx/store';
import { actionRouteMapViewInit } from '../store/route.actions';
import { RouteMapService } from './route-map.service';

@Component({
  selector: 'kpn-route-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div [id]="service.mapId" class="kpn-map">
      <kpn-layer-switcher />
      <kpn-map-link-menu />
    </div>
  `,
  providers: [
    {
      provide: MAP_SERVICE_TOKEN,
      useExisting: RouteMapService,
    },
  ],
  standalone: true,
  imports: [LayerSwitcherComponent, MapLinkMenuComponent],
})
export class RouteMapComponent implements AfterViewInit, OnDestroy {
  constructor(protected service: RouteMapService, private store: Store) {}

  ngAfterViewInit(): void {
    this.store.dispatch(actionRouteMapViewInit());
  }

  ngOnDestroy(): void {
    this.service.destroy();
  }
}
