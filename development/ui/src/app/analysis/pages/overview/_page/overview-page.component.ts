import {Component, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";

@Component({
  selector: 'kpn-overview-page',
  templateUrl: './overview-page.component.html',
  styleUrls: ['./overview-page.component.scss']
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
