import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-map-detail-route",
  template: `
    <p>
      map-detail-route
    </p>
  `
})
export class MapDetailRouteComponent {

  @Input() routeId: number;
  @Input() routeName: string;

}
