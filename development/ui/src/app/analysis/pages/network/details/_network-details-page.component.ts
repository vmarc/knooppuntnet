import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs";
import {AppService} from "../../../../app.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {NetworkDetailsPage} from "../../../../kpn/shared/network/network-details-page";
import {Subset} from "../../../../kpn/shared/subset";
import {Country} from "../../../../kpn/shared/country";
import {NetworkType} from "../../../../kpn/shared/network-type";
import {PageService} from "../../../../components/shared/page.service";
import {NetworkCacheService} from "../../../../services/network-cache.service";
import {PageTitleBuilder} from "../../../../components/shared/page-title-builder";

@Component({
  selector: 'kpn-network-details-page',
  template: `
    
    <kpn-network-page-header [networkId]="networkId" selectedPage="details"></kpn-network-page-header>
    
    <div *ngIf="response?.result">
      <div *ngIf="!response.result">
        <p>Network not found</p>
      </div>
      <div *ngIf="response.result">

        <kpn-data title="Situation on"> <!-- "Situatie op" -->
          <kpn-timestamp [timestamp]="response.situationOn"></kpn-timestamp>
        </kpn-data>

        <kpn-data title="Summary"> <!-- "Samenvatting" -->
          <p>
            {{response.result.attributes.km}} km
          </p>
          <p>
            {{response.result.networkSummary.nodeCount}} nodes
          </p>
          <p>
            {{response.result.networkSummary.routeCount}} routes
          </p>
          <p>
            <kpn-network-type [networkType]="response.result.attributes.networkType"></kpn-network-type>
          </p>
        </kpn-data>

        <kpn-data title="Country">
          <kpn-country-name [country]="response.result.attributes.country"></kpn-country-name>
        </kpn-data>

        <kpn-data title="Last updated">
          <kpn-timestamp [timestamp]="response.result.attributes.lastUpdated"></kpn-timestamp>
        </kpn-data>

        <kpn-data title="Relation last updated">
          <kpn-timestamp [timestamp]="response.result.attributes.relationLastUpdated"></kpn-timestamp>
        </kpn-data>

        <kpn-data title="Tags">
          <tags [tags]="response.result.tags"></tags>
        </kpn-data>
      </div>
    </div>
    <div *ngIf="response">
      <json [object]="response"></json>
    </div>
  `
})
export class NetworkDetailsPageComponent implements OnInit, OnDestroy {

  subset: Subset;
  networkId: string;

  response: ApiResponse<NetworkDetailsPage>;
  paramsSubscription: Subscription;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService,
              private networkCacheService: NetworkCacheService) {
  }

  ngOnInit() {
    this.updatePageTitle();
    this.pageService.initNetworkPage();
    this.paramsSubscription = this.activatedRoute.params.subscribe(params => {
      this.networkId = params['networkId'];
      this.pageService.networkId = this.networkId;
      // TODO this.subset = response.result.network.attributes.country + networkType
      this.subset = new Subset(new Country("nl"), new NetworkType("rwn"));
      this.pageService.subset = this.subset;
      this.appService.networkDetails(this.networkId).subscribe(response => {
        this.response = response;
        this.networkCacheService.setNetworkName(this.networkId, response.result.networkSummary.name);
        this.networkCacheService.setNetworkSummary(this.networkId, response.result.networkSummary);
        this.updatePageTitle();
      });
    });
  }

  isNetworkNameKnown(): boolean {
    return this.networkId && this.networkCacheService.getNetworkName(this.networkId) !== undefined;
  }

  networkName(): string {
    return this.networkCacheService.getNetworkName(this.networkId);
  }

  ngOnDestroy() {
    this.paramsSubscription.unsubscribe();
  }

  updatePageTitle() {
    if (this.isNetworkNameKnown()) {
      PageTitleBuilder.setNetworkPageTitle("details", this.networkName());
    }
    else {
      PageTitleBuilder.setTitle("details");
    }
  }

}
