import {ChangeDetectionStrategy} from '@angular/core';
import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Subject} from 'rxjs';
import {Observable} from 'rxjs';
import {map, mergeMap, tap} from 'rxjs/operators';
import {AppService} from '../../../app.service';
import {PageService} from '../../../components/shared/page.service';
import {NodeMapInfo} from '../../../kpn/api/common/node-map-info';
import {NodeMapPage} from '../../../kpn/api/common/node/node-map-page';
import {ApiResponse} from '../../../kpn/api/custom/api-response';

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
      <div *ngIf="response.result">
        <kpn-node-map [nodeMapInfo]="nodeMapInfo"></kpn-node-map>
      </div>
    </div>
  `
})
export class NodeMapPageComponent implements OnInit, OnDestroy {

  response$: Observable<ApiResponse<NodeMapPage>>;

  nodeId$ = new Subject<string>();
  nodeName$ = new Subject<string>();
  changeCount$ = new Subject<number>();

  nodeMapInfo: NodeMapInfo;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService) {
    this.pageService.showFooter = false;
  }

  ngOnInit(): void {
    this.nodeName$.next(history.state.nodeName);
    this.changeCount$.next(history.state.changeCount);
    this.response$ = this.activatedRoute.params.pipe(
      map(params => params['nodeId']),
      tap(nodeId => this.nodeId$.next(nodeId)),
      mergeMap(nodeId => this.appService.nodeMap(nodeId).pipe(
        tap(response => {
          if (response.result) {
            this.nodeMapInfo = response.result.nodeMapInfo;
            this.nodeName$.next(response.result.nodeMapInfo.name);
            this.changeCount$.next(response.result.changeCount);
          }
        })
      ))
    );
  }

  ngOnDestroy(): void {
    this.pageService.showFooter = true;
  }
}
