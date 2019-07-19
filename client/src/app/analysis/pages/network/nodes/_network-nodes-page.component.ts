import {Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {AppService} from "../../../../app.service";
import {PageService} from "../../../../components/shared/page.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {NetworkNodesPage} from "../../../../kpn/shared/network/network-nodes-page";
import {NetworkCacheService} from "../../../../services/network-cache.service";
import {Subscriptions} from "../../../../util/Subscriptions";
import {flatMap, map, tap} from "rxjs/operators";

@Component({
  selector: "kpn-network-nodes-page",
  template: `

    <kpn-network-page-header
      [networkId]="networkId"
      pageTitle="Nodes"
      i18n-pageTitle="@@network-nodes.title">
    </kpn-network-page-header>

    <div *ngIf="response">
      <network-node-table [nodes]="response.result.nodes"></network-node-table>
      <json [object]="response"></json>
    </div>
  `
})
export class NetworkNodesPageComponent implements OnInit, OnDestroy {

  private readonly subscriptions = new Subscriptions();

  networkId: string;
  response: ApiResponse<NetworkNodesPage>;

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
        flatMap(networkId => this.appService.networkNodes(networkId))
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

  private processResponse(response: ApiResponse<NetworkNodesPage>) {
    this.response = response;
    this.networkCacheService.setNetworkSummary(this.networkId, response.result.networkSummary);
    const networkName = response.result.networkSummary.name;
    this.networkCacheService.setNetworkName(this.networkId, networkName);
  }

}
