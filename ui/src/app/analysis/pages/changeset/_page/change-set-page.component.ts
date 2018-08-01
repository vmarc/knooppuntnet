import {Component, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {ChangeSetPage} from "../../../../kpn/shared/changes/change-set-page";

@Component({
  selector: 'kpn-change-set-page',
  templateUrl: './change-set-page.component.html',
  styleUrls: ['./change-set-page.component.scss']
})
export class ChangeSetPageComponent implements OnInit {

  response: ApiResponse<ChangeSetPage>;

  constructor(private appService: AppService) {
  }

  ngOnInit() {
    this.appService.changeSet(55067698, 2778253).subscribe(response => {
      this.response = response;
    });
  }

}
