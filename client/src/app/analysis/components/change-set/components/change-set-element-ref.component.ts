import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { ChangeSetElementRef } from '@api/common';

export class ChangeSetElement {
  constructor(
    readonly action: string,
    readonly elementType: string,
    readonly ref: ChangeSetElementRef
  ) {}
}

@Component({
  selector: 'kpn-change-set-element-ref',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div [ngClass]="['ref-block', borderStyle()]">
      <div [ngClass]="['icon', element.action]"></div>
      <div [ngClass]="['icon', element.elementType]"></div>
      <div class="ref">
        {{ element.ref.name }}
      </div>
    </div>
  `,
  styleUrls: ['./change-set-element-ref.component.scss'],
})
export class ChangesSetElementRefComponent {
  @Input() element: ChangeSetElement;

  borderStyle() {
    if (this.element.ref.investigate === true) {
      return 'border-investigate';
    }
    if (this.element.ref.happy === true) {
      return 'border-happy';
    }
    return 'border-normal';
  }
}
