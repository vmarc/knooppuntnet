import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { actionMonitorRouteMapZoomToFitRoute } from './store/monitor-route-map.actions';
import { actionMonitorRouteMapJosmZoomToSelectedOsmSegment } from './store/monitor-route-map.actions';
import { actionMonitorRouteMapJosmLoadRouteRelation } from './store/monitor-route-map.actions';
import { actionMonitorRouteMapJosmZoomToSelectedDeviation } from './store/monitor-route-map.actions';
import { actionMonitorRouteMapJosmZoomToFitRoute } from './store/monitor-route-map.actions';
import { selectMonitorRouteMapSelectedOsmSegmentDisabled } from './store/monitor-route-map.selectors';
import { selectMonitorRouteMapSelectedDeviationDisabled } from './store/monitor-route-map.selectors';

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
          [disabled]="josmZoomToSelectedDeviationDisabled$ | async"
          i18n="@@monitor.route.map.action.josm.zoom-to-deviation"
        >
          Zoom to selected deviation
        </button>
        <button
          mat-menu-item
          (click)="josmZoomToSelectedOsmSegment()"
          [disabled]="josmZoomToSelectedOsmSegmentDisabled$ | async"
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
})
export class MonitorRouteMapControlJosmComponent {
  readonly josmZoomToSelectedDeviationDisabled$ = this.store.select(
    selectMonitorRouteMapSelectedDeviationDisabled
  );

  readonly josmZoomToSelectedOsmSegmentDisabled$ = this.store.select(
    selectMonitorRouteMapSelectedOsmSegmentDisabled
  );

  constructor(private store: Store) {}

  zoomToFitRoute(): void {
    this.store.dispatch(actionMonitorRouteMapZoomToFitRoute());
  }

  josmLoadRouteRelation(): void {
    this.store.dispatch(actionMonitorRouteMapJosmLoadRouteRelation());
  }

  josmZoomToFitRoute(): void {
    this.store.dispatch(actionMonitorRouteMapJosmZoomToFitRoute());
  }

  josmZoomToSelectedDeviation(): void {
    this.store.dispatch(actionMonitorRouteMapJosmZoomToSelectedDeviation());
  }

  josmZoomToSelectedOsmSegment(): void {
    this.store.dispatch(actionMonitorRouteMapJosmZoomToSelectedOsmSegment());
  }
}
