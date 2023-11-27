import { NgIf } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { NetworkScopes } from '@app/kpn/common';
import { NetworkTypes } from '@app/kpn/common';
import { MarkdownModule } from 'ngx-markdown';
import { IndicatorDialogComponent } from './indicator-dialog.component';
import { IntegrityIndicatorData } from './integrity-indicator-data';

@Component({
  selector: 'kpn-integrity-indicator-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-indicator-dialog letter="E" i18n-letter="@@integrity-indicator.letter" [color]="color">
      <span dialog-title *ngIf="isGray()" i18n="@@integrity-indicator.gray.title">
        OK - expected route count missing
      </span>
      <markdown dialog-body *ngIf="isGray()" i18n="@@integrity-indicator.gray.text">
        This network node does not have an *"{{ tag }}"* tag. This is OK because the use of this tag
        is optional.
      </markdown>

      <span dialog-title *ngIf="isGreen()" i18n="@@integrity-indicator.green.title">
        OK - expected route count
      </span>
      <markdown dialog-body *ngIf="isGreen()" i18n="@@integrity-indicator.green.text">
        The number of routes found in this network node ({{ actual }}) does match the expected
        number of routes ({{ expected }}) as defined in the *"{{ tag }}"* tag on this node. This is
        what we expect.
      </markdown>

      <span dialog-title *ngIf="isRed()" i18n="@@integrity-indicator.red.title">
        Not OK - unexpected route count
      </span>
      <markdown dialog-body *ngIf="isRed()" i18n="@@integrity-indicator.red.text">
        The number of routes found in this network node ({{ actual }}) does not match the expected
        number of routes ({{ expected }}) as defined in the *"{{ tag }}"* tag on this node.
      </markdown>
    </kpn-indicator-dialog>
  `,
  standalone: true,
  imports: [IndicatorDialogComponent, NgIf, MarkdownModule],
})
export class IntegrityIndicatorDialogComponent {
  constructor(@Inject(MAT_DIALOG_DATA) private indicatorData: IntegrityIndicatorData) {}

  get color(): string {
    return this.indicatorData.color();
  }

  get tag() {
    const networkTypeLetter = NetworkTypes.letter(this.indicatorData.networkType);
    const networkScopeLetter = NetworkScopes.letter(this.indicatorData.networkScope);
    return `expected_${networkScopeLetter}${networkTypeLetter}n_route_relations`;
  }

  get actual() {
    return this.indicatorData.actual;
  }

  get expected() {
    return this.indicatorData.expected;
  }

  isGray() {
    return this.color === 'gray';
  }

  isGreen() {
    return this.color === 'green';
  }

  isRed() {
    return this.color === 'red';
  }
}
