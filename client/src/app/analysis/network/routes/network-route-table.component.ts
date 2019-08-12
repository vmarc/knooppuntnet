import {Component, Input, OnInit, ViewChild} from "@angular/core";
import {MatPaginator, MatTableDataSource} from "@angular/material";
import {List} from "immutable";
import {PageWidthService} from "../../../components/shared/page-width.service";
import {NetworkType} from "../../../kpn/shared/network-type";
import {BehaviorSubject} from "rxjs";
import {NetworkRouteFilterCriteria} from "./network-route-filter-criteria";
import {TimeInfo} from "../../../kpn/shared/time-info";
import {NetworkRouteFilter} from "./network-route-filter";
import {NetworkRouteRow} from "../../../kpn/shared/network/network-route-row";
import {NetworkRoutesService} from "./network-routes.service";

@Component({
  selector: "kpn-network-route-table",
  template: `

    <mat-paginator [pageSizeOptions]="[5, 10, 20, 50, 1000]" [length]="routes?.size" showFirstLastButtons></mat-paginator>
    <mat-divider></mat-divider>

    <mat-table matSort [dataSource]="dataSource">

      <ng-container matColumnDef="nr">
        <mat-header-cell *matHeaderCellDef mat-sort-header i18n="@@network-routes.table.nr">Nr</mat-header-cell>
        <mat-cell *matCellDef="let route; let i = index">{{i + 1}}</mat-cell>
      </ng-container>

      <ng-container matColumnDef="analysis">
        <mat-header-cell *matHeaderCellDef i18n="@@network-routes.table.analysis">Analysis</mat-header-cell>
        <mat-cell *matCellDef="let route">
          <kpn-network-route-analysis [route]="route" [networkType]="networkType"></kpn-network-route-analysis>
        </mat-cell>
      </ng-container>

      <ng-container matColumnDef="route">
        <mat-header-cell *matHeaderCellDef mat-sort-header i18n="@@network-routes.table.node">Route</mat-header-cell>
        <mat-cell *matCellDef="let route">
          <kpn-link-route [routeId]="route.id" [title]="route.name"></kpn-link-route>
        </mat-cell>
      </ng-container>

      <ng-container matColumnDef="lastEdit">
        <mat-header-cell *matHeaderCellDef mat-sort-header i18n="@@network-routes.table.last-edit">Last edit</mat-header-cell>
        <mat-cell *matCellDef="let route" class="kpn-line">
          <kpn-day [timestamp]="route.timestamp"></kpn-day>
          <kpn-josm-relation [relationId]="route.id"></kpn-josm-relation>
          <kpn-osm-link-relation [relationId]="route.id"></kpn-osm-link-relation>
        </mat-cell>
      </ng-container>

      <mat-header-row *matHeaderRowDef="displayedColumns"></mat-header-row>
      <mat-row *matRowDef="let route; columns: displayedColumns;"></mat-row>
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

    .mat-column-route {
      flex: 1 0 60px;
    }

    .mat-column-lastEdit {
      flex: 0 0 200px;
    }

  `]
})
export class NetworkRouteTableComponent implements OnInit {

  @Input() timeInfo: TimeInfo;
  @Input() networkType: NetworkType;
  @Input() routes: List<NetworkRouteRow>;

  dataSource: MatTableDataSource<NetworkRouteRow>;

  @ViewChild(MatPaginator) paginator: MatPaginator;

  private readonly filterCriteria: BehaviorSubject<NetworkRouteFilterCriteria> = new BehaviorSubject(new NetworkRouteFilterCriteria());

  constructor(private pageWidthService: PageWidthService,
              private networkRoutesService: NetworkRoutesService) {
  }

  ngOnInit() {
    this.dataSource = new MatTableDataSource();
    this.dataSource.paginator = this.paginator;
    this.filterCriteria.subscribe(criteria => {
      const filter = new NetworkRouteFilter(this.timeInfo, criteria, this.filterCriteria);
      this.dataSource.data = filter.filter(this.routes).toArray();
      this.networkRoutesService.filterOptions.next(filter.filterOptions(this.routes));
    });
  }

  get displayedColumns() {
    if (this.pageWidthService.isVeryLarge()) {
      return ["nr", "analysis", "route", "lastEdit"];
    }

    if (this.pageWidthService.isLarge()) {
      return ["nr", "analysis", "route"];
    }

    return ["nr", "analysis", "route"];
  }

}
