import {ChangeDetectionStrategy} from '@angular/core';
import {Component, Input} from '@angular/core';
import {NetworkType} from '@api/custom/network-type';
import {Countries} from '../../../kpn/common/countries';
import {Stat} from '../domain/stat';

@Component({
  selector: 'kpn-overview-list-stat-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
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

      <kpn-overview-list-stat-row [stat]="stat" [country]="fr()" [networkType]="cycling()" [rowspan]="2"></kpn-overview-list-stat-row>
      <kpn-overview-list-stat-row [stat]="stat" [country]="fr()" [networkType]="hiking()"></kpn-overview-list-stat-row>

      <kpn-overview-list-stat-row [stat]="stat" [country]="at()" [networkType]="cycling()" [rowspan]="1"></kpn-overview-list-stat-row>

      <kpn-overview-list-stat-row [stat]="stat" [country]="es()" [networkType]="cycling()" [rowspan]="1"></kpn-overview-list-stat-row>

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

  fr() {
    return Countries.fr;
  }

  at() {
    return Countries.at;
  }

  es() {
    return Countries.es;
  }

  cycling(): NetworkType {
    return NetworkType.cycling;
  }

  hiking(): NetworkType {
    return NetworkType.hiking;
  }

  horse(): NetworkType {
    return NetworkType.horseRiding;
  }

  motorboat(): NetworkType {
    return NetworkType.motorboat;
  }

  canoe(): NetworkType {
    return NetworkType.canoe;
  }

  inlineSkating(): NetworkType {
    return NetworkType.inlineSkating;
  }

}
