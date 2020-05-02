import {ChangeDetectionStrategy} from "@angular/core";
import {Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Observable} from "rxjs";
import {flatMap, map, tap} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {PageService} from "../../../components/shared/page.service";
import {NetworkMapPage} from "../../../kpn/api/common/network/network-map-page";
import {ApiResponse} from "../../../kpn/api/custom/api-response";
import {NetworkCacheService} from "../../../services/network-cache.service";

@Component({
  selector: "kpn-network-map-page",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

    <kpn-network-page-header
      [networkId]="networkId"
      pageName="map"
      pageTitle="Map"
      i18n-pageTitle="@@network-map.title">
    </kpn-network-page-header>

    <div *ngIf="response$ | async as response">
      <kpn-json [object]="response"></kpn-json>
    </div>
  `
})
export class NetworkMapPageComponent implements OnInit, OnDestroy {

  networkId: number;
  response$: Observable<ApiResponse<NetworkMapPage>>;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService,
              private networkCacheService: NetworkCacheService) {
    this.pageService.showFooter = false;
  }

  ngOnInit(): void {
    this.response$ = this.activatedRoute.params.pipe(
      map(params => +params["networkId"]),
      tap(networkId => this.networkId = networkId),
      flatMap(networkId => this.appService.networkMap(networkId)),
      tap(response => {
        if (response.result) {
          this.networkCacheService.setNetworkSummary(this.networkId, response.result.networkSummary);
          this.networkCacheService.setNetworkName(this.networkId, response.result.networkSummary.name);
        }
      })
    );
  }

  ngOnDestroy(): void {
    this.pageService.showFooter = true;
  }
}
