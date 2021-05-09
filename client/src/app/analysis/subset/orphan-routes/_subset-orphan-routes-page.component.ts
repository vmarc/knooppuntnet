import { ChangeDetectionStrategy } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { AppState } from '../../../core/core.state';
import { actionSubsetOrphanRoutesPageInit } from '../store/subset.actions';
import { selectSubsetOrphanRoutesPage } from '../store/subset.selectors';

@Component({
  selector: 'kpn-subset-orphan-routes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-subset-page-header-block
      pageName="orphan-routes"
      pageTitle="Free routes"
      i18n-pageTitle="@@subset-orphan-routes.title"
    ></kpn-subset-page-header-block>

    <kpn-error></kpn-error>

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <p>
        <kpn-situation-on [timestamp]="response.situationOn"></kpn-situation-on>
      </p>
      <p *ngIf="response.result.routes.length === 0" class="kpn-line">
        <span i18n="@@subset-orphan-routes.no-routes">No free routes</span>
      </p>
      <div *ngIf="response.result.routes.length > 0">
        <kpn-subset-orphan-routes-table
          [timeInfo]="response.result.timeInfo"
          [networkType]="response.result.subsetInfo.networkType"
          [orphanRoutes]="response.result.routes"
        ></kpn-subset-orphan-routes-table>
      </div>
    </div>
  `,
})
export class SubsetOrphanRoutesPageComponent implements OnInit {
  readonly response$ = this.store.select(selectSubsetOrphanRoutesPage);

  constructor(private store: Store<AppState>) {}

  ngOnInit(): void {
    this.store.dispatch(actionSubsetOrphanRoutesPageInit());
  }
}
