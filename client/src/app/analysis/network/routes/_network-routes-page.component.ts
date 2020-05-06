import {ChangeDetectionStrategy} from "@angular/core";
import {Component, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Observable} from "rxjs";
import {shareReplay} from "rxjs/operators";
import {flatMap, map, tap} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {NetworkRoutesPage} from "../../../kpn/api/common/network/network-routes-page";
import {ApiResponse} from "../../../kpn/api/custom/api-response";
import {NetworkService} from "../network.service";

@Component({
  selector: "kpn-network-routes-page",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

    <kpn-network-page-header
      [networkId]="networkId$ | async"
      pageName="routes"
      pageTitle="Routes"
      i18n-pageTitle="@@network-routes.title">
    </kpn-network-page-header>

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <div *ngIf="!response.result">
        <p i18n="@@network-routes.network-not-found">Network not found</p>
      </div>
      <div *ngIf="response.result">
        <p>
          <kpn-situation-on [timestamp]="response.situationOn"></kpn-situation-on>
        </p>
        <div *ngIf="response.result.routes.isEmpty()" i18n="@@network-routes.no-routes">
          No network routes in network
        </div>
        <kpn-network-route-table
          *ngIf="!response.result.routes.isEmpty()"
          [timeInfo]="response.result.timeInfo"
          [networkType]="response.result.networkType"
          [routes]="response.result.routes">
        </kpn-network-route-table>
      </div>
      <kpn-json [object]="response"></kpn-json>
    </div>
  `
})
export class NetworkRoutesPageComponent implements OnInit {

  networkId$: Observable<number>;
  response$: Observable<ApiResponse<NetworkRoutesPage>>;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private networkService: NetworkService) {
  }

  ngOnInit(): void {

    this.networkId$ = this.activatedRoute.params.pipe(
      map(params => +params["networkId"]),
      tap(networkId => this.networkService.init(networkId)),
      shareReplay()
    );

    this.response$ = this.networkId$.pipe(
      flatMap(networkId =>
        this.appService.networkRoutes(networkId).pipe(
          tap(response => {
            if (response.result) {
              this.networkService.update(networkId, response.result.networkSummary);
            }
          })
        )
      )
    );
  }
}
