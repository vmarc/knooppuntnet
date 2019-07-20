import {Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {AppService} from "../../../../app.service";
import {PageService} from "../../../../components/shared/page.service";
import {Util} from "../../../../components/shared/util";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {Subset} from "../../../../kpn/shared/subset";
import {SubsetOrphanNodesPage} from "../../../../kpn/shared/subset/subset-orphan-nodes-page";
import {SubsetCacheService} from "../../../../services/subset-cache.service";
import {Subscriptions} from "../../../../util/Subscriptions";
import {flatMap, map, tap} from "rxjs/operators";

@Component({
  selector: "kpn-subset-orphan-nodes-page",
  template: `

    <kpn-subset-page-header-block
      [subset]="subset"
      pageName="orphan-nodes"
      pageTitle="Orphan nodes"
      i18n-pageTitle="@@subset-orphan-nodes.title">
    </kpn-subset-page-header-block>

    <div *ngIf="response">
      <kpn-subset-orphan-nodes-table [nodes]="response.result.rows"></kpn-subset-orphan-nodes-table>
      <json [object]="response"></json>
    </div>
  `
})
export class SubsetOrphanNodesPageComponent implements OnInit, OnDestroy {

  private readonly subscriptions = new Subscriptions();

  subset: Subset;
  response: ApiResponse<SubsetOrphanNodesPage>;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService,
              private subsetCacheService: SubsetCacheService) {
  }

  ngOnInit() {
    this.pageService.initSubsetPage();
    this.subscriptions.add(
      this.activatedRoute.params.pipe(
        map(params => Util.subsetInRoute(params)),
        tap(subset => this.processSubset(subset)),
        flatMap(subset => this.appService.subsetOrphanNodes(subset))
      ).subscribe(response => this.processResponse(response))
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  private processSubset(subset: Subset) {
    this.subset = subset;
    this.pageService.subset = subset;
  }

  private processResponse(response: ApiResponse<SubsetOrphanNodesPage>) {
    this.response = response;
    this.subsetCacheService.setSubsetInfo(this.subset.key(), this.response.result.subsetInfo)
  }

}
