import {ChangeDetectionStrategy} from '@angular/core';
import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Observable} from 'rxjs';
import {shareReplay} from 'rxjs/operators';
import {map, mergeMap, tap} from 'rxjs/operators';
import {AppService} from '../../../app.service';
import {NetworkDetailsPage} from '@api/common/network/network-details-page';
import {ApiResponse} from '@api/custom/api-response';
import {NetworkService} from '../network.service';

@Component({
  selector: 'kpn-network-details-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

    <kpn-network-page-header
      [networkId]="networkId$ | async"
      pageName="details"
      pageTitle="Details"
      i18n-pageTitle="@@network-details.title">
    </kpn-network-page-header>

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <div *ngIf="!response.result">
        <p i18n="@@network-page.network-not-found">Network not found</p>
      </div>
      <div *ngIf="response.result">
        <kpn-network-details [response]="response"></kpn-network-details>
      </div>
    </div>
  `
})
export class NetworkDetailsPageComponent implements OnInit {

  networkId$: Observable<number>;
  response$: Observable<ApiResponse<NetworkDetailsPage>>;

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
        this.appService.networkDetails(networkId).pipe(
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
