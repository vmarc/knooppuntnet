import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { AfterViewInit, Component, Input } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { NodeMoved } from '@api/common/diff/node';
import { MapLinkMenuComponent } from '@app/ol/components';
import { LayerSwitcherComponent } from '@app/ol/components';
import { MAP_SERVICE_TOKEN } from '@app/ol/services';
import { NodeMovedMapService } from './node-moved-map.service';

@Component({
  selector: 'kpn-node-moved-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div [id]="service.mapId" class="kpn-embedded-map">
      <kpn-layer-switcher />
      <kpn-map-link-menu />
    </div>
  `,
  providers: [
    NodeMovedMapService,
    {
      provide: MAP_SERVICE_TOKEN,
      useExisting: NodeMovedMapService,
    },
  ],
  standalone: true,
  imports: [LayerSwitcherComponent, MapLinkMenuComponent],
})
export class NodeMovedMapComponent implements AfterViewInit, OnDestroy {
  @Input() nodeMoved: NodeMoved;

  protected readonly service = inject(NodeMovedMapService);

  ngAfterViewInit(): void {
    setTimeout(() => this.service.init(this.nodeMoved), 1);
  }

  ngOnDestroy(): void {
    this.service.destroy();
  }
}
