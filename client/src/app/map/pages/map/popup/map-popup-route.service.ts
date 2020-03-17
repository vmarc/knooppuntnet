import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {switchMap} from "rxjs/operators";
import {filter} from "rxjs/operators";
import {tap} from "rxjs/operators";
import {AppService} from "../../../../app.service";
import {MapService} from "../../../../components/ol/map.service";
import {MapRouteDetail} from "../../../../kpn/api/common/route/map-route-detail";
import {ApiResponse} from "../../../../kpn/api/custom/api-response";
import {PlannerService} from "../../../planner.service";

@Injectable()
export class MapPopupRouteService {

  response: Observable<ApiResponse<MapRouteDetail>>;

  constructor(private appService: AppService,
              private mapService: MapService,
              private plannerService: PlannerService) {

    console.log("MapPopupRouteService CONSTRUCTOR");

    this.response = this.mapService.routeClicked.pipe(
      tap(xx => console.log("route clicked")),
      filter(routeClick => routeClick !== null),
      switchMap(routeClick =>
        this.appService.mapRouteDetail(routeClick.route.routeId).pipe(
          tap(xx => console.log("route info received")),
          tap(response => this.plannerService.context.overlay.setPosition(routeClick.coordinate))
        )
      )
    );
  }
}
