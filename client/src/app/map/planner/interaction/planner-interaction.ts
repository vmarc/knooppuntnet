import {List} from 'immutable';
import {platformModifierKeyOnly} from 'ol/events/condition';
import {Interaction} from 'ol/interaction';
import Map from 'ol/Map';
import MapBrowserEvent from 'ol/MapBrowserEvent';
import MapBrowserEventType from 'ol/MapBrowserEventType';
import {MapFeature} from '../features/map-feature';
import {Features} from './features';
import {PlannerEngine} from './planner-engine';

export class PlannerInteraction {

  private eventDebugLogCount = 0;
  private readonly eventDebugLogEnabled = false;

  private readonly interaction: Interaction;

  private ctrl = false;

  constructor(private engine: PlannerEngine) {
    this.interaction = this.buildInteraction();
  }

  addToMap(map: Map) {
    /*
      Note that the interaction is added at the end of the collection of interactions that the map
      already has.  When processing an event, the interactions are processing in reversed order of
      the interaction collection.  This means that our interaction is processed first, and we have
      to be careful with the decision to further propagate the event to the other interactions or
      not.
     */
    map.addInteraction(this.interaction);
  }

  private buildInteraction(): Interaction {
    return new Interaction({

      handleEvent: (evt: MapBrowserEvent) => {

        this.eventDebugLog(evt.type + ', platformModifierKeyOnly=' + platformModifierKeyOnly(evt));

        const ctrlState = platformModifierKeyOnly(evt);
        if (ctrlState === true || ctrlState === false) {
          this.ctrl = ctrlState;
        }

        if (MapBrowserEventType.SINGLECLICK === evt.type) {
          return this.engine.handleSingleClickEvent(this.getFeaturesAt(evt), evt.coordinate, this.ctrl);
        }

        if (MapBrowserEventType.POINTERMOVE === evt.type) {
          return this.engine.handleMoveEvent(this.getFeaturesAt(evt), evt.coordinate, this.ctrl);
        }

        if (MapBrowserEventType.POINTERDRAG === evt.type) {
          return this.engine.handleDragEvent(this.getFeaturesAt(evt), evt.coordinate);
        }

        if (MapBrowserEventType.POINTERUP === evt.type) {
          return this.engine.handleUpEvent(this.getFeaturesAt(evt), evt.coordinate);
        }

        if (MapBrowserEventType.POINTERDOWN === evt.type) {
          return this.engine.handleDownEvent(this.getFeaturesAt(evt), evt.coordinate);
        }

        // known unhandled events, propagated to other interactions

        if (MapBrowserEventType.CLICK === evt.type) {
          return true;
        }

        if (MapBrowserEventType.DBLCLICK === evt.type) {
          return true;
        }

        if (MapBrowserEventType.POINTEROVER === evt.type) {
          return true;
        }

        if (MapBrowserEventType.POINTEROUT === evt.type) {
          return true;
        }

        if (MapBrowserEventType.POINTERENTER === evt.type) {
          return true;
        }

        if (MapBrowserEventType.POINTERLEAVE === evt.type) {
          return true;
        }

        if (MapBrowserEventType.POINTERCANCEL === evt.type) {
          return true;
        }

        // unknown unhandled events, propagated to other interactions

        this.eventDebugLog('Unexpected event: ' + evt.type);
        return true;
      },
    });
  }

  private getFeaturesAt(evt: MapBrowserEvent): List<MapFeature> {
    const features = evt.map.getFeaturesAtPixel(evt.pixel);
    if (features) {
      return List(features.map(feature => Features.mapFeature(feature)).filter(f => f !== null));
    }
    return List();
  }

  private eventDebugLog(message: string): void {
    if (this.eventDebugLogEnabled) {
      console.log(`PlannerInteraction ${this.eventDebugLogCount++}: ${message}`);
    }
  }

}
