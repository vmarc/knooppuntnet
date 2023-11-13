import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MarkdownModule } from 'ngx-markdown';

@Component({
  selector: 'kpn-fact-network-extra-member-relation',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <markdown i18n="@@fact.description.network-extra-member-relation">
      The network relation contains members of type *"relation"* that are
      unexpected (expect only valid route relations or network nodes as members
      in the node network relation).
    </markdown>
  `,
  standalone: true,
  imports: [MarkdownModule],
})
export class FactNetworkExtraMemberRelationComponent {}
