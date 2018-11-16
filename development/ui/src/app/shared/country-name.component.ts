import {Component, Input} from "@angular/core";
import {Country} from "../kpn/shared/country";

@Component({
  selector: 'kpn-country-name',
  template: `
    <ng-container *ngIf="country.domain == 'nl'">The Netherlands</ng-container>
    <ng-container *ngIf="country.domain == 'be'">Belgium</ng-container>
    <ng-container *ngIf="country.domain == 'de'">Germany</ng-container>
  `
})
export class CountryNameComponent {
  @Input() country: Country;
}
