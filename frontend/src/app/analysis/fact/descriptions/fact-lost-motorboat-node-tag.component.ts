import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'kpn-fact-lost-motorboat-node-tag',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p i18n="@@fact.description.lost-motorboat-node-tag">
      This node is no longer a valid motorboatnetwork node because the rmn_ref tag has been removed.
    </p>
  `,
  standalone: true,
})
export class FactLostMotorboatNodeTagComponent {}
