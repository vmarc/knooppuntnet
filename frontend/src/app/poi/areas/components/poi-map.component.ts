import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { Component } from '@angular/core';
import { LayerSwitcherComponent } from '@app/ol/components';
import { MAP_SERVICE_TOKEN } from '@app/ol/services';
import { PoiAreasPageService } from '../poi-areas-page.service';
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
  standalone: true,
  imports: [LayerSwitcherComponent],
})
export class PoiMapComponent implements AfterViewInit, OnDestroy {
  protected readonly service = inject(PoiMapService);
  private readonly pageService = inject(PoiAreasPageService);

  ngAfterViewInit(): void {
    this.pageService.afterViewInit();
  }

  ngOnDestroy(): void {
    this.service.destroy();
  }
}
