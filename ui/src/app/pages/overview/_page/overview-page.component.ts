import { Component, OnInit } from '@angular/core';
import {AppService} from "../../../app.service";

@Component({
  selector: 'kpn-overview-page',
  templateUrl: './overview-page.component.html',
  styleUrls: ['./overview-page.component.css']
})
export class OverviewPageComponent implements OnInit {

  content = "Loading...";

  constructor(private appService: AppService) {
  }

  ngOnInit() {
    this.appService.overview().subscribe(content => {
      this.content = content;
    });
  }

}
