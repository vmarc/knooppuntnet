import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FilterComponent } from '@app/analysis/components/filter/filter.component';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { IconHappyComponent } from '@app/shared/components/icon/icon-happy.component';
import { PageFilterComponent } from '@app/shared/components/page/page-filter.component';
import { SituationOnComponent } from '@app/shared/components/timestamp/situation-on.component';
import { RouterService } from '@app/shared/services/router.service';
import { SubsetPageHeaderBlockComponent } from '../components/subset-page-header-block.component';
import { SubsetOrphanNodesTableComponent } from './components/subset-orphan-nodes-table.component';
import { SubsetOrphanNodesPageService } from './subset-orphan-nodes-page.service';

@Component({
  selector: 'ui-subset-orphan-nodes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page-filter>
      <ui-subset-page-header-block
        pageName="orphan-nodes"
        pageTitle="Orphan nodes"
        i18n-pageTitle="@@subset-orphan-nodes.title"
      />

      <ui-error />

      @if (service.response(); as response) {
        <div class="kpn-spacer-above">
          <p>
            <ui-situation-on [timestamp]="response.situationOn" />
          </p>
          @if (response.result.nodes.length === 0) {
            <p class="kpn-line">
              <ui-icon-happy />
              <span i18n="@@subset-orphan-nodes.no-routes">No orphan nodes</span>
            </p>
          } @else {
            <ui-subset-orphan-nodes-table />
          }
        </div>
      }
      <ui-filter [filterOptions]="filterOptions()" filter />
    </ui-page-filter>
  `,
  providers: [SubsetOrphanNodesPageService, RouterService],
  imports: [
    ErrorComponent,
    FilterComponent,
    IconHappyComponent,
    PageFilterComponent,
    SituationOnComponent,
    SubsetOrphanNodesTableComponent,
    SubsetPageHeaderBlockComponent,
  ],
})
export class SubsetOrphanNodesPageComponent implements OnInit {
  protected readonly service = inject(SubsetOrphanNodesPageService);
  protected readonly filterOptions = this.service.filterOptions;

  ngOnInit(): void {
    this.service.onInit();
  }
}
