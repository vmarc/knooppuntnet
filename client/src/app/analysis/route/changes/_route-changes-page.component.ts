import {Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {AppService} from "../../../app.service";
import {PageService} from "../../../components/shared/page.service";
import {ChangesParameters} from "../../../kpn/api/common/changes/filter/changes-parameters";
import {RouteChangesPage} from "../../../kpn/api/common/route/route-changes-page";
import {ApiResponse} from "../../../kpn/api/custom/api-response";
import {UserService} from "../../../services/user.service";
import {Subscriptions} from "../../../util/Subscriptions";
import {ChangeFilterOptions} from "../../components/changes/filter/change-filter-options";
import {RouteChangesService} from "./route-changes.service";

@Component({
  selector: "kpn-route-changes-page",
  template: `
    <kpn-route-page-header
      pageName="changes"
      [routeId]="routeId"
      [routeName]="response?.result?.route.summary.name"
      [changeCount]="response?.result?.changeCount">
    </kpn-route-page-header>

    <div *ngIf="!isLoggedIn()" i18n="@@route-changes.login-required">
      The details of the route changes history is available to registered OpenStreetMap contributors only, after
      <kpn-link-login></kpn-link-login>
      .
    </div>

    <div *ngIf="response">

      <div *ngIf="!page" i18n="@@route-changes.route-not-found">
        Route not found
      </div>

      <div *ngIf="page">
        <kpn-changes [(parameters)]="parameters" [totalCount]="page.totalCount" [changeCount]="page.changes.size">
          <kpn-items>
            <kpn-item *ngFor="let routeChangeInfo of page.changes; let i=index" [index]="i">
              <kpn-route-change [routeChangeInfo]="routeChangeInfo"></kpn-route-change>
            </kpn-item>
          </kpn-items>
        </kpn-changes>
        <div *ngIf="page.incompleteWarning">
          <kpn-history-incomplete-warning></kpn-history-incomplete-warning>
        </div>
      </div>

      <kpn-json [object]="response"></kpn-json>
    </div>
  `
})
export class RouteChangesPageComponent implements OnInit, OnDestroy {

  routeId: string;
  response: ApiResponse<RouteChangesPage>;
  private readonly subscriptions = new Subscriptions();

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private routeChangesService: RouteChangesService,
              private pageService: PageService,
              private userService: UserService) {
  }

  private _parameters: ChangesParameters;

  get parameters() {
    return this._parameters;
  }

  set parameters(parameters: ChangesParameters) {
    this._parameters = parameters;
    this.appService.storeChangesParameters(parameters);
    if (this.isLoggedIn()) {
      this.reload();
    } else {
      this.routeChangesService.resetFilterOptions();
    }
  }

  get page(): RouteChangesPage {
    return this.response.result;
  }

  get route() {
    return this.response.result.route;
  }

  ngOnInit(): void {
    this.pageService.defaultMenu();
    this.subscriptions.add(
      this.activatedRoute.params.subscribe(params => {
        this.routeId = params["routeId"];
        const initialParameters = new ChangesParameters(null, null, +this.routeId, null, null, null, null, 0, 0, false);
        this.parameters = this.appService.changesParameters(initialParameters);
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  isLoggedIn(): boolean {
    return this.userService.isLoggedIn();
  }

  private reload(): void {
    this.subscriptions.add(
      this.appService.routeChanges(this.routeId.toString(), this.parameters).subscribe(response => {
        this.response = response;
        this.routeChangesService.filterOptions.next(
          ChangeFilterOptions.from(
            this.parameters,
            this.response.result.filter,
            (parameters: ChangesParameters) => this.parameters = parameters
          )
        );
      })
    );
  }

}
