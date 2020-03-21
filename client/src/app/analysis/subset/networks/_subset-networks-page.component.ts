import {Component, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {List} from "immutable";
import {Observable} from "rxjs";
import {flatMap, map, tap} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {PageWidthService} from "../../../components/shared/page-width.service";
import {PageService} from "../../../components/shared/page.service";
import {Util} from "../../../components/shared/util";
import {NetworkAttributes} from "../../../kpn/api/common/network/network-attributes";
import {SubsetNetworksPage} from "../../../kpn/api/common/subset/subset-networks-page";
import {ApiResponse} from "../../../kpn/api/custom/api-response";
import {Subset} from "../../../kpn/api/custom/subset";
import {NetworkCacheService} from "../../../services/network-cache.service";
import {SubsetCacheService} from "../../../services/subset-cache.service";

@Component({
  selector: "kpn-subset-networks-page",
  template: `

    <kpn-subset-page-header-block
      [subset]="subset$ | async"
      pageName="networks"
      pageTitle="Networks"
      i18n-pageTitle="@@subset-networks.title">
    </kpn-subset-page-header-block>

    <div *ngIf="response$ | async as response">
      <div *ngIf="networks.isEmpty()" i18n="@@subset-networks.no-networks">
        No networks
      </div>
      <div *ngIf="!networks.isEmpty()">
        <p>
          <kpn-situation-on [timestamp]="response.situationOn"></kpn-situation-on>
        </p>

        <markdown i18n="@@subset-networks.summary">
          _There are __{{page.networkCount | integer}}__ networks, with a total of __{{page.nodeCount | integer}}__ nodes
          and __{{page.routeCount | integer}}__ routes with an overall length of __{{page.km | integer}}__ km._
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
    </div>
  `
})
export class SubsetNetworksPageComponent implements OnInit {

  subset$: Observable<Subset>;
  response$: Observable<ApiResponse<SubsetNetworksPage>>;

  page: SubsetNetworksPage;
  networks: List<NetworkAttributes>;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService,
              private pageWidthService: PageWidthService,
              private networkCacheService: NetworkCacheService,
              private subsetCacheService: SubsetCacheService) {
  }

  ngOnInit(): void {
    this.subset$ = this.activatedRoute.params.pipe(map(params => Util.subsetInRoute(params)));
    this.response$ = this.subset$.pipe(
      flatMap(subset => this.appService.subsetNetworks(subset).pipe(
        tap(response => {
          this.page = response.result;
          this.networks = response.result.networks;
          this.subsetCacheService.setSubsetInfo(subset.key(), response.result.subsetInfo);
          response.result.networks.forEach(networkAttributes => {
            this.networkCacheService.setNetworkName(networkAttributes.id, networkAttributes.name);
          });
        })
      ))
    );
  }

  isLarge(): boolean {
    return this.pageWidthService.isLarge() || this.pageWidthService.isVeryLarge();
  }

}
