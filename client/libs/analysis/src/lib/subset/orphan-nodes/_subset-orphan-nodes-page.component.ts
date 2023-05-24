import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
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

      <div *ngIf="apiResponse() as response" class="kpn-spacer-above">
        <p>
          <kpn-situation-on [timestamp]="response.situationOn" />
        </p>
        <p *ngIf="response.result.nodes.length === 0" class="kpn-line">
          <kpn-icon-happy />
          <span i18n="@@subset-orphan-nodes.no-routes">No orphan nodes</span>
        </p>
        <div *ngIf="response.result.nodes.length > 0">
          <kpn-subset-orphan-nodes-table
            [timeInfo]="response.result.timeInfo"
            [nodes]="response.result.nodes"
          />
        </div>
      </div>
      <kpn-subset-orphan-nodes-sidebar sidebar />
    </kpn-page>
  `,
  standalone: true,
  imports: [
    AsyncPipe,
    ErrorComponent,
    IconHappyComponent,
    NgIf,
    PageComponent,
    SituationOnComponent,
    SubsetOrphanNodesSidebarComponent,
    SubsetOrphanNodesTableComponent,
    SubsetPageHeaderBlockComponent,
  ],
})
export class SubsetOrphanNodesPageComponent implements OnInit {
  readonly apiResponse = this.store.selectSignal(selectSubsetOrphanNodesPage);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionSubsetOrphanNodesPageInit());
  }
}
