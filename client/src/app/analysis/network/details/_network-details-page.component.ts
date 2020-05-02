import {ChangeDetectionStrategy} from "@angular/core";
import {Component, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Observable} from "rxjs";
import {flatMap, map, tap} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {NetworkDetailsPage} from "../../../kpn/api/common/network/network-details-page";
import {ApiResponse} from "../../../kpn/api/custom/api-response";
import {NetworkCacheService} from "../../../services/network-cache.service";

@Component({
  selector: "kpn-network-details-page",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

    <kpn-network-page-header
      [networkId]="networkId"
      pageName="details"
      pageTitle="Details"
      i18n-pageTitle="@@network-details.title">
    </kpn-network-page-header>

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <div *ngIf="!response.result">
        <p i18n="@@network-details.network-not-found">Network not found</p>
      </div>
      <div *ngIf="response.result">
        <kpn-network-details [response]="response"></kpn-network-details>
      </div>
      <kpn-json [object]="response"></kpn-json>
    </div>
  `
})
export class NetworkDetailsPageComponent implements OnInit {

  networkId: number;
  response$: Observable<ApiResponse<NetworkDetailsPage>>;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private networkCacheService: NetworkCacheService) {
  }

  ngOnInit(): void {
    this.response$ = this.activatedRoute.params.pipe(
      map(params => +params["networkId"]),
      tap(networkId => this.networkId = networkId),
      flatMap(networkId => this.appService.networkDetails(networkId)),
      tap(response => {
        if (response.result) {
          this.networkCacheService.setNetworkSummary(this.networkId, response.result.networkSummary);
          this.networkCacheService.setNetworkName(this.networkId, response.result.networkSummary.name);
        }
      })
    );
  }
}
