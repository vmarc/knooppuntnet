import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MarkdownComponent } from 'ngx-markdown';

@Component({
  selector: 'ui-fact-network-extra-member-node',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <markdown i18n="@@fact.description.network-extra-member-node">
      The network relation contains members of type *"node"* that are unexpected (we expect only
      network nodes or information maps as members in the network relation).
    </markdown>
  `,
  imports: [MarkdownComponent],
})
export class FactNetworkExtraMemberNodeComponent {}
