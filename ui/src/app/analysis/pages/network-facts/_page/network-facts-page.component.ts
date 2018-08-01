import {Component, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {NetworkFactsPage} from "../../../../kpn/shared/network/network-facts-page";

@Component({
  selector: 'kpn-network-facts-page',
  templateUrl: './network-facts-page.component.html',
  styleUrls: ['./network-facts-page.component.scss']
})
export class NetworkFactsPageComponent implements OnInit {

  response: ApiResponse<NetworkFactsPage>;

  constructor(private appService: AppService) {
  }

  ngOnInit() {
    this.appService.networkFacts(3138543).subscribe(response => {
      this.response = response;
    });
  }

}
