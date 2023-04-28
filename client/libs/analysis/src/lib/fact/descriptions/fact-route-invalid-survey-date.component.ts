import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'kpn-fact-route-invalid-survey-date',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <markdown i18n="@@fact.description.route-invalid-survey-date">
      The route survey date has an unexpected format (valid format: YYYY-MM-DD
      or YYYY-MM). The survey date is defined in the *"survey:date"* tag, or the
      *"source:date"* tag if *"source=survey"*.
    </markdown>
  `,
})
export class FactRouteInvalidSurveyDateComponent {}
