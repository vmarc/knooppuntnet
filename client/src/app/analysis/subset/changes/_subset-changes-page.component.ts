import {Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {AppService} from "../../../app.service";
import {PageService} from "../../../components/shared/page.service";
import {Util} from "../../../components/shared/util";
import {ChangesPage} from "../../../kpn/api/common/changes-page";
import {ChangesParameters} from "../../../kpn/api/common/changes/filter/changes-parameters";
import {SubsetChangesPage} from "../../../kpn/api/common/subset/subset-changes-page";
import {ApiResponse} from "../../../kpn/api/custom/api-response";
import {Subset} from "../../../kpn/api/custom/subset";
import {SubsetCacheService} from "../../../services/subset-cache.service";
import {UserService} from "../../../services/user.service";
import {Subscriptions} from "../../../util/Subscriptions";
import {ChangeFilterOptions} from "../../components/changes/filter/change-filter-options";
import {SubsetChangesService} from "./subset-changes.service";

@Component({
  selector: "kpn-subset-changes-page",
  template: `

    <kpn-subset-page-header-block
      [subset]="subset"
      pageName="changes"
      pageTitle="Changes"
      i18n-pageTitle="@@subset-changes.title">
    </kpn-subset-page-header-block>

    <div *ngIf="!isLoggedIn()" i18n="@@subset-changes.login-required">
      This details of the changes history are available to registered OpenStreetMap contributors only, after
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

    </div>
  `
})
export class SubsetChangesPageComponent implements OnInit, OnDestroy {

  subset: Subset;
  response: ApiResponse<SubsetChangesPage>;

  private readonly subscriptions = new Subscriptions();

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private subsetChangesService: SubsetChangesService,
              private pageService: PageService,
              private userService: UserService,
              private subsetCacheService: SubsetCacheService) {
  }

  private _parameters: ChangesParameters;

  get parameters() {
    return this._parameters;
  }

  set parameters(parameters: ChangesParameters) {
    this.appService.storeChangesParameters(parameters);
    this._parameters = parameters;
    if (this.isLoggedIn()) {
      this.reload();
    } else {
      this.subsetChangesService.resetFilterOptions();
    }
  }

  get page(): ChangesPage {
    return this.response.result;
  }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(params => {
      this.subset = Util.subsetInRoute(params);
      const initialParameters = new ChangesParameters(null, null, null, null, null, null, null, 0, 0, false);
      this.parameters = this.appService.changesParameters(initialParameters);
    });
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
    this.appService.subsetChanges(this.subset, this.parameters).subscribe(response => {
      this.response = response;
      this.subsetCacheService.setSubsetInfo(this.subset.key(), this.response.result.subsetInfo);
      this.subsetChangesService.filterOptions.next(
        ChangeFilterOptions.from(
          this.parameters,
          this.response.result.filter,
          (parameters: ChangesParameters) => this.parameters = parameters
        )
      );
    });
  }
}
