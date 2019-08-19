import {Component, OnDestroy, OnInit} from "@angular/core";
import {MatDialog} from "@angular/material";
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
import {SubsetMapNetworkDialogComponent} from "./subset-map-network-dialog.component";

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
      <kpn-subset-map [networks]="networks" (networkClicked)="networkClicked($event)"></kpn-subset-map>
    </div>
  `
})
export class SubsetMapPageComponent implements OnInit, OnDestroy {

  subset: Subset;
  response: ApiResponse<SubsetNetworksPage>;
  private readonly subscriptions = new Subscriptions();

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService,
              private networkCacheService: NetworkCacheService,
              private subsetCacheService: SubsetCacheService,
              private dialog: MatDialog) {
    this.pageService.showFooter = false;
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
    this.pageService.showFooter = true;
    this.subscriptions.unsubscribe();
  }

  get page(): SubsetNetworksPage {
    return this.response.result;
  }

  get networks(): List<NetworkAttributes> {
    return this.page.networks;
  }

  networkClicked(networkId: number): void {
    const network = this.networks.find(network => network.id === networkId);
    if (network) {
      this.dialog.open(SubsetMapNetworkDialogComponent, {data: network});
    }
  }

  private processResponse(response: ApiResponse<SubsetNetworksPage>) {
    this.response = response;
    this.subsetCacheService.setSubsetInfo(this.subset.key(), this.response.result.subsetInfo);
    response.result.networks.forEach(networkAttributes => {
      this.networkCacheService.setNetworkName(networkAttributes.id, networkAttributes.name);
    });
  }

}
