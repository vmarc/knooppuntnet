import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input, OnInit } from '@angular/core';
import { ChangeSetElementRef } from '@api/common/change-set-element-ref';
import { ChangeSetElementRefs } from '@api/common/change-set-element-refs';
import { ChangeSetElement } from './change-set-element-ref.component';

@Component({
  selector: 'kpn-change-set-element-refs',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div>
      <kpn-change-set-element-ref
        *ngFor="let element of elements"
        [element]="element"
      >
      </kpn-change-set-element-ref>
    </div>
  `,
})
export class ChangesSetElementRefsComponent implements OnInit {
  @Input() elementType: string;
  @Input() changeSetElementRefs: ChangeSetElementRefs;

  elements: ChangeSetElement[];

  ngOnInit(): void {
    this.elements = this.refsToElements(this.changeSetElementRefs);
  }

  private refsToElements(refs: ChangeSetElementRefs): ChangeSetElement[] {
    const removed = this.buildElements('remove', refs.removed);
    const added = this.buildElements('add', refs.added);
    const updated = this.buildElements('update', refs.updated);
    return removed.concat(added).concat(updated);
  }

  private buildElements(
    action: string,
    refs: ChangeSetElementRef[]
  ): ChangeSetElement[] {
    return refs.map((ref) => this.buildElement(action, ref));
  }

  private buildElement(
    action: string,
    ref: ChangeSetElementRef
  ): ChangeSetElement {
    return new ChangeSetElement(action, this.elementType, ref);
  }
}
