import { NgClass } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { ChangeSetElementRef } from '@api/common/change-set-element-ref';
import { NzIconDirective } from 'ng-zorro-antd/icon';

export class ChangeSetElement {
  constructor(
    readonly action: string,
    readonly elementType: string,
    readonly ref: ChangeSetElementRef
  ) {}
}

@Component({
  selector: 'ui-change-set-element-ref',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <span [ngClass]="['ref-block', borderStyle()]">
      @switch (element().action) {
        @case ('remove') {
          <nz-icon nzType="close" />
        }
        @case ('add') {
          <nz-icon nzType="plus" />
        }
        @case ('update') {
          <nz-icon nzType="undo" />
        }
      }
      @switch (element().elementType) {
        @case ('node') {
          <nz-icon nzType="outline:node" />
        }
        @case ('route') {
          <nz-icon nzType="outline:route" />
        }
      }

      <span>
        {{ element().ref.name }}
      </span>
    </span>
  `,
  styleUrl: './change-set-element-ref.component.scss',
  imports: [NgClass, NzIconDirective],
})
export class ChangesSetElementRefComponent {
  readonly element = input.required<ChangeSetElement>();

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
