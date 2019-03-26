import {Component} from '@angular/core';

import Map from 'ol/Map';
import Coordinate from 'ol/View';
import Extent from 'ol/View';
import {fromLonLat} from 'ol/proj';
import {createXYZ} from 'ol/tilegrid';
import {click, pointerMove} from 'ol/events/condition';
import MapBrowserEvent from 'ol/events'
import {Fill, Icon, Stroke, Style} from 'ol/style';
import PointerInteraction from 'ol/interaction/Pointer';
import {Tile as TileLayer} from 'ol/layer';
import {OSM} from 'ol/source';
import {PlannerCrosshairLayer} from "./planner-crosshair-layer";
import {PlannerRouteLayer} from "./planner-route-layer";
import {TestRouteData} from "./test-route-data";

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

  crosshairLayer = new PlannerCrosshairLayer();
  routeLayer = new PlannerRouteLayer();

  ngAfterViewInit(): void {

    const map = new Map({
      layers: [
        new TileLayer({
          source: new OSM()
        })
      ],
      target: "map-trout-2"
    });

    this.crosshairLayer.addToMap(map);

    const testRouteData = new TestRouteData();

    this.routeLayer.addStartNodeFlag("32", testRouteData.aCoordinates.get(0));
    this.routeLayer.addViaNodeFlag("93", testRouteData.cCoordinates.get(0));
    this.routeLayer.addViaNodeFlag("11", testRouteData.eCoordinates.get(0));
    this.routeLayer.addEndNodeFlag("35", testRouteData.gCoordinates.get(testRouteData.gCoordinates.size - 1));
    this.routeLayer.addRouteLeg(testRouteData.aCoordinates);
    this.routeLayer.addRouteLeg(testRouteData.bCoordinates);
    this.routeLayer.addRouteLeg(testRouteData.cCoordinates);
    this.routeLayer.addRouteLeg(testRouteData.dCoordinates);
    this.routeLayer.addRouteLeg(testRouteData.eCoordinates);
    this.routeLayer.addRouteLeg(testRouteData.fCoordinates);
    this.routeLayer.addRouteLeg(testRouteData.gCoordinates);

    this.routeLayer.addToMap(map);

    const a: Coordinate = fromLonLat([4.43, 51.45]);
    const b: Coordinate = fromLonLat([4.52, 51.47]);
    const extent: Extent = [a[0], a[1], b[0], b[1]];
    map.getView().fit(extent);

    const interaction = new PointerInteraction({
      handleDownEvent: (evt: MapBrowserEvent) => {
        this.updatePosition(evt.coordinate);
        return true;
      },
      handleDragEvent: evt => {
        this.updatePosition(evt.coordinate);
        return true;
      },
      handleUpEvent: evt => {
        return false;
      }
    });

    map.addInteraction(interaction);

  }

  private updatePosition(coordinate: Coordinate) {
    this.crosshairLayer.updatePosition(coordinate);
    this.routeLayer.updateDoubleElasticBandPosition(coordinate);
  }

}
