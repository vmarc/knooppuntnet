import {Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Observable} from "rxjs";
import {shareReplay} from "rxjs/operators";
import {flatMap, map, tap} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {PageService} from "../../../components/shared/page.service";
import {NodeInfo} from "../../../kpn/api/common/node-info";
import {NodeMapPage} from "../../../kpn/api/common/node/node-map-page";
import {ApiResponse} from "../../../kpn/api/custom/api-response";

@Component({
  selector: "kpn-node-map-page",
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a></li>
      <li i18n="@@breadcrumb.node-map">Node map</li>
    </ul>

    <kpn-node-page-header
      *ngIf="nodeId$ | async as nodeId"
      pageName="map"
      [nodeId]="nodeId"
      [nodeName]="nodeName"
      [changeCount]="changeCount">
    </kpn-node-page-header>

    <div *ngIf="response$ | async as response">
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

  nodeId$: Observable<string>;
  response$: Observable<ApiResponse<NodeMapPage>>;

  nodeName: string;
  changeCount = 0;
  nodeInfo: NodeInfo;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService) {
    this.pageService.showFooter = false;
  }

  ngOnInit(): void {
    this.nodeName = history.state.nodeName;
    this.changeCount = history.state.changeCount;
    this.pageService.showFooter = false;
    this.nodeId$ = this.activatedRoute.params.pipe(
      map(params => params["nodeId"]),
      shareReplay()
    );
  }

  ngAfterViewInit(): void {
    this.response$ = this.nodeId$.pipe(
      flatMap(nodeId => this.appService.nodeMap(nodeId).pipe(
        tap(response => {
          this.nodeInfo = response.result.nodeInfo;
          this.nodeName = response.result.nodeInfo.name;
          this.changeCount = response.result.changeCount;
        })
      ))
    );
  }

  ngOnDestroy(): void {
    this.pageService.showFooter = true;
  }

}
