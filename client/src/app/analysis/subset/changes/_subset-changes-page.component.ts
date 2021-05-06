import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ChangesParameters } from '@api/common/changes/filter/changes-parameters';
import { SubsetChangesPage } from '@api/common/subset/subset-changes-page';
import { SubsetInfo } from '@api/common/subset/subset-info';
import { ApiResponse } from '@api/custom/api-response';
import { Subset } from '@api/custom/subset';
import { Store } from '@ngrx/store';
import { combineLatest } from 'rxjs';
import { BehaviorSubject } from 'rxjs';
import { first } from 'rxjs/operators';
import { AppService } from '../../../app.service';
import { PageService } from '../../../components/shared/page.service';
import { Util } from '../../../components/shared/util';
import { AppState } from '../../../core/core.state';
import { actionPreferencesImpact } from '../../../core/preferences/preferences.actions';
import { selectPreferencesImpact } from '../../../core/preferences/preferences.selectors';
import { selectPreferencesItemsPerPage } from '../../../core/preferences/preferences.selectors';
import { Subsets } from '../../../kpn/common/subsets';
import { SubsetCacheService } from '../../../services/subset-cache.service';
import { UserService } from '../../../services/user.service';
import { ChangeFilterOptions } from '../../components/changes/filter/change-filter-options';
import { SubsetChangesService } from './subset-changes.service';

@Component({
  selector: 'kpn-subset-changes-page',
  template: `
    <kpn-subset-page-header-block
      pageName="changes"
      pageTitle="Changes"
      i18n-pageTitle="@@subset-changes.title"
    ></kpn-subset-page-header-block>

    <kpn-error></kpn-error>

    <div class="kpn-spacer-above">
      <div *ngIf="!isLoggedIn()" i18n="@@subset-changes.login-required">
        This details of the changes history are available to registered
        OpenStreetMap contributors only, after
        <kpn-link-login></kpn-link-login>
        .
      </div>

      <div *ngIf="response?.result">
        <p>
          <kpn-situation-on
            [timestamp]="response.situationOn"
          ></kpn-situation-on>
        </p>
        <kpn-changes
          [(parameters)]="parameters"
          [totalCount]="page.changeCount"
          [changeCount]="page.changes.size"
        >
          <kpn-items>
            <kpn-item
              *ngFor="let changeSet of page.changes; let i = index"
              [index]="rowIndex(i)"
            >
              <kpn-change-set [changeSet]="changeSet"></kpn-change-set>
            </kpn-item>
          </kpn-items>
        </kpn-changes>
      </div>
    </div>
  `,
})
export class SubsetChangesPageComponent implements OnInit {
  subset: Subset;
  subsetInfo$ = new BehaviorSubject<SubsetInfo>(null);
  response: ApiResponse<SubsetChangesPage>;
  private _parameters: ChangesParameters;

  constructor(
    private activatedRoute: ActivatedRoute,
    private appService: AppService,
    private subsetChangesService: SubsetChangesService,
    private pageService: PageService,
    private userService: UserService,
    private subsetCacheService: SubsetCacheService,
    private store: Store<AppState>
  ) {}

  get parameters() {
    return this._parameters;
  }

  set parameters(parameters: ChangesParameters) {
    this._parameters = parameters;
    if (this.isLoggedIn()) {
      this.reload();
    } else {
      this.subsetChangesService.resetFilterOptions();
    }
  }

  get page(): SubsetChangesPage {
    return this.response.result;
  }

  ngOnInit(): void {
    combineLatest([
      this.activatedRoute.params,
      this.store.select(selectPreferencesItemsPerPage),
      this.store.select(selectPreferencesImpact),
    ])
      .pipe(first())
      .subscribe(([params, itemsPerPage, impact]) => {
        this.subset = Util.subsetInRoute(params);
        const initialParameters = Util.defaultChangesParameters();
        this.parameters = { ...initialParameters, impact, itemsPerPage };
      });
  }

  rowIndex(index: number): number {
    return this.parameters.pageIndex * this.parameters.itemsPerPage + index;
  }

  isLoggedIn(): boolean {
    return this.userService.isLoggedIn();
  }

  private reload() {
    this.appService
      .subsetChanges(this.subset, this.parameters)
      .subscribe((response) => {
        this.response = response;
        this.subsetCacheService.setSubsetInfo(
          Subsets.key(this.subset),
          response.result.subsetInfo
        );
        this.subsetInfo$.next(response.result.subsetInfo);
        this.subsetChangesService.setFilterOptions(
          ChangeFilterOptions.from(
            this.parameters,
            this.response.result.filter,
            (parameters: ChangesParameters) => {
              this.store.dispatch(
                actionPreferencesImpact({ impact: parameters.impact })
              );
              this.parameters = parameters;
            }
          )
        );
      });
  }
}
