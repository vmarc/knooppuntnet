import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {AppService} from "../../../app.service";
import {ApiResponse} from "../../../kpn/shared/api-response";
import {ChangesPage} from "../../../kpn/shared/changes-page";
import {ChangesParameters} from "../../../kpn/shared/changes/filter/changes-parameters";

@Component({
  selector: 'kpn-changes-page',
  template: `
    <kpn-page>
      <kpn-toolbar toolbar></kpn-toolbar>
      <kpn-sidenav sidenav></kpn-sidenav>
      <div content>
        <h1>
          Changes
        </h1>
        <kpn-changes-table [parameters]="parameters"></kpn-changes-table>
      </div>
    </kpn-page>
  `
})
export class ChangesPageComponent implements OnInit {

  response: ApiResponse<ChangesPage>;

  parameters = new ChangesParameters();

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService) {
    this.parameters.itemsPerPage = 15;
    this.parameters.impact = true;
  }

  ngOnInit() {
  }

}
