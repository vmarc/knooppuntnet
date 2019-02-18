import {ElementRef, Injectable} from "@angular/core";
import {List, Map} from "immutable";
import {PoiConfig} from "./poi-config";
import {BehaviorSubject} from "rxjs";
import {InterpretedPoiConfiguration} from "./components/ol/domain/interpreted-poi-configuration";

@Injectable()
export class PoiService {

  changed: BehaviorSubject<PoiConfig> = new BehaviorSubject(null);

  private zoomLevel: number;
  private poiConfig: PoiConfig;
  private poiActive: Map<string, boolean> = null;
  private poiNames: Map<string, string> = null;

  constructor() {
    const keysAndValues: Array<[string, boolean]> = [];
    keysAndValues.push(["windmill" , true]);
    keysAndValues.push(["bench" , true]);
    this.poiActive = Map<string, boolean>(keysAndValues);
  }

  updateRegistry(element: ElementRef) {
    if (this.poiNames === null) {
      const spans = element.nativeElement.childNodes;
      const keysAndValues: Array<[string, string]> = [];
      spans.forEach(span => {
        const id = span.getAttribute("id");
        const translation = span.textContent;
        keysAndValues.push([id, translation]);
      });
      this.poiNames = Map<string, string>(keysAndValues);
    }
  }

  name(poiId: string): string {
    return this.poiNames.get(poiId);
  }

  isPoiActive(poiId: string): boolean {
    if (this.poiActive == null) {
      return null;
    }
    return this.poiActive.get(poiId, false);
  }

  updateZoomLevel(zoomLevel: number): void {
    this.zoomLevel = zoomLevel;
    this.updatePoiActive();
  }

  updatePoiConfig(poiConfig: PoiConfig): void {
    this.poiConfig = poiConfig;
    this.updatePoiActive();
  }

  updateEnabled(enabled: boolean): void {


    this.updatePoiActive();
  }

  updateGroupEnabled(enabled: boolean): void {


    this.updatePoiActive();
  }

  updatePoiLevel(poiId: string, minLevel): void {

    const keysAndValues: Array<[string, boolean]> = [];
    keysAndValues.push([poiId, true]);
    this.poiActive = Map<string, boolean>(keysAndValues);

    this.updatePoiActive();
  }

  updatePoiActive(): void {
    if (this.zoomLevel != null && this.poiConfig != null) {


    }
    this.changed.next(new PoiConfig(true, List()));
  }

}
