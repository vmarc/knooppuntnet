import {Component, Input} from "@angular/core";
import {Countries} from "../../kpn/common/countries";
import {NetworkTypes} from "../../kpn/common/network-types";
import {NetworkType} from "../../kpn/shared/network-type";
import {Stat} from "./stat";

@Component({
  selector: "kpn-overview-list-stat-table",
  template: `
    <table class="kpn-table">
      <tbody>

      <kpn-overview-list-stat-row [stat]="stat" [country]="nl()" [networkType]="cycling()" [rowspan]="6"></kpn-overview-list-stat-row>
      <kpn-overview-list-stat-row [stat]="stat" [country]="nl()" [networkType]="hiking()"></kpn-overview-list-stat-row>
      <kpn-overview-list-stat-row [stat]="stat" [country]="nl()" [networkType]="horse()"></kpn-overview-list-stat-row>
      <kpn-overview-list-stat-row [stat]="stat" [country]="nl()" [networkType]="motorboat()"></kpn-overview-list-stat-row>
      <kpn-overview-list-stat-row [stat]="stat" [country]="nl()" [networkType]="canoe()"></kpn-overview-list-stat-row>
      <kpn-overview-list-stat-row [stat]="stat" [country]="nl()" [networkType]="inlineSkating()"></kpn-overview-list-stat-row>

      <kpn-overview-list-stat-row [stat]="stat" [country]="be()" [networkType]="cycling()" [rowspan]="3"></kpn-overview-list-stat-row>
      <kpn-overview-list-stat-row [stat]="stat" [country]="be()" [networkType]="hiking()"></kpn-overview-list-stat-row>
      <kpn-overview-list-stat-row [stat]="stat" [country]="be()" [networkType]="horse()"></kpn-overview-list-stat-row>

      <kpn-overview-list-stat-row [stat]="stat" [country]="de()" [networkType]="cycling()" [rowspan]="3"></kpn-overview-list-stat-row>
      <kpn-overview-list-stat-row [stat]="stat" [country]="de()" [networkType]="hiking()"></kpn-overview-list-stat-row>
      <kpn-overview-list-stat-row [stat]="stat" [country]="de()" [networkType]="horse()"></kpn-overview-list-stat-row>

      <tr>
        <td colspan="2" i18n="@@overview.total">
          Total
        </td>
        <td class="value">
          {{stat.figures.total}}
        </td>
      </tr>

      </tbody>
    </table>
  `,
  styles: [`
    .value {
      text-align: right;
      vertical-align: middle;
      width: 3.5em;
    }
  `]
})
export class OverviewListStatTableComponent {

  @Input() stat: Stat;

  nl() {
    return Countries.nl;
  }

  be() {
    return Countries.be;
  }

  de() {
    return Countries.de;
  }

  cycling(): NetworkType {
    return NetworkTypes.cycling;
  }

  hiking(): NetworkType {
    return NetworkTypes.hiking;
  }

  horse(): NetworkType {
    return NetworkTypes.horse;
  }

  motorboat(): NetworkType {
    return NetworkTypes.motorboat;
  }

  canoe(): NetworkType {
    return NetworkTypes.canoe;
  }

  inlineSkating(): NetworkType {
    return NetworkTypes.inlineSkating;
  }

}
