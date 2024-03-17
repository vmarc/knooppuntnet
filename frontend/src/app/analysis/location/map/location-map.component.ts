import { inject } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MapLinkMenuComponent } from '@app/ol/components';
import { LayerSwitcherComponent } from '@app/ol/components';
import { MAP_SERVICE_TOKEN } from '@app/ol/services';
import { LocationMapService } from './location-map.service';
import { LocationMapStore } from './location-map.store';

@Component({
  selector: 'kpn-location-map',
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
      useExisting: LocationMapService,
    },
  ],
  standalone: true,
  imports: [LayerSwitcherComponent, MapLinkMenuComponent],
})
export class LocationMapComponent implements AfterViewInit {
  private readonly store = inject(LocationMapStore);
  protected readonly service = inject(LocationMapService);

  ngAfterViewInit(): void {
    this.store.afterViewInit();
  }
}
