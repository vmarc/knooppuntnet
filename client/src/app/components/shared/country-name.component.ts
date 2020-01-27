import {Component, Input} from "@angular/core";
import {Country} from "../../kpn/api/custom/country";
import {Util} from "./util";
import {I18nService} from "../../i18n/i18n.service";

@Component({
  selector: "kpn-country-name",
  template: `
    <ng-container *ngIf="country">
      {{countryName()}}
    </ng-container>
    <ng-container *ngIf="!country" i18n="@@country.unsupported">
      Unsupported (not Belgium, The Netherlands, Germany, France or Austria)
    </ng-container>
  `
})
export class CountryNameComponent {

  @Input() country: Country;

  constructor(private i18nService: I18nService) {
  }

  countryName(): string {
    return this.i18nService.translation("@@country." + Util.safeGet(() => this.country.domain));
  }

}
