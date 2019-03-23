import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs";
import {AppService} from "../../../../app.service";
import {RoutePage} from "../../../../kpn/shared/route/route-page";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {PageService} from "../../../../components/shared/page.service";

@Component({
  selector: 'kpn-route-changes-page',
  template: `
    <kpn-route-page-header [routeId]="routeId" [routeName]="response?.result?.route.summary.name" [pageName]="'route-changes'"></kpn-route-page-header>

    <div *ngIf="response">

      <div *ngIf="!response.result">
        Route not found
      </div>

      <div *ngIf="response.result">
      </div>

      <json [object]="response"></json>
    </div>
  `
})
export class RouteChangesPageComponent implements OnInit, OnDestroy {

  routeId: string;
  response: ApiResponse<RoutePage>;
  paramsSubscription: Subscription;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService) {
  }

  ngOnInit() {
    this.pageService.defaultMenu();
    this.paramsSubscription = this.activatedRoute.params.subscribe(params => {
      this.routeId = params['routeId'];
      this.appService.route(this.routeId).subscribe(response => {
        this.response = response;
      });
    });
  }

  ngOnDestroy() {
    this.paramsSubscription.unsubscribe();
  }

  get route() {
    return this.response.result.route;
  }
}
