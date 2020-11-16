import {List} from 'immutable';
import {platformModifierKeyOnly} from 'ol/events/condition';
import PointerInteraction from 'ol/interaction/Pointer';
import Map from 'ol/Map';
import MapBrowserEvent from 'ol/MapBrowserEvent';
import {MapFeature} from '../features/map-feature';
import {PlannerEngine} from './planner-engine';
import {Features} from './features';

export class PlannerInteraction {

  private downEventTime = 0;

  private readonly interaction: PointerInteraction;

  constructor(private engine: PlannerEngine) {
    this.interaction = this.buildInteraction();
  }

  addToMap(map: Map) {
    map.addInteraction(this.interaction);
  }

  private buildInteraction(): PointerInteraction {
    return new PointerInteraction({
      handleDownEvent: (evt: MapBrowserEvent) => {
        this.downEventTime = new Date().getTime();
        return this.engine.handleDownEvent(this.getFeaturesAt(evt), evt.coordinate, platformModifierKeyOnly(evt));
      },
      handleMoveEvent: (evt: MapBrowserEvent) => this.engine.handleMoveEvent(this.getFeaturesAt(evt), evt.coordinate, platformModifierKeyOnly(evt)),
      handleDragEvent: (evt: MapBrowserEvent) => this.engine.handleDragEvent(this.getFeaturesAt(evt), evt.coordinate, platformModifierKeyOnly(evt)),
      handleUpEvent: (evt: MapBrowserEvent) => {
        const upEventTime = new Date().getTime();
        const timeSinceUp = upEventTime - this.downEventTime;
        const singleClick = timeSinceUp < 800;
        return this.engine.handleUpEvent(this.getFeaturesAt(evt), evt.coordinate, singleClick, platformModifierKeyOnly(evt));
      }
    });
  }

  private getFeaturesAt(evt: MapBrowserEvent): List<MapFeature> {
    const features = evt.map.getFeaturesAtPixel(evt.pixel);
    if (features) {
      return List(features.map(feature => Features.mapFeature(feature)).filter(f => f !== null));
    }
    return List();
  }

}
