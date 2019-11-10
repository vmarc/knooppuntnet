import {Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {flatMap, map, tap} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {ApiResponse} from "../../../kpn/api/custom/api-response";
import {NetworkNodesPage} from "../../../kpn/api/common/network/network-nodes-page";
import {NetworkCacheService} from "../../../services/network-cache.service";
import {Subscriptions} from "../../../util/Subscriptions";

@Component({
  selector: "kpn-network-nodes-page",
  template: `

    <kpn-network-page-header
      [networkId]="networkId"
      pageTitle="Nodes"
      i18n-pageTitle="@@network-nodes.title">
    </kpn-network-page-header>

    <div *ngIf="response">
      <div *ngIf="!page">
        <p i18n="@@network-nodes.network-not-found">Network not found</p>
      </div>
      <div *ngIf="page">
        <div *ngIf="page.nodes.isEmpty()" i18n="@@network-nodes.no-nodes">
          No network nodes in network
        </div>
        <kpn-network-node-table
          *ngIf="!page.nodes.isEmpty()"
          [networkType]="page.networkType"
          [timeInfo]="page.timeInfo"
          [nodes]="page.nodes">
        </kpn-network-node-table>
      </div>
      <kpn-json [object]="response"></kpn-json>
    </div>
  `
})
export class NetworkNodesPageComponent implements OnInit, OnDestroy {

  private readonly subscriptions = new Subscriptions();

  networkId: number;
  response: ApiResponse<NetworkNodesPage>;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private networkCacheService: NetworkCacheService) {
  }

  ngOnInit(): void {
    this.subscriptions.add(
      this.activatedRoute.params.pipe(
        map(params => +params["networkId"]),
        tap(networkId => this.networkId = networkId),
        flatMap(networkId => this.appService.networkNodes(networkId))
      ).subscribe(response => this.processResponse(response))
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  get page(): NetworkNodesPage {
    return this.response.result;
  }

  private processResponse(response: ApiResponse<NetworkNodesPage>) {
    this.response = response;
    if (this.page) {
      this.networkCacheService.setNetworkSummary(this.networkId, this.page.networkSummary);
      this.networkCacheService.setNetworkName(this.networkId, this.page.networkSummary.name);
    }
  }

}
