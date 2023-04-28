import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'kpn-fact-node-member-missing',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p i18n="@@fact.description.node-member-missing">
      The node is not member of the network relation.
    </p>
  `,
})
export class FactNodeMemberMissingComponent {}
