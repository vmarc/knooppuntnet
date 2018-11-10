import {Component, Input} from '@angular/core';

@Component({
  selector: 'kpn-map-detail-route',
  templateUrl: './map-detail-route.component.html',
  styleUrls: ['./map-detail-route.component.scss']
})
export class MapDetailRouteComponent {

  @Input() routeId: number;
  @Input() routeName: string;

}
