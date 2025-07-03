import { output } from '@angular/core';
import { input } from '@angular/core';
import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouteDetailsPageData } from '@api/common/route/route-details-page-data';
import { RouteSegment } from '@api/common/route/route-segment';
import { Timestamp } from '@api/custom/timestamp';
import { FactInfo } from '@app/analysis/fact/components/fact-info';
import { FactsComponent } from '@app/analysis/fact/components/facts.component';
import { MapModeComponent } from '@app/route/internal/components/map-mode.component';
import { RoutePathsComponent } from '@app/route/internal/components/route-paths.component';
import { RouteSegmentsComponent } from '@app/route/internal/components/route-segments.component';
import { DataComponent } from '@app/shared/components/data/data.component';
import { DividerComponent } from '@app/shared/components/divider.component';
import { InterpretedTags } from '@app/shared/components/tags/interpreted-tags';
import { TagTableComponent } from '@app/shared/components/tags/tag-table.component';
import { TimestampComponent } from '@app/shared/components/timestamp/timestamp.component';
import { RouterService } from '@app/shared/services/router.service';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { NzCollapsePanelComponent } from 'ng-zorro-antd/collapse';
import { NzCollapseComponent } from 'ng-zorro-antd/collapse';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { RouteEndNodesComponent } from '@app/route/internal/components/route-end-nodes.component';
import { RouteStructureComponent } from '@app/route/internal/components/route-structure.component';
import { RouteNetworkReferencesComponent } from '@app/route/internal/components/route-network-references.component';
import { RouteParentsComponent } from '@app/route/internal/components/route-parents.component';
import { RouteRedundantNodesComponent } from '@app/route/internal/components/route-redundant-nodes.component';
import { RouteStartNodesComponent } from '@app/route/internal/components/route-start-nodes.component';
import { RouteSummaryComponent } from '@app/route/internal/components/route-summary.component';

@Component({
  selector: 'ui-route-details',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @let data = routeDetailsData();
    <ui-route-structure [routeType]="data.summary.routeTypes[0]" [rows]="data.structureRows" />
    <div>
      <ui-route-summary [route]="data" />
      <ui-divider />
      <div class="data2">
        <div class="title">
          <span i18n="@@route.situation-on">Situation on</span>
        </div>
        <div class="body">
          <ui-timestamp [timestamp]="situationOn()" />
        </div>
      </div>
      <div class="data2">
        <div class="title">
          <span i18n="@@route.last-updated">Last updated</span>
        </div>
        <div class="body">
          <ui-timestamp [timestamp]="data.lastUpdated" />
        </div>
      </div>
      <ui-data title="Relation last updated" i18n-title="@@route.relation-last-updated">
        <ui-timestamp [timestamp]="data.summary.timestamp" />
      </ui-data>
      <ui-data title="Network" i18n-title="@@route.network">
        <ui-route-network-references [references]="data.networkReferences" />
      </ui-data>

      @if (data.parentRoutes.length > 0) {
        <ui-data title="Part of" i18n-title="@@route.parent-routes">
          <ui-route-parents [parentRoutes]="data.parentRoutes" />
        </ui-data>
      }

      <div>
        @if (data.nodes; as nodes) {
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
          {{ data.summary.wayCount }}
        </ui-data>
      </div>

      <ui-divider />
      <p i18n="@@route.tags">Tags</p>
      <ui-tag-table [tags]="routeTags()" />

      <ui-divider />
      <ui-facts [factInfos]="factInfos()" />

      <ui-divider />
      <div class="kpn-button-group">
        <button nz-button (click)="zoomToFitRoute()">
          <nz-icon nzType="fullscreen-exit" />
          <span>Zoom to fit entire route</span>
        </button>
        <ui-map-mode />
      </div>

      <ui-divider />

      <nz-collapse>
        <nz-collapse-panel [nzHeader]="segmentsHeader">
          <ng-template #segmentsHeader>
            <span i18n="@@route.segments.title">Segments</span>
            <span class="kpn-brackets">{{ segmentCount() }}</span>
          </ng-template>
          <ui-route-segments
            [segments]="segments()"
            [selectedSegment]="undefined"
            (selectSegment)="selectSegment($event)"
          />
        </nz-collapse-panel>
        <nz-collapse-panel [nzHeader]="pathsHeader">
          <ng-template #pathsHeader>
            <span i18n="@@route.paths.title">Paths</span>
            <span class="kpn-brackets">{{ pathCount() }}</span>
          </ng-template>
          <ui-route-paths [paths]="paths()" />
        </nz-collapse-panel>
        <!--        <nz-collapse-panel [nzHeader]="membersHeader">-->
        <!--          <ng-template #membersHeader>-->
        <!--            <span i18n="@@route.members.title">Route members</span>-->
        <!--            <span class="kpn-brackets">{{ memberCount() }}</span>-->
        <!--          </ng-template>-->
        <!--          <ui-route-structure-->
        <!--            [routeType]="data.summary.routeTypes[0]"-->
        <!--            [rows]="data.structureRows"-->
        <!--          />-->
        <!--        </nz-collapse-panel>-->
      </nz-collapse>
    </div>
  `,
  styleUrl: '../shared/components/data/data.component.scss',
  providers: [RouterService],
  imports: [
    DataComponent,
    DividerComponent,
    FactsComponent,
    FormsModule,
    MapModeComponent,
    NzButtonComponent,
    NzCollapseComponent,
    NzCollapsePanelComponent,
    NzIconDirective,
    RouteEndNodesComponent,
    RouteNetworkReferencesComponent,
    RouteParentsComponent,
    RoutePathsComponent,
    RouteRedundantNodesComponent,
    RouteSegmentsComponent,
    RouteStartNodesComponent,
    RouteStructureComponent,
    RouteSummaryComponent,
    TagTableComponent,
    TimestampComponent,
  ],
})
export class RouteDetailsComponent {
  readonly situationOn = input.required<Timestamp>();
  readonly routeDetailsData = input.required<RouteDetailsPageData>();

  readonly segmentSelection = output<RouteSegment>();

  protected readonly segments = computed(() => this.routeDetailsData().segments);
  protected readonly segmentCount = computed(() => this.routeDetailsData().segments.length);
  protected readonly paths = computed(() => this.routeDetailsData().paths);
  protected readonly pathCount = computed(() => this.paths().length);
  protected readonly memberCount = computed(() => this.routeDetailsData().structureRows.length);

  routeTags() {
    return InterpretedTags.routeTags(this.routeDetailsData().summary.tags);
  }

  factInfos(): FactInfo[] {
    return this.routeDetailsData().facts.map((fact) => {
      if (fact === 'RouteUnexpectedNode') {
        const unexpectedNodeIds = this.routeDetailsData().unexpectedNodeIds;
        return new FactInfo(fact, undefined, undefined, undefined, unexpectedNodeIds);
      }
      if (fact === 'RouteUnexpectedRelation') {
        const unexpectedRelationIds = this.routeDetailsData().unexpectedRelationIds;
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

  selectSegment(segment: RouteSegment): void {
    this.segmentSelection.emit(segment);
  }

  zoomToFitRoute(): void {
    this.segmentSelection.emit(undefined);
  }
}
