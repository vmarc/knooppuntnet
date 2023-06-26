import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { EditService } from '@app/components/shared';
import { MonitorMapMode } from './monitor-map-mode';
import { MonitorRouteMapStateService } from './monitor-route-map-state.service';

@Component({
  selector: 'kpn-monitor-route-map-control-josm',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-spacer-above">
      <button
        mat-raised-button
        (click)="zoomToFitRoute()"
        i18n="@@monitor.route.map.action.zoom-to-fit-route"
      >
        Zoom to fit route
      </button>
      <a
        [matMenuTriggerFor]="menu"
        class="josm"
        title="JOSM remote control actions"
        i18n-title="@@monitor.route.map.action.josm.title"
        i18n="@@monitor.route.map.action.josm"
      >
        josm
      </a>
      <mat-menu #menu="matMenu">
        <button
          mat-menu-item
          (click)="josmLoadRouteRelation()"
          i18n="@@monitor.route.map.action.josm.load-relation"
        >
          Load route relation
        </button>
        <button
          mat-menu-item
          (click)="josmZoomToFitRoute()"
          i18n="@@monitor.route.map.action.josm.zoom-to-fit-route"
        >
          Zoom to fit route
        </button>
        <button
          mat-menu-item
          (click)="josmZoomToSelectedDeviation()"
          [disabled]="josmZoomToSelectedDeviationDisabled()"
          i18n="@@monitor.route.map.action.josm.zoom-to-deviation"
        >
          Zoom to selected deviation
        </button>
        <button
          mat-menu-item
          (click)="josmZoomToSelectedOsmSegment()"
          [disabled]="josmZoomToSelectedOsmSegmentDisabled()"
          i18n="@@monitor.route.map.action.josm.zoom-to-osm-segment"
        >
          Zoom to selected OSM segment
        </button>
      </mat-menu>
    </div>
  `,
  styles: [
    `
      .josm {
        padding-left: 0.8em;
      }
    `,
  ],
  standalone: true,
  imports: [MatButtonModule, MatMenuModule, AsyncPipe],
})
export class MonitorRouteMapControlJosmComponent {
  readonly #service = inject(MonitorRouteMapStateService);
  readonly #editService = inject(EditService);

  readonly josmZoomToSelectedDeviationDisabled = computed(
    () =>
      this.#service.mode() !== MonitorMapMode.comparison ||
      !this.#service.selectedDeviation()
  );

  readonly josmZoomToSelectedOsmSegmentDisabled = computed(
    () =>
      this.#service.mode() !== MonitorMapMode.osmSegments ||
      !this.#service.selectedOsmSegment()
  );

  zoomToFitRoute(): void {
    this.#service.focusChanged(this.#service.page().bounds);
  }

  josmLoadRouteRelation(): void {
    const relationIds = [this.#service.page().relationId];
    this.#editService.edit({
      relationIds,
      fullRelation: true,
    });
  }

  josmZoomToFitRoute(): void {
    const bounds = this.#service.page().bounds;
    this.#editService.edit({ bounds });
  }

  josmZoomToSelectedDeviation(): void {
    const bounds = this.#service.selectedDeviation().bounds;
    this.#editService.edit({ bounds });
  }

  josmZoomToSelectedOsmSegment(): void {
    const bounds = this.#service.selectedOsmSegment().bounds;
    this.#editService.edit({ bounds });
  }
}
