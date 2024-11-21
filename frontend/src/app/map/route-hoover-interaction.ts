import { Signal } from '@angular/core';
import { WritableSignal } from '@angular/core';
import { Interaction } from 'ol/interaction';
import MapBrowserEvent from 'ol/MapBrowserEvent';
import MapBrowserEventType from 'ol/MapBrowserEventType';
import { HooverState } from './hoover-state';
import { RouteHooverHandler } from './route-hoover-handler';
import { RouteHooverAction } from './route-hoover-handler';

export class RouteHooverInteraction extends Interaction {
  private readonly handler: RouteHooverHandler;

  constructor(
    readonly mode: Signal<string>,
    hooverState: WritableSignal<HooverState>,
    clickAction: RouteHooverAction
  ) {
    super();
    this.handler = new RouteHooverHandler(hooverState, clickAction);
  }

  override handleEvent(evt: MapBrowserEvent<UIEvent>) {
    if (this.mode() !== 'explore') {
      return true; // no need to handle event, propagate to other interactions
    }

    if (MapBrowserEventType.SINGLECLICK === evt.type) {
      return this.handler.click(evt.coordinate);
    }

    if (MapBrowserEventType.POINTERMOVE === evt.type) {
      return this.handler.handle(evt.map.getFeaturesAtPixel(evt.pixel), evt.coordinate);
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
