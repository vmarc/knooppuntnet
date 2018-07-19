import { Component, OnInit } from '@angular/core';
import {AppService} from "../../../../app.service";

@Component({
  selector: 'kpn-network-map-page',
  templateUrl: './network-map-page.component.html',
  styleUrls: ['./network-map-page.component.scss']
})
export class NetworkMapPageComponent implements OnInit {

  content = "Loading...";

  constructor(private appService: AppService) {
  }

  ngOnInit() {
    this.appService.networkMap(3138543).subscribe(content => {
      this.content = content;
    });
  }

}
