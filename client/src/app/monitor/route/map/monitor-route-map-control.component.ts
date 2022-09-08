import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatRadioChange } from '@angular/material/radio';
import { Store } from '@ngrx/store';
import { first } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { AppState } from '../../../core/core.state';
import { actionMonitorRouteMapJosmLoadRouteRelation } from '../../store/monitor.actions';
import { actionMonitorRouteMapJosmZoomToSelectedDeviation } from '../../store/monitor.actions';
import { actionMonitorRouteMapJosmZoomToFitRoute } from '../../store/monitor.actions';
import { actionMonitorRouteMapFocus } from '../../store/monitor.actions';
import { actionMonitorRouteMapMode } from '../../store/monitor.actions';
import { selectMonitorRouteMapSelectedDeviation } from '../../store/monitor.selectors';
import { selectMonitorRouteMapBounds } from '../../store/monitor.selectors';
import { selectMonitorRouteMapMode } from '../../store/monitor.selectors';
import { selectMonitorRouteMapOsmSegmentCount } from '../../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-route-map-control',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="mode$ | async as mode" class="control">
      <div *ngIf="modeSelectionEnabled$ | async">
        <mat-radio-group [value]="mode" (change)="modeChanged($event)">
          <mat-radio-button value="comparison">
            <span i18n="@@monitor.route.map.mode.comparison"
              >GPX / OSM comparison</span
            >
          </mat-radio-button>
          <mat-radio-button value="osm-segments">
            <span i18n="@@monitor.route.map.mode.osm-segments"
              >OSM segments</span
            >
            <span class="kpn-brackets">{{ osmSegmentCount$ | async }}</span>
          </mat-radio-button>
        </mat-radio-group>
      </div>

      <kpn-monitor-route-map-layers></kpn-monitor-route-map-layers>

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
        </mat-menu>
      </div>

      <kpn-monitor-route-map-deviations *ngIf="mode === 'comparison'">
      </kpn-monitor-route-map-deviations>

      <kpn-monitor-route-map-osm-segments *ngIf="mode === 'osm-segments'">
      </kpn-monitor-route-map-osm-segments>
    </div>
  `,
  styles: [
    `
      .control {
        padding: 1em;
      }

      mat-radio-button {
        display: block;
        padding-bottom: 10px;
      }

      .josm {
        padding-left: 0.8em;
      }
    `,
  ],
})
export class MonitorRouteMapControlComponent {
  readonly mode$ = this.store.select(selectMonitorRouteMapMode);
  readonly osmSegmentCount$ = this.store.select(
    selectMonitorRouteMapOsmSegmentCount
  );

  readonly modeSelectionEnabled$ = this.store
    .select(selectMonitorRouteMapOsmSegmentCount)
    .pipe(map((osmSegmentCount) => osmSegmentCount > 1));

  readonly josmZoomToSelectedDeviationDisabled$ = this.store
    .select(selectMonitorRouteMapSelectedDeviation)
    .pipe(map((segment) => !segment));

  constructor(private store: Store<AppState>) {}

  modeChanged(event: MatRadioChange): void {
    this.store.dispatch(actionMonitorRouteMapMode({ mode: event.value }));
  }

  zoomToFitRoute(): void {
    this.store
      .select(selectMonitorRouteMapBounds)
      .pipe(first())
      .subscribe((bounds) => {
        this.store.dispatch(actionMonitorRouteMapFocus(bounds));
      });
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
}
