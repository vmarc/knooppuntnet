import { Component, OnInit } from '@angular/core';
import {AppService} from "../../../../app.service";

@Component({
  selector: 'kpn-network-details-page',
  templateUrl: './network-details-page.component.html',
  styleUrls: ['./network-details-page.component.scss']
})
export class NetworkDetailsPageComponent implements OnInit {

  content = "Loading...";

  constructor(private appService: AppService) {
  }

  ngOnInit() {
    this.appService.networkDetails(3138543).subscribe(content => {
      this.content = content;
    });
  }

}
