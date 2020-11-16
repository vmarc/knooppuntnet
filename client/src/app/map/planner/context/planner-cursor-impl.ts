import Map from 'ol/Map';
import {PlannerCursor} from './planner-cursor';

export class PlannerCursorImpl implements PlannerCursor {

  viewPort: HTMLElement;

  addToMap(map: Map) {
    this.viewPort = map.getViewport();
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

  private setStyle(style: string): void {
    this.viewPort.style.cursor = style;
  }

}
