import { AfterViewInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { MapLinkMenuComponent } from '@app/ol/components';
import { LayerSwitcherComponent } from '@app/ol/components';
import { MAP_SERVICE_TOKEN } from '@app/ol/services';
import { Store } from '@ngrx/store';
import { actionFrisoMapViewInit } from '../store/friso.actions';
import { FrisoMapService } from './friso-map.service';

@Component({
  selector: 'kpn-friso-map',
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
      useExisting: FrisoMapService,
    },
  ],
  standalone: true,
  imports: [LayerSwitcherComponent, MapLinkMenuComponent],
})
export class FrisoMapComponent implements AfterViewInit, OnDestroy {
  constructor(protected service: FrisoMapService, private store: Store) {}

  ngAfterViewInit(): void {
    this.store.dispatch(actionFrisoMapViewInit());
  }

  ngOnDestroy(): void {
    this.service.destroy();
  }
}
