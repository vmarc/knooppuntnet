import {Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {AppService} from "../../../../app.service";
import {PageService} from "../../../../components/shared/page.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {UserService} from "../../../../services/user.service";
import {Subscriptions} from "../../../../util/Subscriptions";
import {RouteChangesPage} from "../../../../kpn/shared/route/route-changes-page";
import {flatMap, map, tap} from "rxjs/operators";

@Component({
  selector: "kpn-route-changes-page",
  template: `
    <kpn-route-page-header [routeId]="routeId" [routeName]="response?.result?.route.summary.name"></kpn-route-page-header>

    <div *ngIf="!isLoggedIn()">
      <span>The route history is available to registered OpenStreetMap contributors only, after</span>
      <kpn-link-login></kpn-link-login>
      .
    </div>

    <div *ngIf="response">

      <div *ngIf="!response.result">
        Route not found
      </div>

      <div *ngIf="response.result">

        <kpn-items>
          <kpn-item *ngFor="let routeChangeInfo of response.result.changes; let i=index" index="{{i}}">
            <kpn-route-change [routeChangeInfo]="routeChangeInfo"></kpn-route-change>
          </kpn-item>
        </kpn-items>

        <div *ngIf="response.result.incompleteWarning">
          <kpn-history-incomplete-warning></kpn-history-incomplete-warning>
        </div>

      </div>

      <json [object]="response"></json>
    </div>
  `
})
export class RouteChangesPageComponent implements OnInit, OnDestroy {

  private readonly subscriptions = new Subscriptions();

  routeId: string;
  response: ApiResponse<RouteChangesPage>;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService,
              private userService: UserService) {
  }

  isLoggedIn(): boolean {
    return this.userService.isLoggedIn();
  }

  ngOnInit() {
    this.pageService.defaultMenu();
    this.subscriptions.add(
      this.activatedRoute.params.pipe(
        map(params => params["routeId"]),
        tap(routeId => this.routeId = routeId),
        flatMap(routeId => this.appService.routeChanges(routeId))
      ).subscribe(response => {
        this.response = response;
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  get route() {
    return this.response.result.route;
  }

}
