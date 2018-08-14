import {Component, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {ChangesPage} from "../../../../kpn/shared/changes-page";
import {ActivatedRoute} from "@angular/router";
import {ChangesParameters} from "../../../../kpn/shared/changes/filter/changes-parameters";

@Component({
  selector: 'kpn-changes-page',
  templateUrl: './changes-page.component.html',
  styleUrls: ['./changes-page.component.scss']
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
