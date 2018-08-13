import {Component, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {NetworkRoutesPage} from "../../../../kpn/shared/network/network-routes-page";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'kpn-network-routes-page',
  templateUrl: './network-routes-page.component.html',
  styleUrls: ['./network-routes-page.component.scss']
})
export class NetworkRoutesPageComponent implements OnInit {

  response: ApiResponse<NetworkRoutesPage>;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService) {
  }

  ngOnInit() {
    const networkId = this.activatedRoute.snapshot.paramMap.get('networkId');
    this.appService.networkRoutes(networkId).subscribe(response => {
      this.response = response;
    });
  }

}
