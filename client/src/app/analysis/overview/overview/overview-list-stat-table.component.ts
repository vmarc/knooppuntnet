import {ChangeDetectionStrategy} from '@angular/core';
import {Component, Input} from '@angular/core';
import {Country} from '@api/custom/country';
import {NetworkType} from '@api/custom/network-type';
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

  nl(): Country {
    return 'nl';
  }

  be(): Country {
    return 'be';
  }

  de(): Country {
    return 'de';
  }

  fr(): Country {
    return 'fr';
  }

  at(): Country {
    return 'at';
  }

  es(): Country {
    return 'es';
  }

  cycling(): NetworkType {
    return 'cycling';
  }

  hiking(): NetworkType {
    return 'hiking';
  }

  horse(): NetworkType {
    return 'horse-riding';
  }

  motorboat(): NetworkType {
    return 'motorboat';
  }

  canoe(): NetworkType {
    return 'canoe';
  }

  inlineSkating(): NetworkType {
    return 'inline-skating';
  }

}
