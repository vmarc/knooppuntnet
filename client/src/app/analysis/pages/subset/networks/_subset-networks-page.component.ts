import {Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {AppService} from "../../../../app.service";
import {PageService} from "../../../../components/shared/page.service";
import {Util} from "../../../../components/shared/util";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {Subset} from "../../../../kpn/shared/subset";
import {SubsetNetworksPage} from "../../../../kpn/shared/subset/subset-networks-page";
import {NetworkCacheService} from "../../../../services/network-cache.service";
import {SubsetCacheService} from "../../../../services/subset-cache.service";
import {Subscriptions} from "../../../../util/Subscriptions";

@Component({
  selector: "kpn-subset-networks-page",
  template: `

    <kpn-subset-page-header-block
      [subset]="subset"
      pageName="networks"
      pageTitle="Networks"
      i18n-pageTitle="@@subset-networks.title">
    </kpn-subset-page-header-block>

    <kpn-subset-network-list
      *ngIf="response"
      [networks]="response.result.networks">
    </kpn-subset-network-list>

    <br/>
    <br/>
    <br/>

    <kpn-subset-network-table
      *ngIf="response"
      [networks]="response.result.networks">
    </kpn-subset-network-table>

    <div *ngIf="response">
      <json [object]="response"></json>
    </div>
  `
})
export class SubsetNetworksPageComponent implements OnInit, OnDestroy {

  private readonly subscriptions = new Subscriptions();

  subset: Subset;
  response: ApiResponse<SubsetNetworksPage>;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService,
              private networkCacheService: NetworkCacheService,
              private subsetCacheService: SubsetCacheService) {
  }

  ngOnInit() {
    this.pageService.initSubsetPage();
    this.subscriptions.add(this.activatedRoute.params.subscribe(params => {
      this.subset = Util.subsetInRoute(params);
      this.pageService.subset = this.subset;
      this.response = null;
      this.subscriptions.add(this.appService.subsetNetworks(this.subset).subscribe(response => {
        this.response = response;
        this.subsetCacheService.setSubsetInfo(this.subset.key(), this.response.result.subsetInfo);
        response.result.networks.forEach(networkAttributes => {
          this.networkCacheService.setNetworkName(networkAttributes.id.toString(), networkAttributes.name);
        });
      }));
    }));
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

}
