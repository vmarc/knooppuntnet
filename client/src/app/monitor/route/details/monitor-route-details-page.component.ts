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
          <p
            *ngIf="!route.relationId"
            i18n="@@monitor.route.details.relation-id-undefined"
          >
            Route relation has not been defined yet
          </p>
          <div *ngIf="route.relationId">
            <p class="kpn-separated">
              <kpn-osm-link-relation
                [title]="route.relationId.toString()"
                [relationId]="route.relationId"
              ></kpn-osm-link-relation>
              <kpn-josm-relation
                [relationId]="route.relationId"
              ></kpn-josm-relation>
            </p>
            <p>
              <span>{{ route.wayCount }}</span>
              <span i18n="@@monitor.route.details.ways">ways</span>
            </p>
            <p class="kpn-km">{{ route.osmDistance }}</p>
          </div>
        </kpn-data>

        <kpn-data title="Reference">
          <div
            *ngIf="!route.referenceType"
            i18n="@@monitor.route.details.reference-undefined"
          >
            Reference not defined yet
          </div>
          <p *ngIf="!!route.referenceType">
            <span>{{ route.referenceDay | day }}</span>
          </p>
          <p
            *ngIf="route.referenceType === 'osm'"
            i18n="@@monitor.route.details.reference.osm"
          >
            OSM relation snapshot
          </p>
          <div *ngIf="route.referenceType === 'gpx'">
            <p>
              {{ 'GPX: "' + route.referenceFilename + '"' }}
            </p>
          </div>
          <p class="kpn-km">{{ route.referenceDistance }}</p>
        </kpn-data>

        <kpn-data *ngIf="route.relationId" title="Analysis">
          <p *ngIf="route.happy" class="kpn-line">
            <span i18n="@@monitor.route.details.analysis.ok">All ok</span>
            <kpn-icon-happy></kpn-icon-happy>
          </p>
          <div *ngIf="!route.happy">
            <p>
              <span>{{ route.deviationCount + ' ' }}</span>
              <span i18n="@@monitor.route.details.analysis.deviations"
                >deviations</span
              >
              <span class="kpn-brackets">
                <span class="kpn-km">{{ route.deviationDistance }}</span>
              </span>
            </p>
            <p>
              <span>{{ route.osmSegmentCount + ' ' }}</span>
              <span i18n="@@monitor.route.details.analysis.osm-segments"
                >OSM segment(s)</span
              >
            </p>
          </div>
        </kpn-data>

        <kpn-data *ngIf="route.comment" title="Comment">
          <markdown [data]="route.comment"></markdown>
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
