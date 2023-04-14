import { ChangeDetectionStrategy } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { AfterViewInit, Component } from '@angular/core';
import { MAP_SERVICE_TOKEN } from '@app/components/ol/services';
import { Store } from '@ngrx/store';
import { actionPoiAreasPageMapViewInit } from '../store/poi.actions';
import { PoiMapService } from './poi-map.service';

@Component({
  selector: 'kpn-poi-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div [id]="service.mapId" class="kpn-map">
      <kpn-layer-switcher />
    </div>
  `,
  providers: [
    {
      provide: MAP_SERVICE_TOKEN,
      useExisting: PoiMapService,
    },
  ],
})
export class PoiMapComponent implements AfterViewInit, OnDestroy {
  constructor(protected service: PoiMapService, private store: Store) {}

  ngAfterViewInit(): void {
    this.store.dispatch(actionPoiAreasPageMapViewInit());
  }

  ngOnDestroy(): void {
    this.service.destroy();
  }
}
