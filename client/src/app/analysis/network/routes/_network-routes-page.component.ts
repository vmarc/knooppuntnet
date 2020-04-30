import {Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {flatMap, map, tap} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {ApiResponse} from "../../../kpn/api/custom/api-response";
import {NetworkRoutesPage} from "../../../kpn/api/common/network/network-routes-page";
import {NetworkCacheService} from "../../../services/network-cache.service";
import {Subscriptions} from "../../../util/Subscriptions";

@Component({
  selector: "kpn-network-routes-page",
  template: `

    <kpn-network-page-header
      [networkId]="networkId"
      pageName="routes"
      pageTitle="Routes"
      i18n-pageTitle="@@network-routes.title">
    </kpn-network-page-header>

    <div *ngIf="response" class="kpn-spacer-above">
      <div *ngIf="!page">
        <p i18n="@@network-routes.network-not-found">Network not found</p>
      </div>
      <div *ngIf="page">
        <div *ngIf="page.routes.isEmpty()" i18n="@@network-routes.no-routes">
          No network routes in network
        </div>
        <kpn-network-route-table
          *ngIf="!page.routes.isEmpty()"
          [timeInfo]="page.timeInfo"
          [networkType]="page.networkType"
          [routes]="page.routes">
        </kpn-network-route-table>
      </div>
      <kpn-json [object]="response"></kpn-json>
    </div>
  `
})
export class NetworkRoutesPageComponent implements OnInit, OnDestroy {

  private readonly subscriptions = new Subscriptions();

  networkId: number;
  response: ApiResponse<NetworkRoutesPage>;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private networkCacheService: NetworkCacheService) {
  }

  ngOnInit(): void {
    this.subscriptions.add(
      this.activatedRoute.params.pipe(
        map(params => +params["networkId"]),
        tap(networkId => this.networkId = networkId),
        flatMap(networkId => this.appService.networkRoutes(networkId))
      ).subscribe(response => this.processResponse(response))
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  get page(): NetworkRoutesPage {
    return this.response.result;
  }

  private processResponse(response: ApiResponse<NetworkRoutesPage>) {
    this.response = response;
    if (this.page) {
      this.networkCacheService.setNetworkSummary(this.networkId, this.page.networkSummary);
      this.networkCacheService.setNetworkName(this.networkId, this.page.networkSummary.name);
    }
  }

}
