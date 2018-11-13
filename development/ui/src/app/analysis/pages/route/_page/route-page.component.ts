import {Component, OnDestroy, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";
import {RoutePage} from "../../../../kpn/shared/route/route-page";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs";

@Component({
  selector: 'kpn-route-page',
  templateUrl: './route-page.component.html',
  styleUrls: ['./route-page.component.scss']
})
export class RoutePageComponent implements OnInit, OnDestroy {

  response: ApiResponse<RoutePage>;
  paramsSubscription: Subscription;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService) {
  }

  ngOnInit() {
    this.paramsSubscription = this.activatedRoute.params.subscribe(params => {
      const routeId = params['routeId'];
      this.appService.route(routeId).subscribe(response => {
        this.response = response;
      });
    });
  }

  ngOnDestroy() {
    this.paramsSubscription.unsubscribe();
  }
}
