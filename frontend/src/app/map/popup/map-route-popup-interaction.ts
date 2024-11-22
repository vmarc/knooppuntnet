import { State } from '@app/state';
import { Interaction } from 'ol/interaction';
import MapBrowserEvent from 'ol/MapBrowserEvent';
import MapBrowserEventType from 'ol/MapBrowserEventType';
import { MapRoutePopupHandler } from './map-route-popup-handler';

export class MapRoutePopupInteraction extends Interaction {
  private readonly handler: MapRoutePopupHandler;

  constructor(readonly state: State) {
    super();
    this.handler = new MapRoutePopupHandler(state);
  }

  override handleEvent(evt: MapBrowserEvent<UIEvent>) {
    if (this.state.map.mode() !== 'explore') {
      return true; // no need to handle event, propagate to other interactions
    }

    if (MapBrowserEventType.SINGLECLICK === evt.type) {
      return this.handler.click();
    }

    if (MapBrowserEventType.POINTERMOVE === evt.type) {
      const event = evt.originalEvent as PointerEvent;
      return this.handler.handle(evt.map.getFeaturesAtPixel(evt.pixel), [
        event.clientX,
        event.clientY,
      ]);
    }

    if (MapBrowserEventType.POINTEROUT === evt.type) {
      return this.handler.handle([], null);
    }

    if (MapBrowserEventType.POINTERLEAVE === evt.type) {
      return this.handler.handle([], null);
    }

    if (MapBrowserEventType.POINTERCANCEL === evt.type) {
      return true;
    }

    return true;
  }
}
