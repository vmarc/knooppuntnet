import {ChangeDetectionStrategy} from "@angular/core";
import {Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {ReplaySubject} from "rxjs";
import {combineLatest} from "rxjs";
import {Observable} from "rxjs";
import {flatMap} from "rxjs/operators";
import {switchMap} from "rxjs/operators";
import {tap} from "rxjs/operators";
import {map} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {PageService} from "../../../components/shared/page.service";
import {Util} from "../../../components/shared/util";
import {ChangesParameters} from "../../../kpn/api/common/changes/filter/changes-parameters";
import {RouteChangesPage} from "../../../kpn/api/common/route/route-changes-page";
import {RouteInfo} from "../../../kpn/api/common/route/route-info";
import {ApiResponse} from "../../../kpn/api/custom/api-response";
import {UserService} from "../../../services/user.service";
import {ChangeFilterOptions} from "../../components/changes/filter/change-filter-options";
import {RouteChangesService} from "./route-changes.service";

@Component({
  selector: "kpn-route-changes-page",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a></li>
      <li i18n="@@breadcrumb.route-changes">Route changes</li>
    </ul>

    <kpn-route-page-header
      pageName="changes"
      [routeId]="routeId$ | async"
      [routeName]="routeName$ | async"
      [changeCount]="changeCount$ | async">
    </kpn-route-page-header>

    <div class="kpn-spacer-above">
      <div *ngIf="!isLoggedIn()" i18n="@@route-changes.login-required">
        The details of the route changes history is available to registered OpenStreetMap contributors only, after
        <kpn-link-login></kpn-link-login>
        .
      </div>

      <div *ngIf="response$ | async as response">

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
      </div>
    </div>
  `
})
export class RouteChangesPageComponent implements OnInit, OnDestroy {

  response$: Observable<ApiResponse<RouteChangesPage>>;

  routeId$ = new ReplaySubject<string>(1);
  routeName$ = new ReplaySubject<string>(1);
  changeCount$ = new ReplaySubject<number>(1);

  page: RouteChangesPage;
  route: RouteInfo;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private routeChangesService: RouteChangesService,
              private pageService: PageService,
              private userService: UserService) {
  }

  get parameters() {
    return this.routeChangesService.parameters$.value;
  }

  set parameters(parameters: ChangesParameters) {
    if (this.isLoggedIn()) {
      this.routeChangesService.updateParameters(parameters);
    }
  }

  ngOnInit(): void {
    this.routeName$.next(history.state.routeName);
    this.changeCount$.next(history.state.changeCount);
    this.response$ = this.activatedRoute.params.pipe(
      map(params => params["routeId"]),
      tap(routeId => this.routeId$.next(routeId)),
      tap(routeId => this.updateParameters(routeId)),
      flatMap(routeId =>
        combineLatest([this.routeId$, this.routeChangesService.parameters$]).pipe(
          switchMap(([nodeId, changeParameters]) =>
            this.appService.routeChanges(nodeId, changeParameters).pipe(
              tap(response => {
                this.page = Util.safeGet(() => response.result);
                this.routeName$.next(Util.safeGet(() => response.result.route.summary.name));
                this.changeCount$.next(Util.safeGet(() => response.result.changeCount));
                this.routeChangesService.setFilterOptions(
                  ChangeFilterOptions.from(
                    this.parameters,
                    response.result.filter,
                    (parameters: ChangesParameters) => this.parameters = parameters
                  )
                );
              })
            )
          )
        )
      )
    );
  }

  ngOnDestroy(): void {
    this.routeChangesService.resetFilterOptions();
  }

  isLoggedIn(): boolean {
    return this.userService.isLoggedIn();
  }

  private updateParameters(routeId: string) {
    this.parameters = new ChangesParameters(
      null,
      null,
      null,
      +routeId,
      null,
      this.parameters.year,
      this.parameters.month,
      this.parameters.day,
      this.parameters.itemsPerPage,
      this.parameters.pageIndex,
      this.parameters.impact
    );
  }
}
