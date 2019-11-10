import {Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {flatMap, map, tap} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {PageService} from "../../../components/shared/page.service";
import {ApiResponse} from "../../../kpn/api/custom/api-response";
import {RouteInfo} from "../../../kpn/api/common/route/route-info";
import {RouteMapPage} from "../../../kpn/api/common/route/route-map-page";
import {Subscriptions} from "../../../util/Subscriptions";

@Component({
  selector: "kpn-route-changes-page",
  template: `

    <kpn-route-page-header
      [routeId]="routeId"
      [routeName]="response?.result?.route.summary.name"
      [changeCount]="response?.result?.changeCount">
    </kpn-route-page-header>

    <div *ngIf="response">
      <div *ngIf="!response.result" i18n="@@route-map-page.route-not-found">
        Route not found
      </div>
      <div *ngIf="response.result">
        <kpn-route-map [routeInfo]="response.result.route"></kpn-route-map>
      </div>
    </div>
  `
})
export class RouteMapPageComponent implements OnInit, OnDestroy {

  routeId: string;
  response: ApiResponse<RouteMapPage>;

  private readonly subscriptions = new Subscriptions();

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService) {
    this.pageService.showFooter = false;
  }

  get route(): RouteInfo {
    return this.response.result.route;
  }

  ngOnInit(): void {
    this.subscriptions.add(
      this.activatedRoute.params.pipe(
        map(params => params["routeId"]),
        tap(routeId => this.routeId = routeId),
        flatMap(routeId => this.appService.routeMap(routeId))
      ).subscribe(response => this.response = response)
    );
  }

  ngOnDestroy(): void {
    this.pageService.showFooter = true;
    this.subscriptions.unsubscribe();
  }
}
