import {Component, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {NetworkNodesPage} from "../../../../kpn/shared/network/network-nodes-page";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'kpn-network-nodes-page',
  templateUrl: './network-nodes-page.component.html',
  styleUrls: ['./network-nodes-page.component.scss']
})
export class NetworkNodesPageComponent implements OnInit {

  response: ApiResponse<NetworkNodesPage>;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService) {
  }

  ngOnInit() {
    const networkId = this.activatedRoute.snapshot.paramMap.get('networkId');
    this.appService.networkNodes(networkId).subscribe(response => {
      this.response = response;
    });
  }

}
