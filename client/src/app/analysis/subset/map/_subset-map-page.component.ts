import {Component, OnInit} from "@angular/core";
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
  selector: "kpn-subset-map-page",
  template: `

    <kpn-subset-page-header-block
      [subset]="subset"
      pageName="map"
      pageTitle="Map"
      i18n-pageTitle="@@subset-map.title">
    </kpn-subset-page-header-block>

    <div *ngIf="response">
      <p>TODO</p>
      <kpn-json [object]="response"></kpn-json>
    </div>
  `,
  styles: []
})
export class SubsetMapPageComponent implements OnInit {

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

  get page(): SubsetNetworksPage {
    return this.response.result;
  }

  get networks(): List<NetworkAttributes> {
    return this.page.networks;
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

  private processResponse(response: ApiResponse<SubsetNetworksPage>) {
    this.response = response;
    this.subsetCacheService.setSubsetInfo(this.subset.key(), this.response.result.subsetInfo);
    response.result.networks.forEach(networkAttributes => {
      this.networkCacheService.setNetworkName(networkAttributes.id, networkAttributes.name);
    });
  }

}
