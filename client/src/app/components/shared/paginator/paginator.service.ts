import {Injectable} from "@angular/core";

@Injectable({
  providedIn: "root"
})
export class PaginatorService {

  itemsPerPageLabel: string = null;
  nextPageLabel: string = null;
  previousPageLabel: string = null;
  firstPageLabel: string = null;
  lastPageLabel: string = null;
  of: string = null;

  updateTranslations(elements: HTMLCollection) {
    if (!this.isTranslationsUpdated()) {
      this.itemsPerPageLabel = this.translate(elements, "itemsPerPageLabel");
      this.nextPageLabel = this.translate(elements, "nextPageLabel");
      this.previousPageLabel = this.translate(elements, "previousPageLabel");
      this.firstPageLabel = this.translate(elements, "firstPageLabel");
      this.lastPageLabel = this.translate(elements, "lastPageLabel");
      this.of = this.translate(elements, "of");
    }
  }

  isTranslationsUpdated() {
    return this.itemsPerPageLabel !== null;
  }

  private translate(elements: HTMLCollection, id: string): string {
    const element = Array.from(elements).find(span => span.getAttribute("id") === id);
    if (element === undefined) {
      console.error(`PaginatorService: could not find element with id '${id}'`);
      return "?";
    }
    return element.textContent;
  }

}
