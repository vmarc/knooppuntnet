import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FilterComponent } from '@app/analysis/components/filter';
import { ErrorComponent } from '@app/components/shared/error';
import { SituationOnComponent } from '@app/components/shared/timestamp';
import { PageFilterComponent } from '../../../shared/components/shared/page/page-filter.component';
import { RouterService } from '../../../shared/services/router.service';
import { SubsetPageHeaderBlockComponent } from '../components/subset-page-header-block.component';
import { SubsetOrphanRoutesTableComponent } from './components/subset-orphan-routes-table.component';
import { SubsetOrphanRoutesPageService } from './subset-orphan-routes-page.service';

@Component({
  selector: 'kpn-subset-orphan-routes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page-filter>
      <kpn-filter [filterOptions]="filterOptions()" filter />

      <kpn-subset-page-header-block
        pageName="orphan-routes"
        pageTitle="Free routes"
        i18n-pageTitle="@@subset-orphan-routes.title"
      />

      <kpn-error />

      @if (service.response(); as response) {
        <div class="kpn-spacer-above">
          <p>
            <kpn-situation-on [timestamp]="response.situationOn" />
          </p>
          @if (response.result.routes.length === 0) {
            <p class="kpn-line">
              <span i18n="@@subset-orphan-routes.no-routes">No free routes</span>
            </p>
          } @else {
            <kpn-subset-orphan-routes-table />
          }
        </div>
      }
    </kpn-page-filter>
  `,
  providers: [SubsetOrphanRoutesPageService, RouterService],
  imports: [
    ErrorComponent,
    FilterComponent,
    PageFilterComponent,
    SituationOnComponent,
    SubsetOrphanRoutesTableComponent,
    SubsetPageHeaderBlockComponent,
  ],
})
export class SubsetOrphanRoutesPageComponent implements OnInit {
  protected readonly service = inject(SubsetOrphanRoutesPageService);
  protected readonly filterOptions = this.service.filterOptions;

  ngOnInit(): void {
    this.service.onInit();
  }
}
