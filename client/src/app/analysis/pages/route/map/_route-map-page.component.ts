import {Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {AppService} from "../../../../app.service";
import {PageService} from "../../../../components/shared/page.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {Subscriptions} from "../../../../util/Subscriptions";
import {RouteMapPage} from "../../../../kpn/shared/route/route-map-page";

@Component({
  selector: "kpn-route-changes-page",
  template: `
    <kpn-route-page-header [routeId]="routeId" [routeName]="response?.result?.route.summary.name"></kpn-route-page-header>

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
export class RouteMapPageComponent implements OnInit, OnDestroy {

  private readonly subscriptions = new Subscriptions();

  routeId: string;
  response: ApiResponse<RouteMapPage>;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService) {
  }

  ngOnInit() {
    this.pageService.defaultMenu();
    this.subscriptions.add(this.activatedRoute.params.subscribe(params => {
      this.routeId = params["routeId"];
      this.subscriptions.add(this.appService.routeMap(this.routeId).subscribe(response => {
        this.response = response;
      }));
    }));
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  get route() {
    return this.response.result.route;
  }
}
