import {Component, OnInit} from '@angular/core';
import {AppService} from "../../../app.service";

@Component({
  selector: 'kpn-overview-page',
  template: `
    <kpn-page>
      <kpn-toolbar toolbar></kpn-toolbar>
      <kpn-sidenav sidenav></kpn-sidenav>
      <div content>
        <h1>
          Overview
        </h1>
        <div *ngIf="response">
          <json [object]="response"></json>
        </div>
      </div>
    </kpn-page>
  `
})
export class OverviewPageComponent implements OnInit {

  response: any;

  constructor(private appService: AppService) {
  }

  ngOnInit() {
    this.appService.overview().subscribe(response => {
      this.response = response;
    });
  }

}
