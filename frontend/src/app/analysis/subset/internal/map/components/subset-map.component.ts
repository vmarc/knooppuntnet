import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { Component } from '@angular/core';
import { MAP_SERVICE_TOKEN } from '@app/ol/services';
import { SubsetMapService } from '../subset-map.service';
import { SubsetMapPageService } from '../subset-map-page.service';

@Component({
  selector: 'kpn-subset-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <div [id]="service.mapId" class="kpn-map"></div> `,
  providers: [
    {
      provide: MAP_SERVICE_TOKEN,
      useExisting: SubsetMapService,
    },
  ],
})
export class SubsetMapComponent implements AfterViewInit {
  protected readonly service = inject(SubsetMapService);
  private readonly mapPageService = inject(SubsetMapPageService);

  ngAfterViewInit(): void {
    this.mapPageService.afterViewInit();
  }
}
