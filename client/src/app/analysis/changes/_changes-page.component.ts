import {Component, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {AppService} from "../../app.service";
import {PageService} from "../../components/shared/page.service";
import {ApiResponse} from "../../kpn/api/custom/api-response";
import {ChangesPage} from "../../kpn/api/common/changes-page";
import {ChangesParameters} from "../../kpn/api/common/changes/filter/changes-parameters";
import {Subscriptions} from "../../util/Subscriptions";
import {ChangeFilterOptions} from "../components/changes/filter/change-filter-options";
import {ChangesService} from "../components/changes/filter/changes.service";
import {UserService} from "../../services/user.service";

@Component({
  selector: "kpn-changes-page",
  template: `

    <div>
      <a routerLink="/" i18n="@@breadcrumb.home">Home</a>
      <span class="breadcrumb-separator"></span>
      <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a>
      <span class="breadcrumb-separator"></span>
      <span i18n="@@breadcrumb.changes">Changes</span>
    </div>

    <kpn-page-header subject="changes-page" i18n="@@changes-page.title">Changes</kpn-page-header>

    <div *ngIf="!isLoggedIn()" i18n="@@changes-page.login-required">
      The details of the changes history are available to registered OpenStreetMap contributors only, after
      <kpn-link-login></kpn-link-login>
      .
    </div>

    <div *ngIf="response">

      <p>
        <kpn-situation-on [timestamp]="response.situationOn"></kpn-situation-on>
      </p>

      <kpn-changes
        [(parameters)]="parameters"
        [totalCount]="page.changeCount"
        [changeCount]="page.changes.size"
        [showFirstLastButtons]="false">
        <kpn-items>
          <kpn-item *ngFor="let changeSet of page.changes; let i=index" [index]="rowIndex(i)">
            <kpn-change-set [changeSet]="changeSet"></kpn-change-set>
          </kpn-item>
        </kpn-items>
      </kpn-changes>

      <kpn-json [object]="response"></kpn-json>
    </div>
  `
})
export class ChangesPageComponent implements OnInit {

  response: ApiResponse<ChangesPage>;
  private readonly subscriptions = new Subscriptions();

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private changesService: ChangesService,
              private pageService: PageService,
              private userService: UserService) {
  }

  private _parameters = new ChangesParameters(null, null, null, null, null, null, null, 15, 0, true);

  get parameters() {
    return this._parameters;
  }

  set parameters(parameters: ChangesParameters) {
    this._parameters = parameters;
    this.reload();
  }

  get page(): ChangesPage {
    return this.response.result;
  }

  ngOnInit(): void {
    this.pageService.defaultMenu();
    if (this.isLoggedIn()) {
      this.reload();
    } else {
      this.changesService.resetFilterOptions();
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  rowIndex(index: number): number {
    return this.parameters.pageIndex * this.parameters.itemsPerPage + index;
  }

  isLoggedIn(): boolean {
    return this.userService.isLoggedIn();
  }

  private reload() {
    this.appService.changes(this.parameters).subscribe(response => {
      this.response = response;
      this.changesService.filterOptions.next(
        ChangeFilterOptions.from(
          this.parameters,
          this.response.result.filter,
          (parameters: ChangesParameters) => this.parameters = parameters
        )
      );
    });
  }

}
