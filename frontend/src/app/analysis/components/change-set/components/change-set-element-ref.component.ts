import { NgClass } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
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
      <div [ngClass]="['icon', element().action]"></div>
      <div [ngClass]="['icon', element().elementType]"></div>
      <div class="ref">
        {{ element().ref.name }}
      </div>
    </div>
  `,
  styleUrl: './change-set-element-ref.component.scss',
  standalone: true,
  imports: [NgClass],
})
export class ChangesSetElementRefComponent {
  element = input.required<ChangeSetElement>();

  borderStyle() {
    if (this.element().ref.investigate === true) {
      return 'border-investigate';
    }
    if (this.element().ref.happy === true) {
      return 'border-happy';
    }
    return 'border-normal';
  }
}
