import {ElementRef, Injectable} from "@angular/core";
import {Map} from "immutable";

@Injectable()
export class PoiService {

  private poiNames: Map<string, string> = null;

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

}
