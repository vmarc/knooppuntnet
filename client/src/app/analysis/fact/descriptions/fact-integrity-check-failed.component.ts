import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';

@Component({
  selector: 'kpn-fact-integrity-check-failed',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p i18n="@@fact.description.integrity-check-failed">
      The actual number of routes does not match the expected number of routes. Routes with
      tag "state" equal to "connection" or "alternate" are not counted.
    </p>
  `
})
export class FactIntegrityCheckFailedComponent {
}
