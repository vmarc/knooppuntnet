import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs";
import {AppService} from "../../../app.service";
import {RoutePage} from "../../../kpn/shared/route/route-page";
import {ApiResponse} from "../../../kpn/shared/api-response";
import {PageService} from "../../../shared/page.service";

@Component({
  selector: 'kpn-route-page',
  template: `
    <h1>
      Route
    </h1>
    <div *ngIf="response">
      <json [object]="response"></json>
    </div>
  `
})
export class RoutePageComponent implements OnInit, OnDestroy {

  response: ApiResponse<RoutePage>;
  paramsSubscription: Subscription;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService) {
  }

  ngOnInit() {
    this.pageService.defaultMenu();
    this.paramsSubscription = this.activatedRoute.params.subscribe(params => {
      const routeId = params['routeId'];
      this.appService.route(routeId).subscribe(response => {
        this.response = response;
      });
    });
  }

  ngOnDestroy() {
    this.paramsSubscription.unsubscribe();
  }
}
