import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { TermComponent } from '@app/shared/components/term/term.component';

@Component({
  selector: 'ui-role-connection-indicator',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-term
      level="info"
      name="Role connection"
      i18n-name="@@role-connection-indicator.blue.title"
      i18n="@@role-connection-indicator.blue.text"
    >
      This node is a connection to another network. This node has role *"connection"* in the network
      relation.
    </ui-term>
  `,
  styles: `
    :host {
      display: block;
    }
  `,
  imports: [TermComponent],
})
export class RoleConnectionIndicatorComponent {}
