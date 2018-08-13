import {Component, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {SubsetNetworksPage} from "../../../../kpn/shared/subset/subset-networks-page";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'kpn-subset-networks-page',
  templateUrl: './subset-networks-page.component.html',
  styleUrls: ['./subset-networks-page.component.scss']
})
export class SubsetNetworksPageComponent implements OnInit {

  response: ApiResponse<SubsetNetworksPage>;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService) {
  }

  ngOnInit() {
    const country = this.activatedRoute.snapshot.paramMap.get('country');
    const networkType = this.activatedRoute.snapshot.paramMap.get('networkType');
    this.appService.subsetNetworks(country, networkType).subscribe(response => {
      this.response = response;
    });
  }

}
