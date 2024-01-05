import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { ChangeSetPage } from '@api/common/changes';
import { ApiResponse } from '@api/custom';
import { Util } from '@app/components/shared';
import { PageComponent } from '@app/components/shared/page';
import { ApiService } from '@app/services';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { mergeMap } from 'rxjs/operators';
import { AnalysisSidebarComponent } from '../../analysis/analysis-sidebar.component';
import { ChangeSetHeaderComponent } from './change-set-header.component';
import { ChangeSetLocationChangesComponent } from './change-set-location-changes.component';
import { ChangeSetNetworkChangesComponent } from './change-set-network-changes.component';
import { ChangeSetOrphanNodeChangesComponent } from './change-set-orphan-node-changes.component';
import { ChangeSetOrphanRouteChangesComponent } from './change-set-orphan-route-changes.component';

class ChangeSetKey {
  constructor(
    readonly changeSetId: string,
    readonly replicationNumber: string
  ) {}
}

@Component({
  selector: 'kpn-change-set-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <h1>
        <ng-container i18n="@@change-set.title">Changeset</ng-container>
        {{ changeSetTitle }}
      </h1>

      @if (response$ | async; as response) {
        @if (!response.result) {
          <div i18n="@@changeset.not-found">Changeset not found</div>
        } @else {
          <kpn-change-set-header [page]="response.result" />
          <kpn-change-set-location-changes [changess]="response.result.summary.locationChanges" />
          <kpn-change-set-network-changes [page]="response.result" />
          <kpn-change-set-orphan-node-changes [page]="response.result" />
          <kpn-change-set-orphan-route-changes [page]="response.result" />
        }
      }
      <kpn-analysis-sidebar sidebar />
    </kpn-page>
  `,
  standalone: true,
  imports: [
    AnalysisSidebarComponent,
    AsyncPipe,
    ChangeSetHeaderComponent,
    ChangeSetLocationChangesComponent,
    ChangeSetNetworkChangesComponent,
    ChangeSetOrphanNodeChangesComponent,
    ChangeSetOrphanRouteChangesComponent,
    PageComponent,
  ],
})
export class ChangeSetPageComponent implements OnInit {
  private readonly activatedRoute = inject(ActivatedRoute);
  private readonly apiService = inject(ApiService);
  protected response$: Observable<ApiResponse<ChangeSetPage>>;
  protected changeSetTitle = '';

  ngOnInit(): void {
    this.response$ = this.activatedRoute.params.pipe(
      map((params) => this.interpretParams(params)),
      mergeMap((key) => this.apiService.changeSet(key.changeSetId, key.replicationNumber)),
      tap((response) => {
        if (response.result) {
          const a = response.result.summary.key.changeSetId;
          const b = Util.replicationName(response.result.summary.key.replicationNumber);
          this.changeSetTitle = a + ' ' + b;
        }
      })
    );
  }

  private interpretParams(params: Params): ChangeSetKey {
    const changeSetId = params['changeSetId'];
    const replicationNumber = params['replicationNumber'];
    return new ChangeSetKey(changeSetId, replicationNumber);
  }
}
