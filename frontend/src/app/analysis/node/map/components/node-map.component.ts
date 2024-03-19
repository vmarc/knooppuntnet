import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { AfterViewInit, Component } from '@angular/core';
import { MapLinkMenuComponent } from '@app/ol/components';
import { LayerSwitcherComponent } from '@app/ol/components';
import { MAP_SERVICE_TOKEN } from '@app/ol/services';
import { NodeMapPageService } from '../node-map-page.service';
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
  private readonly pageService = inject(NodeMapPageService);
  protected readonly service = inject(NodeMapService);

  ngAfterViewInit(): void {
    this.pageService.onAfterViewInit();
  }

  ngOnDestroy(): void {
    this.service.destroy();
  }
}
