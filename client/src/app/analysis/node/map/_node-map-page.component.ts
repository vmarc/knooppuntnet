import { ChangeDetectionStrategy } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { selectDefined } from '@app/core/core.state';
import { actionNodeMapPageInit } from '../store/node.actions';
import { selectNodeId } from '../store/node.selectors';
import { selectNodeName } from '../store/node.selectors';
import { selectNodeChangeCount } from '../store/node.selectors';
import { selectNodeMapPage } from '../store/node.selectors';

@Component({
  selector: 'kpn-node-map-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li>
        <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a>
      </li>
      <li i18n="@@breadcrumb.node-map">Node map</li>
    </ul>

    <kpn-node-page-header
      pageName="map"
      [nodeId]="nodeId$ | async"
      [nodeName]="nodeName$ | async"
      [changeCount]="changeCount$ | async"
    />

    <kpn-error />

    <div *ngIf="response$ | async as response">
      <div
        *ngIf="!response.result"
        class="kpn-spacer-above"
        i18n="@@node.node-not-found"
      >
        Node not found
      </div>
      <div *ngIf="response.result as page">
        <kpn-node-map />
      </div>
    </div>
  `,
})
export class NodeMapPageComponent implements OnInit {
  protected readonly nodeId$ = this.store.select(selectNodeId);
  protected readonly nodeName$ = this.store.select(selectNodeName);
  protected readonly changeCount$ = this.store.select(selectNodeChangeCount);
  protected readonly response$ = selectDefined(this.store, selectNodeMapPage);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionNodeMapPageInit());
  }
}
