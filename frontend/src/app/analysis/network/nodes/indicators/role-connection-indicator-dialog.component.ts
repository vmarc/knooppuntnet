import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { IndicatorDialogComponent } from '@app/components/shared/indicator';
import { MarkdownModule } from 'ngx-markdown';

@Component({
  selector: 'kpn-role-connection-indicator-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-indicator-dialog
      letter="C"
      i18n-letter="@@role-connection-indicator.letter"
      [color]="color"
    >
      <div dialog-title>
        @switch (color) {
          @case ('blue') {
            <span i18n="@@role-connection-indicator.blue.title">OK - Connection</span>
          }
          @case ('gray') {
            <span i18n="@@role-connection-indicator.gray.title">OK - No connection role</span>
          }
        }
      </div>
      <div dialog-body>
        @switch (color) {
          @case ('blue') {
            <markdown i18n="@@role-connection-indicator.blue.text">
              This node is a connection to another network. This node has role *"connection"* in the
              network relation.
            </markdown>
          }
          @case ('gray') {
            <markdown i18n="@@role-connection-indicator.gray.text">
              This node does not have role *"connection"* in het network relation.
            </markdown>
          }
        }
      </div>
    </kpn-indicator-dialog>
  `,
  standalone: true,
  imports: [IndicatorDialogComponent, MarkdownModule],
})
export class RoleConnectionIndicatorDialogComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public color: string) {}
}
