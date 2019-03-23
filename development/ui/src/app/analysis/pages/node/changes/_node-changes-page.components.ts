import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs";
import {AppService} from "../../../../app.service";
import {NodePage} from "../../../../kpn/shared/node/node-page";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {PageService} from "../../../../components/shared/page.service";

@Component({
  selector: 'kpn-node-changes-page',
  template: `

    <kpn-node-page-header [nodeId]="nodeId" [nodeName]="response?.result?.nodeInfo.name" [pageName]="'node-changes'"></kpn-node-page-header>
    
    <div *ngIf="response?.result">
      <div *ngIf="!response.result">
        Node not found
      </div>
      <div *ngIf="response.result">
        <json [object]="response"></json>
      </div>
    </div>
  `
})
export class NodeChangesPageComponent implements OnInit, OnDestroy {

  nodeId: string;
  response: ApiResponse<NodePage>;
  paramsSubscription: Subscription;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService) {
  }

  ngOnInit() {
    this.pageService.defaultMenu();
    this.paramsSubscription = this.activatedRoute.params.subscribe(params => {
      this.nodeId = params['nodeId'];
      this.appService.node(this.nodeId).subscribe(response => {
        this.response = response;
      });
    });
  }

  ngOnDestroy() {
    this.paramsSubscription.unsubscribe();
  }

  get nodeInfo() {
    return this.response.result.nodeInfo;
  }

  get references() {
    return this.response.result.references;
  }

}
