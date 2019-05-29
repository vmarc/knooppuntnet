import {Component, Input} from "@angular/core";
import {Country} from "../../kpn/shared/country";

@Component({
  selector: "kpn-country-name",
  template: `
    <ng-container *ngIf="country?.domain == 'nl'" i18n="@@country.nl">The Netherlands</ng-container>
    <ng-container *ngIf="country?.domain == 'be'" i18n="@@country.be">Belgium</ng-container>
    <ng-container *ngIf="country?.domain == 'de'" i18n="@@country.de">Germany</ng-container>
  `
})
export class CountryNameComponent {
  @Input() country: Country;
}
