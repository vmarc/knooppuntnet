import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs";
import {AppService} from "../../../app.service";
import {ApiResponse} from "../../../kpn/shared/api-response";
import {SubsetOrphanNodesPage} from "../../../kpn/shared/subset/subset-orphan-nodes-page";
import {Util} from "../../../components/shared/util";
import {Subset} from "../../../kpn/shared/subset";
import {PageService} from "../../../components/shared/page.service";

@Component({
  selector: 'kpn-subset-orphan-nodes-page',
  template: `
    <h1>
      <kpn-subset-name [subset]="subset"></kpn-subset-name>
    </h1>
    <h2>
      Orphan nodes
    </h2>

    <div *ngIf="response">
      <kpn-subset-orphan-nodes-table [nodes]="response.result.rows"></kpn-subset-orphan-nodes-table>
      <json [object]="response"></json>
    </div>
  `
})
export class SubsetOrphanNodesPageComponent implements OnInit, OnDestroy {

  subset: Subset;
  response: ApiResponse<SubsetOrphanNodesPage>;
  paramsSubscription: Subscription;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService) {
  }

  ngOnInit() {
    this.pageService.initSubsetPage();
    this.paramsSubscription = this.activatedRoute.params.subscribe(params => {
      this.subset = Util.subsetInRoute(params);
      this.pageService.subset = this.subset;
      this.response = null;
      this.appService.subsetOrphanNodes(this.subset).subscribe(response => {
        this.response = response;
      });
    });
  }

  ngOnDestroy() {
    this.paramsSubscription.unsubscribe();
  }

}
