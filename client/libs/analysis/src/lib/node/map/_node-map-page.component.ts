import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { OnDestroy } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ErrorComponent } from '@app/components/shared/error';
import { PageComponent } from '@app/components/shared/page';
import { Store } from '@ngrx/store';
import { NodePageHeaderComponent } from '../components/node-page-header.component';
import { NodeDetailsSidebarComponent } from '../details/node-details-sidebar.component';
import { actionNodeMapPageDestroy } from '../store/node.actions';
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
    <kpn-page>
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
      <kpn-node-details-sidebar sidebar />
    </kpn-page>
  `,
  standalone: true,
  imports: [
    AsyncPipe,
    ErrorComponent,
    NgIf,
    NodeDetailsSidebarComponent,
    NodeMapComponent,
    NodePageHeaderComponent,
    PageComponent,
    RouterLink,
  ],
})
export class NodeMapPageComponent implements OnInit, OnDestroy {
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

  ngOnDestroy(): void {
    this.store.dispatch(actionNodeMapPageDestroy());
  }
}
