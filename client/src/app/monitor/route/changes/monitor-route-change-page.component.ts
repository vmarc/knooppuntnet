import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Timestamp } from '@api/custom/timestamp';
import { Store } from '@ngrx/store';
import { Util } from '@app/components/shared/util';
import { actionMonitorRouteChangePageDestroy } from '../../store/monitor.actions';
import { actionMonitorRouteChangePageInit } from '../../store/monitor.actions';
import { selectMonitorRouteName } from '../../store/monitor.selectors';
import { selectMonitorRouteChangePage } from '../../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-route-change-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- work-in-progress -->
    <!-- eslint-disable @angular-eslint/template/i18n -->

    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/monitor/routes">Monitor</a></li>
      <li>Route</li>
    </ul>

    <h1 class="title">
      {{ routeName$ | async }}
    </h1>

    <kpn-error></kpn-error>

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <div *ngIf="!response.result">Route/changeset not found</div>
      <div *ngIf="response.result">
        <table class="kpn-table">
          <tbody>
            <tr>
              <td>Changeset</td>
              <td>
                <div class="kpn-line">
                  <span>{{ response.result.key.changeSetId }}</span>
                  <kpn-osm-link-change-set
                    [changeSetId]="response.result.key.changeSetId"
                  />
                  <span>
                    <a
                      class="external"
                      rel="nofollow noreferrer"
                      target="_blank"
                      [href]="
                        'https://overpass-api.de/achavi/?changeset=' +
                        response.result.key.changeSetId
                      "
                      i18n="@@change-set.header.achavi"
                    >
                      achavi
                    </a>
                  </span>
                  <span>
                    <a
                      class="external"
                      rel="nofollow noreferrer"
                      target="_blank"
                      [href]="
                        'https://osmcha.org/changesets/' +
                        response.result.key.changeSetId
                      "
                      i18n="@@change-set.header.osmcha"
                    >
                      osmcha
                    </a>
                  </span>
                </div>
              </td>
            </tr>
            <tr>
              <td i18n="@@change-set.header.timestamp">Timestamp</td>
              <td>
                <kpn-timestamp
                  [timestamp]="timestamp(response.result.key.timestamp)"
                />
              </td>
            </tr>
            <tr>
              <td i18n="@@change-set.header.replication-number">Minute diff</td>
              <td>
                {{ replicationName() }}
              </td>
            </tr>
            <tr *ngIf="response.result.comment">
              <td>Comment</td>
              <td>
                {{ response.result.comment }}
              </td>
            </tr>
            <tr>
              <td>Reference</td>
              <td>
                {{ 'TODO response.result.reference.filename' }}
              </td>
            </tr>
            <tr>
              <td>Distance</td>
              <td>
                <div>
                  <span class="distance-label">GPX</span>
                  <span>{{ 'TODO response.result.reference.distance' }}km</span>
                </div>
                <div>
                  <span class="distance-label">OSM</span>
                  <span>{{ response.result.osmDistance }}km</span>
                </div>
              </td>
            </tr>
            <tr>
              <td i18n="@@change-set.header.analysis">Analysis</td>
              <td>
                <div *ngIf="response.result.happy" class="kpn-line">
                  <kpn-icon-happy />
                  <span i18n="@@change-set.header.analysis.happy">
                    This changeset brought improvements.
                  </span>
                </div>

                <div *ngIf="response.result.investigate" class="kpn-line">
                  <kpn-icon-investigate />
                  <span i18n="@@change-set.header.analysis.investigate">
                    Maybe this changeset is worth a closer look.
                  </span>
                </div>

                <div
                  *ngIf="!(response.result.happy || !response.result.happy)"
                  class="kpn-line"
                >
                  <span i18n="@@change-set.header.analysis.no-impact">
                    The changes do not seem to have an impact on the analysis
                    result.
                  </span>
                </div>

                <div
                  *ngIf="response.result.routeSegmentCount !== 1"
                  class="kpn-line route-analysis"
                >
                  <mat-icon svgIcon="warning" />
                  <span
                    >Not OK: {{ response.result.routeSegmentCount }} route
                    segments</span
                  >
                </div>
                <div
                  *ngIf="response.result.routeSegmentCount === 1"
                  class="kpn-line route-analysis"
                >
                  <span>OK: 1 route segment</span>
                </div>
              </td>
            </tr>
          </tbody>
        </table>

        <p>
          Total ways={{ response.result.wayCount }}, ways added={{
            response.result.waysAdded
          }}, ways removed={{ response.result.waysRemoved }}, ways updated={{
            response.result.waysUpdated
          }}
        </p>

        <div *ngIf="response.result.resolvedDeviations.length > 0">
          <h2>
            Resolved deviation segments
            <span class="kpn-thin"
              >({{ response.result.resolvedDeviations.length }})</span
            >
          </h2>
          <div
            *ngFor="
              let segment of response.result.resolvedDeviations;
              let i = index
            "
            class="segment"
          >
            <p>Deviation length: {{ segment.meters }}m</p>
            <p>Maximum distance from reference: {{ segment.distance }}m</p>

            <kpn-monitor-route-change-map
              [mapId]="'resolved-map-' + i + 1"
              [referenceJson]="response.result.reference.geoJson"
              [routeSegments]="response.result.routeSegments"
              [deviation]="segment"
            />
          </div>
        </div>

        <div *ngIf="response.result.newDeviations.length > 0">
          <h2>
            New deviation segments
            <span class="kpn-thin"
              >({{ response.result.newDeviations.length }})</span
            >
          </h2>
          <div
            *ngFor="
              let deviation of response.result.newDeviations;
              let i = index
            "
            class="segment"
          >
            <p>Deviation length: {{ deviation.meters }}m</p>
            <p>Maximum distance from reference: {{ deviation.distance }}m</p>
            <kpn-monitor-route-change-map
              [mapId]="'new-map-' + i + 1"
              [referenceJson]="response.result.reference.geoJson"
              [routeSegments]="response.result.routeSegments"
              [deviation]="deviation"
            />
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [
    `
      .distance-label {
        display: inline-block;
        width: 2.5em;
      }

      .route-analysis {
        padding-top: 0.5em;
      }

      .segment {
        border-top: 1px solid lightgray;
        padding-bottom: 2em;
      }
    `,
  ],
})
export class MonitorRouteChangePageComponent implements OnInit, OnDestroy {
  util = Util;

  readonly routeName$ = this.store.select(selectMonitorRouteName);
  readonly response$ = this.store.select(selectMonitorRouteChangePage);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionMonitorRouteChangePageInit());
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionMonitorRouteChangePageDestroy());
  }

  timestamp(timestampString: string): Timestamp {
    // temporary hack until Timestamp is interface
    // @ts-ignore
    return timestampString as Timestamp;
  }

  replicationName(): string {
    return '000/000/000';
  }
}
