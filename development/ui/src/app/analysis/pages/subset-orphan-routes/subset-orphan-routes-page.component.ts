import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs";
import {AppService} from "../../../app.service";
import {ApiResponse} from "../../../kpn/shared/api-response";
import {SubsetOrphanRoutesPage} from "../../../kpn/shared/subset/subset-orphan-routes-page";
import {Subset} from "../../../kpn/shared/subset";
import {Util} from "../../../shared/util";
import {PageService} from "../../../shared/page.service";

@Component({
  selector: 'kpn-subset-orphan-routes-page',
  template: `
    <h1>
      <kpn-subset-name [subset]="subset"></kpn-subset-name>
    </h1>
    <h2>
      Orphan routes
    </h2>

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
              private pageService: PageService) {
  }

  ngOnInit() {
    this.pageService.initSubsetPage();
    this.paramsSubscription = this.activatedRoute.params.subscribe(params => {
      this.subset = Util.subsetInRoute(params);
      this.pageService.subset = this.subset;
      this.response = null;
      this.appService.subsetOrphanRoutes(this.subset).subscribe(response => {
        this.response = response;
      });
    });
  }

  ngOnDestroy() {
    this.paramsSubscription.unsubscribe();
  }

}
