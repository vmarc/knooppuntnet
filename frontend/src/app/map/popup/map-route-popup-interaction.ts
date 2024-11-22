import { Signal } from '@angular/core';
import { WritableSignal } from '@angular/core';
import { Interaction } from 'ol/interaction';
import MapBrowserEvent from 'ol/MapBrowserEvent';
import MapBrowserEventType from 'ol/MapBrowserEventType';
import { MapRoutePopupState } from './map-route-popup-state';
import { MapRoutePopupHandler } from './map-route-popup-handler';

export class MapRoutePopupInteraction extends Interaction {
  private readonly handler: MapRoutePopupHandler;

  constructor(
    readonly mode: Signal<string>,
    hooverState: WritableSignal<MapRoutePopupState>,
    clickAction: () => void
  ) {
    super();
    this.handler = new MapRoutePopupHandler(hooverState, clickAction);
  }

  override handleEvent(evt: MapBrowserEvent<UIEvent>) {
    if (this.mode() !== 'explore') {
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
