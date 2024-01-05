import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { IndicatorDialogComponent } from '@app/components/shared/indicator';
import { MarkdownModule } from 'ngx-markdown';

@Component({
  selector: 'kpn-network-indicator-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-indicator-dialog letter="N" i18n-letter="@@network-indicator.letter" [color]="color">
      <div dialog-title>
        @switch (color) {
          @case ('orange') {
            <span i18n="@@network-indicator.orange.title"
              >Unexpected - Defined in network relation</span
            >
          }
          @case ('green') {
            <span i18n="@@network-indicator.green.title">OK - Defined in network relation</span>
          }
          @case ('gray') {
            <span i18n="@@network-indicator.gray.title">OK - Not defined in network relation</span>
          }
          @case ('red') {
            <span i18n="@@network-indicator.red.title"
              >Not OK - Not defined in network relation</span
            >
          }
        }
      </div>
      <div dialog-body>
        @switch (color) {
          @case ('orange') {
            <markdown i18n="@@network-indicator.orange.text">
              This node is included as a member in the network relation. We did not expect this,
              because all routes to this node have role *"connection"*. This would mean that the
              node is part of another network. We expect that the node is not included in the
              network relation, unless it receives the role *"connection"*.
            </markdown>
          }
          @case ('green') {
            <span i18n="@@network-indicator.green.text">
              This node is included as a member in the network relation. This is what we expect.
            </span>
          }
          @case ('gray') {
            <markdown i18n="@@network-indicator.gray.text">
              This node is not included as a member in the network relation. This is OK. This node
              must belong to a different network, because all routes to this node within this
              network have the role *"connection"* in the network relation.
            </markdown>
          }
          @case ('red') {
            <markdown i18n="@@network-indicator.red.text">
              This node is not included as a member in the network relation. This is not OK. The
              convention is to include each node in the network relation. An exception is when the
              node belongs to another network (all routes to this node have role *"connection"* in
              the network relation), then the node does not have to be included as member in the
              network relation. The node can be added the network relation, but should get the role
              *"connection"* in that case.
            </markdown>
          }
        }
      </div>
    </kpn-indicator-dialog>
  `,
  standalone: true,
  imports: [IndicatorDialogComponent, MarkdownModule],
})
export class NetworkIndicatorDialogComponent {
  protected readonly color: string = inject(MAT_DIALOG_DATA);
}
