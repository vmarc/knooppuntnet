import {Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {flatMap, map, tap} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {InterpretedTags} from "../../../components/shared/tags/interpreted-tags";
import {ApiResponse} from "../../../kpn/shared/api-response";
import {NetworkDetailsPage} from "../../../kpn/shared/network/network-details-page";
import {NetworkCacheService} from "../../../services/network-cache.service";
import {Subscriptions} from "../../../util/Subscriptions";

@Component({
  selector: "kpn-network-details-page",
  template: `

    <kpn-network-page-header
        [networkId]="networkId"
        pageTitle="Details"
        i18n-pageTitle="@@network-details.title">
    </kpn-network-page-header>

    <div *ngIf="response?.result">
      <div *ngIf="!page">
        <p i18n="@@network-details.network-not-found">Network not found</p>
      </div>
      <div *ngIf="page">

        <kpn-data title="Situation on" i18n-title="@@network-details.situation-on">
          <kpn-timestamp [timestamp]="response.situationOn"></kpn-timestamp>
        </kpn-data>

        <kpn-data title="Summary" i18n-title="@@network-details.summary">
          <kpn-network-summary [page]="page"></kpn-network-summary>
        </kpn-data>

        <kpn-data title="Country" i18n-title="@@network-details.country">
          <kpn-country-name [country]="page.attributes.country"></kpn-country-name>
        </kpn-data>

        <kpn-data title="Last updated" i18n-title="@@network-details.last-updated">
          <kpn-timestamp [timestamp]="page.attributes.lastUpdated"></kpn-timestamp>
        </kpn-data>

        <kpn-data title="Relation last updated" i18n-title="@@network-details.relation-last-updated">
          <kpn-timestamp [timestamp]="page.attributes.relationLastUpdated"></kpn-timestamp>
        </kpn-data>

        <kpn-data title="Tags" i18n-title="@@network-details.tags">
          <kpn-tags-table [tags]="tags"></kpn-tags-table>
        </kpn-data>
      </div>
    </div>
    <div *ngIf="response">
      <kpn-json [object]="response"></kpn-json>
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
              private networkCacheService: NetworkCacheService) {
  }

  ngOnInit() {
    this.subscriptions.add(
      this.activatedRoute.params.pipe(
        map(params => params["networkId"]),
        tap(networkId => this.networkId = networkId),
        flatMap(networkId => this.appService.networkDetails(networkId))
      ).subscribe(response => this.processResponse(response))
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  get page() {
    return this.response.result;
  }

  private processResponse(response: ApiResponse<NetworkDetailsPage>) {
    this.response = response;
    this.tags = InterpretedTags.networkTags(this.page.tags);
    this.networkCacheService.setNetworkSummary(this.networkId, this.page.networkSummary);
    this.networkCacheService.setNetworkName(this.networkId, this.page.networkSummary.name);
  }

}
