import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs";
import {AppService} from "../../../../app.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {SubsetOrphanRoutesPage} from "../../../../kpn/shared/subset/subset-orphan-routes-page";
import {Subset} from "../../../../kpn/shared/subset";
import {Util} from "../../../../components/shared/util";
import {PageService} from "../../../../components/shared/page.service";
import {SubsetCacheService} from "../../../../services/subset-cache.service";

@Component({
  selector: 'kpn-subset-orphan-routes-page',
  template: `

    <kpn-subset-page-header [subset]="subset" pageName="orphan-routes"></kpn-subset-page-header>

    <div *ngIf="response">
      <kpn-subset-orphan-routes-table [orphanRoutes]="response.result.rows"></kpn-subset-orphan-routes-table>
      <json [object]="response"></json>
    </div>
  `
})
export class SubsetOrphanRoutesPageComponent implements OnInit, OnDestroy {

  subset: Subset;
  response: ApiResponse<SubsetOrphanRoutesPage>;
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
      this.appService.subsetOrphanRoutes(this.subset).subscribe(response => {
        this.response = response;
        this.subsetCacheService.setSubsetInfo(this.subset.key(), this.response.result.subsetInfo)
      });
    });
  }

  ngOnDestroy() {
    this.paramsSubscription.unsubscribe();
  }

}
