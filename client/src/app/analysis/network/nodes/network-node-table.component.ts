import {Component, Input, OnInit, ViewChild} from "@angular/core";
import {MatPaginator, MatTableDataSource} from "@angular/material";
import {List} from "immutable";
import {NetworkNodeInfo2} from "../../../kpn/shared/network/network-node-info2";
import {PageWidthService} from "../../../components/shared/page-width.service";
import {NetworkType} from "../../../kpn/shared/network-type";
import {BehaviorSubject} from "rxjs";
import {NetworkNodesService} from "./network-nodes.service";
import {TimeInfo} from "../../../kpn/shared/time-info";
import {NetworkNodeFilter} from "./network-node-filter";
import {NetworkNodeFilterCriteria} from "./network-node-filter-criteria";

@Component({
  selector: "kpn-network-node-table",
  template: `
    <mat-paginator [pageSizeOptions]="[5, 10, 20, 50, 1000]" [length]="nodes?.size" showFirstLastButtons></mat-paginator>
    <mat-divider></mat-divider>

    <mat-table matSort [dataSource]="dataSource">

      <ng-container matColumnDef="nr">
        <mat-header-cell *matHeaderCellDef mat-sort-header i18n="@@network-nodes.table.nr">Nr</mat-header-cell>
        <mat-cell *matCellDef="let node">{{node.number}}</mat-cell>
      </ng-container>

      <ng-container matColumnDef="analysis">
        <mat-header-cell *matHeaderCellDef i18n="@@network-nodes.table.analysis">Analysis</mat-header-cell>
        <mat-cell *matCellDef="let node">
          <kpn-network-node-analysis [node]="node" [networkType]="networkType"></kpn-network-node-analysis>
        </mat-cell>
      </ng-container>

      <ng-container matColumnDef="node">
        <mat-header-cell *matHeaderCellDef mat-sort-header i18n="@@network-nodes.table.node">Node</mat-header-cell>
        <mat-cell *matCellDef="let node">
          <kpn-link-node [nodeId]="node.id" [nodeName]="node.name"></kpn-link-node>
        </mat-cell>
      </ng-container>

      <ng-container matColumnDef="routes">
        <mat-header-cell *matHeaderCellDef i18n="@@network-nodes.table.routes">Routes</mat-header-cell>
        <mat-cell *matCellDef="let node">
          <network-node-routes [node]="node"></network-node-routes>
        </mat-cell>
      </ng-container>

      <ng-container matColumnDef="lastEdit">
        <mat-header-cell *matHeaderCellDef mat-sort-header i18n="@@network-nodes.table.last-edit">Last edit</mat-header-cell>
        <mat-cell *matCellDef="let node" class="kpn-line">
          <kpn-day [timestamp]="node.timestamp"></kpn-day>
          <kpn-josm-node [nodeId]="node.id"></kpn-josm-node>
          <kpn-osm-link-node [nodeId]="node.id"></kpn-osm-link-node>
        </mat-cell>
      </ng-container>

      <mat-header-row *matHeaderRowDef="displayedColumns"></mat-header-row>
      <mat-row *matRowDef="let node; columns: displayedColumns;"></mat-row>
    </mat-table>
  `,
  styles: [`

    .mat-header-cell {
      margin-right: 10px;
    }

    .mat-cell {
      margin-right: 10px;
      display: inline-block;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      line-height: 45px;
      vertical-align: middle;
    }

    .mat-column-nr {
      flex: 0 0 30px;
    }

    .mat-column-analysis {
      flex: 0 0 200px;
    }

    .mat-column-node {
      flex: 1 0 60px;
    }

    .mat-column-routes {
      flex: 2 0 200px;
    }

    .mat-column-lastEdit {
      flex: 0 0 200px;
    }

  `]
})
export class NetworkNodeTableComponent implements OnInit {

  @Input() networkType: NetworkType;
  @Input() nodes: List<NetworkNodeInfo2> = List();

  dataSource: MatTableDataSource<NetworkNodeInfo2>;

  @ViewChild(MatPaginator) paginator: MatPaginator;

  private readonly filterCriteria: BehaviorSubject<NetworkNodeFilterCriteria> = new BehaviorSubject(new NetworkNodeFilterCriteria());

  constructor(private pageWidthService: PageWidthService,
              private networkNodesService: NetworkNodesService) {
  }

  ngOnInit() {
    this.dataSource = new MatTableDataSource();
    this.dataSource.paginator = this.paginator;
    this.filterCriteria.subscribe(criteria => {
      const timeInfo: TimeInfo = null;
      const filter = new NetworkNodeFilter(timeInfo, criteria, this.filterCriteria);
      this.dataSource.data = filter.filter(this.nodes).toArray();
      this.networkNodesService.filterOptions.next(filter.filterOptions(this.nodes));
    });
  }

  get displayedColumns() {
    if (this.pageWidthService.isVeryLarge()) {
      return ["nr", "analysis", "node", "routes", "lastEdit"];
    }

    if (this.pageWidthService.isLarge()) {
      return ["nr", "analysis", "node", "routes"];
    }

    return ["nr", "analysis", "node"];
  }

}
