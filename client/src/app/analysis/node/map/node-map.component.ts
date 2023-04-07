import { ChangeDetectionStrategy } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { AfterViewInit, Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { NodeMapService } from '@app/analysis/node/map/node-map.service';
import { MAP_SERVICE_TOKEN } from '@app/components/ol/services/openlayers-map-service';
import { actionNodeMapViewInit } from '@app/analysis/node/store/node.actions';

@Component({
  selector: 'kpn-node-map',
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
      useExisting: NodeMapService,
    },
  ],
})
export class NodeMapComponent implements AfterViewInit, OnDestroy {
  constructor(private store: Store, protected service: NodeMapService) {}

  ngAfterViewInit(): void {
    this.store.dispatch(actionNodeMapViewInit());
  }

  ngOnDestroy(): void {
    this.service.destroy();
  }
}
