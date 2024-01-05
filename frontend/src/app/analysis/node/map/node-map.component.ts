import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { AfterViewInit, Component } from '@angular/core';
import { MapLinkMenuComponent } from '@app/ol/components';
import { LayerSwitcherComponent } from '@app/ol/components';
import { MAP_SERVICE_TOKEN } from '@app/ol/services';
import { Store } from '@ngrx/store';
import { actionNodeMapViewInit } from '../store/node.actions';
import { NodeMapService } from './node-map.service';

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
  standalone: true,
  imports: [LayerSwitcherComponent, MapLinkMenuComponent],
})
export class NodeMapComponent implements AfterViewInit, OnDestroy {
  private readonly store = inject(Store);
  protected readonly service = inject(NodeMapService);

  ngAfterViewInit(): void {
    this.store.dispatch(actionNodeMapViewInit());
  }

  ngOnDestroy(): void {
    this.service.destroy();
  }
}
