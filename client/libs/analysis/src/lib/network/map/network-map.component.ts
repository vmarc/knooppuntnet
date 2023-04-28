import { ChangeDetectionStrategy } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { AfterViewInit, Component, Input } from '@angular/core';
import { NetworkMapPage } from '@api/common/network';
import { MapLinkMenuComponent } from '@app/components/ol/components';
import { LayerSwitcherComponent } from '@app/components/ol/components';
import { NetworkMapPosition } from '@app/components/ol/domain';
import { MAP_SERVICE_TOKEN } from '@app/components/ol/services';
import { Util } from '@app/components/shared';
import { Store } from '@ngrx/store';
import { actionNetworkMapViewInit } from '../store/network.actions';
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
  imports: [
    NetworkControlComponent,
    LayerSwitcherComponent,
    MapLinkMenuComponent,
  ],
})
export class NetworkMapComponent implements AfterViewInit, OnDestroy {
  @Input() networkId: number;
  @Input() page: NetworkMapPage;
  @Input() mapPositionFromUrl: NetworkMapPosition;

  constructor(protected service: NetworkMapService, private store: Store) {}

  ngAfterViewInit(): void {
    this.store.dispatch(actionNetworkMapViewInit());
    // TODO   () => this.mapLayerService.restoreMapLayerStates(this.layers),
  }

  ngOnDestroy(): void {
    this.service.destroy();
  }

  zoomInToNetwork(): void {
    const extent = Util.toExtent(this.page.bounds, 0.1);
    this.service.map.getView().fit(extent);
  }
}
