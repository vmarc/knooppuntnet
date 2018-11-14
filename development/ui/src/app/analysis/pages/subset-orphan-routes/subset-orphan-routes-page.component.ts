import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs";
import {AppService} from "../../../app.service";
import {ApiResponse} from "../../../kpn/shared/api-response";
import {SubsetOrphanRoutesPage} from "../../../kpn/shared/subset/subset-orphan-routes-page";
import {Subset} from "../../../kpn/shared/subset";
import {Util} from "../../../shared/util";

@Component({
  selector: 'kpn-subset-orphan-routes-page',
  template: `
    <kpn-page>
      <kpn-toolbar toolbar></kpn-toolbar>
      <kpn-subset-sidenav sidenav [subset]="subset"></kpn-subset-sidenav>
      <div content>
        <h1>
          Subset orphan routes
        </h1>
        <div *ngIf="response">
          <kpn-subset-orphan-routes-table [orphanRoutes]="response.result.rows"></kpn-subset-orphan-routes-table>
          <json [object]="response"></json>
        </div>
      </div>
    </kpn-page>
  `
})
export class SubsetOrphanRoutesPageComponent implements OnInit, OnDestroy {

  subset: Subset;
  response: ApiResponse<SubsetOrphanRoutesPage>;
  paramsSubscription: Subscription;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService) {
  }

  ngOnInit() {
    this.paramsSubscription = this.activatedRoute.params.subscribe(params => {
      this.subset = Util.subsetInRoute(params);
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
