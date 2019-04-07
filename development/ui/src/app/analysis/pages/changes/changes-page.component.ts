import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {AppService} from "../../../app.service";
import {PageService} from "../../../components/shared/page.service";
import {ChangesParameters} from "../../../kpn/shared/changes/filter/changes-parameters";

@Component({
  selector: 'kpn-changes-page',
  template: `

    <div>
      <a routerLink="/">Home</a> >
      <a routerLink="/analysis">Analysis</a> >
      Changes
    </div>

    <h1>
      Changes
    </h1>

    <kpn-changes-table [parameters]="parameters"></kpn-changes-table>
  `
})
export class ChangesPageComponent implements OnInit {

  parameters = new ChangesParameters(null, null, null, null, null, null, null, 15, 0, false);

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService) {
  }

  ngOnInit() {
    this.pageService.defaultMenu();
  }

}
