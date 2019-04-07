import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs";
import {AppService} from "../../../../app.service";
import {PageService} from "../../../../components/shared/page.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {Country} from "../../../../kpn/shared/country";
import {NetworkType} from "../../../../kpn/shared/network-type";
import {NetworkNodesPage} from "../../../../kpn/shared/network/network-nodes-page";
import {Subset} from "../../../../kpn/shared/subset";
import {NetworkCacheService} from "../../../../services/network-cache.service";

@Component({
  selector: 'kpn-network-nodes-page',
  template: `
    <kpn-network-page-header [networkId]="networkId" selectedPage="nodes"></kpn-network-page-header>
    <div *ngIf="response">
      <network-node-table [nodes]="response.result.nodes"></network-node-table>
      <json [object]="response"></json>
    </div>
  `
})
export class NetworkNodesPageComponent implements OnInit, OnDestroy {

  subset: Subset;
  networkId: string;

  response: ApiResponse<NetworkNodesPage>;
  paramsSubscription: Subscription;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService,
              private networkCacheService: NetworkCacheService) {
  }

  ngOnInit() {
    this.networkCacheService.updatePageTitle("nodes", this.networkId);
    this.pageService.initNetworkPage();
    this.paramsSubscription = this.activatedRoute.params.subscribe(params => {
      this.networkId = params['networkId'];
      this.pageService.networkId = this.networkId;
      // TODO this.subset = response.result.network.attributes.country + networkType
      this.subset = new Subset(new Country("nl"), new NetworkType("rwn"));
      this.pageService.subset = this.subset;
      this.appService.networkNodes(this.networkId).subscribe(response => {
        this.response = response;
        this.networkCacheService.setNetworkName(this.networkId, response.result.networkSummary.name);
        this.networkCacheService.setNetworkSummary(this.networkId, response.result.networkSummary);
        this.networkCacheService.updatePageTitle("nodes", this.networkId);
      });
    });
  }

  ngOnDestroy() {
    this.paramsSubscription.unsubscribe();
  }

}
