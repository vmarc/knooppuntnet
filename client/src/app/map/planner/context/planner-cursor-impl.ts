import Map from "ol/Map";
import {PlannerCursor} from "./planner-cursor";

export class PlannerCursorImpl implements PlannerCursor {

  viewPort: HTMLElement;

  addToMap(map: Map) {
    this.viewPort = map.getViewport();
  }

  setStyleMove(): void {
    this.setStyle("move");
  }

  setStylePointer(): void {
    this.setStyle("pointer");
  }

  setStyleDefault(): void {
    this.setStyle("default");
  }

  private setStyle(style: string): void {

    console.log("DEBUG cursor set style " + style);

    this.viewPort.style.cursor = style;
  }

}
