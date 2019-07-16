import {Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {AppService} from "../../../../app.service";
import {PageService} from "../../../../components/shared/page.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {Country} from "../../../../kpn/shared/country";
import {NetworkType} from "../../../../kpn/shared/network-type";
import {NetworkDetailsPage} from "../../../../kpn/shared/network/network-details-page";
import {Subset} from "../../../../kpn/shared/subset";
import {NetworkCacheService} from "../../../../services/network-cache.service";
import {Subscriptions} from "../../../../util/Subscriptions";
import {InterpretedTags} from "../../../../components/shared/tags/interpreted-tags";
import {I18nService} from "../../../../i18n/i18n.service";
import {Util} from "../../../../components/shared/util";

@Component({
  selector: "kpn-network-details-page",
  template: `

    <kpn-network-page-header
      [networkId]="networkId"
      selectedPage="details"
      pageTitle="Details"
      i18n-pageTitle="@@network-details.title">
    </kpn-network-page-header>
    
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
          {{countryName()}}
        </kpn-data>

        <kpn-data title="Last updated">
          <kpn-timestamp [timestamp]="response.result.attributes.lastUpdated"></kpn-timestamp>
        </kpn-data>

        <kpn-data title="Relation last updated">
          <kpn-timestamp [timestamp]="response.result.attributes.relationLastUpdated"></kpn-timestamp>
        </kpn-data>

        <kpn-data title="Tags">
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

  subset: Subset;
  networkId: string;

  response: ApiResponse<NetworkDetailsPage>;
  tags: InterpretedTags;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService,
              private networkCacheService: NetworkCacheService,
              private i18nService: I18nService) {
  }

  ngOnInit() {
    this.pageService.initNetworkPage();
    this.subscriptions.add(this.activatedRoute.params.subscribe(params => {
      this.networkId = params["networkId"];
      this.pageService.networkId = this.networkId;
      // TODO this.subset = response.result.network.attributes.country + networkType
      this.subset = new Subset(new Country("nl"), new NetworkType("rwn", "hiking"));
      this.pageService.subset = this.subset;
      this.subscriptions.add(this.appService.networkDetails(this.networkId).subscribe(response => {
        this.response = response;
        this.tags = InterpretedTags.networkTags(this.response.result.tags);
        this.networkCacheService.setNetworkSummary(this.networkId, response.result.networkSummary);
        const networkName = response.result.networkSummary.name;
        this.networkCacheService.setNetworkName(this.networkId, networkName);
      }));
    }));
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  countryName(): string {
    return this.i18nService.name("country." + Util.safeGet(() => this.response.result.attributes.country.domain));
  }
}
