import {Component, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";
import {RoutePage} from "../../../../kpn/shared/route/route-page";
import {ApiResponse} from "../../../../kpn/shared/api-response";

@Component({
  selector: 'kpn-route-page',
  templateUrl: './route-page.component.html',
  styleUrls: ['./route-page.component.scss']
})
export class RoutePageComponent implements OnInit {

  response: ApiResponse<RoutePage>;

  constructor(private appService: AppService) {
  }

  ngOnInit() {
    this.appService.route(3148634).subscribe(response => {
      this.response = response;
    });
  }

}
