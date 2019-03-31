import {Component} from '@angular/core';

import Map from 'ol/Map';
import Coordinate from 'ol/View';
import Extent from 'ol/View';
import {fromLonLat} from 'ol/proj';
import {Tile as TileLayer} from 'ol/layer';
import {OSM} from 'ol/source';
import {NetworkVectorTileLayer} from "../../../components/ol/domain/network-vector-tile-layer";
import {NetworkTypes} from "../../../kpn/common/network-types";
import {PlannerInteraction} from "./planner-interaction";
import {PlannerService} from "../../planner.service";

@Component({
  selector: 'kpn-map-tryout-2-page',
  template: `
    <div id="map-trout-2" class="map"></div>
  `,
  styles: [`
    .map {
      position: absolute;
      top: 48px;
      left: 0;
      right: 0;
      bottom: 0;
    }
  `]
})
export class MapTryout2PageComponent {

  constructor(private plannerService: PlannerService) {
  }

  interaction = new PlannerInteraction(
    this.plannerService.context,
    this.plannerService.engine
  );

  ngAfterViewInit(): void {

    const map = new Map({
      layers: [
        new TileLayer({
          source: new OSM()
        }),
        NetworkVectorTileLayer.build(NetworkTypes.rwn)
      ],
      target: "map-trout-2"
    });

    this.plannerService.context.crosshairLayer.addToMap(map);
    this.plannerService.context.routeLayer.addToMap(map);
    this.interaction.addToMap(map);

    const a: Coordinate = fromLonLat([4.43, 51.45]);
    const b: Coordinate = fromLonLat([4.52, 51.47]);
    const extent: Extent = [a[0], a[1], b[0], b[1]];
    map.getView().fit(extent);
  }

}
