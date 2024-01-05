import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { IndicatorDialogComponent } from '@app/components/shared/indicator';
import { MarkdownModule } from 'ngx-markdown';

@Component({
  selector: 'kpn-connection-indicator-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-indicator-dialog
      letter="C"
      i18n-letter="@@node-connection-indicator.letter"
      [color]="color"
    >
      <div dialog-title>
        @switch (color) {
          @case ('blue') {
            <span i18n="@@node-connection-indicator.blue.title">OK - Connection</span>
          }
          @case ('gray') {
            <span i18n="@@node-connection-indicator.gray.title">OK - No connection</span>
          }
        }
      </div>
      <div dialog-body>
        @switch (color) {
          @case ('blue') {
            <markdown i18n="@@node-connection-indicator.blue.text">
              This node is a connection to another network. All routes to this node have the role
              *"connection"* in the network relation.
            </markdown>
          }
          @case ('gray') {
            <markdown i18n="@@node-connection-indicator.gray.text">
              This node is not a connection to another network. The node would have been considered
              as a connection to another network if all routes to this node within the network had
              role *"connection"* in the network relation.
            </markdown>
          }
        }
      </div>
    </kpn-indicator-dialog>
  `,
  standalone: true,
  imports: [IndicatorDialogComponent, MarkdownModule],
})
export class NodeConnectionIndicatorDialogComponent {
  protected readonly color: string = inject(MAT_DIALOG_DATA);
}
