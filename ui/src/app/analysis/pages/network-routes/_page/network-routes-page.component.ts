import { Component, OnInit } from '@angular/core';
import {AppService} from "../../../../app.service";

@Component({
  selector: 'kpn-network-routes-page',
  templateUrl: './network-routes-page.component.html',
  styleUrls: ['./network-routes-page.component.scss']
})
export class NetworkRoutesPageComponent implements OnInit {

  content = "Loading...";

  constructor(private appService: AppService) {
  }

  ngOnInit() {
    this.appService.networkRoutes(3138543).subscribe(content => {
      this.content = content;
    });
  }

}
