import { ChangeDetectionStrategy } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NetworkNodesPage } from '@api/common/network/network-nodes-page';
import { ApiResponse } from '@api/custom/api-response';
import { Observable } from 'rxjs';
import { shareReplay } from 'rxjs/operators';
import { map, mergeMap, tap } from 'rxjs/operators';
import { AppService } from '../../../app.service';
import { NetworkService } from '../network.service';

@Component({
  selector: 'kpn-network-nodes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-network-page-header
      [networkId]="networkId$ | async"
      pageName="nodes"
      pageTitle="Nodes"
      i18n-pageTitle="@@network-nodes.title"
    ></kpn-network-page-header>

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <div *ngIf="!response.result">
        <p i18n="@@network-page.network-not-found">Network not found</p>
      </div>
      <div *ngIf="response.result">
        <p>
          <kpn-situation-on
            [timestamp]="response.situationOn"
          ></kpn-situation-on>
        </p>
        <div
          *ngIf="response.result.nodes.length == 0"
          i18n="@@network-nodes.no-nodes"
        >
          No network nodes in network
        </div>
        <kpn-network-node-table
          *ngIf="response.result.nodes.length > 0"
          [networkType]="response.result.networkType"
          [timeInfo]="response.result.timeInfo"
          [surveyDateInfo]="response.result.surveyDateInfo"
          [nodes]="response.result.nodes"
        ></kpn-network-node-table>
      </div>
    </div>
  `,
})
export class NetworkNodesPageComponent implements OnInit {
  networkId$: Observable<number>;
  response$: Observable<ApiResponse<NetworkNodesPage>>;

  constructor(
    private activatedRoute: ActivatedRoute,
    private appService: AppService,
    private networkService: NetworkService
  ) {}

  ngOnInit(): void {
    this.networkId$ = this.activatedRoute.params.pipe(
      map((params) => +params['networkId']),
      tap((networkId) => this.networkService.init(networkId)),
      shareReplay()
    );

    this.response$ = this.networkId$.pipe(
      mergeMap((networkId) =>
        this.appService.networkNodes(networkId).pipe(
          tap((response) => {
            if (response.result) {
              this.networkService.update(
                networkId,
                response.result.networkSummary
              );
            }
          })
        )
      )
    );
  }
}
