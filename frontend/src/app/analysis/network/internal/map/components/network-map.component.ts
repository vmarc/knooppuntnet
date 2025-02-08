import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { AfterViewInit, Component } from '@angular/core';
import { input } from '@angular/core';
import { NetworkMapPage } from '@api/common/network/network-map-page';
import { MAP_SERVICE_TOKEN } from '@app/ol/services';
import { Util } from '@app/shared/components/util';
import { NetworkMapPageService } from '../network-map-page.service';
import { NetworkControlComponent } from './network-control.component';
import { NetworkMapService } from './network-map.service';

@Component({
  selector: 'kpn-network-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div [id]="networkMapService.mapId" class="kpn-map">
      <kpn-network-control (action)="zoomInToNetwork()" />
    </div>
  `,
  providers: [
    {
      provide: MAP_SERVICE_TOKEN,
      useExisting: NetworkMapService,
    },
  ],
  imports: [NetworkControlComponent],
})
export class NetworkMapComponent implements AfterViewInit {
  networkId = input.required<number>();
  page = input.required<NetworkMapPage>();

  protected readonly networkMapService = inject(NetworkMapService);
  private readonly networkMapPageService = inject(NetworkMapPageService);

  ngAfterViewInit(): void {
    this.networkMapPageService.afterViewInit();
  }

  zoomInToNetwork(): void {
    const extent = Util.toExtent(this.page().bounds, 0.1);
    this.networkMapService.map.getView().fit(extent);
  }
}
