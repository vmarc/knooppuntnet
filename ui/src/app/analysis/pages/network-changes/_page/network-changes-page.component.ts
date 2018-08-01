import {Component, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {NetworkChangesPage} from "../../../../kpn/shared/network/network-changes-page";

@Component({
  selector: 'kpn-network-changes-page',
  templateUrl: './network-changes-page.component.html',
  styleUrls: ['./network-changes-page.component.scss']
})
export class NetworkChangesPageComponent implements OnInit {

  response: ApiResponse<NetworkChangesPage>;

  constructor(private appService: AppService) {
  }

  ngOnInit() {
    this.appService.networkChanges(3138543).subscribe(response => {
      this.response = response;
    });
  }

}
