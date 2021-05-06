import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { OrphanNodeInfo } from '@api/common/orphan-node-info';
import { TimeInfo } from '@api/common/time-info';
import { BehaviorSubject } from 'rxjs';
import { PaginatorComponent } from '../../../components/shared/paginator/paginator.component';
import { SubsetOrphanNodeFilter } from './subset-orphan-node-filter';
import { SubsetOrphanNodeFilterCriteria } from './subset-orphan-node-filter-criteria';
import { SubsetOrphanNodesService } from './subset-orphan-nodes.service';

@Component({
  selector: 'kpn-subset-orphan-nodes-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-paginator
      [length]="dataSource.data.length"
      [showPageSizeSelection]="true"
      [showFirstLastButtons]="true"
    >
    </kpn-paginator>

    <table mat-table [dataSource]="dataSource">
      <ng-container matColumnDef="nr">
        <th
          *matHeaderCellDef
          mat-header-cell
          i18n="@@subset-orphan-nodes.table.nr"
        >
          Nr
        </th>
        <td mat-cell *matCellDef="let i = index">{{ rowNumber(i) }}</td>
      </ng-container>

      <ng-container matColumnDef="analysis">
        <th
          mat-header-cell
          *matHeaderCellDef
          i18n="@@subset-orphan-nodes.table.analysis"
        >
          Analysis
        </th>
        <td mat-cell *matCellDef="let node">analysis</td>
      </ng-container>

      <ng-container matColumnDef="node">
        <th
          *matHeaderCellDef
          mat-header-cell
          i18n="@@subset-orphan-nodes.table.node"
        >
          Node
        </th>
        <td mat-cell *matCellDef="let node">
          <kpn-link-node
            [nodeId]="node.id"
            [nodeName]="node.name"
          ></kpn-link-node>
        </td>
      </ng-container>

      <ng-container matColumnDef="name">
        <th
          *matHeaderCellDef
          mat-header-cell
          i18n="@@subset-orphan-nodes.table.name"
        >
          Name
        </th>
        <td mat-cell *matCellDef="let node">
          {{ node.longName }}
        </td>
      </ng-container>

      <ng-container matColumnDef="last-survey">
        <th
          *matHeaderCellDef
          mat-header-cell
          i18n="@@subset-orphan-nodes.table.last-survey"
        >
          Survey
        </th>
        <td mat-cell *matCellDef="let node">
          {{ node.lastSurvey }}
        </td>
      </ng-container>

      <ng-container matColumnDef="last-edit">
        <th
          *matHeaderCellDef
          mat-header-cell
          i18n="@@subset-orphan-nodes.table.last-edit"
        >
          Last edit
        </th>
        <td mat-cell *matCellDef="let node" class="kpn-separated">
          <kpn-day [timestamp]="node.lastUpdated"></kpn-day>
          <kpn-josm-node [nodeId]="node.id"></kpn-josm-node>
          <kpn-osm-link-node [nodeId]="node.id"></kpn-osm-link-node>
        </td>
      </ng-container>

      <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
      <tr mat-row *matRowDef="let node; columns: displayedColumns"></tr>
    </table>
  `,
  styles: [
    `
      .mat-column-nr {
        width: 3rem;
      }
    `,
  ],
})
export class SubsetOrphanNodesTableComponent implements OnInit {
  @Input() timeInfo: TimeInfo;
  @Input() nodes: OrphanNodeInfo[];

  @ViewChild(PaginatorComponent, { static: true })
  paginator: PaginatorComponent;
  dataSource: MatTableDataSource<OrphanNodeInfo>;

  displayedColumns = ['nr', 'node', 'name', 'last-survey', 'last-edit'];

  private readonly filterCriteria = new BehaviorSubject(
    new SubsetOrphanNodeFilterCriteria()
  );

  constructor(private subsetOrphanNodesService: SubsetOrphanNodesService) {}

  ngOnInit(): void {
    this.dataSource = new MatTableDataSource();
    this.dataSource.paginator = this.paginator.matPaginator;
    this.filterCriteria.subscribe((criteria) => {
      const filter = new SubsetOrphanNodeFilter(
        this.timeInfo,
        criteria,
        this.filterCriteria
      );
      this.dataSource.data = filter.filter(this.nodes);
      this.subsetOrphanNodesService.filterOptions$.next(
        filter.filterOptions(this.nodes)
      );
    });
  }

  rowNumber(index: number): number {
    return this.paginator.rowNumber(index);
  }
}
