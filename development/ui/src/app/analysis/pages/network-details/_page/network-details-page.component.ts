import {Component, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {NetworkDetailsPage} from "../../../../kpn/shared/network/network-details-page";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'kpn-network-details-page',
  templateUrl: './network-details-page.component.html',
  styleUrls: ['./network-details-page.component.scss']
})
export class NetworkDetailsPageComponent implements OnInit {

  response: ApiResponse<NetworkDetailsPage>;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService) {
  }

  ngOnInit() {
    const networkId = this.activatedRoute.snapshot.paramMap.get('networkId');
    this.appService.networkDetails(networkId).subscribe(response => {
      this.response = response;
    });
  }

}
