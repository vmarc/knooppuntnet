import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ErrorComponent } from '@app/components/shared/error';
import { Store } from '@ngrx/store';
import { NodePageHeaderComponent } from '../components/node-page-header.component';
import { actionNodeMapPageInit } from '../store/node.actions';
import { selectNodeId } from '../store/node.selectors';
import { selectNodeName } from '../store/node.selectors';
import { selectNodeChangeCount } from '../store/node.selectors';
import { selectNodeMapPage } from '../store/node.selectors';
import { NodeMapComponent } from './node-map.component';

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
      [nodeId]="nodeId()"
      [nodeName]="nodeName()"
      [changeCount]="changeCount()"
    />

    <kpn-error />

    <div *ngIf="apiResponse() as response">
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
  standalone: true,
  imports: [
    RouterLink,
    NodePageHeaderComponent,
    ErrorComponent,
    NgIf,
    NodeMapComponent,
    AsyncPipe,
  ],
})
export class NodeMapPageComponent implements OnInit {
  protected readonly nodeId = this.store.selectSignal(selectNodeId);
  protected readonly nodeName = this.store.selectSignal(selectNodeName);
  protected readonly changeCount = this.store.selectSignal(
    selectNodeChangeCount
  );
  protected readonly apiResponse = this.store.selectSignal(selectNodeMapPage);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionNodeMapPageInit());
  }
}
