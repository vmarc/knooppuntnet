import {Component, OnInit} from '@angular/core';

import Map from 'ol/Map.js';
import View from 'ol/View.js';
import TileLayer from 'ol/layer/Tile.js';
import OSM from 'ol/source/OSM.js';

@Component({
  selector: 'kpn-map-page',
  templateUrl: './map-page.component.html',
  styleUrls: ['./map-page.component.css']
})
export class MapPageComponent implements OnInit {

  constructor() {
  }

  ngOnInit() {

    const map = new Map({
      target: 'map',
      layers: [
        new TileLayer({
          source: new OSM()
        })
      ],
      view: new View({
        center: [-472202, 7530279],
        zoom: 12
      })
    });
  }
}
