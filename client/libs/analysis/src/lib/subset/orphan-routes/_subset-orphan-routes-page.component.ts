import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { ErrorComponent } from '@app/components/shared/error';
import { SituationOnComponent } from '@app/components/shared/timestamp';
import { Store } from '@ngrx/store';
import { SubsetPageHeaderBlockComponent } from '../components/subset-page-header-block.component';
import { actionSubsetOrphanRoutesPageInit } from '../store/subset.actions';
import { selectSubsetOrphanRoutesPage } from '../store/subset.selectors';
import { SubsetOrphanRoutesTableComponent } from './subset-orphan-routes-table.component';

@Component({
  selector: 'kpn-subset-orphan-routes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-subset-page-header-block
      pageName="orphan-routes"
      pageTitle="Free routes"
      i18n-pageTitle="@@subset-orphan-routes.title"
    />

    <kpn-error />

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <p>
        <kpn-situation-on [timestamp]="response.situationOn" />
      </p>
      <p *ngIf="response.result.routes.length === 0" class="kpn-line">
        <span i18n="@@subset-orphan-routes.no-routes">No free routes</span>
      </p>
      <div *ngIf="response.result.routes.length > 0">
        <kpn-subset-orphan-routes-table
          [timeInfo]="response.result.timeInfo"
          [networkType]="response.result.subsetInfo.networkType"
          [orphanRoutes]="response.result.routes"
        />
      </div>
    </div>
  `,
  standalone: true,
  imports: [
    SubsetPageHeaderBlockComponent,
    ErrorComponent,
    NgIf,
    SituationOnComponent,
    SubsetOrphanRoutesTableComponent,
    AsyncPipe,
  ],
})
export class SubsetOrphanRoutesPageComponent implements OnInit {
  readonly response$ = this.store.select(selectSubsetOrphanRoutesPage);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionSubsetOrphanRoutesPageInit());
  }
}
