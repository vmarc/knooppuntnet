import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { MatButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { MatLabel } from '@angular/material/select';
import { RouterLink } from '@angular/router';
import { RouteDetailsPage } from '@api/common/route';
import { FactInfo } from '@app/analysis/fact';
import { FactsComponent } from '@app/analysis/fact';
import { BackButtonComponent } from '@app/components/shared';
import { DividerComponent } from '@app/components/shared';
import { PageWidthService } from '@app/components/shared';
import { DataComponent } from '@app/components/shared/data';
import { PageButtonsComponent } from '@app/components/shared/page';
import { InterpretedTags } from '@app/components/shared/tags';
import { TagTableComponent } from '@app/components/shared/tags';
import { TimestampComponent } from '@app/components/shared/timestamp';
import { PageComponent } from '../../../shared/components/shared/page/page.component';
import { RouterService } from '../../../shared/services/router.service';
import { RoutePageHeaderComponent } from '../components/route-page-header.component';
import { RouteEndNodesComponent } from './components/route-end-nodes.component';
import { RouteMembersComponent } from './components/route-members.component';
import { RouteNetworkReferencesComponent } from './components/route-network-references.component';
import { RouteParentsComponent } from './components/route-parents.component';
import { RouteRedundantNodesComponent } from './components/route-redundant-nodes.component';
import { RouteStartNodesComponent } from './components/route-start-nodes.component';
import { RouteSummaryComponent } from './components/route-summary.component';
import { RouteDetailsPageService } from './route-details-page.service';

@Component({
  selector: 'kpn-route-details-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page-buttons>
      <kpn-back-button />
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
                  <!-- TODO redesign networkTypes[0]-->
                  <kpn-route-members
                    [networkType]="page.route.summary.networkTypes[0]"
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
  styleUrl: '../../../shared/components/shared/data/data.component.scss',
  providers: [RouteDetailsPageService, RouterService],
  imports: [
    BackButtonComponent,
    DataComponent,
    DividerComponent,
    FactsComponent,
    MatButton,
    MatIcon,
    MatLabel,
    PageButtonsComponent,
    PageComponent,
    RouteEndNodesComponent,
    RouteMembersComponent,
    RouteNetworkReferencesComponent,
    RoutePageHeaderComponent,
    RouteRedundantNodesComponent,
    RouteStartNodesComponent,
    RouteSummaryComponent,
    RouterLink,
    TagTableComponent,
    TimestampComponent,
    RouteParentsComponent,
  ],
})
export class RouteDetailsPageComponent implements OnInit {
  protected readonly service = inject(RouteDetailsPageService);
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
