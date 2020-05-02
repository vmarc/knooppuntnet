import {ChangeDetectionStrategy} from "@angular/core";
import {AfterViewInit} from "@angular/core";
import {Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Observable} from "rxjs";
import {shareReplay} from "rxjs/operators";
import {flatMap, map, tap} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {PageService} from "../../../components/shared/page.service";
import {RouteMapPage} from "../../../kpn/api/common/route/route-map-page";
import {ApiResponse} from "../../../kpn/api/custom/api-response";

@Component({
  selector: "kpn-route-changes-page",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a></li>
      <li i18n="@@breadcrumb.route-map">Route map</li>
    </ul>

    <kpn-route-page-header
      *ngIf="routeId$ | async as routeId"
      pageName="map"
      [routeId]="routeId"
      [routeName]="routeName"
      [changeCount]="changeCount">
    </kpn-route-page-header>

    <div *ngIf="response$ | async as response">
      <div *ngIf="!response.result" i18n="@@route-map-page.route-not-found">
        Route not found
      </div>
      <div *ngIf="response.result">
        <kpn-route-map [routeInfo]="response.result.route"></kpn-route-map>
      </div>
    </div>
  `
})
export class RouteMapPageComponent implements OnInit, OnDestroy, AfterViewInit {

  routeId$: Observable<string>;
  response$: Observable<ApiResponse<RouteMapPage>>;

  routeName: string;
  changeCount = 0;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService) {
    this.pageService.showFooter = false;
  }

  ngOnInit(): void {
    this.routeName = history.state.routeName;
    this.changeCount = history.state.changeCount;
    this.routeId$ = this.activatedRoute.params.pipe(
      map(params => params["routeId"]),
      shareReplay()
    );
  }

  ngAfterViewInit(): void {
    this.response$ = this.routeId$.pipe(
      flatMap(routeId => this.appService.routeMap(routeId).pipe(
        tap(response => {
          this.routeName = response.result.route.summary.name;
          this.changeCount = response.result.changeCount;
        })
      ))
    );
  }

  ngOnDestroy(): void {
    this.pageService.showFooter = true;
  }
}
