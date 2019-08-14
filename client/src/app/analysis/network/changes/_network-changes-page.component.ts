import {Component, OnDestroy, OnInit} from "@angular/core";
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

        <kpn-changes [(parameters)]="parameters" [totalCount]="page.totalCount">
          <kpn-items>
            <kpn-item *ngFor="let networkChangeInfo of page.changes; let i=index" [index]="rowIndex(i)">
              <kpn-network-change-set [networkChangeInfo]="networkChangeInfo"></kpn-network-change-set>
            </kpn-item>
          </kpn-items>
        </kpn-changes>

      </div>
      <kpn-json [object]="response"></kpn-json>
    </div>
  `
})
export class NetworkChangesPageComponent implements OnInit, OnDestroy {

  private readonly subscriptions = new Subscriptions();

  networkId: number;
  response: ApiResponse<NetworkChangesPage>;
  _parameters = new ChangesParameters(null, null, null, null, null, null, null, 5, 0, true);

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
      })
    );
  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }

  isLoggedIn(): boolean {
    return this.userService.isLoggedIn();
  }

  rowIndex(index: number): number {
    return this.parameters.pageIndex * this.parameters.itemsPerPage + index;
  }

  get parameters(): ChangesParameters {
    return this._parameters;
  }

  set parameters(parameters: ChangesParameters) {
    this._parameters = parameters;
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
