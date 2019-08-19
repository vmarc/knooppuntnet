import {Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {List} from "immutable";
import {flatMap, map, tap} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {PageWidthService} from "../../../components/shared/page-width.service";
import {PageService} from "../../../components/shared/page.service";
import {Util} from "../../../components/shared/util";
import {ApiResponse} from "../../../kpn/shared/api-response";
import {NetworkAttributes} from "../../../kpn/shared/network/network-attributes";
import {Subset} from "../../../kpn/shared/subset";
import {SubsetNetworksPage} from "../../../kpn/shared/subset/subset-networks-page";
import {NetworkCacheService} from "../../../services/network-cache.service";
import {SubsetCacheService} from "../../../services/subset-cache.service";
import {Subscriptions} from "../../../util/Subscriptions";

@Component({
  selector: "kpn-subset-networks-page",
  template: `

    <kpn-subset-page-header-block
      [subset]="subset"
      pageName="networks"
      pageTitle="Networks"
      i18n-pageTitle="@@subset-networks.title">
    </kpn-subset-page-header-block>

    <div *ngIf="response">
      <div *ngIf="networks.isEmpty()" i18n="@@subset-networks.no-networks">
        No networks
      </div>
      <div *ngIf="!networks.isEmpty()">
        <p>
          <kpn-situation-on [timestamp]="response.situationOn"></kpn-situation-on>
        </p>
        
        <markdown i18n="@@subset-networks.summary">
          _There are __{{page.networkCount}}__ networks, with a total of __{{page.nodeCount}}__ nodes
          and __{{page.routeCount}}__ routes with an overall length of __{{page.km}}__ km._
        </markdown>
        
        <kpn-subset-network-list
          *ngIf="!isLarge()"
          [networks]="networks">
        </kpn-subset-network-list>
        <kpn-subset-network-table
          *ngIf="isLarge()"
          [networks]="networks">
        </kpn-subset-network-table>
      </div>
      <kpn-json [object]="response"></kpn-json>
    </div>
  `
})
export class SubsetNetworksPageComponent implements OnInit, OnDestroy {

  subset: Subset;
  response: ApiResponse<SubsetNetworksPage>;
  private readonly subscriptions = new Subscriptions();

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService,
              private pageWidthService: PageWidthService,
              private networkCacheService: NetworkCacheService,
              private subsetCacheService: SubsetCacheService) {
  }

  ngOnInit(): void {
    this.subscriptions.add(
      this.activatedRoute.params.pipe(
        map(params => Util.subsetInRoute(params)),
        tap(subset => this.subset = subset),
        flatMap(subset => this.appService.subsetNetworks(subset))
      ).subscribe(response => this.processResponse(response))
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  get page(): SubsetNetworksPage {
    return this.response.result;
  }

  get networks(): List<NetworkAttributes> {
    return this.page.networks;
  }

  isLarge(): boolean {
    return this.pageWidthService.isLarge() || this.pageWidthService.isVeryLarge();
  }

  private processResponse(response: ApiResponse<SubsetNetworksPage>) {
    this.response = response;
    this.subsetCacheService.setSubsetInfo(this.subset.key(), this.response.result.subsetInfo);
    response.result.networks.forEach(networkAttributes => {
      this.networkCacheService.setNetworkName(networkAttributes.id, networkAttributes.name);
    });
  }

}
