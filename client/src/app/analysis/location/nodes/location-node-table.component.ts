import { ChangeDetectionStrategy } from '@angular/core';
import { OnChanges } from '@angular/core';
import { SimpleChanges } from '@angular/core';
import { EventEmitter } from '@angular/core';
import { Output } from '@angular/core';
import { ViewChild } from '@angular/core';
import { Input } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { LocationNodeInfo } from '@api/common/location/location-node-info';
import { TimeInfo } from '@api/common/time-info';
import { NetworkScope } from '@api/custom/network-scope';
import { NetworkType } from '@api/custom/network-type';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { PageWidthService } from '../../../components/shared/page-width.service';
import { PaginatorComponent } from '../../../components/shared/paginator/paginator.component';
import { AppState } from '../../../core/core.state';

@Component({
  selector: 'kpn-location-node-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-paginator
      (page)="page.emit($event)"
      [length]="nodeCount"
      [pageIndex]="pageIndex"
      [showPageSizeSelection]="true"
      [showFirstLastButtons]="true"
    >
    </kpn-paginator>

    <table mat-table [dataSource]="dataSource">
      <ng-container matColumnDef="nr">
        <th mat-header-cell *matHeaderCellDef i18n="@@location-nodes.table.nr">
          Nr
        </th>
        <td mat-cell *matCellDef="let i = index">{{ rowNumber(i) }}</td>
      </ng-container>

      <ng-container matColumnDef="analysis">
        <th
          mat-header-cell
          *matHeaderCellDef
          i18n="@@location-nodes.table.analysis"
        >
          Analysis
        </th>
        <td mat-cell *matCellDef="let node">
          <kpn-location-node-analysis
            [node]="node"
            [networkType]="networkType"
            [networkScope]="networkScope"
          ></kpn-location-node-analysis>
        </td>
      </ng-container>

      <ng-container matColumnDef="node">
        <th
          mat-header-cell
          *matHeaderCellDef
          i18n="@@location-nodes.table.node"
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
          mat-header-cell
          *matHeaderCellDef
          i18n="@@location-nodes.table.name"
        >
          Name
        </th>
        <td mat-cell *matCellDef="let node">
          {{ node.longName }}
        </td>
      </ng-container>

      <ng-container matColumnDef="expectedRouteCount">
        <th
          mat-header-cell
          *matHeaderCellDef
          i18n="@@location-nodes.table.expected-route-count"
        >
          Expected
        </th>
        <td mat-cell *matCellDef="let node">
          {{ node.expectedRouteCount }}
        </td>
      </ng-container>

      <ng-container matColumnDef="routes">
        <th
          mat-header-cell
          *matHeaderCellDef
          i18n="@@location-nodes.table.routes"
        >
          Routes
        </th>
        <td mat-cell *matCellDef="let node">
          <kpn-location-node-routes [node]="node"></kpn-location-node-routes>
        </td>
      </ng-container>

      <ng-container matColumnDef="last-survey">
        <th
          mat-header-cell
          *matHeaderCellDef
          i18n="@@location-nodes.table.last-survey"
        >
          Survey
        </th>
        <td mat-cell *matCellDef="let node">
          {{ node.lastSurvey | day }}
        </td>
      </ng-container>

      <ng-container matColumnDef="lastEdit">
        <th
          mat-header-cell
          *matHeaderCellDef
          i18n="@@location-nodes.table.last-edit"
        >
          Last edit
        </th>
        <td mat-cell *matCellDef="let node" class="kpn-separated">
          <kpn-day [timestamp]="node.lastUpdated"></kpn-day>
          <kpn-josm-node [nodeId]="node.id"></kpn-josm-node>
          <kpn-osm-link-node [nodeId]="node.id"></kpn-osm-link-node>
        </td>
      </ng-container>

      <tr mat-header-row *matHeaderRowDef="displayedColumns$ | async"></tr>
      <tr
        mat-row
        *matRowDef="let node; columns: displayedColumns$ | async"
      ></tr>
    </table>

    <kpn-paginator
      (page)="page.emit($event)"
      [length]="nodeCount"
      [pageIndex]="pageIndex"
    >
    </kpn-paginator>
  `,
  styles: [
    `
      .mat-column-nr {
        flex: 0 0 4em;
      }
    `,
  ],
})
export class LocationNodeTableComponent implements OnInit, OnChanges {
  // TODO !!!
  networkType: NetworkType = NetworkType.cycling;
  networkScope: NetworkScope = NetworkScope.regional;

  @Input() timeInfo: TimeInfo;
  @Input() nodes: LocationNodeInfo[];
  @Input() nodeCount: number;
  @Input() pageIndex: number;
  @Output() page = new EventEmitter<PageEvent>();

  @ViewChild(PaginatorComponent, { static: true })
  paginator: PaginatorComponent;

  dataSource: MatTableDataSource<LocationNodeInfo>;
  displayedColumns$: Observable<Array<string>>;

  constructor(
    private pageWidthService: PageWidthService,
    private store: Store<AppState>
  ) {
    this.dataSource = new MatTableDataSource();
    this.displayedColumns$ = pageWidthService.current$.pipe(
      map(() => this.displayedColumns())
    );
  }

  ngOnInit(): void {
    this.dataSource.data = this.nodes;
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['nodes']) {
      this.dataSource.data = this.nodes;
    }
  }

  rowNumber(index: number): number {
    return this.paginator.rowNumber(index);
  }

  private displayedColumns() {
    if (this.pageWidthService.isVeryLarge()) {
      return [
        'nr',
        'analysis',
        'node',
        'name',
        'expectedRouteCount',
        'routes',
        'last-survey',
        'lastEdit',
      ];
    }

    if (this.pageWidthService.isLarge()) {
      return ['nr', 'analysis', 'node', 'name', 'expectedRouteCount', 'routes'];
    }

    return ['nr', 'node', 'expectedRouteCount', 'routes'];
  }
}
