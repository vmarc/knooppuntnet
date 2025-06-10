import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MarkdownComponent } from 'ngx-markdown';

@Component({
  selector: 'ui-fact-network-extra-member-way',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <markdown i18n="@@fact.description.network-extra-member-way">
      The network relation contains members of type *"way"* (expect only route relations or network
      nodes as members in the node network relation).
    </markdown>
  `,
  imports: [MarkdownComponent],
})
export class FactNetworkExtraMemberWayComponent {}
