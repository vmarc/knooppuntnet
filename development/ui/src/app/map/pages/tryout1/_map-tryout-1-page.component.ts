import {Component} from '@angular/core';

import Map from 'ol/Map';
import View from 'ol/View';
import Coordinate from 'ol/View';
import {fromLonLat} from 'ol/proj';
import {createXYZ} from 'ol/tilegrid';
import {click, pointerMove} from 'ol/events/condition';
import MapBrowserEvent from 'ol/events'
import {Circle as CircleStyle, Fill, Icon, Stroke, Style} from 'ol/style';
import Feature from 'ol/Feature';

import {unByKey} from 'ol/Observable.js';
import Overlay from 'ol/Overlay.js';
import {getLength} from 'ol/sphere.js';
import {LineString} from 'ol/geom.js';
import Draw from 'ol/interaction/Draw.js';
import {Tile as TileLayer, Vector as VectorLayer} from 'ol/layer';
import {OSM, Vector as VectorSource} from 'ol/source';

@Component({
  selector: 'kpn-map-tryout-1-page',
  template: `
    <div id="map-trout-1" class="map"></div>
  `,
  styles: [`
    .map {
      position: absolute;
      top: 48px;
      left: 0;
      right: 0;
      bottom: 0;
    }

    /deep/ .tooltip {
      position: relative;
      background: rgba(0, 0, 0, 0.5);
      border-radius: 4px;
      color: white;
      padding: 4px 8px;
      opacity: 0.7;
      white-space: nowrap;
    }

    /deep/ .tooltip-measure {
      opacity: 1;
      font-weight: bold;
    }

    /deep/ .tooltip-static {
      background-color: #ffcc33;
      color: black;
      border: 1px solid white;
    }

    /deep/ .tooltip-measure:before,
    .tooltip-static:before {
      border-top: 6px solid rgba(0, 0, 0, 0.5);
      border-right: 6px solid transparent;
      border-left: 6px solid transparent;
      content: "";
      position: absolute;
      bottom: -6px;
      margin-left: -7px;
      left: 50%;
    }

    /deep/ .tooltip-static:before {
      border-top-color: #ffcc33;
    }

  `]
})
export class MapTryout1PageComponent {

  ngAfterViewInit(): void {

    const raster = new TileLayer({
      source: new OSM()
    });

    const source = new VectorSource();

    const vector = new VectorLayer({
      source: source,
      style: new Style({
        fill: new Fill({
          color: 'rgba(255, 255, 255, 0.2)'
        }),
        stroke: new Stroke({
          color: '#ffcc33',
          width: 2
        }),
        image: new CircleStyle({
          radius: 7,
          fill: new Fill({
            color: '#ffcc33'
          })
        })
      })
    });

    let sketch: Feature; // Currently drawn feature
    let helpTooltipElement: Element;
    let helpTooltip: Overlay; // Overlay to show the help messages
    let measureTooltipElement: Element;
    let measureTooltip: Overlay; // Overlay to show the measurement.

    const pointerMoveHandler = function (evt: MapBrowserEvent) {
      if (evt.dragging) {
        return;
      }

      let helpMsg = "Click to start drawing";
      if (sketch) {
        helpMsg = "Click to continue drawing the line, click twice to end the line";
      }

      helpTooltipElement.innerHTML = helpMsg;
      helpTooltip.setPosition(evt.coordinate);

      helpTooltipElement.classList.remove('hidden');
    };


    const map = new Map({
      layers: [raster, vector],
      target: "map-trout-1",
      view: new View({
        center: [-11000000, 4600000],
        zoom: 15
      })
    });

    map.on("pointermove", pointerMoveHandler);

    map.getViewport().addEventListener("mouseout", function () {
      helpTooltipElement.classList.add('hidden');
    });

    let draw: Draw; // global so we can remove it later

    const formatLength = function (line: LineString) {
      const length = getLength(line);
      if (length > 100) {
        return (Math.round(length / 1000 * 100) / 100) + " km";
      }
      return (Math.round(length * 100) / 100) + " m";
    };

    function addInteraction() {
      draw = new Draw({
        source: source,
        type: "LineString",
        style: new Style({
          fill: new Fill({
            color: "rgba(0, 0, 255, 0.2)"
          }),
          stroke: new Stroke({
            color: "rgba(0, 0, 255, 0.5)",
            lineDash: [10, 10],
            width: 2
          }),
          image: new CircleStyle({
            radius: 5,
            stroke: new Stroke({
              color: "rgba(0, 0, 0, 0.7)"
            }),
            fill: new Fill({
              color: "rgba(0, 0, 255, 0.2)"
            })
          })
        })
      });
      map.addInteraction(draw);

      createMeasureTooltip();
      createHelpTooltip();

      let listener;
      draw.on("drawstart",
        function (evt) {
          // set sketch
          sketch = evt.feature;

          let tooltipCoord: Coordinate = evt.coordinate;

          listener = sketch.getGeometry().on("change", function (evt) {
            const geom = evt.target;
            const output = formatLength(geom);
            tooltipCoord = geom.getLastCoordinate();
            measureTooltipElement.innerHTML = output;
            measureTooltip.setPosition(tooltipCoord);
          });
        }, this);

      draw.on("drawend",
        function () {
          measureTooltipElement.className = "tooltip tooltip-static";
          measureTooltip.setOffset([0, -7]);
          // unset sketch
          sketch = null;
          // unset tooltip so that a new one can be created
          measureTooltipElement = null;
          createMeasureTooltip();
          unByKey(listener);
        }, this);
    }

    function createHelpTooltip() {
      if (helpTooltipElement) {
        helpTooltipElement.parentNode.removeChild(helpTooltipElement);
      }
      helpTooltipElement = document.createElement("div");
      helpTooltipElement.className = "tooltip hidden";
      helpTooltip = new Overlay({
        element: helpTooltipElement,
        offset: [15, 0],
        positioning: "center-left"
      });
      map.addOverlay(helpTooltip);
    }

    function createMeasureTooltip() {
      if (measureTooltipElement) {
        measureTooltipElement.parentNode.removeChild(measureTooltipElement);
      }
      measureTooltipElement = document.createElement("div");
      measureTooltipElement.className = "tooltip tooltip-measure";
      measureTooltip = new Overlay({
        element: measureTooltipElement,
        offset: [0, -15],
        positioning: "bottom-center"
      });
      map.addOverlay(measureTooltip);
    }

    addInteraction();
  }

}
