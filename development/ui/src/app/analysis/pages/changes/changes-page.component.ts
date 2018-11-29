import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {AppService} from "../../../app.service";
import {ApiResponse} from "../../../kpn/shared/api-response";
import {ChangesPage} from "../../../kpn/shared/changes-page";
import {ChangesParameters} from "../../../kpn/shared/changes/filter/changes-parameters";
import {PageService} from "../../../shared/page.service";

@Component({
  selector: 'kpn-changes-page',
  template: `
    <h1>
      Changes
    </h1>
    <kpn-changes-table [parameters]="parameters"></kpn-changes-table>
  `
})
export class ChangesPageComponent implements OnInit {

  response: ApiResponse<ChangesPage>;

  parameters = new ChangesParameters(null, null, null, null, null, null, null, 15, 0, false);

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService) {
  }

  ngOnInit() {
    this.pageService.defaultMenu();
  }

}
