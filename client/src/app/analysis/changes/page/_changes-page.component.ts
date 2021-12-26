import { OnDestroy } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { ChangesPage } from '@api/common/changes-page';
import { ChangesParameters } from '@api/common/changes/filter/changes-parameters';
import { ApiResponse } from '@api/custom/api-response';
import { Store } from '@ngrx/store';
import { combineLatest } from 'rxjs';
import { first } from 'rxjs/operators';
import { AppService } from '../../../app.service';
import { PageService } from '../../../components/shared/page.service';
import { Util } from '../../../components/shared/util';
import { AppState } from '../../../core/core.state';
import { actionPreferencesImpact } from '../../../core/preferences/preferences.actions';
import { selectPreferencesAnalysisMode } from '../../../core/preferences/preferences.selectors';
import { selectPreferencesImpact } from '../../../core/preferences/preferences.selectors';
import { selectPreferencesItemsPerPage } from '../../../core/preferences/preferences.selectors';
import { UserService } from '../../../services/user.service';
import { Subscriptions } from '../../../util/Subscriptions';
import { ChangeFilterOptions } from '../../components/changes/filter/change-filter-options';
import { ChangesService } from '../../components/changes/filter/changes.service';

@Component({
  selector: 'kpn-changes-page',
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li>
        <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a>
      </li>
      <li i18n="@@breadcrumb.changes">Changes</li>
    </ul>

    <kpn-page-header subject="changes-page" i18n="@@changes-page.title">
      Changes
    </kpn-page-header>

    <div
      *ngIf="!isLoggedIn()"
      i18n="@@changes-page.login-required"
      class="kpn-spacer-above"
    >
      The details of the changes history are available to registered
      OpenStreetMap contributors only, after
      <kpn-link-login></kpn-link-login>
      .
    </div>

    <kpn-error></kpn-error>

    <div *ngIf="response" class="kpn-spacer-above">
      <div *ngIf="response.result">
        <p>
          <kpn-situation-on
            [timestamp]="response.situationOn"
          ></kpn-situation-on>
        </p>
        <kpn-changes
          [(parameters)]="parameters"
          [totalCount]="page.changeCount"
          [changeCount]="page.changes.length"
        >
          <kpn-items>
            <kpn-item
              *ngFor="let changeSet of page.changes; let i = index"
              [index]="rowIndex(i)"
            >
              <kpn-change-network-analysis-summary
                *ngIf="changeSet.network"
                [changeSet]="changeSet"
              ></kpn-change-network-analysis-summary>
              <kpn-change-location-analysis-summary
                *ngIf="changeSet.location"
                [changeSet]="changeSet"
              ></kpn-change-location-analysis-summary>
            </kpn-item>
          </kpn-items>
        </kpn-changes>
      </div>
    </div>
  `,
})
export class ChangesPageComponent implements OnInit, OnDestroy {
  response: ApiResponse<ChangesPage>;
  private readonly subscriptions = new Subscriptions();

  private _parameters;

  constructor(
    private activatedRoute: ActivatedRoute,
    private appService: AppService,
    private changesService: ChangesService,
    private pageService: PageService,
    private userService: UserService,
    private store: Store<AppState>,
    private router: Router,
    private route: ActivatedRoute
  ) {}

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
      combineLatest([
        this.store.select(selectPreferencesItemsPerPage),
        this.store.select(selectPreferencesImpact),
      ])
        .pipe(first())
        .subscribe(([itemsPerPage, impact]) => {
          const initialParameters = Util.defaultChangesParameters();
          this.parameters = {
            ...initialParameters,
            impact,
            itemsPerPage: +itemsPerPage,
          };
          // note that this.reload() is called by parameter setter!
        });
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
    this.store
      .select(selectPreferencesAnalysisMode)
      .subscribe((analysisMode) => {
        this.appService
          .changes(analysisMode, this.parameters)
          .subscribe((response) => {
            this.response = response;
            if (response.result) {
              this.changesService.setFilterOptions(
                ChangeFilterOptions.from(
                  this.parameters,
                  this.response.result.filter,
                  (parameters: ChangesParameters) => {
                    this.store.dispatch(
                      actionPreferencesImpact({ impact: parameters.impact })
                    );

                    this.router.navigate([], {
                      relativeTo: this.route,
                      queryParams: parameters,
                    });

                    this.parameters = parameters;
                  }
                )
              );
            }
          });
      });
  }
}
