import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Store } from '@ngrx/store';
import { AppState } from '../../../core/core.state';
import { actionMonitorRouteDetailsPageDestroy } from '../../store/monitor.actions';
import { actionMonitorRouteDetailsPageInit } from '../../store/monitor.actions';
import { selectMonitorRouteDetailsPage } from '../../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-route-details-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-monitor-route-page-header
      pageName="details"
    ></kpn-monitor-route-page-header>

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <div *ngIf="!response.result" i18n="@@monitor.route.details.not-found">
        Route not found
      </div>

      <div *ngIf="response.result as route">
        <kpn-data title="Summary">
          <p class="kpn-separated">
            <kpn-osm-link-relation
              [relationId]="route.relationId"
            ></kpn-osm-link-relation>
            <kpn-josm-relation
              [relationId]="route.relationId"
            ></kpn-josm-relation>
          </p>
        </kpn-data>

        <kpn-data title="OSM">
          <p i18n="@@monitor.route.details.ways">{{ route.wayCount }} ways</p>
          <p class="kpn-km">{{ route.osmDistance }}</p>
        </kpn-data>

        <kpn-data title="GPX">
          <p>
            {{ route.gpxFilename }}
          </p>
          <p class="kpn-km">{{ route.gpxDistance }}</p>
        </kpn-data>

        <kpn-data title="Analysis">
          <p *ngIf="route.happy" class="kpn-line">
            <span i18n="@@monitor.route.details.analysis.ok">All ok</span>
            <kpn-icon-happy></kpn-icon-happy>
          </p>
          <div *ngIf="!route.happy && route.gpxDistance === 0">
            <p i18n="@@monitor.route.details.analysis.no-deviations">
              No GPX, so no known deviations.
            </p>
          </div>
          <div *ngIf="!route.happy && route.osmSegmentCount === 1">
            <p i18n="@@monitor.route.details.analysis.trace">
              The OSM route looks ok: a GPX trace can be created from it.
            </p>
          </div>

          <div *ngIf="route.osmSegmentCount > 1" class="kpn-line warning-line">
            <div>
              <mat-icon svgIcon="warning" class="warning-icon"></mat-icon>
            </div>
            <span i18n="@@monitor.route.details.analysis.osm-segment-warning"
              >The OSM route relation contains
              {{ route.osmSegmentCount }} segments. It will not be possible to
              create a GPX trace from it.</span
            >
          </div>
          <div
            *ngIf="route.gpxNokSegmentCount > 0"
            class="kpn-line warning-line"
          >
            <div>
              <mat-icon svgIcon="warning" class="warning-icon"></mat-icon>
            </div>
            <span i18n="@@monitor.route.details.analysis.gpx-segment-warning"
              >There are {{ route.gpxNokSegmentCount }} segments in the GPX
              trace where the distance to the closest OSM way is more than 10
              meters.</span
            >
          </div>
        </kpn-data>
      </div>
    </div>
  `,
  styles: [
    `
      .warning-line {
        padding-bottom: 1em;
      }

      .warning-icon {
        width: 2em;
        height: 2em;
      }
    `,
  ],
})
export class MonitorRouteDetailsPageComponent implements OnInit, OnDestroy {
  readonly response$ = this.store.select(selectMonitorRouteDetailsPage);

  constructor(private snackBar: MatSnackBar, private store: Store<AppState>) {}

  ngOnInit(): void {
    this.store.dispatch(actionMonitorRouteDetailsPageInit());
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionMonitorRouteDetailsPageDestroy());
  }

  gpxUpload(): void {
    this.snackBar.open('Sorry, GPX file upload not implemented yet', 'close', {
      panelClass: ['mat-toolbar', 'mat-primary'],
    });
  }

  gpxDownload(): void {
    this.snackBar.open(
      'Sorry, GPX file download not implemented yet',
      'close',
      { panelClass: ['mat-toolbar', 'mat-primary'] }
    );
  }
}
