import {AfterViewInit, Component, OnDestroy, OnInit, ViewChild} from "@angular/core";
import {MatPaginator} from "@angular/material";
import {ActivatedRoute} from "@angular/router";
import {AppService} from "../../../app.service";
import {ApiResponse} from "../../../kpn/shared/api-response";
import {ChangesParameters} from "../../../kpn/shared/changes/filter/changes-parameters";
import {NetworkChangesPage} from "../../../kpn/shared/network/network-changes-page";
import {NetworkCacheService} from "../../../services/network-cache.service";
import {UserService} from "../../../services/user.service";
import {Subscriptions} from "../../../util/Subscriptions";

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
        [pageSizeOptions]="[5, 25, 50, 100, 250, 1000]"
        [showFirstLastButtons]="true">
    </mat-paginator>

    <div *ngIf="!isLoggedIn()">
      <span i18n="@@network-changes.login-required">The network history is available to registered OpenStreetMap contributors only, after</span>
      <kpn-link-login></kpn-link-login>
      .
    </div>

    <div *ngIf="response?.result">
      <div *ngIf="!response.result" i18n="@@network-changes.network-not-found">
        Network not found
      </div>
      <div *ngIf="response.result">
        <div *ngIf="response.result.changes.isEmpty()" i18n="@@network-changes.no-history">
          No history
        </div>
        <div *ngIf="!response.result.changes.isEmpty()">
          <kpn-items>
            <kpn-item *ngFor="let networkChangeInfo of response.result.changes; let i=index" [index]="rowIndex(i)">
              <kpn-network-change [networkChangeInfo]="networkChangeInfo"></kpn-network-change>
            </kpn-item>
          </kpn-items>
        </div>
      </div>
      <kpn-json [object]="response"></kpn-json>
    </div>
  `
})
export class NetworkChangesPageComponent implements OnInit, AfterViewInit, OnDestroy {

  private readonly subscriptions = new Subscriptions();

  networkId: string;
  response: ApiResponse<NetworkChangesPage>;
  parameters = new ChangesParameters(null, null, null, null, null, null, null, 5, 0, false);

  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private networkCacheService: NetworkCacheService,
              private userService: UserService) {
  }

  ngOnInit() {
    this.subscriptions.add(
      this.activatedRoute.params.subscribe(params => this.networkId = params["networkId"])
    );
  }

  ngAfterViewInit() {
    this.subscriptions.add(
      this.paginator.page.subscribe(event => this.reload())
    );
    this.reload();
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  isLoggedIn(): boolean {
    return this.userService.isLoggedIn();
  }

  rowIndex(index: number): number {
    return this.parameters.pageIndex * this.parameters.itemsPerPage + index;
  }

  private reload() {
    this.updateParameters();
    this.subscriptions.add(
      this.appService.networkChanges(this.networkId, this.parameters).subscribe(response => {
        this.processResponse(response);
        this.paginator.length = this.response.result.totalCount;
      })
    );
  }

  private processResponse(response: ApiResponse<NetworkChangesPage>) {
    this.response = response;
    this.networkCacheService.setNetworkSummary(this.networkId, this.response.result.network);
    this.networkCacheService.setNetworkName(this.networkId, this.response.result.network.name);
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
