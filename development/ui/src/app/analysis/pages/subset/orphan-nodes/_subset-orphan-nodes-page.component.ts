import {Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs";
import {AppService} from "../../../../app.service";
import {PageService} from "../../../../components/shared/page.service";
import {Util} from "../../../../components/shared/util";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {Subset} from "../../../../kpn/shared/subset";
import {SubsetOrphanNodesPage} from "../../../../kpn/shared/subset/subset-orphan-nodes-page";
import {SubsetCacheService} from "../../../../services/subset-cache.service";

@Component({
  selector: "kpn-subset-orphan-nodes-page",
  template: `

    <kpn-subset-page-header [subset]="subset" pageName="orphan-nodes"></kpn-subset-page-header>

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
              private pageService: PageService,
              private subsetCacheService: SubsetCacheService) {
  }

  ngOnInit() {
    this.pageService.initSubsetPage();
    this.paramsSubscription = this.activatedRoute.params.subscribe(params => {
      this.subset = Util.subsetInRoute(params);
      this.pageService.subset = this.subset;
      this.response = null;
      this.appService.subsetOrphanNodes(this.subset).subscribe(response => {
        this.response = response;
        this.subsetCacheService.setSubsetInfo(this.subset.key(), this.response.result.subsetInfo)
      });
    });
  }

  ngOnDestroy() {
    this.paramsSubscription.unsubscribe();
  }

}
