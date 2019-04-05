import {Component} from '@angular/core';
import Coordinate from 'ol/coordinate';
import {Tile as TileLayer} from 'ol/layer';
import Map from 'ol/Map';
import {fromLonLat} from 'ol/proj';
import {OSM} from 'ol/source';
import Extent from 'ol/View';
import {NetworkVectorTileLayer} from "../../../components/ol/domain/network-vector-tile-layer";
import {NetworkTypes} from "../../../kpn/common/network-types";
import {PlannerService} from "../../planner.service";
import {PlannerContextImpl} from "./planner-context-impl";
import {PlannerInteraction} from "./planner-interaction";

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

    (this.plannerService.context as PlannerContextImpl).viewPort = map.getViewport();
    this.plannerService.init(map);
    this.interaction.addToMap(map);

    const a: Coordinate = fromLonLat([4.43, 51.45]);
    const b: Coordinate = fromLonLat([4.52, 51.47]);
    const extent: Extent = [a[0], a[1], b[0], b[1]];
    map.getView().fit(extent);
  }

}
