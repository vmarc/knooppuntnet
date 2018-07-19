import { Component, OnInit } from '@angular/core';
import {AppService} from "../../../../app.service";

@Component({
  selector: 'kpn-route-page',
  templateUrl: './route-page.component.html',
  styleUrls: ['./route-page.component.scss']
})
export class RoutePageComponent implements OnInit {

  content = "Loading...";

  constructor(private appService: AppService) {
  }

  ngOnInit() {
    this.appService.route(3148634).subscribe(content => {
      this.content = content;
    });
  }

}
