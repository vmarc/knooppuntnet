import {Component, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {NetworkMapPage} from "../../../../kpn/shared/network/network-map-page";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'kpn-network-map-page',
  templateUrl: './network-map-page.component.html',
  styleUrls: ['./network-map-page.component.scss']
})
export class NetworkMapPageComponent implements OnInit {

  response: ApiResponse<NetworkMapPage>;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService) {
  }

  ngOnInit() {
    const networkId = this.activatedRoute.snapshot.paramMap.get('networkId');
    this.appService.networkMap(networkId).subscribe(response => {
      this.response = response;
    });
  }

}
