import { ChangeDetectionStrategy } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { Component } from '@angular/core';
import { MAP_SERVICE_TOKEN } from '@app/components/ol/services';
import { Store } from '@ngrx/store';
import { actionSubsetMapViewInit } from '../store/subset.actions';
import { SubsetMapService } from './subset-map.service';

@Component({
  selector: 'kpn-subset-map',
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
      useExisting: SubsetMapService,
    },
  ],
})
export class SubsetMapComponent implements AfterViewInit, OnDestroy {
  constructor(protected service: SubsetMapService, private store: Store) {}

  ngAfterViewInit(): void {
    this.store.dispatch(actionSubsetMapViewInit());
  }

  ngOnDestroy(): void {
    this.service.destroy();
  }
}
