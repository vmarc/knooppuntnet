import {Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {AppService} from "../../../../app.service";
import {PageService} from "../../../../components/shared/page.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {Subscriptions} from "../../../../util/Subscriptions";
import {NodeMapPage} from "../../../../kpn/shared/node/node-map-page";
import {flatMap, map, tap} from "rxjs/operators";

@Component({
  selector: "kpn-node-map-page",
  template: `
    <kpn-node-page-header [nodeId]="nodeId" [nodeName]="nodeName"></kpn-node-page-header>
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

  nodeId: number;
  nodeName: string;
  response: ApiResponse<NodeMapPage>;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService) {
    this.pageService.showFooter = false;
  }

  ngOnInit() {
    this.nodeName = history.state.nodeName;
    this.pageService.defaultMenu();
    this.subscriptions.add(
      this.activatedRoute.params.pipe(
        map(params => params["nodeId"]),
        tap(nodeId => this.nodeId = +nodeId),
        flatMap(nodeId => this.appService.nodeMap(nodeId))
      ).subscribe(response => this.processResponse(response))
    );
  }

  ngOnDestroy(): void {
    this.pageService.showFooter = true;
    this.subscriptions.unsubscribe();
  }

  get nodeInfo() {
    return this.response.result.nodeInfo;
  }

  private processResponse(response: ApiResponse<NodeMapPage>) {
    this.response = response;
    this.nodeName = response.result.nodeInfo.name;
  }

}
