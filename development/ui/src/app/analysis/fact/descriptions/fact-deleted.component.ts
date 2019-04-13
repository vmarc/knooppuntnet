import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-deleted",
  template: `
    <ng-container i18n="@@fact.description.deleted">
      Deleted from the OpenStreetMap database.
    </ng-container>
  `
})
export class FactDeletedComponent {
}
