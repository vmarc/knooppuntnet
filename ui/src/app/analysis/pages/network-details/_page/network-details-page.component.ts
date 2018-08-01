import {Component, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {NetworkDetailsPage} from "../../../../kpn/shared/network/network-details-page";

@Component({
  selector: 'kpn-network-details-page',
  templateUrl: './network-details-page.component.html',
  styleUrls: ['./network-details-page.component.scss']
})
export class NetworkDetailsPageComponent implements OnInit {

  response: ApiResponse<NetworkDetailsPage>;

  constructor(private appService: AppService) {
  }

  ngOnInit() {
    this.appService.networkDetails(3138543).subscribe(response => {
      this.response = response;
    });
  }

}
