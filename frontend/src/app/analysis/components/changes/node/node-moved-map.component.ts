import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { Component } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { input } from '@angular/core';
import { NodeMoved } from '@api/common/diff/node/node-moved';
import { MAP_SERVICE_TOKEN } from '@app/ol/services/openlayers-map-service';
import { NodeMovedMapService } from './node-moved-map.service';

@Component({
  selector: 'kpn-node-moved-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <div [id]="service.mapId" class="kpn-embedded-map"></div> `,
  providers: [
    NodeMovedMapService,
    {
      provide: MAP_SERVICE_TOKEN,
      useExisting: NodeMovedMapService,
    },
  ],
})
export class NodeMovedMapComponent implements AfterViewInit, OnDestroy {
  nodeMoved = input.required<NodeMoved>();

  protected readonly service = inject(NodeMovedMapService);

  ngAfterViewInit(): void {
    setTimeout(() => this.service.init(this.nodeMoved()), 1);
  }

  ngOnDestroy(): void {
    this.service.destroy();
  }
}
