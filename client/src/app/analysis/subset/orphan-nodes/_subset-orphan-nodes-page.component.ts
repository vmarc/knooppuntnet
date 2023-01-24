import { ChangeDetectionStrategy } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { AppState } from '../../../core/core.state';
import { actionSubsetOrphanNodesPageInit } from '../store/subset.actions';
import { selectSubsetOrphanNodesPage } from '../store/subset.selectors';

@Component({
  selector: 'kpn-subset-orphan-nodes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-subset-page-header-block
      pageName="orphan-nodes"
      pageTitle="Orphan nodes"
      i18n-pageTitle="@@subset-orphan-nodes.title"
    />

    <kpn-error/>

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <p>
        <kpn-situation-on [timestamp]="response.situationOn"/>
      </p>
      <p *ngIf="response.result.nodes.length === 0" class="kpn-line">
        <kpn-icon-happy/>
        <span i18n="@@subset-orphan-nodes.no-routes">No orphan nodes</span>
      </p>
      <div *ngIf="response.result.nodes.length > 0">
        <kpn-subset-orphan-nodes-table
          [timeInfo]="response.result.timeInfo"
          [nodes]="response.result.nodes"
        />
      </div>
    </div>
  `,
})
export class SubsetOrphanNodesPageComponent implements OnInit {
  readonly response$ = this.store.select(selectSubsetOrphanNodesPage);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionSubsetOrphanNodesPageInit());
  }
}
