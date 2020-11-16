import {ChangeDetectionStrategy} from '@angular/core';
import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Observable} from 'rxjs';
import {shareReplay} from 'rxjs/operators';
import {map, mergeMap, tap} from 'rxjs/operators';
import {AppService} from '../../../app.service';
import {NetworkFactsPage} from '../../../kpn/api/common/network/network-facts-page';
import {ApiResponse} from '../../../kpn/api/custom/api-response';
import {NetworkService} from '../network.service';

@Component({
  selector: 'kpn-network-facts-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

    <kpn-network-page-header
      [networkId]="networkId$ | async"
      pageName="facts"
      pageTitle="Facts"
      i18n-pageTitle="@@network-facts.title">
    </kpn-network-page-header>

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <div *ngIf="!response.result" i18n="@@network-page.network-not-found">
        Network not found
      </div>
      <div *ngIf="response.result">

        <kpn-situation-on [timestamp]="response.situationOn"></kpn-situation-on>

        <p *ngIf="response.result.facts.isEmpty()" class="kpn-line">
          <span i18n="@@network-facts.no-facts">No facts</span>
          <kpn-icon-happy></kpn-icon-happy>
        </p>

        <kpn-items *ngIf="!response.result.facts.isEmpty()">
          <kpn-item *ngFor="let fact of response.result.facts; let i=index" [index]="i">
            <kpn-network-fact [fact]="fact"></kpn-network-fact>
          </kpn-item>
        </kpn-items>
      </div>
    </div>
  `
})
export class NetworkFactsPageComponent implements OnInit {

  networkId$: Observable<number>;
  response$: Observable<ApiResponse<NetworkFactsPage>>;

  constructor(public networkService: NetworkService,
              private activatedRoute: ActivatedRoute,
              private appService: AppService) {
  }

  ngOnInit(): void {

    this.networkId$ = this.activatedRoute.params.pipe(
      map(params => +params['networkId']),
      tap(networkId => this.networkService.init(networkId)),
      shareReplay()
    );

    this.response$ = this.networkId$.pipe(
      mergeMap(networkId =>
        this.appService.networkFacts(networkId).pipe(
          tap(response => {
            if (response.result) {
              this.networkService.update(networkId, response.result.networkSummary);
            }
          })
        )
      )
    );
  }
}
