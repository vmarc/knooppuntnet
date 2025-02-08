import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { MatButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { MatLabel } from '@angular/material/select';
import { RouterLink } from '@angular/router';
import { RouteDetailsPage } from '@api/common/route/route-details-page';
import { FactInfo } from '@app/analysis/fact/components/fact-info';
import { FactsComponent } from '@app/analysis/fact/components/facts.component';
import { RouteSummaryComponent } from '@app/analysis/route';
import { DataComponent } from '@app/shared/components/data/data.component';
import { DividerComponent } from '@app/shared/components/divider.component';
import { PageWidthService } from '@app/shared/components/page-width.service';
import { PageButtonsComponent } from '@app/shared/components/page/page-buttons.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { InterpretedTags } from '@app/shared/components/tags/interpreted-tags';
import { TagTableComponent } from '@app/shared/components/tags/tag-table.component';
import { TimestampComponent } from '@app/shared/components/timestamp/timestamp.component';
import { NzBreadCrumbItemComponent } from 'ng-zorro-antd/breadcrumb';
import { NzBreadCrumbComponent } from 'ng-zorro-antd/breadcrumb';
import { RouterService } from '../../../../shared/services/router.service';
import { RoutePageHeaderComponent } from '../components/route-page-header.component';
import { RouteEndNodesComponent } from './components/route-end-nodes.component';
import { RouteMembersComponent } from './components/route-members.component';
import { RouteNetworkReferencesComponent } from './components/route-network-references.component';
import { RouteParentsComponent } from './components/route-parents.component';
import { RouteRedundantNodesComponent } from './components/route-redundant-nodes.component';
import { RouteStartNodesComponent } from './components/route-start-nodes.component';
import { RouteDetailsPageService } from './route-details-page.service';

@Component({
  selector: 'kpn-route-details-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page-buttons>
      <button mat-stroked-button routerLink="changes">
        <mat-icon>list</mat-icon>
        <mat-label>segments (3)</mat-label>
      </button>
      <button mat-stroked-button routerLink="changes">
        <mat-icon>history</mat-icon>
        <mat-label>changes</mat-label>
      </button>
    </kpn-page-buttons>
    <kpn-page>
      <nz-breadcrumb>
        <nz-breadcrumb-item>
          <a [routerLink]="'/'" i18n="@@breadcrumb.home">Home</a>
        </nz-breadcrumb-item>
        <nz-breadcrumb-item>
          <a [routerLink]="'/analysis'" i18n="@@breadcrumb.analysis">Analysis</a>
        </nz-breadcrumb-item>
        <nz-breadcrumb-item>
          <span i18n="@@breadcrumb.route">Route</span>
        </nz-breadcrumb-item>
      </nz-breadcrumb>
      <kpn-route-page-header pageName="details" />

      @if (service.response(); as response) {
        <div class="kpn-spacer-above">
          @if (!response.result) {
            <div i18n="@@route.route-not-found">Route not found</div>
          }
          @if (response.result; as page) {
            <div>
              <kpn-route-summary [route]="page.route" />
              <kpn-divider />
              <div class="data2">
                <div class="title">
                  <span i18n="@@route.situation-on">Situation on</span>
                </div>
                <div class="body">
                  <kpn-timestamp [timestamp]="response.situationOn" />
                </div>
              </div>
              <div class="data2">
                <div class="title">
                  <span i18n="@@route.last-updated">Last updated</span>
                </div>
                <div class="body">
                  <kpn-timestamp [timestamp]="page.route.lastUpdated" />
                </div>
              </div>
              <kpn-data title="Relation last updated" i18n-title="@@route.relation-last-updated">
                <kpn-timestamp [timestamp]="page.route.summary.timestamp" />
              </kpn-data>
              <kpn-data title="Network" i18n-title="@@route.network">
                <kpn-route-network-references [references]="page.networkReferences" />
              </kpn-data>

              @if (page.route.parentRoutes.length > 0) {
                <kpn-data title="Part of" i18n-title="@@route.parent-routes">
                  <kpn-route-parents [parentRoutes]="page.route.parentRoutes" />
                </kpn-data>
              }

              <div>
                @if (page.route.nodes; as nodes) {
                  <kpn-data title="Start node" i18n-title="@@route.start-node">
                    <kpn-route-start-nodes [nodes]="nodes" />
                  </kpn-data>

                  <kpn-data title="End node" i18n-title="@@route.end-node">
                    <kpn-route-end-nodes [nodes]="nodes" />
                  </kpn-data>
                  @if (nodes.redundantNodes.length > 0) {
                    <div>
                      <kpn-data title="Redundant node" i18n-title="@@route.redundant-node">
                        <kpn-route-redundant-nodes [nodes]="nodes.redundantNodes" />
                      </kpn-data>
                    </div>
                  }
                }
                <kpn-data title="Number of ways" i18n-title="@@route.number-of-ways">
                  {{ page.route.summary.wayCount }}
                </kpn-data>
              </div>

              <kpn-divider />
              <p i18n="@@route.tags">Tags</p>
              <kpn-tag-table [tags]="routeTags(page)" />

              <kpn-divider />

              <kpn-facts [factInfos]="factInfos(page)" />
              @if (showRouteDetails()) {
                <kpn-divider />
                <div>
                  <!-- TODO redesign routeTypes[0]-->
                  <kpn-route-members
                    [routeType]="page.route.summary.routeTypes[0]"
                    [rows]="page.route.structureRows"
                  />
                </div>
              }
            </div>
          }
        </div>
      }
    </kpn-page>
  `,
  styleUrl: '../../../../shared/components/data/data.component.scss',
  providers: [RouteDetailsPageService, RouterService],
  imports: [
    DataComponent,
    DividerComponent,
    FactsComponent,
    MatButton,
    MatIcon,
    MatLabel,
    NzBreadCrumbComponent,
    NzBreadCrumbItemComponent,
    PageButtonsComponent,
    PageComponent,
    RouteEndNodesComponent,
    RouteMembersComponent,
    RouteNetworkReferencesComponent,
    RoutePageHeaderComponent,
    RouteParentsComponent,
    RouteRedundantNodesComponent,
    RouteStartNodesComponent,
    RouteSummaryComponent,
    RouterLink,
    TagTableComponent,
    TimestampComponent,
  ],
})
export class RouteDetailsPageComponent implements OnInit {
  readonly service = inject(RouteDetailsPageService);
  private readonly pageWidthService = inject(PageWidthService);

  readonly showRouteDetails = computed(() => !this.pageWidthService.isAllSmall());

  ngOnInit(): void {
    this.service.onInit();
  }

  routeTags(page: RouteDetailsPage) {
    return InterpretedTags.routeTags(page.route.summary.tags);
  }

  factInfos(page: RouteDetailsPage): FactInfo[] {
    return page.route.facts.map((fact) => {
      if (fact === 'RouteUnexpectedNode') {
        const unexpectedNodeIds = page.route.unexpectedNodeIds;
        return new FactInfo(fact, undefined, undefined, undefined, unexpectedNodeIds);
      }
      if (fact === 'RouteUnexpectedRelation') {
        const unexpectedRelationIds = page.route.unexpectedRelationIds;
        return new FactInfo(
          fact,
          undefined,
          undefined,
          undefined,
          undefined,
          unexpectedRelationIds
        );
      }
      return new FactInfo(fact);
    });
  }
}
