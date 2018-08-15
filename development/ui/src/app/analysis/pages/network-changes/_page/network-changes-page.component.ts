import {Component, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {NetworkChangesPage} from "../../../../kpn/shared/network/network-changes-page";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'kpn-network-changes-page',
  templateUrl: './network-changes-page.component.html',
  styleUrls: ['./network-changes-page.component.scss']
})
export class NetworkChangesPageComponent implements OnInit {

  response: ApiResponse<NetworkChangesPage>;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService) {
  }

  ngOnInit() {
    const networkId = this.activatedRoute.snapshot.paramMap.get('networkId');
    this.appService.networkChanges(networkId).subscribe(response => {
      this.response = response;
    });
  }

}
