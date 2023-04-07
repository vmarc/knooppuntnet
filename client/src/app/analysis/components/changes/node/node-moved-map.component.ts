import { ChangeDetectionStrategy } from '@angular/core';
import { AfterViewInit, Component, Input } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { NodeMoved } from '@api/common/diff/node/node-moved';
import { NodeMovedMapService } from '@app/analysis/components/changes/node/node-moved-map.service';

@Component({
  selector: 'kpn-node-moved-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div [id]="service.mapId" class="kpn-embedded-map">
      <kpn-layer-switcher
        [layerStates]="service.layerStates$ | async"
        (layerStateChange)="service.layerStateChange($event)"
      />
      <kpn-map-link-menu [map]="service.map"></kpn-map-link-menu>
    </div>
  `,
  providers: [NodeMovedMapService],
})
export class NodeMovedMapComponent implements AfterViewInit, OnDestroy {
  @Input() nodeMoved: NodeMoved;

  constructor(protected service: NodeMovedMapService) {}

  ngAfterViewInit(): void {
    setTimeout(() => this.service.init(this.nodeMoved), 1);
  }

  ngOnDestroy(): void {
    this.service.destroy();
  }
}
