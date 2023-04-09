import { AfterViewInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { MAP_SERVICE_TOKEN } from '@app/components/ol/services/openlayers-map-service';
import { FrisoMapService } from '@app/friso/friso/friso-map.service';
import { actionFrisoMapViewInit } from '@app/friso/store/friso.actions';
import { Store } from '@ngrx/store';

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
