import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-integrity-check-failed",
  template: `
    <ng-container i18n="@@fact.description.integrity-check-failed">
      The actual number of routes does not match the expected number of routes. Routes with
      tag "state" equal to "connection or "alternate" are not counted.
    </ng-container>
  `
})
export class FactIntegrityCheckFailedComponent {
}
