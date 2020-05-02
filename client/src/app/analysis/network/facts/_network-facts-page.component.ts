import {ChangeDetectionStrategy} from "@angular/core";
import {Component, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Observable} from "rxjs";
import {flatMap, map, tap} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {NetworkFactsPage} from "../../../kpn/api/common/network/network-facts-page";
import {ApiResponse} from "../../../kpn/api/custom/api-response";
import {NetworkCacheService} from "../../../services/network-cache.service";

@Component({
  selector: "kpn-network-facts-page",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

    <kpn-network-page-header
      [networkId]="networkId"
      pageName="facts"
      pageTitle="Facts"
      i18n-pageTitle="@@network-facts.title">
    </kpn-network-page-header>

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <div *ngIf="!response.result" i18n="@@network-facts.network-not-found">
        Network not found
      </div>
      <div *ngIf="response.result">

        <kpn-situation-on [timestamp]="response.situationOn"></kpn-situation-on>

        <p *ngIf="response.result.facts.isEmpty()" i18n="@@network-facts.no-facts">
          No facts
        </p>

        <kpn-items *ngIf="!response.result.facts.isEmpty()">
          <kpn-item *ngFor="let fact of response.result.facts; let i=index" [index]="i">
            <kpn-network-fact [fact]="fact"></kpn-network-fact>
          </kpn-item>
        </kpn-items>
      </div>
      <kpn-json [object]="response"></kpn-json>
    </div>
  `
})
export class NetworkFactsPageComponent implements OnInit {

  networkId: number;
  response$: Observable<ApiResponse<NetworkFactsPage>>;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private networkCacheService: NetworkCacheService) {
  }

  ngOnInit(): void {
    this.response$ = this.activatedRoute.params.pipe(
      map(params => +params["networkId"]),
      tap(networkId => this.networkId = networkId),
      flatMap(networkId => this.appService.networkFacts(networkId)),
      tap(response => {
        if (response.result) {
          this.networkCacheService.setNetworkSummary(this.networkId, response.result.networkSummary);
          this.networkCacheService.setNetworkName(this.networkId, response.result.networkSummary.name);
        }
      })
    );
  }
}
