import {Component} from '@angular/core';

import Map from 'ol/Map';
import Coordinate from 'ol/View';
import Extent from 'ol/View';
import {fromLonLat} from 'ol/proj';
import {createXYZ} from 'ol/tilegrid';
import {click, pointerMove} from 'ol/events/condition';
import MapBrowserEvent from 'ol/events'
import {Circle as CircleStyle, Fill, Icon, Stroke, Style} from 'ol/style';
import Feature from 'ol/Feature';
import PointerInteraction from 'ol/interaction/Pointer';
import {Circle, LineString, Point} from 'ol/geom.js';
import {Tile as TileLayer, Vector as VectorLayer} from 'ol/layer';
import {OSM, Vector as VectorSource} from 'ol/source';

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

  crossStyle = new Style({
    stroke: new Stroke({
      color: "rgba(255, 0, 0, 0.7)",
      width: 3
    })
  });

  coordinates1 = fromLonLat([4.4484, 51.4627]); // Hemelrijk
  coordinates2 = fromLonLat([4.4835, 51.4748]); // Steenpaal

  point1 = new Feature(new Point(this.coordinates1));
  point2 = new Feature(new Point(this.coordinates2));

  line1 = new LineString([this.coordinates1, this.coordinates2]);
  line2 = new LineString([this.coordinates2, this.coordinates1]);

  cross1a = new LineString([[0, 0], [0, 0]]);
  cross1b = new LineString([[0, 0], [0, 0]]);
  cross2a = new LineString([[0, 0], [0, 0]]);
  cross2b = new LineString([[0, 0], [0, 0]]);
  circle = new Circle([0, 0], 200);

  ngAfterViewInit(): void {

    const raster = new TileLayer({
      source: new OSM()
    });

    const f1 = new Feature(this.cross1a);
    const f2 = new Feature(this.cross1b);
    const f3 = new Feature(this.cross2a);
    const f4 = new Feature(this.cross2b);
    const f5 = new Feature(this.circle);
    f1.setStyle(this.crossStyle);
    f2.setStyle(this.crossStyle);
    f3.setStyle(this.crossStyle);
    f4.setStyle(this.crossStyle);
    f5.setStyle(this.crossStyle);

    const source = new VectorSource({
      features: [
        this.point1,
        this.point2,
        new Feature(this.line1),
        new Feature(this.line2),
        f1,
        f2,
        f3,
        f4,
        f5
      ]
    });

    const vector = new VectorLayer({
      source: source,
      style: new Style({
        fill: new Fill({
          color: "rgba(255, 255, 255, 0.2)"
        }),
        stroke: new Stroke({
          color: "rgba(0, 0, 255, 0.7)",
          lineDash: [10, 10],
          width: 2
        }),
        image: new CircleStyle({
          radius: 7,
          fill: new Fill({
            color: "#ff00ff"
          })
        })
      })
    });

    const map = new Map({
      layers: [raster, vector],
      target: "map-trout-2"
    });

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

    this.line1.setCoordinates([this.coordinates1, coordinate]);
    this.line2.setCoordinates([this.coordinates2, coordinate]);

    const x = coordinate[0];
    const y = coordinate[1];

    const x11 = x - 400;
    const x12 = x - 200;
    this.cross1a.setCoordinates([[x11, y], [x12, y]]);

    const x21 = x + 200;
    const x22 = x + 400;
    this.cross1b.setCoordinates([[x21, y], [x22, y]]);

    const y31 = y - 400;
    const y32 = y - 200;
    this.cross2a.setCoordinates([[x, y31], [x, y32]]);

    const y41 = y + 200;
    const y42 = y + 400;
    this.cross2b.setCoordinates([[x, y41], [x, y42]]);

    this.circle.setCenter(coordinate);
  }

}
