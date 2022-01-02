import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { SubsetInfo } from '@api/common/subset/subset-info';
import { Subset } from '@api/custom/subset';
import { Store } from '@ngrx/store';
import { BehaviorSubject } from 'rxjs';
import { AppService } from '../../../app.service';
import { PageService } from '../../../components/shared/page.service';
import { AppState } from '../../../core/core.state';
import { SubsetCacheService } from '../../../services/subset-cache.service';
import { UserService } from '../../../services/user.service';
import { selectChangesPageIndex } from '../../changes/store/changes.selectors';
import { actionSubsetChangesPageIndex } from '../store/subset.actions';
import { actionSubsetChangesPageInit } from '../store/subset.actions';
import { selectSubsetChangesPageIndex } from '../store/subset.selectors';
import { selectSubsetChangesPage } from '../store/subset.selectors';

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

      <div *ngIf="response$ | async as response">
        <p>
          <kpn-situation-on
            [timestamp]="response.situationOn"
          ></kpn-situation-on>
        </p>
        <kpn-changes
          [totalCount]="response.result.changeCount"
          [changeCount]="response.result.changes.length"
          [pageIndex]="pageIndex$ | async"
          (pageIndexChange)="pageIndexChanged($event)"
        >
          <kpn-items>
            <kpn-item
              *ngFor="let changeSet of response.result.changes"
              [index]="changeSet.rowIndex"
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
export class SubsetChangesPageComponent implements OnInit {
  subset: Subset;
  subsetInfo$ = new BehaviorSubject<SubsetInfo>(null);

  readonly response$ = this.store.select(selectSubsetChangesPage);
  readonly pageIndex$ = this.store.select(selectSubsetChangesPageIndex);

  constructor(
    private activatedRoute: ActivatedRoute,
    private appService: AppService,
    private pageService: PageService,
    private userService: UserService,
    private subsetCacheService: SubsetCacheService,
    private store: Store<AppState>
  ) {}

  ngOnInit(): void {
    this.store.dispatch(actionSubsetChangesPageInit());

    //   .subscribe(([params, itemsPerPage, impact]) => {
    //     this.subset = Util.subsetInRoute(params);
    //     const initialParameters = Util.defaultChangesParameters();
    //     this.parameters = { ...initialParameters, impact, itemsPerPage };
    //   });
  }

  isLoggedIn(): boolean {
    return this.userService.isLoggedIn();
  }

  //     .subscribe((response) => {
  //       this.response$ = response;
  //       this.subsetCacheService.setSubsetInfo(
  //         Subsets.key(this.subset),
  //         response.result.subsetInfo
  //       );
  //       this.subsetInfo$.next(response.result.subsetInfo);

  pageIndexChanged(pageIndex: number): void {
    this.store.dispatch(actionSubsetChangesPageIndex({ pageIndex }));
  }
}
