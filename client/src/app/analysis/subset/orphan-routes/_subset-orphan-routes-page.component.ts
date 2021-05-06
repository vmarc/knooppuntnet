import { ChangeDetectionStrategy } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { SubsetInfo } from '@api/common/subset/subset-info';
import { SubsetOrphanRoutesPage } from '@api/common/subset/subset-orphan-routes-page';
import { ApiResponse } from '@api/custom/api-response';
import { Subset } from '@api/custom/subset';
import { Observable } from 'rxjs';
import { BehaviorSubject } from 'rxjs';
import { map, mergeMap, tap } from 'rxjs/operators';
import { AppService } from '../../../app.service';
import { Util } from '../../../components/shared/util';
import { Subsets } from '../../../kpn/common/subsets';
import { SubsetCacheService } from '../../../services/subset-cache.service';

@Component({
  selector: 'kpn-subset-orphan-routes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-subset-page-header-block
      [subset]="subset$ | async"
      [subsetInfo$]="subsetInfo$"
      pageName="orphan-routes"
      pageTitle="Free routes"
      i18n-pageTitle="@@subset-orphan-routes.title"
    >
    </kpn-subset-page-header-block>

    <kpn-error></kpn-error>

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <p>
        <kpn-situation-on [timestamp]="response.situationOn"></kpn-situation-on>
      </p>
      <p *ngIf="response.result.routes.length === 0" class="kpn-line">
        <span i18n="@@subset-orphan-routes.no-routes">No free routes</span>
      </p>
      <div *ngIf="response.result.routes.length > 0">
        <kpn-subset-orphan-routes-table
          [timeInfo]="response.result.timeInfo"
          [orphanRoutes]="response.result.routes"
        >
        </kpn-subset-orphan-routes-table>
      </div>
    </div>
  `,
})
export class SubsetOrphanRoutesPageComponent implements OnInit {
  subset$: Observable<Subset>;
  subsetInfo$ = new BehaviorSubject<SubsetInfo>(null);
  response$: Observable<ApiResponse<SubsetOrphanRoutesPage>>;

  constructor(
    private activatedRoute: ActivatedRoute,
    private appService: AppService,
    private subsetCacheService: SubsetCacheService
  ) {}

  ngOnInit(): void {
    this.subset$ = this.activatedRoute.params.pipe(
      map((params) => Util.subsetInRoute(params)),
      tap((subset) =>
        this.subsetInfo$.next(
          this.subsetCacheService.getSubsetInfo(Subsets.key(subset))
        )
      )
    );
    this.response$ = this.subset$.pipe(
      mergeMap((subset) =>
        this.appService.subsetOrphanRoutes(subset).pipe(
          tap((response) => {
            if (response.result) {
              this.subsetCacheService.setSubsetInfo(
                Subsets.key(subset),
                response.result.subsetInfo
              );
              this.subsetInfo$.next(response.result.subsetInfo);
            }
          })
        )
      )
    );
  }
}
