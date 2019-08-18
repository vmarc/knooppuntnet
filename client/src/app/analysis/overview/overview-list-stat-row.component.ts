import {Component, Input} from "@angular/core";
import {Country} from "../../kpn/shared/country";
import {NetworkType} from "../../kpn/shared/network-type";
import {Subset} from "../../kpn/shared/subset";
import {Stat} from "./stat";

@Component({
  selector: "kpn-overview-list-stat-row",
  template: `
    <tr>
      <td *ngIf="rowspan" [rowSpan]="rowspan">
        <kpn-country-name [country]="country"></kpn-country-name>
      </td>
      <td>
        <kpn-network-type-icon [networkType]="networkType"></kpn-network-type-icon>
      </td>
      <td class="value">
        <kpn-overview-table-cell [stat]="stat" [subset]="subset(country, networkType)"></kpn-overview-table-cell>
      </td>
    </tr>
  `,
  styles: [`
    :host {
      display: contents;
    }

    .value {
      text-align: right;
      vertical-align: middle;
      width: 3.5em;
    }
    
  `]
})
export class OverviewListStatRowComponent {

  @Input() rowspan: number = null;
  @Input() country: Country;
  @Input() networkType: NetworkType;
  @Input() stat: Stat;

  subset(country: Country, networkType: NetworkType): Subset {
    return new Subset(country, networkType);
  }

}
