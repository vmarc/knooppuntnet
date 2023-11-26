import { Location } from '@angular/common';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { MapBrowserEvent } from 'ol';
import { platformModifierKeyOnly } from 'ol/events/condition';
import { FeatureLike } from 'ol/Feature';
import Interaction from 'ol/interaction/Interaction';
import Map from 'ol/Map';
import MapBrowserEventType from 'ol/MapBrowserEventType';

/*
   Navigates to the node or route specific page when clicking on node or route in the map.
 */
@Injectable({
  providedIn: 'root',
})
export class MapClickService {
  private interaction: Interaction = this.buildInteraction();
  private ctrl = false;

  constructor(
    private router: Router,
    private location: Location
  ) {}

  installOn(map: Map): void {
    map.addInteraction(this.interaction);
  }

  onDestroy(map: Map): void {
    map.removeInteraction(this.interaction);
  }

  private buildInteraction(): Interaction {
    return new Interaction({
      handleEvent: (event: MapBrowserEvent<UIEvent>) => {
        const ctrlState = platformModifierKeyOnly(event);
        if (ctrlState === true || ctrlState === false) {
          this.ctrl = ctrlState;
        }
        if (MapBrowserEventType.SINGLECLICK === event.type) {
          return this.handleSingleClickEvent(event);
        }
        if (MapBrowserEventType.POINTERMOVE === event.type) {
          return this.handleMoveEvent(event);
        }
        return true; // propagate event
      },
    });
  }

  private handleSingleClickEvent(evt: MapBrowserEvent<UIEvent>): boolean {
    const features = this.getFeatures(evt);
    const nodeFeature = this.findFeature(features, this.isNode);
    if (nodeFeature) {
      this.handleNodeClicked(nodeFeature, this.ctrl);
      return true; // do not propagate event
    }
    const routeFeature = this.findFeature(features, this.isRoute);
    if (routeFeature) {
      this.handleRouteClicked(routeFeature, this.ctrl);
      return true; // do not propagate event
    }
    return true; // propagate event
  }

  private handleMoveEvent(evt: MapBrowserEvent<UIEvent>): boolean {
    let cursorStyle = 'default';
    if (this.isHooveringOverNodeOrRoute(evt)) {
      cursorStyle = 'pointer';
    }
    evt.map.getTargetElement().style.cursor = cursorStyle;
    return true; // propagate event
  }

  private getFeatures(evt: MapBrowserEvent<UIEvent>): Array<FeatureLike> {
    return evt.map.getFeaturesAtPixel(evt.pixel, { hitTolerance: 10 });
  }

  private findFeature(
    features: Array<FeatureLike>,
    predicate: (feature: FeatureLike) => boolean
  ): FeatureLike {
    for (const feature of features) {
      if (predicate(feature)) {
        return feature;
      }
    }
    return null;
  }

  private handleRouteClicked(feature: FeatureLike, openNewTab: boolean): void {
    const featureId = feature.get('id');
    const routeName = feature.get('name');
    const routeId = featureId.substring(0, featureId.indexOf('-'));
    const url = `/analysis/route/${routeId}`;
    if (openNewTab) {
      const externalUrl = this.location.prepareExternalUrl(url);
      window.open(externalUrl);
    } else {
      this.interaction.getMap().removeInteraction(this.interaction);
      setTimeout(
        () => this.router.navigateByUrl(url, { state: { routeName } }),
        250
      );
    }
  }

  private handleNodeClicked(feature: FeatureLike, openNewTab: boolean): void {
    const nodeId = feature.get('id');
    const nodeName = feature.get('name');
    const url = `/analysis/node/${nodeId}`;
    if (openNewTab) {
      const externalUrl = this.location.prepareExternalUrl(url);
      window.open(externalUrl);
    } else {
      this.interaction.getMap().removeInteraction(this.interaction);
      setTimeout(
        () => this.router.navigateByUrl(url, { state: { nodeName } }),
        250
      );
    }
  }

  private isHooveringOverNodeOrRoute(evt: MapBrowserEvent<UIEvent>): boolean {
    const features = this.getFeatures(evt);
    if (features) {
      for (const feature of features) {
        if (this.isNode(feature) || this.isRoute(feature)) {
          return true;
        }
      }
    }
    return false;
  }

  private isNode(feature: FeatureLike): boolean {
    const layer = feature.get('layer');
    return layer && (layer.endsWith('node') || layer === 'node-marker');
  }

  private isRoute(feature: FeatureLike): boolean {
    const layer = feature.get('layer');
    return layer && layer.endsWith('route');
  }
}
