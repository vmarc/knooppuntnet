import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs";
import {AppService} from "../../../app.service";
import {ApiResponse} from "../../../kpn/shared/api-response";
import {ChangeSetPage} from "../../../kpn/shared/changes/change-set-page";

@Component({
  selector: 'kpn-change-set-page',
  template: `
    <kpn-page>
      <kpn-toolbar toolbar></kpn-toolbar>
      <kpn-sidenav sidenav></kpn-sidenav>
      <div content>
        <h1>
          Changeset
        </h1>
        <div *ngIf="response">
          <json [object]="response"></json>
        </div>
      </div>
    </kpn-page>
  `
})
export class ChangeSetPageComponent implements OnInit, OnDestroy {

  response: ApiResponse<ChangeSetPage>;
  paramsSubscription: Subscription;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService) {
  }

  ngOnInit() {
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
