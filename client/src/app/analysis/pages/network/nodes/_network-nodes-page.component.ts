import {Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {AppService} from "../../../../app.service";
import {PageService} from "../../../../components/shared/page.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {Country} from "../../../../kpn/shared/country";
import {NetworkType} from "../../../../kpn/shared/network-type";
import {NetworkNodesPage} from "../../../../kpn/shared/network/network-nodes-page";
import {Subset} from "../../../../kpn/shared/subset";
import {NetworkCacheService} from "../../../../services/network-cache.service";
import {Subscriptions} from "../../../../util/Subscriptions";

@Component({
  selector: "kpn-network-nodes-page",
  template: `
    <kpn-network-page-header [networkId]="networkId" selectedPage="nodes"></kpn-network-page-header>
    <div *ngIf="response">
      <network-node-table [nodes]="response.result.nodes"></network-node-table>
      <json [object]="response"></json>
    </div>
  `
})
export class NetworkNodesPageComponent implements OnInit, OnDestroy {

  private readonly subscriptions = new Subscriptions();

  subset: Subset;
  networkId: string;

  response: ApiResponse<NetworkNodesPage>;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService,
              private networkCacheService: NetworkCacheService) {
  }

  ngOnInit() {
    this.pageService.initNetworkPage();
    this.subscriptions.add(this.activatedRoute.params.subscribe(params => {
      this.networkId = params["networkId"];
      this.pageService.networkId = this.networkId;
      const cachedNetworkName = this.networkCacheService.getNetworkName(this.networkId);
      if (cachedNetworkName) {
        this.pageService.setNetworkPageTitle("nodes", cachedNetworkName);
      }
      // TODO this.subset = response.result.network.attributes.country + networkType
      this.subset = new Subset(new Country("nl"), new NetworkType("rwn", "hiking"));
      this.pageService.subset = this.subset;
      this.subscriptions.add(this.appService.networkNodes(this.networkId).subscribe(response => {
        this.response = response;
        this.networkCacheService.setNetworkSummary(this.networkId, response.result.networkSummary);
        const networkName = response.result.networkSummary.name;
        this.networkCacheService.setNetworkName(this.networkId, networkName);
        this.pageService.setNetworkPageTitle("nodes", networkName);
      }));
    }));
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

}
