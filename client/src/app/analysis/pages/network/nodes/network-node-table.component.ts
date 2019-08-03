import {Component, Input, OnInit, ViewChild} from "@angular/core";
import {MatPaginator, MatTableDataSource} from "@angular/material";
import {List} from "immutable";
import {NetworkNodeInfo2} from "../../../../kpn/shared/network/network-node-info2";
import {PageWidthService} from "../../../../components/shared/page-width.service";
import {NetworkType} from "../../../../kpn/shared/network-type";

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

  constructor(private pageWidthService: PageWidthService) {
  }

  ngOnInit() {
    this.dataSource = new MatTableDataSource(this.nodes.toArray());
    this.dataSource.paginator = this.paginator;
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
