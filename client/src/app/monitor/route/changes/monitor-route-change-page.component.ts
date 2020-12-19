import {OnInit} from '@angular/core';
import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';
import {Timestamp} from '@api/custom/timestamp';
import {Store} from '@ngrx/store';
import {Util} from '../../../components/shared/util';
import {AppState} from '../../../core/core.state';
import {actionMonitorRouteChangeInit} from '../../store/monitor.actions';
import {selectMonitorRouteName} from '../../store/monitor.selectors';
import {selectMonitorRouteChange} from '../../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-route-change-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/monitor/routes">Monitor</a></li>
      <li>Route</li>
    </ul>

    <h1 class="title">
      {{routeName$ | async}}
    </h1>

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <div *ngIf="!response.result">
        Route/changeset not found
      </div>
      <div *ngIf="response.result">

        <table class="kpn-table">
          <tbody>
          <tr>
            <td>
              Changeset
            </td>
            <td>
              <div class="kpn-line">
                <span>{{response.result.change.key.changeSetId}}</span>
                <kpn-osm-link-change-set [changeSetId]="response.result.change.key.changeSetId"></kpn-osm-link-change-set>
                <span>
                  <a
                    class="external"
                    rel="nofollow noreferrer"
                    target="_blank"
                    [href]="'https://overpass-api.de/achavi/?changeset=' + response.result.change.key.changeSetId"
                    i18n="@@change-set.header.achavi">
                    achavi
                  </a>
                </span>
                <span>
                  <a
                    class="external"
                    rel="nofollow noreferrer"
                    target="_blank"
                    [href]="'https://osmcha.org/changesets/' + response.result.change.key.changeSetId"
                    i18n="@@change-set.header.osmcha">
                    osmcha
                  </a>
                </span>
              </div>
            </td>
          </tr>
          <tr>
            <td i18n="@@change-set.header.timestamp">
              Timestamp
            </td>
            <td>
              <kpn-timestamp [timestamp]="timestamp(response.result.change.key.timestamp)"></kpn-timestamp>
            </td>
          </tr>
          <tr>
            <td i18n="@@change-set.header.replication-number">
              Minute diff
            </td>
            <td>
              {{replicationName()}}
            </td>
          </tr>
          <tr *ngIf="response.result.change.comment">
            <td>
              Comment
            </td>
            <td>
              {{response.result.change.comment}}
            </td>
          </tr>
          <tr>
            <td>
              Reference
            </td>
            <td>
              {{response.result.change.gpxFilename}}
            </td>
          </tr>
          <tr>
            <td>
              Distance
            </td>
            <td>
              <div>
                <span class="distance-label">GPX</span>
                <span>{{response.result.change.gpxDistance}}km</span>
              </div>
              <div>
                <span class="distance-label">OSM</span>
                <span>{{response.result.change.osmDistance}}km</span>
              </div>
            </td>
          </tr>
          <tr>
            <td i18n="@@change-set.header.analysis">
              Analysis
            </td>
            <td>
              <div *ngIf="response.result.change.happy" class="kpn-line">
                <kpn-icon-happy></kpn-icon-happy>
                <span i18n="@@change-set.header.analysis.happy">
                  This changeset brought improvements.
                </span>
              </div>

              <div *ngIf="response.result.change.investigate" class="kpn-line">
                <kpn-icon-investigate></kpn-icon-investigate>
                <span i18n="@@change-set.header.analysis.investigate">
                  Maybe this changeset is worth a closer look.
                </span>
              </div>

              <div *ngIf="!(response.result.change.happy || !response.result.change.happy)" class="kpn-line">
                <span i18n="@@change-set.header.analysis.no-impact">
                  The changes do not seem to have an impact on the analysis result.
                </span>
              </div>

              <div *ngIf="response.result.change.routeSegmentCount !== 1" class="kpn-line route-analysis">
                <mat-icon svgIcon="warning"></mat-icon>
                <span>Not OK: {{response.result.change.routeSegmentCount}} route segments</span>
              </div>
              <div *ngIf="response.result.change.routeSegmentCount === 1" class="kpn-line route-analysis">
                <span>OK: 1 route segment</span>
              </div>
            </td>
          </tr>
          </tbody>
        </table>

        <p>
          Total ways={{response.result.change.wayCount}},
          ways added={{response.result.change.waysAdded}},
          ways removed={{response.result.change.waysRemoved}},
          ways updated={{response.result.change.waysUpdated}}
        </p>

        <div *ngIf="response.result.change.resolvedNokSegments.length > 0">
          <h2>Resolved deviation segments <span class="kpn-thin">({{response.result.change.resolvedNokSegments.length}})</span></h2>
          <div *ngFor="let segment of response.result.change.resolvedNokSegments; let i=index" class="segment">
            <p>
              Deviation length: {{segment.meters}}m
            </p>
            <p>
              Maximum distance from reference: {{segment.distance}}m
            </p>

            <kpn-monitor-route-change-map
              [mapId]="'resolved-map-' + i + 1"
              [referenceJson]="response.result.change.referenceJson"
              [routeSegments]="response.result.change.routeSegments"
              [nokSegment]="segment">
            </kpn-monitor-route-change-map>

          </div>
        </div>

        <div *ngIf="response.result.change.newNokSegments.length > 0">
          <h2>New deviation segments <span class="kpn-thin">({{response.result.change.newNokSegments.length}})</span></h2>
          <div *ngFor="let segment of response.result.change.newNokSegments; let i=index" class="segment">
            <p>
              Deviation length: {{segment.meters}}m
            </p>
            <p>
              Maximum distance from reference: {{segment.distance}}m
            </p>
            <kpn-monitor-route-change-map
              [mapId]="'new-map-' + i + 1"
              [referenceJson]="response.result.change.referenceJson"
              [routeSegments]="response.result.change.routeSegments"
              [nokSegment]="segment">
            </kpn-monitor-route-change-map>

          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
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
  `]
})
export class MonitorRouteChangePageComponent implements OnInit {

  util = Util;

  readonly routeName$ = this.store.select(selectMonitorRouteName);
  readonly response$ = this.store.select(selectMonitorRouteChange);

  constructor(private store: Store<AppState>) {
  }

  ngOnInit(): void {
    this.store.dispatch(actionMonitorRouteChangeInit());
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
