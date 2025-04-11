import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { SurveyDateInfo } from '@api/common/survey-date-info';
import { TimeInfo } from '@api/common/time-info';
import { RouteScope } from '@api/common/route-scope';
import { RouteType } from '@api/common/route-type';
import { NetworkNodeRow } from '@api/common/network/network-node-row';
import { EditLinkComponent } from '@app/analysis/components/edit/edit-link.component';
import { FilterComponent } from '@app/analysis/components/filter/filter.component';
import { DayComponent } from '@app/shared/components/day/day.component';
import { DayPipe } from '@app/shared/components/format/day.pipe';
import { LinkNodeComponent } from '@app/shared/components/link/link-node.component';
import { ListItemComponent } from '@app/shared/components/list/list-item.component';
import { ListComponent } from '@app/shared/components/list/list.component';
import { NzTableCellDirective } from 'ng-zorro-antd/table';
import { ActionButtonNodeComponent } from '../../../../components/action/action-button-node.component';
import { NetworkNodesPageService } from '../network-nodes-page.service';
import { NetworkNodeAnalysisComponent } from './network-node-analysis.component';
import { NetworkNodeRoutesComponent } from './network-node-routes.component';

@Component({
  selector: 'kpn-network-node-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-list
      [pageIndex]="pageIndex()"
      (pageIndexChange)="onPageIndexChange($event)"
      [pageSize]="pageSize()"
      (pageSizeChange)="onPageSizeChange($event)"
      [length]="nodeCount()"
      [filter]="true"
    >
      <kpn-filter [filterOptions]="filterOptions()" filter />

      <kpn-edit-link
        header-extra
        (edit)="edit()"
        i18n-title="@@network-nodes.edit.title"
        title="Load the nodes in this page in JOSM"
      />

      @for (node of filteredNodes(); track node.detail.id; let i = $index) {
        <kpn-list-item [selected]="false">
          <div class="kpn-line">
            {{ rowNumber(i) }}
            <kpn-action-button-node [nodeId]="node.detail.id" />
            <kpn-link-node [nodeId]="node.detail.id" [nodeName]="node.detail.name" />
            <span>{{ node.detail.longName }}</span>
          </div>
          <div class="kpn-line">
            <kpn-network-node-analysis
              [routeType]="routeType()"
              [routeScope]="routeScope()"
              [node]="node"
            />
            @if (node.detail.lastSurvey) {
              <span>
                <span i18n="@@network-nodes.table.last-survey" class="kpn-label">Survey</span>
                <span>
                  {{ node.detail.lastSurvey | day }}
                </span>
              </span>
            }
            <span>
              <span i18n="@@network-nodes.table.last-edit" class="kpn-label">Last edit</span>
              <span>
                <kpn-day [timestamp]="node.detail.timestamp" />
              </span>
            </span>
          </div>
          <div>
            <span>
              <span i18n="@@network-nodes.table.routes.expected" class="kpn-label"> Expected </span>
              <span>{{ expectedRouteCount(node) }}</span>
            </span>
          </div>

          <div>
            <kpn-network-node-routes [node]="node" />
          </div>
          <td></td>
        </kpn-list-item>
      }
    </kpn-list>
  `,
  imports: [
    ActionButtonNodeComponent,
    DayComponent,
    DayPipe,
    EditLinkComponent,
    FilterComponent,
    LinkNodeComponent,
    ListComponent,
    ListItemComponent,
    NetworkNodeAnalysisComponent,
    NetworkNodeRoutesComponent,
    NzTableCellDirective,
  ],
})
export class NetworkNodeTableComponent {
  private readonly service = inject(NetworkNodesPageService);

  routeType = input.required<RouteType>();
  routeScope = input.required<RouteScope>();
  timeInfo = input.required<TimeInfo>();
  surveyDateInfo = input.required<SurveyDateInfo>();
  nodes = input.required<NetworkNodeRow[]>();

  // private readonly editService = inject(EditService);

  readonly pageIndex = this.service.pageIndex;
  readonly pageSize = this.service.pageSize;
  readonly filteredNodes = this.service.filteredNodes;
  readonly nodeCount = computed(() => this.filteredNodes()?.length);
  readonly filterOptions = this.service.filterOptions;

  rowNumber(index: number): number {
    // return this.editAndPaginator().paginator().rowNumber(index);
    return 0;
  }

  expectedRouteCount(node: NetworkNodeRow): string {
    return node.detail.expectedRouteCount ? node.detail.expectedRouteCount.toString() : '-';
  }

  onPageIndexChange(pageIndex: number) {
    // this.service.updatePageIndex(pageSize);
  }

  onPageSizeChange(pageSize: number) {
    this.service.updatePageSize(pageSize);
  }

  edit(): void {
    const nodeIds = this.filteredNodes().map((node) => node.detail.id);
    // this.editService.edit({
    //   nodeIds,
    // });
  }
}
