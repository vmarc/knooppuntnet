import { ChangeDetectionStrategy } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { LocationNodesPage } from '@api/common/location/location-nodes-page';
import { Store } from '@ngrx/store';
import { AppState } from '../../../core/core.state';
import { actionLocationNodesPageIndex } from '../store/location.actions';
import { selectLocationNodesPageIndex } from '../store/location.selectors';

@Component({
  selector: 'kpn-location-nodes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div
      *ngIf="page.nodes.length === 0"
      class="kpn-spacer-above"
      i18n="@@location-nodes.no-nodes"
    >
      No nodes
    </div>
    <kpn-location-node-table
      *ngIf="page.nodes.length > 0"
      [pageIndex]="pageIndex$ | async"
      (page)="pageChanged($event)"
      [timeInfo]="page.timeInfo"
      [nodes]="page.nodes"
      [nodeCount]="page.nodeCount"
    >
    </kpn-location-node-table>
  `,
})
export class LocationNodesComponent {
  @Input() page: LocationNodesPage;

  readonly pageIndex$ = this.store.select(selectLocationNodesPageIndex);

  constructor(private store: Store<AppState>) {}

  pageChanged(pageIndex: number): void {
    window.scroll(0, 0);
    this.store.dispatch(actionLocationNodesPageIndex({ pageIndex }));
  }
}
