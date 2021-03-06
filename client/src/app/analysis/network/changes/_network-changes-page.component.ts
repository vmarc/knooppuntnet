import { ChangeDetectionStrategy } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ChangesParameters } from '@api/common/changes/filter/changes-parameters';
import { NetworkChangesPage } from '@api/common/network/network-changes-page';
import { ApiResponse } from '@api/custom/api-response';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { combineLatest } from 'rxjs';
import { first } from 'rxjs/operators';
import { shareReplay } from 'rxjs/operators';
import { switchMap } from 'rxjs/operators';
import { tap } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { AppService } from '../../../app.service';
import { AppState } from '../../../core/core.state';
import { actionPreferencesImpact } from '../../../core/preferences/preferences.actions';
import { selectPreferencesImpact } from '../../../core/preferences/preferences.selectors';
import { selectPreferencesItemsPerPage } from '../../../core/preferences/preferences.selectors';
import { NetworkCacheService } from '../../../services/network-cache.service';
import { UserService } from '../../../services/user.service';
import { ChangeFilterOptions } from '../../components/changes/filter/change-filter-options';
import { NetworkService } from '../network.service';
import { NetworkChangesService } from './network-changes.service';

@Component({
  selector: 'kpn-network-changes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-network-page-header
      [networkId]="networkId$ | async"
      pageName="changes"
      pageTitle="Changes"
      i18n-pageTitle="@@network-changes.title"
    >
    </kpn-network-page-header>

    <div class="kpn-spacer-above">
      <div *ngIf="!isLoggedIn()" i18n="@@network-changes.login-required">
        The details of network changes history are available to registered
        OpenStreetMap contributors only, after
        <kpn-link-login></kpn-link-login>
        .
      </div>

      <div *ngIf="isLoggedIn() && response$ | async as response">
        <div *ngIf="!response.result" i18n="@@network-page.network-not-found">
          Network not found
        </div>
        <div *ngIf="response.result">
          <p>
            <kpn-situation-on
              [timestamp]="response.situationOn"
            ></kpn-situation-on>
          </p>
          <kpn-changes
            [(parameters)]="parameters"
            [totalCount]="response.result.totalCount"
            [changeCount]="response.result.changes.length"
          >
            <kpn-items>
              <kpn-item
                *ngFor="
                  let networkChangeInfo of response.result.changes;
                  let i = index
                "
                [index]="rowIndex(i)"
              >
                <kpn-network-change-set
                  [networkChangeInfo]="networkChangeInfo"
                ></kpn-network-change-set>
              </kpn-item>
            </kpn-items>
          </kpn-changes>
        </div>
      </div>
    </div>
  `,
})
export class NetworkChangesPageComponent implements OnInit {
  networkId$: Observable<number>;
  response$: Observable<ApiResponse<NetworkChangesPage>>;

  constructor(
    private activatedRoute: ActivatedRoute,
    private appService: AppService,
    private networkChangesService: NetworkChangesService,
    private networkService: NetworkService,
    private networkCacheService: NetworkCacheService,
    private userService: UserService,
    private store: Store<AppState>
  ) {}

  get parameters(): ChangesParameters {
    return this.networkChangesService._parameters$.value;
  }

  set parameters(parameters: ChangesParameters) {
    this.networkChangesService.updateParameters(parameters);
  }

  ngOnInit(): void {
    this.networkId$ = this.activatedRoute.params.pipe(
      map((params) => +params['networkId']),
      tap((networkId) => this.networkService.init(networkId)),
      shareReplay()
    );

    combineLatest([
      this.store.select(selectPreferencesItemsPerPage),
      this.store.select(selectPreferencesImpact),
    ])
      .pipe(first())
      .subscribe(([itemsPerPage, impact]) => {
        this.networkChangesService._parameters$.next({
          ...this.networkChangesService._parameters$.value,
          itemsPerPage,
          impact,
        });

        this.response$ = combineLatest([
          this.networkId$,
          this.networkChangesService.parameters$,
        ]).pipe(
          switchMap(([networkId, changeParameters]) =>
            this.appService.networkChanges(networkId, changeParameters).pipe(
              tap((response) => {
                if (response.result) {
                  this.networkService.update(
                    networkId,
                    response.result.network
                  );
                  this.networkChangesService.setFilterOptions(
                    ChangeFilterOptions.from(
                      this.parameters,
                      response.result.filter,
                      (parameters: ChangesParameters) => {
                        this.store.dispatch(
                          actionPreferencesImpact({ impact: parameters.impact })
                        );
                        this.parameters = parameters;
                      }
                    )
                  );
                }
              })
            )
          )
        );
      });
  }

  isLoggedIn(): boolean {
    return this.userService.isLoggedIn();
  }

  rowIndex(index: number): number {
    return this.parameters.pageIndex * this.parameters.itemsPerPage + index;
  }
}
