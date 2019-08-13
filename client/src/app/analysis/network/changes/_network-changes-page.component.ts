import {Component, OnDestroy, OnInit} from "@angular/core";
import {MatSlideToggleChange, PageEvent} from "@angular/material";
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

    <div *ngIf="!isLoggedIn()">
      <span i18n="@@network-changes.login-required">The network history is available to registered OpenStreetMap contributors only, after</span>
      <kpn-link-login></kpn-link-login>
      .
    </div>

    <div *ngIf="response">
      <div *ngIf="!page" i18n="@@network-changes.network-not-found">
        Network not found
      </div>
      <div *ngIf="page">

        <kpn-situation-on [timestamp]="response.situationOn"></kpn-situation-on>

        <mat-slide-toggle [checked]="parameters.impact" (change)="impactChanged($event)">Impact</mat-slide-toggle>

        <div *ngIf="page.changes.isEmpty()" i18n="@@network-changes.no-history">
          No history
        </div>
        <div *ngIf="!page.changes.isEmpty()">

          <kpn-changes-top-paginator 
              [parameters]="parameters" 
              [totalCount]="page.totalCount" 
              (pageIndexChanged)="pageIndexChanged($event)">
          </kpn-changes-top-paginator>
          
          <kpn-items>
            <kpn-item *ngFor="let networkChangeInfo of page.changes; let i=index" [index]="rowIndex(i)">
              <kpn-network-change-set [networkChangeInfo]="networkChangeInfo"></kpn-network-change-set>
            </kpn-item>
          </kpn-items>

          <kpn-changes-bottom-paginator
              [parameters]="parameters"
              [totalCount]="page.totalCount"
              (pageIndexChanged)="pageIndexChanged($event)">
          </kpn-changes-bottom-paginator>

        </div>
      </div>
      <kpn-json [object]="response"></kpn-json>
    </div>
  `
})
export class NetworkChangesPageComponent implements OnInit, OnDestroy {

  private readonly subscriptions = new Subscriptions();

  networkId: number;
  response: ApiResponse<NetworkChangesPage>;
  parameters = new ChangesParameters(null, null, null, null, null, null, null, 5, 0, true);

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private networkCacheService: NetworkCacheService,
              private userService: UserService) {
  }

  ngOnInit() {
    this.subscriptions.add(
      this.activatedRoute.params.subscribe(params => {
        this.networkId = +params["networkId"];
        this.parameters = {...this.parameters, networkId: this.networkId};
        this.reload();
      })
    );
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

  impactChanged(event: MatSlideToggleChange) {
    this.parameters = {
      ...this.parameters,
      impact: event.checked,
      pageIndex: 0
    };
    this.reload();
  }

  pageIndexChanged(pageIndex: number) {
    this.parameters = {...this.parameters, pageIndex: pageIndex};
    this.reload();
  }

  get page(): NetworkChangesPage {
    return this.response.result;
  }

  private reload() {
    this.appService.networkChanges(this.networkId, this.parameters).subscribe(response => {
      this.processResponse(response);
    });
  }

  private processResponse(response: ApiResponse<NetworkChangesPage>) {
    this.response = response;
    if (this.page) {
      this.networkCacheService.setNetworkSummary(this.networkId, this.page.network);
      this.networkCacheService.setNetworkName(this.networkId, this.page.network.name);
    }
  }

}
