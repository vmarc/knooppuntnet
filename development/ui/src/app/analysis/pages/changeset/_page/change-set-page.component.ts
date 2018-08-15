import {Component, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {ChangeSetPage} from "../../../../kpn/shared/changes/change-set-page";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'kpn-change-set-page',
  templateUrl: './change-set-page.component.html',
  styleUrls: ['./change-set-page.component.scss']
})
export class ChangeSetPageComponent implements OnInit {

  response: ApiResponse<ChangeSetPage>;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService) {
  }

  ngOnInit() {
    const changeSetId = this.activatedRoute.snapshot.paramMap.get('changeSetId');
    const replicationNumber = this.activatedRoute.snapshot.paramMap.get('replicationNumber');
    this.appService.changeSet(changeSetId, replicationNumber).subscribe(response => {
      this.response = response;
    });
  }

}
