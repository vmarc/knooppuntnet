import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs";
import {AppService} from "../../../../app.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {SubsetChangesPage} from "../../../../kpn/shared/subset/subset-changes-page";
import {Subset} from "../../../../kpn/shared/subset";
import {Util} from "../../../../components/shared/util";
import {PageService} from "../../../../components/shared/page.service";

@Component({
  selector: 'kpn-subset-changes-page',
  template: `
    <h1>
      <kpn-subset-name [subset]="subset"></kpn-subset-name>
    </h1>
    <h2>
      Changes
    </h2>

    <div *ngIf="response">
      <json [object]="response"></json>
    </div>
  `
})
export class SubsetChangesPageComponent implements OnInit, OnDestroy {

  subset: Subset;
  response: ApiResponse<SubsetChangesPage>;
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
      this.appService.subsetChanges(this.subset).subscribe(response => {
        this.response = response;
      });
    });
  }

  ngOnDestroy() {
    this.paramsSubscription.unsubscribe();
  }

}
