import {Component, OnInit} from '@angular/core';
import {Feature, Map, Overlay, View, Style} from 'ol';
import {fromLonLat} from 'ol/proj';
import PointerInteraction from 'ol/interaction/Pointer';
import LayerSwitcher from 'ol-layerswitcher';
import Group from 'ol/layer/Group';
import VectorLayer from 'ol/layer/Vector';

import {ToastrService} from "ngx-toastr";
import {TranslateService} from "@ngx-translate/core";
import {ActivatedRoute} from "@angular/router";

import {PoiInformation, RouteState} from "../../../model";

import {NetworkService, PoiService, RouteDetailsService, RouteService, RouteStateService} from '../../../service'

import {
  buildVectorSource,
  convertPoiToPoiInformation,
  createBaseLayer,
  createCyclingMvtLayer,
  createCyclingPngLayer,
  createMoveInteraction,
  createNominatimGeocoder,
  createWalkingMvtLayer,
  createWalkingPngLayer,
  MapMoveHandler,
  MapState,
  MapStyling,
  PoiLayer
} from './domain'

@Component({
  selector: 'app-map-container',
  templateUrl: './map-container.component.html',
  styleUrls: ['./map-container.component.scss']
})
export class MapContainerComponent implements OnInit {

  map: Map;
  mapStyling: (feature, resolution) => Style;
  mapState: MapState = new MapState();
  poiInformation: PoiInformation;
  poiLayer: PoiLayer = new PoiLayer();
  routeState: RouteState = new RouteState();
  draggedNode: Feature;
  isCyclingSelected: boolean;

  moveInteraction = createMoveInteraction();

  cyclingPngLayer = createCyclingPngLayer();
  cyclingMvtLayer = createCyclingMvtLayer();
  walkingPngLayer = createWalkingPngLayer();
  walkingMvtLayer = createWalkingMvtLayer();
  geocoder = createNominatimGeocoder();
  routeLayer = new VectorLayer({zIndex: 50});

  overlay: Overlay;
  container: HTMLElement;
  content: HTMLElement;
  closer: HTMLElement;

  constructor(
    private routeService: RouteService,
    private translate: TranslateService,
    private activatedRoute: ActivatedRoute,
    private poiService: PoiService,
    private toastr: ToastrService,
    private routeDetailsService: RouteDetailsService,
    private routeStateService: RouteStateService,
    private state: RouteStateService,
    private networkService: NetworkService) {
  }

  ngOnInit() {
    this.container = document.getElementById('popup');
    this.content = document.getElementById('popup-content');
    this.closer = document.getElementById('popup-closer');

    this.overlay = new Overlay({
      element: this.container,
      autoPan: true,
      autoPanAnimation: {
        duration: 250
      }
    });

    this.map = new Map({
      target: 'map-container',
      view: new View({
        center: fromLonLat([4.3517103, 50.8503396]),
        zoom: 7,
        minZoom: 6,
        maxZoom: 17
      }),
      layers: [
        createBaseLayer(), this.routeLayer
      ],
      overlays: [this.overlay]
    });
    this.map.addControl(this.geocoder);
    this.map.addControl(new LayerSwitcher());

    let group;
    this.translate.stream("POI-LAYER").subscribe((tr) => {
      this.map.removeLayer(group);
      group = new Group({
        title: tr.TITLE,
        layers: [
          this.poiLayer.createPublicLayer(tr.PUBLIC),
          this.poiLayer.createDrinkAndFoodLayer(tr.FOOD_DRINK),
          this.poiLayer.createCulturalLayer(tr.CULTURE),
          this.poiLayer.createShopLayer(tr.SHOP),
          this.poiLayer.createSportLayer(tr.SPORT),
          this.poiLayer.createHotelLayer(tr.HOTEL)
        ]
      });
      this.map.addLayer(group);
    });

    this.networkService.networkObservable.subscribe(networkType => {
      this.resetStyle();
      this.displayNetworkLayer(networkType);
    });
    this.routeDetailsService.routeStateObservable.subscribe((routeState: RouteState) => {
      this.resetRouteState(routeState);
    });

    this.getCurrentPosition();

    this.geocoder.on('addresschosen', (evt) => this.map.getView().animate({center: evt.coordinate}));
    this.map.getView().on('change:resolution', () => this.zoom(this.map.getView().getZoom()));

    this.initializeMoveInteraction();
    this.map.addInteraction(this.moveInteraction);

    let drag = new PointerInteraction({
      handleDownEvent: evt => {
        let dragEnabled = false;

        evt.map.forEachFeatureAtPixel(evt.pixel, feature => {
          if (feature.values_.layer === 'node') {
            this.draggedNode = feature;
            dragEnabled = true;
          } else {
            if (feature.values_.type === 'way' || feature.values_.type === 'node') {
              this.handlePoiClick(feature);
            }
          }
        });

        return dragEnabled;
      },
      handleDragEvent: evt => {
        return true;
      },
      handleUpEvent: evt => {

        evt.map.forEachFeatureAtPixel(evt.pixel, feature => {
          if (this.draggedNode !== null && feature.values_.layer === 'node') {
            this.handleNodeClick(feature)
          }
        });
        return false;
      }
    });
    this.map.addInteraction(drag);
  }

  private initializeMoveInteraction() {
    this.moveInteraction.on("select", (e) => {
      if (e.selected[0] !== undefined && e.selected[0].values_.layer === 'node') {
        new MapMoveHandler(this.map, this.mapState).handle(e);
        //this.styleFeatures(e.selected);
      }
    });
  }

  private resetRouteState(routeState: RouteState) {
    this.resetStyle();
    this.routeState = routeState;

    if (this.routeState.selectedRoute.selectedNodesByUser.length >= 2) {
      this.calculateRoute();
    }
  }

  private resetStyle() {
    this.routeLayer.setSource(null);
    this.styleFeatures([]);
  }

  private getCurrentPosition() {
    navigator.geolocation.getCurrentPosition((pos) => {
      let coords = fromLonLat([pos.coords.longitude, pos.coords.latitude]);
      this.map.getView().animate({center: coords, zoom: 10});
    });
  }

  private handlePoiClick(feature) {
    this.poiService.getPoiInformation(feature.values_.type, feature.values_.id).subscribe(poi => {
      this.poiInformation = convertPoiToPoiInformation(poi);
      this.overlay.setPosition(fromLonLat([+poi.longitude, +poi.latitude]));
    });
  }

  private handleNodeClick(droppedNode) {
    this.routeDetailsService.addRouteState(this.routeState);
    this.routeLayer.setSource(null);

    if (this.draggedNode.values_.id !== droppedNode.values_.id) {
      this.routeState.replaceFeature(this.draggedNode, droppedNode);
    } else {
      this.routeState.addFeature(droppedNode);
    }

    if (this.routeState.selectedRoute.selectedNodesByUser.length >= 2) {
      this.calculateRoute();
    } else {
      this.routeDetailsService.displayRoute(null);
      this.styleFeatures(this.routeState.selectedFeatures)
    }

    this.draggedNode = null;
  }

  private styleFeatures(features: Feature[]) {
    this.mapStyling = new MapStyling(this.map, new MapState()).styleFunction(features);
    this.styleVectorLayer();
  }


  private calculateRoute() {
    this.routeStateService.saveState = true;

    if (this.isCyclingSelected) {
      this.routeService.calculateCyclingRoute(this.routeState.selectedRoute).subscribe(route => {
        this.routeLayer.setSource(buildVectorSource(route.sections, this.routeState.selectedFeatures));
        this.routeDetailsService.displayRoute(route);
      }, () => {
        this.translate.get("ROUTE_ERROR").subscribe(res => this.toastr.error(res));
      });
    } else {
      this.routeService.calculateHikingRoute(this.routeState.selectedRoute).subscribe(route => {
        this.routeLayer.setSource(buildVectorSource(route.sections, this.routeState.selectedFeatures));
        this.routeDetailsService.displayRoute(route);
      }, () => {
        this.translate.get("ROUTE_ERROR").subscribe(res => this.toastr.error(res));
      });
    }
  }

  private displayNetworkLayer(networkType: string) {
    this.routeDetailsService.clearRoute();
    localStorage.setItem('type', networkType.toLowerCase());

    if (networkType.toLowerCase() === "cycling") {
      this.map.removeLayer(this.walkingPngLayer);
      this.map.removeLayer(this.walkingMvtLayer);
      this.map.addLayer(this.cyclingPngLayer);
      this.map.addLayer(this.cyclingMvtLayer);
      this.isCyclingSelected = true;
    } else {
      this.map.removeLayer(this.cyclingPngLayer);
      this.map.removeLayer(this.cyclingMvtLayer);
      this.map.addLayer(this.walkingPngLayer);
      this.map.addLayer(this.walkingMvtLayer);
      this.isCyclingSelected = false;
    }
  }

  private zoom(zoomLevel: number) {
    const zoom = Math.round(zoomLevel);

    if (zoom < 6) {
      this.cyclingPngLayer.setVisible(false);
      this.cyclingMvtLayer.setVisible(false);
      this.walkingPngLayer.setVisible(false);
      this.walkingMvtLayer.setVisible(false);
    } else if (zoom <= 12) {
      this.cyclingPngLayer.setVisible(true);
      this.cyclingMvtLayer.setVisible(false);
      this.walkingPngLayer.setVisible(true);
      this.walkingMvtLayer.setVisible(false);
    } else if (zoom >= 12) {
      this.cyclingPngLayer.setVisible(false);
      this.cyclingMvtLayer.setVisible(true);
      this.walkingPngLayer.setVisible(false);
      this.walkingMvtLayer.setVisible(true);
    }
  }

  private styleVectorLayer() {
    this.walkingMvtLayer.setStyle(this.mapStyling);
    this.cyclingMvtLayer.setStyle(this.mapStyling);
  }

  private closePopup() {
    this.overlay.setPosition(undefined);
    this.poiInformation = undefined;
    this.closer.blur();
    return false;
  }
}
