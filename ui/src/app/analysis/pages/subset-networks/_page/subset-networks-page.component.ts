import {Component, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {SubsetNetworksPage} from "../../../../kpn/shared/subset/subset-networks-page";

@Component({
  selector: 'kpn-subset-networks-page',
  templateUrl: './subset-networks-page.component.html',
  styleUrls: ['./subset-networks-page.component.scss']
})
export class SubsetNetworksPageComponent implements OnInit {

  response: ApiResponse<SubsetNetworksPage>;

  constructor(private appService: AppService) {
  }

  ngOnInit() {
    this.appService.subsetNetworks().subscribe(response => {
      this.response = response;
    });
  }

}
