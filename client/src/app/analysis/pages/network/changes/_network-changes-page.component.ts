import {AfterViewInit, Component, OnDestroy, ViewChild} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {AppService} from "../../../../app.service";
import {PageService} from "../../../../components/shared/page.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {NetworkChangesPage} from "../../../../kpn/shared/network/network-changes-page";
import {Subset} from "../../../../kpn/shared/subset";
import {NetworkCacheService} from "../../../../services/network-cache.service";
import {Subscriptions} from "../../../../util/Subscriptions";
import {map, tap} from "rxjs/operators";
import {MatPaginator, MatTableDataSource} from "@angular/material";
import {ChangesParameters} from "../../../../kpn/shared/changes/filter/changes-parameters";
import {NetworkChangeInfo} from "../../../../kpn/shared/changes/details/network-change-info";

@Component({
  selector: "kpn-network-changes-page",
  template: `
    <kpn-network-page-header
        [networkId]="networkId"
        pageTitle="Changes"
        i18n-pageTitle="@@network-changes.title">
    </kpn-network-page-header>

    <p *ngIf="response">
      <kpn-situation-on [timestamp]="response.situationOn"></kpn-situation-on>
    </p>

    <mat-paginator
        [pageIndex]="0"
        [pageSize]="parameters.itemsPerPage"
        [pageSizeOptions]="[25, 50, 100, 250, 1000]">
    </mat-paginator>

    <div *ngIf="response">

      <mat-divider></mat-divider>
      <table mat-table [dataSource]="dataSource" class="kpn-columns-table">

        <ng-container matColumnDef="rowNumber">
          <td mat-cell *matCellDef="let networkChangeInfo; let i = index">
            {{rowNumber(i)}}
          </td>
        </ng-container>

        <ng-container matColumnDef="networkChangeInfo">
          <td mat-cell *matCellDef="let networkChangeInfo">
            <kpn-network-change [networkChangeInfo]="networkChangeInfo"></kpn-network-change>
          </td>
        </ng-container>

        <tr mat-row *matRowDef="let networkChangeInfo; columns: displayedColumns;"></tr>

      </table>
      <kpn-json [object]="response"></kpn-json>
    </div>

    <kpn-json [object]="response"></kpn-json>
  `
})
export class NetworkChangesPageComponent implements AfterViewInit, OnDestroy {

  private readonly subscriptions = new Subscriptions();

  subset: Subset;
  networkId: string;

  response: ApiResponse<NetworkChangesPage>;

  @ViewChild(MatPaginator) paginator: MatPaginator;

  dataSource: MatTableDataSource<NetworkChangeInfo>;

  displayedColumns = ["rowNumber", "networkChangeInfo"];

  parameters = new ChangesParameters(null, null, null, null, null, null, null, 15, 0, false);

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService,
              private networkCacheService: NetworkCacheService) {
  }

  ngAfterViewInit() {
    this.pageService.initNetworkPage();
    this.dataSource = new MatTableDataSource([]);
    this.subscriptions.add(this.paginator.page.subscribe(event => this.reload()));
    this.subscriptions.add(
      this.activatedRoute.params.pipe(
        map(params => params["networkId"]),
        tap(networkId => this.processNetworkId(networkId)),
      ).subscribe(networkId => {
        this.reload();
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  private processNetworkId(networkId: string) {
    this.networkId = networkId;
    this.pageService.networkId = networkId;
  }

  private processResponse(response: ApiResponse<NetworkChangesPage>) {
    this.response = response;
    // TODO this.networkCacheService.setNetworkSummary(this.networkId, response.result.networkSummary);
    const networkName = response.result.network.attributes.name;
    this.networkCacheService.setNetworkName(this.networkId, networkName);
  }

  private reload() {
    this.updateParameters();
    this.subscriptions.add(
      this.appService.networkChanges(this.networkId /*this.parameters*/).subscribe(response => {
        this.processResponse(response);
        this.dataSource.data = this.response.result.changes.toArray();
        this.paginator.length = this.response.result.totalCount;
      })
    );
  }

  rowNumber(index: number): number {
    return this.parameters.pageIndex * this.parameters.itemsPerPage + index + 1;
  }

  private updateParameters() {
    this.parameters = new ChangesParameters(
      this.parameters.subset,
      this.parameters.networkId,
      this.parameters.routeId,
      this.parameters.nodeId,
      this.parameters.year,
      this.parameters.month,
      this.parameters.day,
      this.paginator.pageSize,
      this.paginator.pageIndex,
      this.parameters.impact
    );
  }

}
