import { ChangeDetectionStrategy } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NodeInfo } from '@api/common/node-info';
import { SubsetInfo } from '@api/common/subset/subset-info';
import { SubsetOrphanNodesPage } from '@api/common/subset/subset-orphan-nodes-page';
import { ApiResponse } from '@api/custom/api-response';
import { Subset } from '@api/custom/subset';
import { List } from 'immutable';
import { Observable } from 'rxjs';
import { BehaviorSubject } from 'rxjs';
import { map, mergeMap, tap } from 'rxjs/operators';
import { AppService } from '../../../app.service';
import { Util } from '../../../components/shared/util';
import { SubsetCacheService } from '../../../services/subset-cache.service';

@Component({
  selector: 'kpn-subset-orphan-nodes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-subset-page-header-block
      [subset]="subset$ | async"
      [subsetInfo$]="subsetInfo$"
      pageName="orphan-nodes"
      pageTitle="Orphan nodes"
      i18n-pageTitle="@@subset-orphan-nodes.title"
    >
    </kpn-subset-page-header-block>

    <kpn-error></kpn-error>

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <p>
        <kpn-situation-on [timestamp]="response.situationOn"></kpn-situation-on>
      </p>
      <p *ngIf="nodes.isEmpty()" class="kpn-line">
        <kpn-icon-happy></kpn-icon-happy>
        <span i18n="@@subset-orphan-nodes.no-routes">No orphan nodes</span>
      </p>
      <div *ngIf="!nodes.isEmpty()">
        <kpn-subset-orphan-nodes-table
          [timeInfo]="response.result.timeInfo"
          [nodes]="nodes"
        >
        </kpn-subset-orphan-nodes-table>
      </div>
    </div>
  `,
})
export class SubsetOrphanNodesPageComponent implements OnInit {
  subset$: Observable<Subset>;
  subsetInfo$ = new BehaviorSubject<SubsetInfo>(null);
  response$: Observable<ApiResponse<SubsetOrphanNodesPage>>;

  nodes: List<NodeInfo> = List();

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
          this.subsetCacheService.getSubsetInfo(subset.key())
        )
      )
    );
    this.response$ = this.subset$.pipe(
      mergeMap((subset) =>
        this.appService.subsetOrphanNodes(subset).pipe(
          tap((response) => {
            if (response.result) {
              this.nodes = response.result.rows;
              this.subsetCacheService.setSubsetInfo(
                subset.key(),
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
