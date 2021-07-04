import { ChangeDetectionStrategy } from '@angular/core';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { Bounds } from '@api/common/bounds';
import { SubsetInfo } from '@api/common/subset/subset-info';
import { SubsetMapNetwork } from '@api/common/subset/subset-map-network';
import { SubsetMapPage } from '@api/common/subset/subset-map-page';
import { ApiResponse } from '@api/custom/api-response';
import { Subset } from '@api/custom/subset';
import { List } from 'immutable';
import { Observable } from 'rxjs';
import { BehaviorSubject } from 'rxjs';
import { map, mergeMap, tap } from 'rxjs/operators';
import { AppService } from '../../../app.service';
import { PageService } from '../../../components/shared/page.service';
import { Util } from '../../../components/shared/util';
import { NetworkCacheService } from '../../../services/network-cache.service';
import { SubsetCacheService } from '../../../services/subset-cache.service';
import { SubsetMapNetworkDialogComponent } from './subset-map-network-dialog.component';

@Component({
  selector: 'kpn-subset-map-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-subset-page-header-block
      [subset]="subset$ | async"
      [subsetInfo$]="subsetInfo$"
      pageName="map"
      pageTitle="Map"
      i18n-pageTitle="@@subset-map.title"
    >
    </kpn-subset-page-header-block>

    <kpn-error></kpn-error>

    <div *ngIf="response$ | async">
      <kpn-subset-map
        [bounds]="bounds"
        [networks]="networks"
        (networkClicked)="networkClicked($event)"
      >
      </kpn-subset-map>
    </div>
  `,
})
export class SubsetMapPageComponent implements OnInit, OnDestroy {
  subset$: Observable<Subset>;
  subsetInfo$ = new BehaviorSubject<SubsetInfo>(null);
  response$: Observable<ApiResponse<SubsetMapPage>>;

  bounds: Bounds;
  networks: List<SubsetMapNetwork>;

  constructor(
    private activatedRoute: ActivatedRoute,
    private appService: AppService,
    private pageService: PageService,
    private networkCacheService: NetworkCacheService,
    private subsetCacheService: SubsetCacheService,
    private dialog: MatDialog
  ) {
    this.pageService.showFooter = false;
  }

  ngOnInit(): void {
    this.subset$ = this.activatedRoute.params.pipe(
      map((params) => Util.subsetInRoute(params)),
      tap((subset) =>
        this.subsetInfo$.next(
          this.subsetCacheService.getSubsetInfo(subset.key())
        )
      )
    );
    this.response$ = this.subset$.pipe(
      mergeMap((subset) =>
        this.appService.subsetMap(subset).pipe(
          tap((response) => {
            if (response.result) {
              this.bounds = response.result.bounds;
              this.networks = response.result.networks;
              this.subsetCacheService.setSubsetInfo(
                subset.key(),
                response.result.subsetInfo
              );
              this.subsetInfo$.next(response.result.subsetInfo);
              response.result.networks.forEach((networkAttributes) => {
                this.networkCacheService.setNetworkName(
                  networkAttributes.id,
                  networkAttributes.name
                );
              });
            }
          })
        )
      )
    );
  }

  ngOnDestroy(): void {
    this.pageService.showFooter = true;
  }

  networkClicked(networkId: number): void {
    const network = this.networks.find((n) => n.id === networkId);
    if (network) {
      this.dialog.open(SubsetMapNetworkDialogComponent, {
        data: network,
        maxWidth: 600,
      });
    }
  }
}
