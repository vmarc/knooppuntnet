import {Component, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {NetworkMapPage} from "../../../../kpn/shared/network/network-map-page";

@Component({
  selector: 'kpn-network-map-page',
  templateUrl: './network-map-page.component.html',
  styleUrls: ['./network-map-page.component.scss']
})
export class NetworkMapPageComponent implements OnInit {

  response: ApiResponse<NetworkMapPage>;

  constructor(private appService: AppService) {
  }

  ngOnInit() {
    this.appService.networkMap(3138543).subscribe(response => {
      this.response = response;
    });
  }

}
