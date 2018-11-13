import {Component, OnDestroy, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {ChangeSetPage} from "../../../../kpn/shared/changes/change-set-page";
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs";

@Component({
  selector: 'kpn-change-set-page',
  templateUrl: './change-set-page.component.html',
  styleUrls: ['./change-set-page.component.scss']
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
