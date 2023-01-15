import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { Observable } from 'rxjs';
import { delay } from 'rxjs/operators';
import { ZoomLevel } from '../../../components/ol/domain/zoom-level';
import { MapZoomService } from '../../../components/ol/services/map-zoom.service';

@Component({
  selector: 'kpn-network-map-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <div *ngIf="zoomLevel$ | async as zoomLevel" class="kpn-tip">
        <p
          *ngIf="zoomLevel < minZoom()"
          i18n="@@network-map.side-bar.tip-zoom-in"
        >
          Zoom in for node or route details.
        </p>
        <div *ngIf="zoomLevel >= minZoom()">
          <div class="legend">
            <div>
              <kpn-network-map-legend-icon color="rgb(0,200,0)" />
              <span i18n="@@network-map.side-bar.part-of-network"
              >Part of network</span
              >
            </div>
            <div>
              <kpn-network-map-legend-icon color="rgb(150,150,150)" />
              <span i18n="@@network-map.side-bar.not-part-of-network"
              >Not part of network</span
              >
            </div>
          </div>
          <p i18n="@@network-map.side-bar.tip1">
            Click on node or route to go to detail page.
          </p>
          <p i18n="@@network-map.side-bar.tip2">
            Use ctrl-click to open the detail page in another browser tab.
          </p>
        </div>
      </div>
    </kpn-sidebar>
  `,
  styles: [
    `
      .legend {
        margin-top: 1rem;
      }

      .legend > div {
        display: flex;
        align-items: center;
      }
    `,
  ],
})
export class NetworkMapSidebarComponent implements OnInit {
  zoomLevel$: Observable<number>;

  constructor(private mapZoomService: MapZoomService) {}

  ngOnInit(): void {
    this.zoomLevel$ = this.mapZoomService.zoomLevel$.pipe(delay(0));
  }

  minZoom(): number {
    return ZoomLevel.vectorTileMinZoom;
  }
}
