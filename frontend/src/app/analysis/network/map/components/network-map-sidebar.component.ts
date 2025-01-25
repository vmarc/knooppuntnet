import { inject } from '@angular/core';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { ZoomLevel } from '@app/ol/domain';
import { MapZoomService } from '@app/ol/services';
import { DeviceDetectorService } from 'ngx-device-detector';
import { NetworkMapLegendIconComponent } from './network-map-legend-icon.component';

@Component({
  selector: 'kpn-network-map-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!--    @if (zoomLevel(); as zoomLevel) {-->
    <div class="kpn-tip">
      <!--      @if (zoomLevel < minZoom) {-->
      <p i18n="@@network-map.side-bar.tip-zoom-in">Zoom in for node or route details.</p>
      <!--      }-->
      <!--      @if (zoomLevel >= minZoom) {-->
      <div class="legend">
        <div>
          <kpn-network-map-legend-icon color="rgb(0,200,0)" />
          <span i18n="@@network-map.side-bar.part-of-network">Part of network</span>
        </div>
        <div>
          <kpn-network-map-legend-icon color="rgb(150,150,150)" />
          <span i18n="@@network-map.side-bar.not-part-of-network">Not part of network</span>
        </div>
        <div>
          <kpn-network-map-legend-icon color="rgb(255,150,0)" />
          <span i18n="@@network-map.side-bar.connection">Connection</span>
        </div>
      </div>
      <p i18n="@@network-map.side-bar.tip1">Click on node or route to go to detail page.</p>
      @if (isMac()) {
        <p i18n="@@network-map.side-bar.tip2.mac">
          Use cmd-click to open the detail page in another browser tab.
        </p>
      } @else {
        <p i18n="@@network-map.side-bar.tip2">
          Use ctrl-click to open the detail page in another browser tab.
        </p>
      }
      <!--      }-->
    </div>
    <!--    }-->
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
  imports: [NetworkMapLegendIconComponent],
})
export class NetworkMapSidebarComponent {
  private readonly mapZoomService = inject(MapZoomService);
  private readonly deviceService = inject(DeviceDetectorService);
  protected readonly zoomLevel = this.mapZoomService.zoomLevel;
  protected readonly minZoom = ZoomLevel.vectorTileMinZoom;

  isMac(): boolean {
    return this.deviceService.os === 'Mac';
  }
}
