import { Map as MaplibreMap } from 'maplibre-gl';
import { PlannerCursor } from './planner-cursor';

export class PlannerCursorImpl implements PlannerCursor {
  private htmlElement: HTMLElement;

  addToMap(map: MaplibreMap) {
    this.htmlElement = map.getCanvas();
  }

  setStyleGrab(): void {
    this.setStyle('grab');
  }

  setStyleGrabbing(): void {
    this.setStyle('grabbing');
  }

  setStylePointer(): void {
    this.setStyle('pointer');
  }

  setStyleDefault(): void {
    this.setStyle('default');
  }

  setStyleWait(): void {
    this.setStyle('wait');
  }

  private setStyle(style: string): void {
    this.htmlElement.style.cursor = style;
  }
}
