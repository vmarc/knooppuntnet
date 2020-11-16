import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';

@Component({
  selector: 'kpn-fact-route-suspicious-ways',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p i18n="@@fact.description.route-suspicious-ways">
      Route with funny ways (for example ways with only 1 node).
    </p>
  `
})
export class FactRouteSuspiciousWaysComponent {
}
