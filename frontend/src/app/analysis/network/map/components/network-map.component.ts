import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { AfterViewInit, Component } from '@angular/core';
import { input } from '@angular/core';
import { NetworkMapPage } from '@api/common/network';
import { Util } from '@app/components/shared';
import { MapLinkMenuComponent } from '@app/ol/components';
import { LayerSwitcherComponent } from '@app/ol/components';
import { NetworkMapPosition } from '@app/ol/domain';
import { MAP_SERVICE_TOKEN } from '@app/ol/services';
import { NetworkMapStore } from '../network-map.store';
import { NetworkControlComponent } from './network-control.component';
import { NetworkMapService } from './network-map.service';

@Component({
  selector: 'kpn-network-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div [id]="service.mapId" class="kpn-map">
      <kpn-network-control (action)="zoomInToNetwork()" />
      <kpn-layer-switcher />
      <kpn-map-link-menu />
    </div>
  `,
  providers: [
    {
      provide: MAP_SERVICE_TOKEN,
      useExisting: NetworkMapService,
    },
  ],
  standalone: true,
  imports: [LayerSwitcherComponent, MapLinkMenuComponent, NetworkControlComponent],
})
export class NetworkMapComponent implements AfterViewInit {
  networkId = input.required<number>();
  page = input.required<NetworkMapPage>();
  mapPositionFromUrl = input.required<NetworkMapPosition>();

  protected readonly service = inject(NetworkMapService);
  private readonly store = inject(NetworkMapStore);

  ngAfterViewInit(): void {
    this.store.afterViewInit();
  }

  zoomInToNetwork(): void {
    const extent = Util.toExtent(this.page().bounds, 0.1);
    this.service.map.getView().fit(extent);
  }
}
