import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs";
import {AppService} from "../../../app.service";
import {ApiResponse} from "../../../kpn/shared/api-response";
import {ChangeSetPage} from "../../../kpn/shared/changes/change-set-page";
import {PageService} from "../../../components/shared/page.service";
import {Util} from "../../../components/shared/util";

@Component({
  selector: 'kpn-change-set-page',
  template: `
    <h1>
      <ng-container i18nX="@@kpn-change-set-page.title">Changeset</ng-container> <!-- Wijzigingenset -->
      {{changeSetTitle()}}
    </h1>

    <div *ngIf="response">
      <kpn-change-set-header [page]="response.result"></kpn-change-set-header>
      <kpn-change-set-network-diff-details [page]="response.result"></kpn-change-set-network-diff-details>
      <kpn-change-set-orphan-node-changes [page]="response.result"></kpn-change-set-orphan-node-changes>
      <kpn-change-set-orphan-route-changes [page]="response.result"></kpn-change-set-orphan-route-changes>
      <json [object]="response"></json>
    </div>
  `
})
export class ChangeSetPageComponent implements OnInit, OnDestroy {

  response: ApiResponse<ChangeSetPage>;
  paramsSubscription: Subscription;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService) {
  }

  ngOnInit() {
    this.pageService.defaultMenu();
    this.paramsSubscription = this.activatedRoute.params.subscribe(params => {
      const changeSetId = params['changeSetId'];
      const replicationNumber = params['replicationNumber'];
      this.appService.changeSet(changeSetId, replicationNumber).subscribe(response => {
        this.response = response;
      });
    });
  }

  ngOnDestroy() {
    this.paramsSubscription.unsubscribe();
  }

  changeSetTitle() {
    if (this.response) {
      const a = this.response.result.summary.key.changeSetId;
      const b = Util.replicationName(this.response.result.summary.key.replicationNumber);
      return a + " " + b;
    }
    return "";
  }

}
