import {Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {AppService} from "../../../app.service";
import {PageService} from "../../../components/shared/page.service";
import {Util} from "../../../components/shared/util";
import {ApiResponse} from "../../../kpn/shared/api-response";
import {ChangesPage} from "../../../kpn/shared/changes-page";
import {ChangesParameters} from "../../../kpn/shared/changes/filter/changes-parameters";
import {Subset} from "../../../kpn/shared/subset";
import {SubsetChangesPage} from "../../../kpn/shared/subset/subset-changes-page";
import {SubsetCacheService} from "../../../services/subset-cache.service";
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

    <div *ngIf="response">
      <p>
        <kpn-situation-on [timestamp]="response.situationOn"></kpn-situation-on>
      </p>

      <kpn-changes [(parameters)]="parameters" [totalCount]="page.changeCount" [changeCount]="page.changes.size" [showFirstLastButtons]="false">
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
export class SubsetChangesPageComponent implements OnInit, OnDestroy {

  subset: Subset;
  response: ApiResponse<SubsetChangesPage>;

  private readonly subscriptions = new Subscriptions();
  private _parameters: ChangesParameters;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private subsetChangesService: SubsetChangesService,
              private pageService: PageService,
              private subsetCacheService: SubsetCacheService) {
  }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(params => {
      this.subset = Util.subsetInRoute(params);
      //this.parameters = new ChangesParameters(this.subset, null, null, null, null, null, null, 5, 0, false);
      this.parameters = new ChangesParameters(null, null, null, null, null, null, null, 5, 0, false);
    });
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

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

  rowIndex(index: number): number {
    return this.parameters.pageIndex * this.parameters.itemsPerPage + index;
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
