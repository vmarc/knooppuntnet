import {Component, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {NetworkFactsPage} from "../../../../kpn/shared/network/network-facts-page";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'kpn-network-facts-page',
  templateUrl: './network-facts-page.component.html',
  styleUrls: ['./network-facts-page.component.scss']
})
export class NetworkFactsPageComponent implements OnInit {

  response: ApiResponse<NetworkFactsPage>;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService) {
  }

  ngOnInit() {
    const networkId = this.activatedRoute.snapshot.paramMap.get('networkId');
    this.appService.networkFacts(networkId).subscribe(response => {
      this.response = response;
    });
  }

}
