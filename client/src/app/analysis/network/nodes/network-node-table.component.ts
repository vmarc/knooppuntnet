import {Component, Input, OnInit, ViewChild} from "@angular/core";
import {MatTableDataSource} from "@angular/material/table";
import {List} from "immutable";
import {BehaviorSubject} from "rxjs";
import {PageWidthService} from "../../../components/shared/page-width.service";
import {PaginatorComponent} from "../../../components/shared/paginator/paginator.component";
import {NetworkInfoNode} from "../../../kpn/api/common/network/network-info-node";
import {TimeInfo} from "../../../kpn/api/common/time-info";
import {NetworkType} from "../../../kpn/api/custom/network-type";
import {NetworkNodeFilter} from "./network-node-filter";
import {NetworkNodeFilterCriteria} from "./network-node-filter-criteria";
import {NetworkNodesService} from "./network-nodes.service";

@Component({
  selector: "kpn-network-node-table",
  template: `
    <kpn-paginator [pageSizeOptions]="[5, 10, 20, 50, 1000]" [length]="nodes?.size" [showFirstLastButtons]="true"></kpn-paginator>

    <table mat-table matSort [dataSource]="dataSource" class="kpn-spacer-above">

      <ng-container matColumnDef="nr">
        <th [attr.rowspan]="2" mat-header-cell *matHeaderCellDef mat-sort-header i18n="@@network-nodes.table.nr">Nr</th>
        <td mat-cell *matCellDef="let i=index">{{rowNumber(i)}}</td>
      </ng-container>

      <ng-container matColumnDef="analysis">
        <th [attr.rowspan]="2" mat-header-cell *matHeaderCellDef i18n="@@network-nodes.table.analysis">Analysis</th>
        <td mat-cell *matCellDef="let node">
          <kpn-network-node-analysis [node]="node" [networkType]="networkType"></kpn-network-node-analysis>
        </td>
      </ng-container>

      <ng-container matColumnDef="node">
        <th [attr.rowspan]="2" mat-header-cell *matHeaderCellDef mat-sort-header i18n="@@network-nodes.table.node">Node</th>
        <td mat-cell *matCellDef="let node">
          <kpn-link-node [nodeId]="node.id" [nodeName]="node.name"></kpn-link-node>
        </td>
      </ng-container>

      <ng-container matColumnDef="name">
        <th [attr.rowspan]="2" mat-header-cell *matHeaderCellDef mat-sort-header i18n="@@network-nodes.table.name">Name</th>
        <td mat-cell *matCellDef="let node">
          {{name(node)}}
        </td>
      </ng-container>

      <ng-container matColumnDef="routes-expected">
        <th mat-header-cell *matHeaderCellDef i18n="@@network-nodes.table.routes.expected">Expected</th>
        <td mat-cell *matCellDef="let node">
          {{expectedRouteCount(node)}}
        </td>
      </ng-container>

      <ng-container matColumnDef="routes-actual">
        <th mat-header-cell *matHeaderCellDef i18n="@@network-nodes.table.routes.actual">Actual</th>
        <td mat-cell *matCellDef="let node">
          <network-node-routes [node]="node"></network-node-routes>
        </td>
      </ng-container>

      <ng-container matColumnDef="routes">
        <th [attr.colspan]="2" mat-header-cell *matHeaderCellDef i18n="@@network-nodes.table.routes">Routes</th>
      </ng-container>

      <ng-container matColumnDef="last-survey">
        <th [attr.rowspan]="2" mat-header-cell *matHeaderCellDef mat-sort-header i18n="@@network-nodes.table.last-survey">Last survey</th>
        <td mat-cell *matCellDef="let node">
          {{lastSurvey(node)}}
        </td>
      </ng-container>

      <ng-container matColumnDef="last-edit">
        <th [attr.rowspan]="2" mat-header-cell *matHeaderCellDef mat-sort-header i18n="@@network-nodes.table.last-edit">Last edit</th>
        <td mat-cell *matCellDef="let node" class="kpn-separated">
          <kpn-day [timestamp]="node.timestamp"></kpn-day>
          <kpn-josm-node [nodeId]="node.id"></kpn-josm-node>
          <kpn-osm-link-node [nodeId]="node.id"></kpn-osm-link-node>
        </td>
      </ng-container>

      <tr mat-header-row *matHeaderRowDef="headerColumns1()"></tr>
      <tr mat-header-row *matHeaderRowDef="headerColumns2()"></tr>
      <tr mat-row *matRowDef="let node; columns: displayedColumns()"></tr>

    </table>
  `,
  styles: [`

    .mat-column-nr {
      width: 3rem;
    }

    .mat-column-routes-actual {
      width: 10rem;
    }

  `]
})
export class NetworkNodeTableComponent implements OnInit {

  @Input() networkType: NetworkType;
  @Input() timeInfo: TimeInfo;
  @Input() nodes: List<NetworkInfoNode> = List();

  dataSource: MatTableDataSource<NetworkInfoNode>;

  @ViewChild(PaginatorComponent, {static: true}) paginator: PaginatorComponent;

  private readonly filterCriteria: BehaviorSubject<NetworkNodeFilterCriteria> = new BehaviorSubject(new NetworkNodeFilterCriteria());

  constructor(private pageWidthService: PageWidthService,
              private networkNodesService: NetworkNodesService) {
  }

  ngOnInit(): void {
    this.dataSource = new MatTableDataSource<NetworkInfoNode>();
    this.dataSource.paginator = this.paginator.matPaginator;
    this.filterCriteria.subscribe(criteria => {
      const filter = new NetworkNodeFilter(this.timeInfo, criteria, this.filterCriteria);
      this.dataSource.data = filter.filter(this.nodes).toArray();
      this.networkNodesService.filterOptions.next(filter.filterOptions(this.nodes));
    });
  }

  displayedColumns() {
    if (this.pageWidthService.isVeryLarge()) {
      return ["nr", "analysis", "node", "name", "routes-expected", "routes-actual", "last-survey", "last-edit"];
    }

    if (this.pageWidthService.isLarge()) {
      return ["nr", "analysis", "node", "routes-expected", "routes-actual"];
    }

    return ["nr", "analysis", "node"];
  }

  headerColumns1() {
    if (this.pageWidthService.isVeryLarge()) {
      return ["nr", "analysis", "node", "name", "routes", "last-survey", "last-edit"];
    }

    if (this.pageWidthService.isLarge()) {
      return ["nr", "analysis", "node", "routes"];
    }

    return ["nr", "analysis", "node"];
  }

  headerColumns2() {
    if (this.pageWidthService.isVeryLarge() || this.pageWidthService.isLarge()) {
      return ["routes-expected", "routes-actual"];
    }

    return [];
  }

  rowNumber(index: number): number {
    return this.paginator.rowNumber(index);
  }

  expectedRouteCount(node: NetworkInfoNode): string {
    if (node.integrityCheck && node.integrityCheck.expected) {
      return node.integrityCheck.expected.toString();
    }
    return "-";
  }

  name(node: NetworkInfoNode): string {
    const nameTagKeys = List([`${this.networkType.id}:name`, `name:${this.networkType.id}_ref`]);
    if (node.tags) {
      const nameTag = node.tags.tags.find(tag => nameTagKeys.contains(tag.key));
      if (nameTag) {
        return nameTag.value;
      }
    }
    return "-";
  }

  lastSurvey(node: NetworkInfoNode): string {
    if (node.tags) {
      const nameTag = node.tags.tags.find(tag => tag.key === "survey:date");
      if (nameTag) {
        return nameTag.value;
      }
    }
    return "-";
  }

}
