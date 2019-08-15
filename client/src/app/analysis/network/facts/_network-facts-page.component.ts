import {Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {flatMap, map, tap} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {ApiResponse} from "../../../kpn/shared/api-response";
import {NetworkFactsPage} from "../../../kpn/shared/network/network-facts-page";
import {NetworkCacheService} from "../../../services/network-cache.service";
import {Subscriptions} from "../../../util/Subscriptions";

@Component({
  selector: "kpn-network-facts-page",
  template: `

    <kpn-network-page-header
      [networkId]="networkId"
      pageTitle="Facts"
      i18n-pageTitle="@@network-facts.title">
    </kpn-network-page-header>

    <div *ngIf="response">
      <div *ngIf="!page" i18n="@@network-facts.network-not-found">
        Network not found
      </div>
      <div *ngIf="page">

        <kpn-situation-on [timestamp]="response.situationOn"></kpn-situation-on>

        <div *ngIf="page.facts.isEmpty()" i18n="@@network-facts.no-facts">
          No facts
        </div>

        <kpn-items *ngIf="!page.facts.isEmpty()">
          <kpn-item *ngFor="let fact of page.facts; let i=index" [index]="i">
            <kpn-network-fact [fact]="fact"></kpn-network-fact>
          </kpn-item>
        </kpn-items>
      </div>
      <kpn-json [object]="response"></kpn-json>
    </div>
  `
})
export class NetworkFactsPageComponent implements OnInit, OnDestroy {

  networkId: number;
  response: ApiResponse<NetworkFactsPage>;
  private readonly subscriptions = new Subscriptions();

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private networkCacheService: NetworkCacheService) {
  }

  get page(): NetworkFactsPage {
    return this.response.result;
  }

  ngOnInit(): void {
    this.subscriptions.add(
      this.activatedRoute.params.pipe(
        map(params => +params["networkId"]),
        tap(networkId => this.networkId = networkId),
        flatMap(networkId => this.appService.networkFacts(networkId))
      ).subscribe(response => this.processResponse(response))
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  private processResponse(response: ApiResponse<NetworkFactsPage>) {
    this.response = response;
    if (this.page) {
      this.networkCacheService.setNetworkSummary(this.networkId, this.page.networkSummary);
      this.networkCacheService.setNetworkName(this.networkId, this.page.networkSummary.name);
    }
  }

}
