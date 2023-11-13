import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'kpn-fact-lost-canoe-node-tag',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p i18n="@@fact.description.lost-canoe-node-tag">
      This node is no longer a valid canoenetwork node because the rpn_ref tag
      has been removed.
    </p>
  `,
  standalone: true,
})
export class FactLostCanoeNodeTagComponent {}
