import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { ErrorComponent } from '@app/components/shared/error';
import { IconHappyComponent } from '@app/components/shared/icon';
import { PageComponent } from '@app/components/shared/page';
import { SituationOnComponent } from '@app/components/shared/timestamp';
import { Store } from '@ngrx/store';
import { SubsetPageHeaderBlockComponent } from '../components/subset-page-header-block.component';
import { actionSubsetOrphanNodesPageInit } from '../store/subset.actions';
import { selectSubsetOrphanNodesPage } from '../store/subset.selectors';
import { SubsetOrphanNodesSidebarComponent } from './subset-orphan-nodes-sidebar.component';
import { SubsetOrphanNodesTableComponent } from './subset-orphan-nodes-table.component';

@Component({
  selector: 'kpn-subset-orphan-nodes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <kpn-subset-page-header-block
        pageName="orphan-nodes"
        pageTitle="Orphan nodes"
        i18n-pageTitle="@@subset-orphan-nodes.title"
      />

      <kpn-error />

      @if (apiResponse(); as response) {
        <div class="kpn-spacer-above">
          <p>
            <kpn-situation-on [timestamp]="response.situationOn" />
          </p>
          @if (response.result.nodes.length === 0) {
            <p class="kpn-line">
              <kpn-icon-happy />
              <span i18n="@@subset-orphan-nodes.no-routes">No orphan nodes</span>
            </p>
          } @else {
            <kpn-subset-orphan-nodes-table
              [timeInfo]="response.result.timeInfo"
              [nodes]="response.result.nodes"
            />
          }
        </div>
      }
      <kpn-subset-orphan-nodes-sidebar sidebar />
    </kpn-page>
  `,
  standalone: true,
  imports: [
    AsyncPipe,
    ErrorComponent,
    IconHappyComponent,
    PageComponent,
    SituationOnComponent,
    SubsetOrphanNodesSidebarComponent,
    SubsetOrphanNodesTableComponent,
    SubsetPageHeaderBlockComponent,
  ],
})
export class SubsetOrphanNodesPageComponent implements OnInit {
  private readonly store = inject(Store);
  protected readonly apiResponse = this.store.selectSignal(selectSubsetOrphanNodesPage);

  ngOnInit(): void {
    this.store.dispatch(actionSubsetOrphanNodesPageInit());
  }
}
