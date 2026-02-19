import { MapMouseEvent } from 'maplibre-gl';
import { MapFeature } from '../features/map-feature';
import { Features } from './features';
import { PlannerEngine } from './planner-engine';

export class PlannerInteraction {
  private eventDebugLogCount = 0;
  private readonly eventDebugLogEnabled = false;

  constructor(private engine: PlannerEngine) {}

  handleMousedown(e: MapMouseEvent): void {
    // console.log('PlannerInteraction.handleMousedown', e);
    const features = this.getFeaturesAt(e);
    if (features.length > 0) {
      const coordinate = [e.lngLat.lng, e.lngLat.lat];
      const result = this.engine.handleDownEvent(features, coordinate);
      if (result) {
        e.preventDefault();
      }
    }
  }

  handleMouseup(e: MapMouseEvent): void {
    // console.log('PlannerInteraction.handleMouseup', e);
    const features = this.getFeaturesAt(e);
    if (features.length > 0) {
      const coordinate = [e.lngLat.lng, e.lngLat.lat];
      const result = this.engine.handleUpEvent(features, coordinate);
      if (result) {
        e.preventDefault();
      }
    }
  }

  handleClick(e: MapMouseEvent): void {
    const features = this.getFeaturesAt(e);
    if (features.length > 0) {
      const coordinate = [e.lngLat.lng, e.lngLat.lat]; // TODO planner, is this ok???
      const meta = e.originalEvent.metaKey;
      const result = this.engine.handleSingleClickEvent(features, coordinate, meta);
      if (result) {
        e.preventDefault();
      }
    }
  }

  handleDblclick(e: MapMouseEvent): void {
    // console.log('PlannerInteraction.handleDblclick', e);
  }

  handleMousemove(e: MapMouseEvent): void {
    //  if (MapBrowserEventType.POINTERDRAG === evt.type) { // TODO planner
    //    return this.engine.handleDragEvent(this.getFeaturesAt(evt), evt.coordinate);
    //  }

    const features = this.getFeaturesAt(e);
    if (features.length > 0) {
      const coordinate = [e.lngLat.lng, e.lngLat.lat]; // TODO planner, is this ok???
      const meta = e.originalEvent.metaKey;
      const result = this.engine.handleMoveEvent(features, coordinate, meta);
      if (result) {
        e.preventDefault();
      }
    }
  }

  handleMouseover(e: MapMouseEvent): void {
    // console.log('PlannerInteraction.handleMouseover', e);
  }

  handleMouseenter(e: MapMouseEvent): void {
    // console.log('PlannerInteraction.handleMouseenter', e);
  }

  handleMouseleave(e: MapMouseEvent): void {
    // console.log('PlannerInteraction.handleMouseleave', e);
  }

  handleMouseout(e: MapMouseEvent): void {
    // console.log('PlannerInteraction.handleMouseout', e);
  }

  handleContextmenu(e: MapMouseEvent): void {
    // console.log('PlannerInteraction.handleContextmenu', e);
  }

  private getFeaturesAt(mapMouseEvent: MapMouseEvent): MapFeature[] {
    const features = mapMouseEvent.target.queryRenderedFeatures(mapMouseEvent.point);

    const routeFeatures = features.filter((feature) => {
      return feature.source === 'route-hiking'; // TODO planner
    });

    return routeFeatures
      .map((feature) => Features.mapFeature(feature))
      .filter((f) => f !== undefined);
  }

  private eventDebugLog(message: string): void {
    if (this.eventDebugLogEnabled) {
      console.log(`PlannerInteraction ${this.eventDebugLogCount++}: ${message}`);
    }
  }
}
