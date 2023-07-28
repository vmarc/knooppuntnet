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
  private readonly stateService = inject(MonitorRouteMapStateService);
  private readonly editService = inject(EditService);

  readonly josmZoomToSelectedDeviationDisabled = computed(
    () =>
      this.stateService.mode() !== MonitorMapMode.comparison ||
      !this.stateService.selectedDeviation()
  );

  readonly josmZoomToSelectedOsmSegmentDisabled = computed(
    () =>
      this.stateService.mode() !== MonitorMapMode.osmSegments ||
      !this.stateService.selectedOsmSegment()
  );

  zoomToFitRoute(): void {
    this.stateService.zoomToFitRoute();
  }

  josmLoadRouteRelation(): void {
    const relationIds = [this.stateService.page().relationId];
    this.editService.edit({
      relationIds,
      fullRelation: true,
    });
  }

  josmZoomToFitRoute(): void {
    const bounds = this.stateService.page().bounds;
    this.editService.edit({ bounds });
  }

  josmZoomToSelectedDeviation(): void {
    const bounds = this.stateService.selectedDeviation().bounds;
    this.editService.edit({ bounds });
  }

  josmZoomToSelectedOsmSegment(): void {
    const bounds = this.stateService.selectedOsmSegment().bounds;
    this.editService.edit({ bounds });
  }
}
