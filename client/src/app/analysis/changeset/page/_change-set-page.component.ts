import {ChangeDetectionStrategy} from '@angular/core';
import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params} from '@angular/router';
import {ChangeSetPage} from '@api/common/changes/change-set-page';
import {ApiResponse} from '@api/custom/api-response';
import {Observable} from 'rxjs';
import {tap} from 'rxjs/operators';
import {map, mergeMap} from 'rxjs/operators';
import {AppService} from '../../../app.service';
import {PageService} from '../../../components/shared/page.service';
import {Util} from '../../../components/shared/util';

class ChangeSetKey {
  constructor(readonly changeSetId: string,
              readonly replicationNumber: string) {
  }
}

@Component({
  selector: 'kpn-change-set-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <h1>
      <ng-container i18n="@@change-set.title">Changeset</ng-container>
      {{changeSetTitle}}
    </h1>

    <div *ngIf="response$ | async as response">
      <div *ngIf="!response.result" i18n="@@changeset.not-found">
        Changeset not found
      </div>
      <div *ngIf="response.result">
        <kpn-change-set-header [page]="response.result"></kpn-change-set-header>
        <kpn-change-set-network-changes [page]="response.result"></kpn-change-set-network-changes>
        <kpn-change-set-orphan-node-changes [page]="response.result"></kpn-change-set-orphan-node-changes>
        <kpn-change-set-orphan-route-changes [page]="response.result"></kpn-change-set-orphan-route-changes>
      </div>
    </div>
  `
})
export class ChangeSetPageComponent implements OnInit {

  response$: Observable<ApiResponse<ChangeSetPage>>;
  changeSetTitle = '';

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService) {
  }

  ngOnInit(): void {
    this.pageService.defaultMenu();
    this.response$ = this.activatedRoute.params.pipe(
      map(params => this.interpreteParams(params)),
      mergeMap(key => this.appService.changeSet(key.changeSetId, key.replicationNumber)),
      tap(response => {
        if (response.result) {
          const a = response.result.summary.key.changeSetId;
          const b = Util.replicationName(response.result.summary.key.replicationNumber);
          this.changeSetTitle = a + ' ' + b;
        }
      })
    );
  }

  private interpreteParams(params: Params): ChangeSetKey {
    const changeSetId = params['changeSetId'];
    const replicationNumber = params['replicationNumber'];
    return new ChangeSetKey(changeSetId, replicationNumber);
  }

}
