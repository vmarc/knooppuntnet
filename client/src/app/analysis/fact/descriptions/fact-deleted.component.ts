import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-deleted",
  template: `
    <p i18n="@@fact.description.deleted">
      Deleted from the OpenStreetMap database.
    </p>
  `
})
export class FactDeletedComponent {
}
