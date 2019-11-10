import {Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {flatMap, map, tap} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {ApiResponse} from "../../../kpn/api/custom/api-response";
import {NetworkDetailsPage} from "../../../kpn/api/common/network/network-details-page";
import {NetworkCacheService} from "../../../services/network-cache.service";
import {Subscriptions} from "../../../util/Subscriptions";

@Component({
  selector: "kpn-network-details-page",
  template: `

    <kpn-network-page-header
      [networkId]="networkId"
      pageTitle="Details"
      i18n-pageTitle="@@network-details.title">
    </kpn-network-page-header>

    <div *ngIf="response">
      <div *ngIf="!page">
        <p i18n="@@network-details.network-not-found">Network not found</p>
      </div>
      <div *ngIf="page">
        <kpn-network-details [response]="response"></kpn-network-details>
      </div>
      <kpn-json [object]="response"></kpn-json>
    </div>
  `
})
export class NetworkDetailsPageComponent implements OnInit, OnDestroy {

  private readonly subscriptions = new Subscriptions();

  networkId: number;
  response: ApiResponse<NetworkDetailsPage>;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private networkCacheService: NetworkCacheService) {
  }

  ngOnInit(): void {
    this.subscriptions.add(
      this.activatedRoute.params.pipe(
        map(params => +params["networkId"]),
        tap(networkId => this.networkId = networkId),
        flatMap(networkId => this.appService.networkDetails(networkId))
      ).subscribe(response => this.processResponse(response))
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  get page(): NetworkDetailsPage {
    return this.response.result;
  }

  private processResponse(response: ApiResponse<NetworkDetailsPage>) {
    this.response = response;
    if (this.page) {
      this.networkCacheService.setNetworkSummary(this.networkId, this.page.networkSummary);
      this.networkCacheService.setNetworkName(this.networkId, this.page.networkSummary.name);
    }
  }

}
