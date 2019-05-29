import {Component} from "@angular/core";
import {Subsets} from "../../../kpn/common/subsets";

@Component({
  selector: "kpn-overview-table-header",
  template: `
    <tr>
      <th rowspan="2">Detail</th>
      <th rowspan="2">Total</th> <!-- Totaal -->
      <th colspan="6" i18n="@@country.nl">The Netherlands</th>
      <th colspan="3" i18n="@@country.be">Belgium</th>
      <th colspan="3" i18n="@@country.de">Germany</th>
      <th rowspan="2">Comment</th> <!-- Commentaar -->
    </tr>
    <tr>
      <th class="value-cell" *ngFor="let subset of subsets()">
        <mat-icon [svgIcon]="subset.networkType.name"></mat-icon>
      </th>
    </tr>
  `,
  styles: [`
    :host {
      display: table-header-group;
    }
  `]
})
export class OverviewTableHeaderComponent {
  subsets() {
    return Subsets.all;
  }
}
