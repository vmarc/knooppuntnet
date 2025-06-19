import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouteDetailsPage } from '@api/common/route/route-details-page';
import { FactInfo } from '@app/analysis/fact/components/fact-info';
import { FactsComponent } from '@app/analysis/fact/components/facts.component';
import { RoutePathsComponent } from '@app/analysis/route/internal/details/components/route-paths.component';
import { RouteSegmentsComponent } from '@app/analysis/route/internal/details/components/route-segments.component';
import { DataComponent } from '@app/shared/components/data/data.component';
import { DividerComponent } from '@app/shared/components/divider.component';
import { InterpretedTags } from '@app/shared/components/tags/interpreted-tags';
import { TagTableComponent } from '@app/shared/components/tags/tag-table.component';
import { TimestampComponent } from '@app/shared/components/timestamp/timestamp.component';
import { RouterService } from '@app/shared/services/router.service';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { RouteEndNodesComponent } from './route-end-nodes.component';
import { RouteMembersComponent } from './route-members.component';
import { RouteNetworkReferencesComponent } from './route-network-references.component';
import { RouteParentsComponent } from './route-parents.component';
import { RouteRedundantNodesComponent } from './route-redundant-nodes.component';
import { RouteStartNodesComponent } from './route-start-nodes.component';
import { RouteSummaryComponent } from './route-summary.component';
import { RouteDetailsPageService } from '../route-details-page.service';

@Component({
  selector: 'ui-route-details',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @let page = service.response().result;
    <div>
      <ui-route-summary [route]="page.data" />
      <ui-divider />
      <div class="data2">
        <div class="title">
          <span i18n="@@route.situation-on">Situation on</span>
        </div>
        <div class="body">
          <ui-timestamp [timestamp]="service.response().situationOn" />
        </div>
      </div>
      <div class="data2">
        <div class="title">
          <span i18n="@@route.last-updated">Last updated</span>
        </div>
        <div class="body">
          <ui-timestamp [timestamp]="page.data.lastUpdated" />
        </div>
      </div>
      <ui-data title="Relation last updated" i18n-title="@@route.relation-last-updated">
        <ui-timestamp [timestamp]="page.data.summary.timestamp" />
      </ui-data>
      <ui-data title="Network" i18n-title="@@route.network">
        <ui-route-network-references [references]="page.data.networkReferences" />
      </ui-data>

      @if (page.data.parentRoutes.length > 0) {
        <ui-data title="Part of" i18n-title="@@route.parent-routes">
          <ui-route-parents [parentRoutes]="page.data.parentRoutes" />
        </ui-data>
      }

      <div>
        @if (page.data.nodes; as nodes) {
          <ui-data title="Start node" i18n-title="@@route.start-node">
            <ui-route-start-nodes [nodes]="nodes" />
          </ui-data>

          <ui-data title="End node" i18n-title="@@route.end-node">
            <ui-route-end-nodes [nodes]="nodes" />
          </ui-data>
          @if (nodes.redundantNodes.length > 0) {
            <div>
              <ui-data title="Redundant node" i18n-title="@@route.redundant-node">
                <ui-route-redundant-nodes [nodes]="nodes.redundantNodes" />
              </ui-data>
            </div>
          }
        }
        <ui-data title="Number of ways" i18n-title="@@route.number-of-ways">
          {{ page.data.summary.wayCount }}
        </ui-data>
      </div>

      <ui-divider />
      <p i18n="@@route.tags">Tags</p>
      <ui-tag-table [tags]="routeTags(page)" />

      <ui-divider />
      <ui-facts [factInfos]="factInfos(page)" />

      <ui-divider />
      <button nz-button (click)="zoomToFitRoute()">
        <nz-icon nzType="fullscreen-exit" />
        <span>Zoom to fit entire route</span>
      </button>

      <ui-divider />
      <ui-route-segments />

      <ui-divider />
      <ui-route-paths />

      <ui-divider />
      <ui-route-members
        [routeType]="page.data.summary.routeTypes[0]"
        [rows]="page.data.structureRows"
      />
    </div>
  `,
  styleUrl: '../../../../../shared/components/data/data.component.scss',
  providers: [RouterService],
  imports: [
    DataComponent,
    DividerComponent,
    FactsComponent,
    NzButtonComponent,
    NzIconDirective,
    RouteEndNodesComponent,
    RouteMembersComponent,
    RouteNetworkReferencesComponent,
    RouteParentsComponent,
    RoutePathsComponent,
    RouteRedundantNodesComponent,
    RouteSegmentsComponent,
    RouteStartNodesComponent,
    RouteSummaryComponent,
    TagTableComponent,
    TimestampComponent,
  ],
})
export class RouteDetailsComponent {
  protected readonly service = inject(RouteDetailsPageService);

  routeTags(page: RouteDetailsPage) {
    return InterpretedTags.routeTags(page.data.summary.tags);
  }

  factInfos(page: RouteDetailsPage): FactInfo[] {
    return page.data.facts.map((fact) => {
      if (fact === 'RouteUnexpectedNode') {
        const unexpectedNodeIds = page.data.unexpectedNodeIds;
        return new FactInfo(fact, undefined, undefined, undefined, unexpectedNodeIds);
      }
      if (fact === 'RouteUnexpectedRelation') {
        const unexpectedRelationIds = page.data.unexpectedRelationIds;
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

  zoomToFitRoute(): void {
    this.service.selectSegment(undefined);
  }
}
