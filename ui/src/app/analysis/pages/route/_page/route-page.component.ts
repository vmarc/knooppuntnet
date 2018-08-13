import {Component, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";
import {RoutePage} from "../../../../kpn/shared/route/route-page";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'kpn-route-page',
  templateUrl: './route-page.component.html',
  styleUrls: ['./route-page.component.scss']
})
export class RoutePageComponent implements OnInit {

  response: ApiResponse<RoutePage>;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService) {
  }

  ngOnInit() {
    const routeId = this.activatedRoute.snapshot.paramMap.get('routeId');
    this.appService.route(routeId).subscribe(response => {
      this.response = response;
    });
  }

}
