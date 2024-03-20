import { inject } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { AfterViewInit, Component } from '@angular/core';
import { MapLinkMenuComponent } from '@app/ol/components';
import { LayerSwitcherComponent } from '@app/ol/components';
import { MAP_SERVICE_TOKEN } from '@app/ol/services';
import { RouteMapPageService } from '../route-map-page.service';
import { RouteMapService } from './route-map.service';

@Component({
  selector: 'kpn-route-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div [id]="routeMapService.mapId" class="kpn-map">
      <kpn-layer-switcher />
      <kpn-map-link-menu />
    </div>
  `,
  providers: [
    {
      provide: MAP_SERVICE_TOKEN,
      useExisting: RouteMapService,
    },
  ],
  standalone: true,
  imports: [LayerSwitcherComponent, MapLinkMenuComponent],
})
export class RouteMapComponent implements AfterViewInit, OnDestroy {
  protected readonly routeMapService = inject(RouteMapService);
  private readonly routeMapPageService = inject(RouteMapPageService);

  ngAfterViewInit(): void {
    this.routeMapPageService.onAfterViewInit();
  }

  ngOnDestroy(): void {
    this.routeMapService.destroy();
  }
}
