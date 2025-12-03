import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Timestamp } from '@api/custom/timestamp';
import { BreadcrumbItem } from '@app/shared/components/breadcrumb/breadcrumb-item';
import { BreadcrumbComponent } from '@app/shared/components/breadcrumb/breadcrumb.component';
import { Breadcrumbs } from '@app/shared/components/breadcrumb/breadcrumbs';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { IconHappyComponent } from '@app/shared/components/icon/icon-happy.component';
import { IconInvestigateComponent } from '@app/shared/components/icon/icon-investigate.component';
import { IconWarningComponent } from '@app/shared/components/icon/icon-warning.component';
import { OsmLinkChangeSetComponent } from '@app/shared/components/link/osm-link-change-set.component';
import { NavService } from '@app/shared/components/nav.service';
import { PageComponent } from '@app/shared/components/page/page.component';
import { TimestampComponent } from '@app/shared/components/timestamp/timestamp.component';
import { MonitorRouteChangeMapComponent } from './monitor-route-change-map.component';
import { MonitorRouteChangePageService } from './monitor-route-change-page.service';

@Component({
  selector: 'ui-monitor-route-change-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- work-in-progress -->
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <!-- eslint-disable @angular-eslint/template/cyclomatic-complexity -->

    <ui-page>
      <ui-breadcrumb [breadcrumbItems]="breadcrumbItems" />

      @if (service.state(); as state) {
        <h1 class="title">
          {{ state.routeName }}
        </h1>

        <ui-error />

        @if (state.response; as response) {
          <div class="kpn-spacer-above">
            @if (!response.result) {
              <div>Route/changeset not found</div>
            }

            <!-- eslint-disable-next-line @angular-eslint/template/prefer-at-else -->
            @if (response.result; as page) {
              <div>
                <table class="kpn-table">
                  <tbody>
                    <tr>
                      <td>Changeset</td>
                      <td>
                        <div class="kpn-line">
                          <span>{{ page.key.changeSetId }}</span>
                          <ui-osm-link-change-set [changeSetId]="page.key.changeSetId" />
                          <span>
                            <a
                              class="external"
                              rel="nofollow noreferrer"
                              target="_blank"
                              [href]="achaviLink(page.key.changeSetId)"
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
                              [href]="osmchaLink(page.key.changeSetId)"
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
                        <ui-timestamp [timestamp]="timestamp(page.key.timestamp)" />
                      </td>
                    </tr>
                    <tr>
                      <td i18n="@@change-set.header.replication-number">Minute diff</td>
                      <td>
                        {{ replicationName() }}
                      </td>
                    </tr>

                    @if (page.comment) {
                      <tr>
                        <td>Comment</td>
                        <td>
                          {{ page.comment }}
                        </td>
                      </tr>
                    }

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
                          <span>{{ page.osmDistance }}km</span>
                        </div>
                      </td>
                    </tr>
                    <tr>
                      <td i18n="@@change-set.header.analysis">Analysis</td>
                      <td>
                        @if (page.happy) {
                          <div class="kpn-line">
                            <ui-icon-happy />
                            <span i18n="@@change-set.header.analysis.happy">
                              This changeset brought improvements.
                            </span>
                          </div>
                        }

                        @if (page.investigate) {
                          <div class="kpn-line">
                            <ui-icon-investigate />
                            <span i18n="@@change-set.header.analysis.investigate">
                              Maybe this changeset is worth a closer look.
                            </span>
                          </div>
                        }

                        @if (!(page.happy || !page.happy)) {
                          <div class="kpn-line">
                            <span i18n="@@change-set.header.analysis.no-impact">
                              The changes do not seem to have an impact on the analysis result.
                            </span>
                          </div>
                        }

                        @if (page.routeSegmentCount !== 1) {
                          <div class="kpn-line route-analysis">
                            <ui-icon-warning />
                            <span> Not OK: {{ page.routeSegmentCount }} route segments </span>
                          </div>
                        } @else {
                          <div class="kpn-line route-analysis">
                            <span>OK: 1 route segment</span>
                          </div>
                        }
                      </td>
                    </tr>
                  </tbody>
                </table>

                <p>
                  Total ways={{ page.wayCount }}, ways added={{ page.waysAdded }}, ways removed={{
                    page.waysRemoved
                  }}, ways updated={{ page.waysUpdated }}
                </p>

                @if (page.resolvedDeviations.length > 0) {
                  <div>
                    <h2>
                      Resolved deviation segments
                      <span class="kpn-thin">({{ page.resolvedDeviations.length }})</span>
                    </h2>

                    @for (segment of page.resolvedDeviations; track $index) {
                      <div class="segment">
                        <p>Deviation length: {{ segment.meters }}m</p>
                        <p>Maximum distance from reference: {{ segment.distance }}m</p>

                        <!-- removed: [referenceJson]="page.reference.referenceGeoJson" -->
                        <ui-monitor-route-change-map
                          [referenceJson]=""
                          [routeSegments]="page.routeSegments"
                          [deviation]="segment"
                        />
                      </div>
                    }
                  </div>
                }

                @if (page.newDeviations.length > 0) {
                  <div>
                    <h2>
                      New deviation segments
                      <span class="kpn-thin">({{ page.newDeviations.length }})</span>
                    </h2>

                    @for (deviation of page.newDeviations; track $index) {
                      <div class="segment">
                        <p>Deviation length: {{ deviation.meters }}m</p>
                        <p>
                          Maximum distance from reference:
                          {{ deviation.distance }}m
                        </p>
                        <!-- removed referenceGeoJson [referenceJson]="page.reference.referenceGeoJson"-->
                        <ui-monitor-route-change-map
                          [referenceJson]=""
                          [routeSegments]="page.routeSegments"
                          [deviation]="deviation"
                        />
                      </div>
                    }
                  </div>
                }
              </div>
            }
          </div>
        }
      }
    </ui-page>
  `,
  styles: `
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
  providers: [MonitorRouteChangePageService, NavService],
  imports: [
    BreadcrumbComponent,
    ErrorComponent,
    IconHappyComponent,
    IconInvestigateComponent,
    IconWarningComponent,
    MonitorRouteChangeMapComponent,
    OsmLinkChangeSetComponent,
    PageComponent,
    TimestampComponent,
  ],
})
export class MonitorRouteChangePageComponent {
  protected readonly service = inject(MonitorRouteChangePageService);
  protected readonly breadcrumbItems: BreadcrumbItem[] = [
    Breadcrumbs.home,
    Breadcrumbs.monitorRoutes,
    { label: Breadcrumbs.routeLabel },
  ];

  timestamp(timestampString: string): Timestamp {
    // temporary hack until Timestamp is interface
    return timestampString as Timestamp;
  }

  replicationName(): string {
    return '000/000/000';
  }

  achaviLink(changeSetId: number): string {
    return `https://overpass-api.de/achavi/?changeset=${changeSetId}`;
  }

  osmchaLink(changeSetId: number): string {
    return `https://osmcha.org/changesets/${changeSetId}`;
  }
}
