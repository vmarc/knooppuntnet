import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { ApiResponse } from '@api/custom/api-response';
import { SituationOnComponent } from '@app/shared/components/timestamp/situation-on.component';

@Component({
  selector: 'ui-location-response',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (response()) {
      @if (situationOnEnabled()) {
        <ui-situation-on [timestamp]="response().situationOn" />
      }
      @if (!response().result) {
        <p i18n="@@location.location-not-found">Location not found</p>
      } @else {
        <ng-content />
      }
    }
  `,
  imports: [SituationOnComponent],
})
export class LocationResponseComponent {
  situationOnEnabled = input(true);
  response = input.required<ApiResponse<any>>();
}
