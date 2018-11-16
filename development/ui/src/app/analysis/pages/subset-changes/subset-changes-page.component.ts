import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs";
import {AppService} from "../../../app.service";
import {ApiResponse} from "../../../kpn/shared/api-response";
import {SubsetChangesPage} from "../../../kpn/shared/subset/subset-changes-page";
import {Subset} from "../../../kpn/shared/subset";
import {Util} from "../../../shared/util";

@Component({
  selector: 'kpn-subset-changes-page',
  template: `
    <kpn-page>
      <kpn-toolbar toolbar></kpn-toolbar>
      <kpn-subset-sidenav sidenav [subset]="subset"></kpn-subset-sidenav>
      <div content>
        <h1>
          <kpn-subset-name [subset]="subset"></kpn-subset-name>
        </h1>
        <h2>
          Changes
        </h2>

        <div *ngIf="response">
          <json [object]="response"></json>
        </div>
      </div>
    </kpn-page>
  `
})
export class SubsetChangesPageComponent implements OnInit, OnDestroy {

  subset: Subset;
  response: ApiResponse<SubsetChangesPage>;
  paramsSubscription: Subscription;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService) {
  }

  ngOnInit() {
    this.paramsSubscription = this.activatedRoute.params.subscribe(params => {
      this.subset = Util.subsetInRoute(params);
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
