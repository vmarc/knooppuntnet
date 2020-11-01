import {ChangeDetectionStrategy} from "@angular/core";
import {Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Observable} from "rxjs";
import {shareReplay} from "rxjs/operators";
import {map, mergeMap, tap} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {PageService} from "../../../components/shared/page.service";
import {NetworkMapPage} from "../../../kpn/api/common/network/network-map-page";
import {ApiResponse} from "../../../kpn/api/custom/api-response";
import {NetworkService} from "../network.service";

@Component({
  selector: "kpn-network-map-page",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

    <kpn-network-page-header
      [networkId]="networkId$ | async"
      pageName="map"
      pageTitle="Map"
      i18n-pageTitle="@@network-map.title">
    </kpn-network-page-header>

    <div *ngIf="response$ | async as response">
      <div *ngIf="!response.result">
        <p i18n="@@network-page.network-not-found">Network not found</p>
      </div>
      <div *ngIf="response.result">
        <kpn-network-map [page]="response.result"></kpn-network-map>
      </div>
    </div>
  `
})
export class NetworkMapPageComponent implements OnInit, OnDestroy {

  networkId$: Observable<number>;
  response$: Observable<ApiResponse<NetworkMapPage>>;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService,
              private networkService: NetworkService) {
    this.pageService.showFooter = false;
  }

  ngOnInit(): void {

    this.networkId$ = this.activatedRoute.params.pipe(
      map(params => +params["networkId"]),
      tap(networkId => this.networkService.init(networkId)),
      shareReplay()
    );

    this.response$ = this.networkId$.pipe(
      mergeMap(networkId =>
        this.appService.networkMap(networkId).pipe(
          tap(response => {
            if (response.result) {
              this.networkService.update(networkId, response.result.networkSummary);
            }
          })
        )
      )
    );
  }

  ngOnDestroy(): void {
    this.pageService.showFooter = true;
  }
}
