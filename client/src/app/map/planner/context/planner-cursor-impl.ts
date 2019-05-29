import Map from "ol/Map";

export class PlannerCursorImpl {

  viewPort: HTMLElement;

  addToMap(map: Map) {
    this.viewPort = map.getViewport();
  }

  setStyle(style: string): void {
    this.viewPort.style.cursor = style;
  }

}
