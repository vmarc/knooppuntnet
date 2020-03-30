import {Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {flatMap, map, tap} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {PageService} from "../../../components/shared/page.service";
import {ApiResponse} from "../../../kpn/api/custom/api-response";
import {NetworkMapPage} from "../../../kpn/api/common/network/network-map-page";
import {NetworkCacheService} from "../../../services/network-cache.service";
import {Subscriptions} from "../../../util/Subscriptions";

@Component({
  selector: "kpn-network-map-page",
  template: `

    <kpn-network-page-header
      [networkId]="networkId"
      pageName="map"
      pageTitle="Map"
      i18n-pageTitle="@@network-map.title">
    </kpn-network-page-header>

    <div *ngIf="response">
      <kpn-json [object]="response"></kpn-json>
    </div>
  `
})
export class NetworkMapPageComponent implements OnInit, OnDestroy {

  networkId: number;
  response: ApiResponse<NetworkMapPage>;
  private readonly subscriptions = new Subscriptions();

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService,
              private networkCacheService: NetworkCacheService) {
    this.pageService.showFooter = false;
  }

  get page(): NetworkMapPage {
    return this.response.result;
  }

  ngOnInit(): void {
    this.subscriptions.add(
      this.activatedRoute.params.pipe(
        map(params => +params["networkId"]),
        tap(networkId => this.networkId = networkId),
        flatMap(networkId => this.appService.networkMap(networkId))
      ).subscribe(response => this.processResponse(response))
    );
  }

  ngOnDestroy(): void {
    this.pageService.showFooter = true;
    this.subscriptions.unsubscribe();
  }

  private processResponse(response: ApiResponse<NetworkMapPage>): void {
    this.response = response;
    if (this.page) {
      this.networkCacheService.setNetworkSummary(this.networkId, this.page.networkSummary);
      this.networkCacheService.setNetworkName(this.networkId, this.page.networkSummary.name);
    }
  }

}
