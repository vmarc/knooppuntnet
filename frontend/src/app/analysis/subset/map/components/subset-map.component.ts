import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { Component } from '@angular/core';
import { MapLinkMenuComponent } from '@app/ol/components';
import { LayerSwitcherComponent } from '@app/ol/components';
import { MAP_SERVICE_TOKEN } from '@app/ol/services';
import { SubsetMapService } from '../subset-map.service';
import { SubsetMapPageService } from '../subset-map-page.service';

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
  standalone: true,
  imports: [LayerSwitcherComponent, MapLinkMenuComponent],
})
export class SubsetMapComponent implements AfterViewInit {
  protected readonly service = inject(SubsetMapService);
  private readonly mapPageService = inject(SubsetMapPageService);

  ngAfterViewInit(): void {
    this.mapPageService.afterViewInit();
  }
}
