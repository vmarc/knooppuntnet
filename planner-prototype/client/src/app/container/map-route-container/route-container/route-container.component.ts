import {Component, OnInit} from "@angular/core";
import {MatRadioChange} from "@angular/material";
import {Route, RouteState} from "../../../model/";
import {NetworkService, RouteDetailsService} from "../../../service"

@Component({
  selector: "app-route-container",
  templateUrl: "./route-container.component.html",
  styleUrls: ["./route-container.component.scss"]
})
export class RouteContainerComponent implements OnInit {

  showHelp: boolean = false;
  routeState: RouteState;
  currentRoute: Route;
  currentRouteType: string;

  constructor(private networkService: NetworkService,
              private routeDetailsService: RouteDetailsService) {
  }

  ngOnInit() {
    this.routeDetailsService.routeObservable.subscribe(route => this.currentRoute = route);
    this.networkService.networkObservable.subscribe(network => this.currentRouteType = network);
    this.routeDetailsService.routeStateObservable.subscribe(selected => this.routeState = selected);
  }

  switchMap(event: MatRadioChange) {
    this.networkService.changeNetworkType(event.value);
  }

  undoAction() {
    this.routeDetailsService.returnPreviousState();
  }

  clearRoute() {
    this.routeDetailsService.clearRoute()
  }

  toggleHelp() {
    this.showHelp = !this.showHelp;
  }
}
