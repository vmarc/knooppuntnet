import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { ErrorComponent } from '@app/components/shared/error';
import { PageComponent } from '@app/components/shared/page';
import { SituationOnComponent } from '@app/components/shared/timestamp';
import { Store } from '@ngrx/store';
import { SubsetPageHeaderBlockComponent } from '../components/subset-page-header-block.component';
import { actionSubsetOrphanRoutesPageInit } from '../store/subset.actions';
import { selectSubsetOrphanRoutesPage } from '../store/subset.selectors';
import { SubsetOrphanRoutesSidebarComponent } from './components/subset-orphan-routes-sidebar.component';
import { SubsetOrphanRoutesTableComponent } from './components/subset-orphan-routes-table.component';

@Component({
  selector: 'kpn-subset-orphan-routes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <kpn-subset-page-header-block
        pageName="orphan-routes"
        pageTitle="Free routes"
        i18n-pageTitle="@@subset-orphan-routes.title"
      />

      <kpn-error />

      @if (apiResponse(); as response) {
        <div class="kpn-spacer-above">
          <p>
            <kpn-situation-on [timestamp]="response.situationOn" />
          </p>
          @if (response.result.routes.length === 0) {
            <p class="kpn-line">
              <span i18n="@@subset-orphan-routes.no-routes">No free routes</span>
            </p>
          } @else {
            <kpn-subset-orphan-routes-table
              [timeInfo]="response.result.timeInfo"
              [networkType]="response.result.subsetInfo.networkType"
              [orphanRoutes]="response.result.routes"
            />
          }
        </div>
      }
      <kpn-subset-orphan-routes-sidebar sidebar />
    </kpn-page>
  `,
  standalone: true,
  imports: [
    AsyncPipe,
    ErrorComponent,
    PageComponent,
    SituationOnComponent,
    SubsetOrphanRoutesSidebarComponent,
    SubsetOrphanRoutesTableComponent,
    SubsetPageHeaderBlockComponent,
  ],
})
export class SubsetOrphanRoutesPageComponent implements OnInit {
  private readonly store = inject(Store);
  protected readonly apiResponse = this.store.selectSignal(selectSubsetOrphanRoutesPage);

  ngOnInit(): void {
    this.store.dispatch(actionSubsetOrphanRoutesPageInit());
  }
}
