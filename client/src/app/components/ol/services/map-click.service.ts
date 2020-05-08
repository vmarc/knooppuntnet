import {Injectable} from "@angular/core";
import {Router} from "@angular/router";
import {MapBrowserEvent} from "ol";
import {platformModifierKeyOnly} from "ol/events/condition";
import {FeatureLike} from "ol/Feature";
import Interaction from "ol/interaction/Interaction";
import PointerInteraction from "ol/interaction/Pointer";
import Map from "ol/Map";

/*
   Navigates to the node or route specific page when clicking on node or route in the map.
 */
@Injectable()
export class MapClickService {

  private interaction: Interaction = this.buildInteraction();

  constructor(private router: Router) {
  }

  installOn(map: Map): void {
    map.addInteraction(this.interaction);
  }

  onDestroy(map: Map): void {
    map.removeInteraction(this.interaction);
  }

  private buildInteraction(): Interaction {
    return new PointerInteraction({
      handleDownEvent: (e) => this.handleDownEvent(e),
      handleMoveEvent: (e) => this.handleMoveEvent(e)
    });
  }

  private handleDownEvent(evt: MapBrowserEvent): boolean {
    const features = this.getFeatures(evt);
    const nodeFeature = this.findFeature(features, this.isNode);
    const openNewTab = platformModifierKeyOnly(evt);
    if (nodeFeature) {
      this.handleNodeClicked(nodeFeature, openNewTab);
      return false;
    }
    const routeFeature = this.findFeature(features, this.isRoute);
    if (routeFeature) {
      this.handleRouteClicked(routeFeature, openNewTab);
      return true;
    }
    return false;
  }

  private handleMoveEvent(evt: MapBrowserEvent): boolean {
    let cursorStyle = "default";
    if (this.isHooveringOverNodeOrRoute(evt)) {
      cursorStyle = "pointer";
    }
    evt.map.getTargetElement().style.cursor = cursorStyle;
    return true;
  }

  private getFeatures(evt: MapBrowserEvent): Array<FeatureLike> {
    return evt.map.getFeaturesAtPixel(evt.pixel, {hitTolerance: 10});
  }

  private findFeature(features: Array<FeatureLike>, predicate: (feature: FeatureLike) => boolean): FeatureLike {
    for (const feature of features) {
      if (predicate(feature)) {
        return feature;
      }
    }
    return null;
  }

  private handleRouteClicked(feature: FeatureLike, openNewTab: boolean): void {
    const featureId = feature.get("id");
    const routeName = feature.get("name");
    const routeId = featureId.substring(0, featureId.indexOf("-"));
    const url = `/analysis/route/${routeId}`;
    if (openNewTab) {
      window.open(url);
    } else {
      this.interaction.getMap().removeInteraction(this.interaction);
      setTimeout(() => this.router.navigateByUrl(url, {state: {routeName: routeName}}), 250);
    }
  }

  private handleNodeClicked(feature: FeatureLike, openNewTab: boolean): void {
    const nodeId = feature.get("id");
    const nodeName = feature.get("name");
    const url = `/analysis/node/${nodeId}`;
    if (openNewTab) {
      window.open(url);
    } else {
      this.interaction.getMap().removeInteraction(this.interaction);
      setTimeout(() => this.router.navigateByUrl(url, {state: {nodeName: nodeName}}), 250);
    }
  }

  private isHooveringOverNodeOrRoute(evt: MapBrowserEvent): boolean {
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
    const layer = feature.get("layer");
    return layer && (layer.endsWith("node") || layer === "node-marker");
  }

  private isRoute(feature: FeatureLike): boolean {
    const layer = feature.get("layer");
    return layer && layer.endsWith("route");
  }

}
