import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs";
import {AppService} from "../../../app.service";
import {ApiResponse} from "../../../kpn/shared/api-response";
import {SubsetFactDetailsPage} from "../../../kpn/shared/subset/subset-fact-details-page";
import {Util} from "../../../shared/util";
import {Subset} from "../../../kpn/shared/subset";

@Component({
  selector: 'kpn-subset-fact-details-page',
  template: `
    <kpn-page>
      <kpn-toolbar toolbar></kpn-toolbar>
      <kpn-subset-sidenav sidenav [subset]="subset"></kpn-subset-sidenav>
      <div content>
        <h1>
          Subset fact details
        </h1>
        <div *ngIf="response">
          <json [object]="response"></json>
        </div>
      </div>
    </kpn-page>
  `
})
export class SubsetFactDetailsPageComponent implements OnInit, OnDestroy {

  subset: Subset;
  response: ApiResponse<SubsetFactDetailsPage>;
  paramsSubscription: Subscription;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService) {
  }

  ngOnInit() {
    this.paramsSubscription = this.activatedRoute.params.subscribe(params => {
      this.subset = Util.subsetInRoute(params);
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
