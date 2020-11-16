import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';

@Component({
  selector: 'kpn-fact-network-extra-member-way',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <markdown i18n="@@fact.description.network-extra-member-way">
      The network relation contains members of type _"way"_ (expect only route relations or
      network nodes as members in the node network relation).
    </markdown>
  `
})
export class FactNetworkExtraMemberWayComponent {
}
