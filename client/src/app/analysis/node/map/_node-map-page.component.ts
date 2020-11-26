import {ChangeDetectionStrategy} from '@angular/core';
import {Component, OnDestroy} from '@angular/core';
import {filter} from 'rxjs/operators';
import {PageService} from '../../../components/shared/page.service';
import {selectNodeId} from '../../../core/analysis/node/node.selectors';
import {selectNodeName} from '../../../core/analysis/node/node.selectors';
import {selectNodeChangeCount} from '../../../core/analysis/node/node.selectors';
import {selectNodeMap} from '../../../core/analysis/node/node.selectors';
import {AppState} from '../../../core/core.state';
import {Store} from '@ngrx/store';

@Component({
  selector: 'kpn-node-map-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a></li>
      <li i18n="@@breadcrumb.node-map">Node map</li>
    </ul>

    <kpn-node-page-header
      pageName="map"
      [nodeId]="nodeId$ | async"
      [nodeName]="nodeName$ | async"
      [changeCount]="changeCount$ | async">
    </kpn-node-page-header>

    <kpn-error></kpn-error>

    <div *ngIf="response$ | async as response">
      <div *ngIf="!response.result" class="kpn-spacer-above" i18n="@@node.node-not-found">
        Node not found
      </div>
      <div *ngIf="response.result as page">
        <kpn-node-map [nodeMapInfo]="page.nodeMapInfo"></kpn-node-map>
      </div>
    </div>
  `
})
export class NodeMapPageComponent implements OnDestroy {

  nodeId$ = this.store.select(selectNodeId);
  nodeName$ = this.store.select(selectNodeName);
  changeCount$ = this.store.select(selectNodeChangeCount);

  response$ = this.store.select(selectNodeMap).pipe(filter(x => x !== null));

  constructor(private pageService: PageService,
              private store: Store<AppState>) {
    this.pageService.showFooter = false;
  }

  ngOnDestroy(): void {
    this.pageService.showFooter = true;
  }
}
