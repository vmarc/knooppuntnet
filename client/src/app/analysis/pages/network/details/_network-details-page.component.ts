import {Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {AppService} from "../../../../app.service";
import {PageService} from "../../../../components/shared/page.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {NetworkDetailsPage} from "../../../../kpn/shared/network/network-details-page";
import {NetworkCacheService} from "../../../../services/network-cache.service";
import {Subscriptions} from "../../../../util/Subscriptions";
import {InterpretedTags} from "../../../../components/shared/tags/interpreted-tags";
import {flatMap, map, tap} from "rxjs/operators";

@Component({
  selector: "kpn-network-details-page",
  template: `

    <kpn-network-page-header
      [networkId]="networkId"
      pageTitle="Details"
      i18n-pageTitle="@@network-details.title">
    </kpn-network-page-header>

    <div *ngIf="response?.result">
      <div *ngIf="!response.result">
        <p i18n="@@network-details.network-not-found">Network not found</p>
      </div>
      <div *ngIf="response.result">

        <kpn-data title="Situation on" i18n-title="@@network-details.situation-on">
          <kpn-timestamp [timestamp]="response.situationOn"></kpn-timestamp>
        </kpn-data>

        <kpn-data title="Summary" i18n-title="@@network-details.summary">
          <p>
            {{response.result.attributes.km}} km
          </p>
          <p>
            {{response.result.networkSummary.nodeCount}}
            <ng-container i18n="@@network-details.nodes">nodes</ng-container>
          </p>
          <p>
            {{response.result.networkSummary.routeCount}}
            <ng-container i18n="@@network-details.routes">routes</ng-container>
          </p>
          <p>
            <kpn-network-type [networkType]="response.result.attributes.networkType"></kpn-network-type>
          </p>
        </kpn-data>

        <kpn-data title="Country" i18n-title="@@network-details.country">
          <kpn-country-name [country]="response.result.attributes.country"></kpn-country-name>
        </kpn-data>

        <kpn-data title="Last updated" i18n-title="@@network-details.last-updated">
          <kpn-timestamp [timestamp]="response.result.attributes.lastUpdated"></kpn-timestamp>
        </kpn-data>

        <kpn-data title="Relation last updated" i18n-title="@@network-details.relation-last-updated">
          <kpn-timestamp [timestamp]="response.result.attributes.relationLastUpdated"></kpn-timestamp>
        </kpn-data>

        <kpn-data title="Tags" i18n-title="@@network-details.tags">
          <kpn-tags-table [tags]="tags"></kpn-tags-table>
        </kpn-data>
      </div>
    </div>
    <div *ngIf="response">
      <json [object]="response"></json>
    </div>
  `
})
export class NetworkDetailsPageComponent implements OnInit, OnDestroy {

  private readonly subscriptions = new Subscriptions();

  networkId: string;
  tags: InterpretedTags;
  response: ApiResponse<NetworkDetailsPage>;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService,
              private networkCacheService: NetworkCacheService) {
  }

  ngOnInit() {
    this.pageService.initNetworkPage();
    this.subscriptions.add(
      this.activatedRoute.params.pipe(
        map(params => params["networkId"]),
        tap(networkId => this.processNetworkId(networkId)),
        flatMap(networkId => this.appService.networkDetails(networkId))
      ).subscribe(response => this.processResponse(response))
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  private processNetworkId(networkId: string) {
    this.networkId = networkId;
    this.pageService.networkId = networkId;
  }

  private processResponse(response: ApiResponse<NetworkDetailsPage>) {
    this.response = response;
    this.tags = InterpretedTags.networkTags(response.result.tags);
    this.networkCacheService.setNetworkSummary(this.networkId, response.result.networkSummary);
    const networkName = response.result.networkSummary.name;
    this.networkCacheService.setNetworkName(this.networkId, networkName);
  }

}
