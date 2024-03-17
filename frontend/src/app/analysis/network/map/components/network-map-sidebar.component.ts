import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { ZoomLevel } from '@app/ol/domain';
import { MapZoomService } from '@app/ol/services';
import { delay } from 'rxjs/operators';
import { NetworkMapLegendIconComponent } from './network-map-legend-icon.component';

@Component({
  selector: 'kpn-network-map-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      @if (zoomLevel$ | async; as zoomLevel) {
        <div class="kpn-tip">
          @if (zoomLevel < minZoom) {
            <p i18n="@@network-map.side-bar.tip-zoom-in">Zoom in for node or route details.</p>
          }
          @if (zoomLevel >= minZoom) {
            <div class="legend">
              <div>
                <kpn-network-map-legend-icon color="rgb(0,200,0)" />
                <span i18n="@@network-map.side-bar.part-of-network">Part of network</span>
              </div>
              <div>
                <kpn-network-map-legend-icon color="rgb(150,150,150)" />
                <span i18n="@@network-map.side-bar.not-part-of-network">Not part of network</span>
              </div>
            </div>
            <p i18n="@@network-map.side-bar.tip1">Click on node or route to go to detail page.</p>
            <p i18n="@@network-map.side-bar.tip2">
              Use ctrl-click to open the detail page in another browser tab.
            </p>
          }
        </div>
      }
    </kpn-sidebar>
  `,
  styles: `
    .legend {
      margin-top: 1rem;
    }

    .legend > div {
      display: flex;
      align-items: center;
    }
  `,
  standalone: true,
  imports: [SidebarComponent, NetworkMapLegendIconComponent, AsyncPipe],
})
export class NetworkMapSidebarComponent {
  private readonly mapZoomService = inject(MapZoomService);
  protected readonly zoomLevel$ = this.mapZoomService.zoomLevel$.pipe(delay(0));
  protected readonly minZoom = ZoomLevel.vectorTileMinZoom;
}
