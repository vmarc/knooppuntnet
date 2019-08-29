import {AfterViewInit, Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {AppService} from "../../../app.service";
import {PageService} from "../../../components/shared/page.service";
import {ApiResponse} from "../../../kpn/shared/api-response";
import {ChangesParameters} from "../../../kpn/shared/changes/filter/changes-parameters";
import {RouteChangesPage} from "../../../kpn/shared/route/route-changes-page";
import {UserService} from "../../../services/user.service";
import {Subscriptions} from "../../../util/Subscriptions";

@Component({
  selector: "kpn-route-changes-page",
  template: `
    <kpn-route-page-header
      [routeId]="routeId"
      [routeName]="response?.result?.route.summary.name"
      [changeCount]="response?.result?.changeCount">
    </kpn-route-page-header>

    <div *ngIf="!isLoggedIn()">
      <span i18n="@@route-changes.login-required">The route history is available to registered OpenStreetMap contributors only, after</span>
      <kpn-link-login></kpn-link-login>
      .
    </div>

    <div *ngIf="response">

      <div *ngIf="!page" i18n="@@route-changes.route-not-found">
        Route not found
      </div>

      <div *ngIf="page">
        <kpn-changes [(parameters)]="parameters" [totalCount]="page.changeCount"  [changeCount]="page.changes.size" >
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
export class RouteChangesPageComponent implements OnInit, AfterViewInit, OnDestroy {

  routeId: string;
  response: ApiResponse<RouteChangesPage>;
  parameters = new ChangesParameters(null, null, null, null, null, null, null, 5, 0, false);

  private readonly subscriptions = new Subscriptions();

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService,
              private userService: UserService) {
  }

  get page(): RouteChangesPage {
    return this.response.result;
  }

  get route() {
    return this.response.result.route;
  }

  isLoggedIn(): boolean {
    return this.userService.isLoggedIn();
  }

  ngOnInit(): void {
    this.pageService.defaultMenu();
    this.subscriptions.add(
      this.activatedRoute.params.subscribe(params => {
        this.routeId = params["routeId"];
        this.parameters = new ChangesParameters(null, null, +this.routeId, null, null, null, null, 5, 0, false);
      })
    );
  }

  ngAfterViewInit(): void {
    // this.subscriptions.add(
    //   this.paginator.page.subscribe(event => this.reload())
    // );
    this.reload();
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  private reload(): void {
    // this.updateParameters();
    this.subscriptions.add(
      this.appService.routeChanges(this.routeId.toString(), this.parameters).subscribe(response => {
        this.response = response;
      })
    );
  }

}
