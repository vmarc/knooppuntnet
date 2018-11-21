import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs";
import {AppService} from "../../../app.service";
import {ApiResponse} from "../../../kpn/shared/api-response";
import {SubsetFactDetailsPage} from "../../../kpn/shared/subset/subset-fact-details-page";
import {Util} from "../../../shared/util";
import {Subset} from "../../../kpn/shared/subset";
import {PageService} from "../../../shared/page.service";

@Component({
  selector: 'kpn-subset-fact-details-page',
  template: `
    <h1>
      <kpn-subset-name [subset]="subset"></kpn-subset-name>
    </h1>
    <h2>
      Fact details
    </h2>

    <div *ngIf="response">
      <json [object]="response"></json>
    </div>
  `
})
export class SubsetFactDetailsPageComponent implements OnInit, OnDestroy {

  subset: Subset;
  response: ApiResponse<SubsetFactDetailsPage>;
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
      this.appService.subsetFactDetails(this.subset).subscribe(response => {
        this.response = response;
      });
    });
  }

  ngOnDestroy() {
    this.paramsSubscription.unsubscribe();
  }
}
