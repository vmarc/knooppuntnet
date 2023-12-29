import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { IndicatorDialogComponent } from '@app/components/shared/indicator';
import { MarkdownModule } from 'ngx-markdown';

@Component({
  selector: 'kpn-proposed-indicator-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-indicator-dialog letter="P" i18n-letter="@@proposed-indicator.letter" [color]="color">
      <div dialog-title>
        @switch (color) {
          @case ('blue') {
            <span i18n="@@proposed-indicator.blue.title"> OK - Proposed route </span>
          }
          @case ('gray') {
            <span i18n="@@proposed-indicator.gray.title"> OK - Active node </span>
          }
        }
      </div>
      <div dialog-body>
        @switch (color) {
          @case ('blue') {
            <markdown i18n="@@proposed-indicator.blue.text">
              This node is _"proposed"_. The node has lifecycle prefix "proposed:" in the tag that
              makes it a network node, or has has tag _"state=proposed"_. The node is assumed to
              still be in a planning phase and likely not signposted in the field.
            </markdown>
          }
          @case ('gray') {
            <markdown i18n="@@proposed-indicator.gray.text">
              This network node is not _"proposed"_. It is an active node.
            </markdown>
          }
        }
      </div>
    </kpn-indicator-dialog>
  `,
  standalone: true,
  imports: [IndicatorDialogComponent, MarkdownModule],
})
export class ProposedIndicatorDialogComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public color: string) {}
}
