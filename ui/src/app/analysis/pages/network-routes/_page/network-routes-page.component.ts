import {Component, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {NetworkRoutesPage} from "../../../../kpn/shared/network/network-routes-page";

@Component({
  selector: 'kpn-network-routes-page',
  templateUrl: './network-routes-page.component.html',
  styleUrls: ['./network-routes-page.component.scss']
})
export class NetworkRoutesPageComponent implements OnInit {

  response: ApiResponse<NetworkRoutesPage>;

  constructor(private appService: AppService) {
  }

  ngOnInit() {
    this.appService.networkRoutes(3138543).subscribe(response => {
      this.response = response;
    });
  }

}
