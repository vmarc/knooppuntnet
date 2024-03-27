import { effect } from '@angular/core';
import { viewChild } from '@angular/core';
import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { input } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatTableModule } from '@angular/material/table';
import { SurveyDateInfo } from '@api/common';
import { TimeInfo } from '@api/common';
import { NetworkNodeRow } from '@api/common/network';
import { NetworkScope } from '@api/custom';
import { NetworkType } from '@api/custom';
import { EditAndPaginatorComponent } from '@app/analysis/components/edit';
import { EditService } from '@app/components/shared';
import { PageWidthService } from '@app/components/shared';
import { Util } from '@app/components/shared';
import { DayComponent } from '@app/components/shared/day';
import { DayPipe } from '@app/components/shared/format';
import { LinkNodeComponent } from '@app/components/shared/link';
import { ActionButtonNodeComponent } from '../../../components/action/action-button-node.component';
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

    <table mat-table [dataSource]="dataSource">
      <ng-container matColumnDef="nr">
        <th [attr.rowspan]="2" mat-header-cell *matHeaderCellDef i18n="@@network-nodes.table.nr">
          Nr
        </th>
        <td mat-cell *matCellDef="let i = index">{{ rowNumber(i) }}</td>
      </ng-container>

      <ng-container matColumnDef="analysis">
        <th
          [attr.rowspan]="2"
          mat-header-cell
          *matHeaderCellDef
          i18n="@@network-nodes.table.analysis"
        >
          Analysis
        </th>
        <td mat-cell *matCellDef="let node">
          <kpn-network-node-analysis
            [networkType]="networkType()"
            [networkScope]="networkScope()"
            [node]="node"
          />
        </td>
      </ng-container>

      <ng-container matColumnDef="node">
        <th [attr.rowspan]="2" mat-header-cell *matHeaderCellDef i18n="@@network-nodes.table.node">
          Node
        </th>
        <td mat-cell *matCellDef="let node" class="kpn-align-center node-column">
          <kpn-action-button-node [nodeId]="node.detail.id" />
          <kpn-link-node [nodeId]="node.detail.id" [nodeName]="node.detail.name" />
        </td>
      </ng-container>

      <ng-container matColumnDef="name">
        <th [attr.rowspan]="2" mat-header-cell *matHeaderCellDef i18n="@@network-nodes.table.name">
          Name
        </th>
        <td mat-cell *matCellDef="let node">
          {{ node.detail.longName }}
        </td>
      </ng-container>

      <ng-container matColumnDef="routes-expected">
        <th mat-header-cell *matHeaderCellDef i18n="@@network-nodes.table.routes.expected">
          Expected
        </th>
        <td mat-cell *matCellDef="let node">
          {{ expectedRouteCount(node) }}
        </td>
      </ng-container>

      <ng-container matColumnDef="routes-actual">
        <th mat-header-cell *matHeaderCellDef i18n="@@network-nodes.table.routes.actual">Actual</th>
        <td mat-cell *matCellDef="let node">
          <kpn-network-node-routes [node]="node" />
        </td>
      </ng-container>

      <ng-container matColumnDef="routes">
        <th
          [attr.colspan]="2"
          mat-header-cell
          *matHeaderCellDef
          i18n="@@network-nodes.table.routes"
        >
          Routes
        </th>
      </ng-container>

      <ng-container matColumnDef="last-survey">
        <th
          [attr.rowspan]="2"
          mat-header-cell
          *matHeaderCellDef
          i18n="@@network-nodes.table.last-survey"
        >
          Survey
        </th>
        <td mat-cell *matCellDef="let node">
          {{ node.detail.lastSurvey | day }}
        </td>
      </ng-container>

      <ng-container matColumnDef="last-edit">
        <th
          [attr.rowspan]="2"
          mat-header-cell
          *matHeaderCellDef
          i18n="@@network-nodes.table.last-edit"
        >
          Last edit
        </th>
        <td mat-cell *matCellDef="let node" class="kpn-separated">
          <kpn-day [timestamp]="node.detail.timestamp" />
        </td>
      </ng-container>

      <tr mat-header-row *matHeaderRowDef="headerColumns1()"></tr>
      <tr mat-header-row *matHeaderRowDef="headerColumns2()"></tr>
      <tr mat-row *matRowDef="let node; columns: displayedColumns()"></tr>
    </table>

    <!--    <kpn-paginator-->
    <!--      [length]="nodes()?.size">-->
    <!--    </kpn-paginator>-->
  `,
  styles: `
    .mat-column-nr {
      width: 3rem;
    }

    .mat-column-routes-actual {
      width: 12rem;
    }

    .node-column {
      padding-left: 0 !important;
      padding-right: 1rem !important;
    }
  `,
  standalone: true,
  imports: [
    DayComponent,
    DayPipe,
    EditAndPaginatorComponent,
    LinkNodeComponent,
    MatTableModule,
    NetworkNodeAnalysisComponent,
    NetworkNodeRoutesComponent,
    ActionButtonNodeComponent,
  ],
})
export class NetworkNodeTableComponent implements OnInit {
  networkType = input.required<NetworkType>();
  networkScope = input.required<NetworkScope>();
  timeInfo = input.required<TimeInfo>();
  surveyDateInfo = input.required<SurveyDateInfo>();
  nodes = input.required<NetworkNodeRow[]>();

  private readonly editAndPaginator = viewChild(EditAndPaginatorComponent);

  private readonly pageWidthService = inject(PageWidthService);
  private readonly editService = inject(EditService);
  protected readonly service = inject(NetworkNodesPageService);

  protected readonly dataSource = new MatTableDataSource<NetworkNodeRow>();
  protected readonly headerColumns1 = computed(() => {
    if (this.pageWidthService.isVeryLarge()) {
      return ['nr', 'analysis', 'node', 'name', 'routes', 'last-survey', 'last-edit'];
    }

    if (this.pageWidthService.isLarge()) {
      return ['nr', 'analysis', 'node', 'routes'];
    }

    return ['nr', 'analysis', 'node'];
  });

  protected readonly headerColumns2 = computed(() => {
    if (this.pageWidthService.isVeryLarge() || this.pageWidthService.isLarge()) {
      return ['routes-expected', 'routes-actual'];
    }
    return [];
  });

  protected readonly displayedColumns = computed(() => {
    if (this.pageWidthService.isVeryLarge()) {
      return [
        'nr',
        'analysis',
        'node',
        'name',
        'routes-expected',
        'routes-actual',
        'last-survey',
        'last-edit',
      ];
    }

    if (this.pageWidthService.isLarge()) {
      return ['nr', 'analysis', 'node', 'routes-expected', 'routes-actual'];
    }

    return ['nr', 'analysis', 'node'];
  });

  constructor() {
    effect(() => {
      this.dataSource.data = this.service.filteredNodes();
    });
  }

  ngOnInit(): void {
    this.dataSource.paginator = this.editAndPaginator().paginator().matPaginator();
  }

  rowNumber(index: number): number {
    return this.editAndPaginator().paginator().rowNumber(index);
  }

  expectedRouteCount(node: NetworkNodeRow): string {
    return node.detail.expectedRouteCount ? node.detail.expectedRouteCount.toString() : '-';
  }

  onPageSizeChange(pageSize: number) {
    this.service.setPageSize(pageSize);
  }

  edit(): void {
    const nodeIds = Util.currentPageItems(this.dataSource).map((node) => node.detail.id);
    this.editService.edit({
      nodeIds,
    });
  }
}
