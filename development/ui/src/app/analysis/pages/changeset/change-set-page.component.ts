import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs";
import {AppService} from "../../../app.service";
import {ApiResponse} from "../../../kpn/shared/api-response";
import {ChangeSetPage} from "../../../kpn/shared/changes/change-set-page";
import {PageService} from "../../../shared/page.service";

@Component({
  selector: 'kpn-change-set-page',
  template: `
    <h1>
      Changeset
    </h1>
    <div *ngIf="response">
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
}
