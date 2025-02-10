import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { TimeInfo } from '@api/common/time-info';
import { RouteScope } from '@api/common/route-scope';
import { LocationNodeInfo } from '@api/common/location/location-node-info';
import { EditAndPaginatorComponent } from '@app/analysis/components/edit/edit-and-paginator.component';
import { EditParameters } from '@app/analysis/components/edit/edit-parameters';
import { DayComponent } from '@app/shared/components/day/day.component';
import { EditService } from '@app/shared/components/edit.service';
import { DayPipe } from '@app/shared/components/format/day.pipe';
import { LinkNodeComponent } from '@app/shared/components/link/link-node.component';
import { PaginatorComponent } from '@app/shared/components/paginator/paginator.component';
import { NzTrDirective } from 'ng-zorro-antd/table';
import { NzTheadComponent } from 'ng-zorro-antd/table';
import { NzThMeasureDirective } from 'ng-zorro-antd/table';
import { NzTbodyComponent } from 'ng-zorro-antd/table';
import { NzTableComponent } from 'ng-zorro-antd/table';
import { NzTableCellDirective } from 'ng-zorro-antd/table';
import { ActionButtonNodeComponent } from '../../../../components/action/action-button-node.component';
import { LocationNodesPageService } from '../location-nodes-page.service';
import { LocationNodeAnalysisComponent } from './location-node-analysis.component';
import { LocationNodeRoutesComponent } from './location-node-routes.component';

@Component({
  selector: 'kpn-location-node-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-edit-and-paginator
      (edit)="edit()"
      i18n-editLinkTitle="@@location-nodes.edit.title"
      editLinkTitle="Load the nodes in this page in JOSM"
      [pageIndex]="service.pageIndex()"
      (pageIndexChange)="onPageIndexChange($event)"
      [pageSize]="service.pageSize()"
      (pageSizeChange)="onPageSizeChange($event)"
      [length]="nodeCount()"
      [showFirstLastButtons]="false"
      [showPageSizeSelection]="true"
    />

    <nz-table
      nzBordered
      #nodeTable
      [nzData]="nodes()"
      [nzPageSize]="service.pageSize()"
      nzPaginationPosition="both"
      nzShowSizeChanger="true"
      [nzPageSizeOptions]="[10, 25, 50, 100, 250, 500, 1000]"
      nzSize="small"
    >
      <thead>
        <tr>
          <th i18n="@@location-nodes.table.nr">Nr</th>
          <th i18n="@@location-nodes.table.analysis">Analysis</th>
          <th i18n="@@location-nodes.table.node">Node</th>
          <th i18n="@@location-nodes.table.name">Name</th>
          <th i18n="@@location-nodes.table.expected-route-count">Expected</th>
          <th i18n="@@location-nodes.table.routes">Routes</th>
          <th i18n="@@location-nodes.table.last-survey">Survey</th>
          <th i18n="@@location-nodes.table.last-edit">Last edit</th>
        </tr>
      </thead>

      <tbody>
        @for (node of nodeTable.data; track node.id; let i = $index) {
          <tr>
            <td class="column-nr">
              {{ node.rowIndex + 1 }}
            </td>
            <td>
              <kpn-location-node-analysis
                [node]="node"
                [routeType]="service.routeType()"
                [routeScope]="routeScope"
              />
            </td>
            <td>
              <kpn-action-button-node [nodeId]="node.id" />
              <kpn-link-node [nodeId]="node.id" [nodeName]="node.name" />
            </td>
            <td>
              {{ node.longName }}
            </td>
            <td>
              {{ node.expectedRouteCount }}
            </td>
            <td>
              <kpn-location-node-routes [node]="node" />
            </td>
            <td>
              {{ node.lastSurvey | day }}
            </td>
            <td class="kpn-separated">
              <kpn-day [timestamp]="node.lastUpdated" />
            </td>
          </tr>
        }
      </tbody>
    </nz-table>

    <kpn-paginator
      [pageIndex]="service.pageIndex()"
      (pageIndexChange)="onPageIndexChange($event)"
      [pageSize]="service.pageSize()"
      (pageSizeChange)="onPageSizeChange($event)"
      [length]="nodeCount()"
    />
  `,
  styles: `
    .column-nr {
      flex: 0 0 4em;
    }
  `,
  imports: [
    ActionButtonNodeComponent,
    DayComponent,
    DayPipe,
    DayPipe,
    EditAndPaginatorComponent,
    LinkNodeComponent,
    LocationNodeAnalysisComponent,
    LocationNodeRoutesComponent,
    NzTableCellDirective,
    NzTableComponent,
    NzTbodyComponent,
    NzThMeasureDirective,
    NzTheadComponent,
    NzTrDirective,
    PaginatorComponent,
  ],
})
export class LocationNodeTableComponent {
  readonly service = inject(LocationNodesPageService);
  private readonly editService = inject(EditService);

  timeInfo = input.required<TimeInfo>();
  nodes = input.required<LocationNodeInfo[]>();
  nodeCount = input.required<number>();

  // TODO SIGNAL
  routeScope: RouteScope = 'regional';

  onPageSizeChange(pageSize: number) {
    this.service.updatePageSize(pageSize);
  }

  onPageIndexChange(pageIndex: number) {
    window.scroll(0, 0);
    this.service.setPageIndex(pageIndex);
  }

  edit(): void {
    const editParameters: EditParameters = {
      nodeIds: this.nodes().map((node) => node.id),
    };
    this.editService.edit(editParameters);
  }
}
