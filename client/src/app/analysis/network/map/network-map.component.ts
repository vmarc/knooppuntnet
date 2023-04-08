import { ChangeDetectionStrategy } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { AfterViewInit, Component, Input } from '@angular/core';
import { NetworkMapPage } from '@api/common/network/network-map-page';
import { Util } from '@app/components/shared/util';
import { NetworkMapPosition } from '@app/components/ol/domain/network-map-position';
import { actionNetworkMapViewInit } from '@app/analysis/network/store/network.actions';
import { Store } from '@ngrx/store';
import { NetworkMapService } from '@app/analysis/network/map/network-map.service';
import { MAP_SERVICE_TOKEN } from '@app/components/ol/services/openlayers-map-service';

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
