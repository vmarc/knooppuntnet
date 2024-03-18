import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ErrorComponent } from '@app/components/shared/error';
import { IconHappyComponent } from '@app/components/shared/icon';
import { PageComponent } from '@app/components/shared/page';
import { SituationOnComponent } from '@app/components/shared/timestamp';
import { RouterService } from '../../../shared/services/router.service';
import { SubsetPageHeaderBlockComponent } from '../components/subset-page-header-block.component';
import { SubsetOrphanNodesSidebarComponent } from './components/subset-orphan-nodes-sidebar.component';
import { SubsetOrphanNodesTableComponent } from './components/subset-orphan-nodes-table.component';
import { SubsetOrphanNodesStore } from './subset-orphan-nodes.store';

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

      @if (store.response(); as response) {
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
  providers: [SubsetOrphanNodesStore, RouterService],
  standalone: true,
  imports: [
    ErrorComponent,
    IconHappyComponent,
    PageComponent,
    SituationOnComponent,
    SubsetOrphanNodesSidebarComponent,
    SubsetOrphanNodesTableComponent,
    SubsetPageHeaderBlockComponent,
  ],
})
export class SubsetOrphanNodesPageComponent {
  protected readonly store = inject(SubsetOrphanNodesStore);
}
