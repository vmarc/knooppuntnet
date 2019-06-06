import {Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {AppService} from "../../../../app.service";
import {PageService} from "../../../../components/shared/page.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {Subscriptions} from "../../../../util/Subscriptions";
import {NodeMapPage} from "../../../../kpn/shared/node/node-map-page";

@Component({
  selector: "kpn-node-map-page",
  template: `
    <kpn-node-page-header [nodeId]="nodeId" [nodeName]="response?.result?.nodeInfo.name" [pageName]="'node-map'"></kpn-node-page-header>
    <div *ngIf="response?.result">
      <div *ngIf="!response.result" i18n="@@node.node-not-found">
        Node not found
      </div>
      <div *ngIf="response.result">
        <kpn-node-map [nodeInfo]="nodeInfo"></kpn-node-map>
      </div>
    </div>
  `
})
export class NodeMapPageComponent implements OnInit, OnDestroy {

  private readonly subscriptions = new Subscriptions();

  nodeId: string;
  response: ApiResponse<NodeMapPage>;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService) {
    this.pageService.showFooter = false;
  }

  ngOnInit() {
    this.pageService.defaultMenu();
    this.subscriptions.add(this.activatedRoute.params.subscribe(params => {
      this.nodeId = params["nodeId"];
      this.subscriptions.add(this.appService.nodeMap(this.nodeId).subscribe(response => {
        this.response = response;
      }));
    }));
  }

  ngOnDestroy(): void {
    this.pageService.showFooter = true;
    this.subscriptions.unsubscribe();
  }

  get nodeInfo() {
    return this.response.result.nodeInfo;
  }

}
