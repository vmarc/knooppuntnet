import { viewChild } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { SurveyDateInfo } from '@api/common/survey-date-info';
import { TimeInfo } from '@api/common/time-info';
import { RouteScope } from '@api/common/route-scope';
import { RouteType } from '@api/common/route-type';
import { NetworkNodeRow } from '@api/common/network/network-node-row';
import { EditService } from '@app/analysis/components/edit/edit.service';
import { EditAndPaginatorComponent } from '@app/analysis/components/edit/edit-and-paginator.component';
import { DayComponent } from '@app/shared/components/day/day.component';
import { DayPipe } from '@app/shared/components/format/day.pipe';
import { LinkNodeComponent } from '@app/shared/components/link/link-node.component';
import { NzTbodyComponent } from 'ng-zorro-antd/table';
import { NzTrDirective } from 'ng-zorro-antd/table';
import { NzTheadComponent } from 'ng-zorro-antd/table';
import { NzThMeasureDirective } from 'ng-zorro-antd/table';
import { NzTableCellDirective } from 'ng-zorro-antd/table';
import { NzTableComponent } from 'ng-zorro-antd/table';
import { ActionButtonNodeComponent } from '../../../../components/action/action-button-node.component';
import { NetworkNodesPageService } from '../network-nodes-page.service';
import { NetworkNodeAnalysisComponent } from './network-node-analysis.component';
import { NetworkNodeRoutesComponent } from './network-node-routes.component';

@Component({
  selector: 'kpn-network-node-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-edit-and-paginator
      (edit)="edit()"
      i18n-editLinkTitle="@@network-nodes.edit.title"
      editLinkTitle="Load the nodes in this page in JOSM"
      [pageSize]="service.pageSize()"
      (pageSizeChange)="onPageSizeChange($event)"
      [length]="nodes()?.length"
      [showPageSizeSelection]="true"
      [showFirstLastButtons]="true"
    />

    <nz-table
      nzBordered
      #nodeTable
      [nzData]="filteredNodes()"
      [nzPageSize]="pageSize()"
      nzPaginationPosition="both"
      nzShowSizeChanger="true"
      [nzPageSizeOptions]="[10, 25, 50, 100, 250, 500, 1000]"
      nzSize="small"
    >
      <thead>
        <tr>
          <th rowSpan="2" i18n="@@network-nodes.table.nr">Nr</th>
          <th rowSpan="2" i18n="@@network-nodes.table.analysis">Analysis</th>
          <th rowSpan="2" i18n="@@network-nodes.table.node">Node</th>
          <th rowSpan="2" i18n="@@network-nodes.table.name">Name</th>
          <th colSpan="2" i18n="@@network-nodes.table.routes">Routes</th>
          <th rowSpan="2" i18n="@@network-nodes.table.last-survey">Survey</th>
          <th rowSpan="2" i18n="@@network-nodes.table.last-edit">Last edit</th>
        </tr>
        <tr>
          <th i18n="@@network-nodes.table.routes.expected">Expected</th>
          <th i18n="@@network-nodes.table.routes.actual">Actual</th>
        </tr>
      </thead>

      <tbody>
        @for (node of nodeTable.data; track node.detail.id; let i = $index) {
          <tr>
            <td class="nr-column">
              {{ rowNumber(i) }}
            </td>
            <td>
              <kpn-network-node-analysis
                [routeType]="routeType()"
                [routeScope]="routeScope()"
                [node]="node"
              />
            </td>
            <td class="kpn-align-center node-column">
              <kpn-action-button-node [nodeId]="node.detail.id" />
              <kpn-link-node [nodeId]="node.detail.id" [nodeName]="node.detail.name" />
            </td>
            <td>
              {{ node.detail.longName }}
            </td>
            <td>
              {{ expectedRouteCount(node) }}
            </td>
            <td class="routes-actual-column">
              <kpn-network-node-routes [node]="node" />
            </td>
            <td>
              {{ node.detail.lastSurvey | day }}
            </td>
            <td class="kpn-separated">
              <kpn-day [timestamp]="node.detail.timestamp" />
            </td>
          </tr>
        }
      </tbody>
    </nz-table>
  `,
  styles: `
    .nr-column {
      width: 3rem;
    }

    .routes-actual-column {
      width: 12rem;
    }

    .node-column {
      padding-left: 0 !important;
      padding-right: 1rem !important;
    }
  `,
  imports: [
    ActionButtonNodeComponent,
    DayComponent,
    DayPipe,
    EditAndPaginatorComponent,
    LinkNodeComponent,
    NetworkNodeAnalysisComponent,
    NetworkNodeRoutesComponent,
    NzTableCellDirective,
    NzTableComponent,
    NzTbodyComponent,
    NzThMeasureDirective,
    NzTheadComponent,
    NzTrDirective,
    DayPipe,
  ],
})
export class NetworkNodeTableComponent {
  routeType = input.required<RouteType>();
  routeScope = input.required<RouteScope>();
  timeInfo = input.required<TimeInfo>();
  surveyDateInfo = input.required<SurveyDateInfo>();
  nodes = input.required<NetworkNodeRow[]>();

  private readonly editAndPaginator = viewChild(EditAndPaginatorComponent);

  private readonly editService = inject(EditService);
  protected readonly service = inject(NetworkNodesPageService);

  readonly pageSize = this.service.pageSize;
  readonly filteredNodes = this.service.filteredNodes;

  rowNumber(index: number): number {
    return this.editAndPaginator().paginator().rowNumber(index);
  }

  expectedRouteCount(node: NetworkNodeRow): string {
    return node.detail.expectedRouteCount ? node.detail.expectedRouteCount.toString() : '-';
  }

  onPageSizeChange(pageSize: number) {
    this.service.updatePageSize(pageSize);
  }

  edit(): void {
    const nodeIds = this.filteredNodes().map((node) => node.detail.id);
    this.editService.edit({
      nodeIds,
    });
  }
}
