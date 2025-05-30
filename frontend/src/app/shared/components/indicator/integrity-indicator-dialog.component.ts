import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { RouteScopes } from '@app/shared/kpn/common/route-scopes';
import { RouteTypes } from '@app/shared/kpn/common/route-types';
import { MarkdownModule } from 'ngx-markdown';
import { IndicatorDialogComponent } from './indicator-dialog.component';
import { IntegrityIndicatorData } from './integrity-indicator-data';

@Component({
  selector: 'ui-integrity-indicator-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-indicator-dialog letter="E" i18n-letter="@@integrity-indicator.letter" [color]="color">
      @if (isGray()) {
        <span dialog-title i18n="@@integrity-indicator.gray.title">
          OK - expected route count missing
        </span>
      }
      @if (isGray()) {
        <markdown dialog-body i18n="@@integrity-indicator.gray.text">
          This network node does not have an *"{{ tag }}"* tag. This is OK because the use of this
          tag is optional.
        </markdown>
      }

      @if (isGreen()) {
        <span dialog-title i18n="@@integrity-indicator.green.title">
          OK - expected route count
        </span>
      }
      @if (isGreen()) {
        <markdown dialog-body i18n="@@integrity-indicator.green.text">
          The number of routes found in this network node ({{ actual }}) does match the expected
          number of routes ({{ expected }}) as defined in the *"{{ tag }}"* tag on this node. This
          is what we expect.
        </markdown>
      }

      @if (isRed()) {
        <span dialog-title i18n="@@integrity-indicator.red.title">
          Not OK - unexpected route count
        </span>
      }
      @if (isRed()) {
        <markdown dialog-body i18n="@@integrity-indicator.red.text">
          The number of routes found in this network node ({{ actual }}) does not match the expected
          number of routes ({{ expected }}) as defined in the *"{{ tag }}"* tag on this node.
        </markdown>
      }
    </ui-indicator-dialog>
  `,
  imports: [IndicatorDialogComponent, MarkdownModule],
})
export class IntegrityIndicatorDialogComponent {
  private readonly indicatorData: IntegrityIndicatorData = inject(MAT_DIALOG_DATA);

  get color(): string {
    return this.indicatorData.color();
  }

  get tag() {
    const routeTypeLetter = RouteTypes.letter(this.indicatorData.routeType);
    const routeScopeLetter = RouteScopes.letter(this.indicatorData.routeScope);
    return `expected_${routeScopeLetter}${routeTypeLetter}n_route_relations`;
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
