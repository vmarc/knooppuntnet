import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';
import {MatRadioChange} from '@angular/material/radio/radio';
import {select} from '@ngrx/store';
import {Store} from '@ngrx/store';
import {first} from 'rxjs/operators';
import {map} from 'rxjs/operators';
import {AppState} from '../../../core/core.state';
import {actionMonitorRouteMapFocus} from '../../store/monitor.actions';
import {actionMonitorRouteMapMode} from '../../store/monitor.actions';
import {selectMonitorRouteMapBounds} from '../../store/monitor.selectors';
import {selectMonitorRouteMapMode} from '../../store/monitor.selectors';
import {selectMonitorRouteMapOsmSegmentCount} from '../../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-route-map-control',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="mode$ | async as mode" class="control">

      <div *ngIf="modeSelectionEnabled$ | async">
        <mat-radio-group [value]="mode" (change)="modeChanged($event)">
          <mat-radio-button value="comparison">
            <span>GPX / OSM comparison</span>
          </mat-radio-button>
          <mat-radio-button value="osm-segments">
            <span>OSM segments</span>
            <span class="kpn-brackets">{{osmSegmentCount$ | async}}</span>
          </mat-radio-button>
        </mat-radio-group>
      </div>

      <kpn-monitor-route-map-layers></kpn-monitor-route-map-layers>

      <div class="kpn-spacer-above">
        <button mat-raised-button (click)="zoomToFitRoute()">Zoom to fit route</button>
      </div>

      <kpn-monitor-route-map-nok-segments *ngIf="mode === 'comparison'">
      </kpn-monitor-route-map-nok-segments>

      <kpn-monitor-route-map-osm-segments *ngIf="mode === 'osm-segments'">
      </kpn-monitor-route-map-osm-segments>

    </div>
  `,
  styles: [`

    .control {
      padding: 1em;
    }

    mat-radio-button {
      display: block;
      padding-bottom: 10px;
    }

  `]
})
export class MonitorRouteMapControlComponent {

  mode$ = this.store.select(selectMonitorRouteMapMode);
  osmSegmentCount$ = this.store.select(selectMonitorRouteMapOsmSegmentCount);

  modeSelectionEnabled$ = this.store.pipe(
    select(selectMonitorRouteMapOsmSegmentCount),
    map(osmSegmentCount => osmSegmentCount > 1)
  );

  constructor(private store: Store<AppState>) {
  }

  modeChanged(event: MatRadioChange): void {
    this.store.dispatch(actionMonitorRouteMapMode({mode: event.value}));
  }

  zoomToFitRoute(): void {
    this.store.select(selectMonitorRouteMapBounds).pipe(first()).subscribe(bounds => {
      this.store.dispatch(actionMonitorRouteMapFocus({bounds}));
    });
  }
}
