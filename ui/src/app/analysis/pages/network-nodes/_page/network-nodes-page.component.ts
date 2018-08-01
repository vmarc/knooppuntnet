import {Component, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {NetworkNodesPage} from "../../../../kpn/shared/network/network-nodes-page";

@Component({
  selector: 'kpn-network-nodes-page',
  templateUrl: './network-nodes-page.component.html',
  styleUrls: ['./network-nodes-page.component.scss']
})
export class NetworkNodesPageComponent implements OnInit {

  response: ApiResponse<NetworkNodesPage>;

  constructor(private appService: AppService) {
  }

  ngOnInit() {
    this.appService.networkNodes(3138543).subscribe(response => {
      this.response = response;
    });
  }

}
