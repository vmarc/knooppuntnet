import {Component, Input, OnInit, ViewChild} from "@angular/core";
import {MatTableDataSource} from "@angular/material/table";
import {List} from "immutable";
import {BehaviorSubject} from "rxjs";
import {PageWidthService} from "../../../components/shared/page-width.service";
import {PaginatorComponent} from "../../../components/shared/paginator/paginator.component";
import {NetworkRouteRow} from "../../../kpn/api/common/network/network-route-row";
import {TimeInfo} from "../../../kpn/api/common/time-info";
import {NetworkType} from "../../../kpn/api/custom/network-type";
import {NetworkRouteFilter} from "./network-route-filter";
import {NetworkRouteFilterCriteria} from "./network-route-filter-criteria";
import {NetworkRoutesService} from "./network-routes.service";

@Component({
  selector: "kpn-network-route-table",
  template: `

    <kpn-paginator
      [pageSizeOptions]="[5, 10, 20, 50, 1000]"
      [length]="routes?.size" [showFirstLastButtons]="true">
    </kpn-paginator>

    <table mat-table matSort [dataSource]="dataSource" class="kpn-spacer-above">

      <ng-container matColumnDef="nr">
        <th mat-header-cell *matHeaderCellDef mat-sort-header i18n="@@network-routes.table.nr">Nr</th>
        <td mat-cell *matCellDef="let route; let i = index">{{rowNumber(i)}}</td>
      </ng-container>

      <ng-container matColumnDef="analysis">
        <th mat-header-cell *matHeaderCellDef i18n="@@network-routes.table.analysis">Analysis</th>
        <td mat-cell *matCellDef="let route">
          <kpn-network-route-analysis [route]="route" [networkType]="networkType"></kpn-network-route-analysis>
        </td>
      </ng-container>

      <ng-container matColumnDef="route">
        <th mat-header-cell *matHeaderCellDef mat-sort-header i18n="@@network-routes.table.node">Route</th>
        <td mat-cell *matCellDef="let route">
          <kpn-link-route [routeId]="route.id" [title]="route.name"></kpn-link-route>
        </td>
      </ng-container>

      <ng-container matColumnDef="distance">
        <th mat-header-cell *matHeaderCellDef mat-sort-header i18n="@@network-routes.table.distance">Distance</th>
        <td mat-cell *matCellDef="let route">
          {{route.length + "m"}}
        </td>
      </ng-container>

      <ng-container matColumnDef="role">
        <th mat-header-cell *matHeaderCellDef mat-sort-header i18n="@@network-routes.table.role">Role</th>
        <td mat-cell *matCellDef="let route">
          {{route.role ? route.role : "-"}}
        </td>
      </ng-container>

      <ng-container matColumnDef="last-survey">
        <th mat-header-cell *matHeaderCellDef mat-sort-header i18n="@@network-routes.table.last-survey">Last survey</th>
        <td mat-cell *matCellDef="let route">
          {{route.lastSurvey ? route.lastSurvey : "-"}}
        </td>
      </ng-container>

      <ng-container matColumnDef="last-edit">
        <th mat-header-cell *matHeaderCellDef mat-sort-header i18n="@@network-routes.table.last-edit">Last edit</th>
        <td mat-cell *matCellDef="let route" class="kpn-separated">
          <kpn-day [timestamp]="route.lastUpdated"></kpn-day>
          <kpn-josm-relation [relationId]="route.id"></kpn-josm-relation>
          <kpn-osm-link-relation [relationId]="route.id"></kpn-osm-link-relation>
        </td>
      </ng-container>

      <tr mat-header-row *matHeaderRowDef="displayedColumns()"></tr>
      <tr mat-row *matRowDef="let route; columns: displayedColumns();"></tr>
    </table>
  `,
  styles: [`
    .mat-column-nr {
      width: 3rem;
    }
  `]
})
export class NetworkRouteTableComponent implements OnInit {

  @Input() timeInfo: TimeInfo;
  @Input() networkType: NetworkType;
  @Input() routes: List<NetworkRouteRow>;

  dataSource: MatTableDataSource<NetworkRouteRow>;

  @ViewChild(PaginatorComponent, {static: true}) paginator: PaginatorComponent;

  private readonly filterCriteria: BehaviorSubject<NetworkRouteFilterCriteria> = new BehaviorSubject(new NetworkRouteFilterCriteria());

  constructor(private pageWidthService: PageWidthService,
              private networkRoutesService: NetworkRoutesService) {
  }

  ngOnInit(): void {
    this.dataSource = new MatTableDataSource();
    this.dataSource.paginator = this.paginator.matPaginator;
    this.filterCriteria.subscribe(criteria => {
      const filter = new NetworkRouteFilter(this.timeInfo, criteria, this.filterCriteria);
      this.dataSource.data = filter.filter(this.routes).toArray();
      this.networkRoutesService.filterOptions.next(filter.filterOptions(this.routes));
    });
  }

  displayedColumns() {
    if (this.pageWidthService.isVeryLarge()) {
      return ["nr", "analysis", "route", "distance", "role", "last-survey", "last-edit"];
    }

    if (this.pageWidthService.isLarge()) {
      return ["nr", "analysis", "route", "distance", "role"];
    }

    return ["nr", "analysis", "route"];
  }

  rowNumber(index: number): number {
    return this.paginator.rowNumber(index);
  }
}
