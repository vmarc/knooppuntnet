import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs";
import {AppService} from "../../../../app.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {NetworkRoutesPage} from "../../../../kpn/shared/network/network-routes-page";
import {Subset} from "../../../../kpn/shared/subset";
import {Country} from "../../../../kpn/shared/country";
import {NetworkType} from "../../../../kpn/shared/network-type";
import {PageService} from "../../../../components/shared/page.service";
import {NetworkCacheService} from "../../../../services/network-cache.service";

@Component({
  selector: 'kpn-network-routes-page',
  template: `
    <kpn-network-page-header [networkId]="networkId" selectedPage="routes"></kpn-network-page-header>
    <div *ngIf="response">
      <json [object]="response"></json>
    </div>
  `
})
export class NetworkRoutesPageComponent implements OnInit, OnDestroy {

  subset: Subset;
  networkId: string;

  response: ApiResponse<NetworkRoutesPage>;
  paramsSubscription: Subscription;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService,
              private networkCacheService: NetworkCacheService) {
  }

  ngOnInit() {
    this.networkCacheService.updatePageTitle("routes", this.networkId);
    this.pageService.initNetworkPage();
    this.paramsSubscription = this.activatedRoute.params.subscribe(params => {
      this.networkId = params['networkId'];
      this.pageService.networkId = this.networkId;
      // TODO this.subset = response.result.network.attributes.country + networkType
      this.subset = new Subset(new Country("nl"), new NetworkType("rwn"));
      this.pageService.subset = this.subset;
      this.appService.networkRoutes(this.networkId).subscribe(response => {
        this.response = response;
        this.networkCacheService.setNetworkName(this.networkId, response.result.networkSummary.name);
        this.networkCacheService.setNetworkSummary(this.networkId, response.result.networkSummary);
        this.networkCacheService.updatePageTitle("routes", this.networkId);
      });
    });
  }

  ngOnDestroy() {
    this.paramsSubscription.unsubscribe();
  }

}
