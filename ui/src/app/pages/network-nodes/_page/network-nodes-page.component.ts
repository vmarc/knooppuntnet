import { Component, OnInit } from '@angular/core';
import {AppService} from "../../../app.service";

@Component({
  selector: 'kpn-network-nodes-page',
  templateUrl: './network-nodes-page.component.html',
  styleUrls: ['./network-nodes-page.component.css']
})
export class NetworkNodesPageComponent implements OnInit {

  content = "Loading...";

  constructor(private appService: AppService) {
  }

  ngOnInit() {
    this.appService.networkNodes(3138543).subscribe(content => {
      this.content = content;
    });
  }

}
