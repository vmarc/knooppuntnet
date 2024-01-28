import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { input } from '@angular/core';
import { ChangeSetElementRef } from '@api/common';
import { ChangeSetElementRefs } from '@api/common';
import { ChangeSetElement } from './change-set-element-ref.component';
import { ChangesSetElementRefComponent } from './change-set-element-ref.component';

@Component({
  selector: 'kpn-change-set-element-refs',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div>
      @for (element of elements; track element) {
        <kpn-change-set-element-ref [element]="element" />
      }
    </div>
  `,
  standalone: true,
  imports: [ChangesSetElementRefComponent],
})
export class ChangesSetElementRefsComponent implements OnInit {
  elementType = input<string | undefined>();
  changeSetElementRefs = input<ChangeSetElementRefs | undefined>();

  elements: ChangeSetElement[];

  ngOnInit(): void {
    this.elements = this.refsToElements(this.changeSetElementRefs());
  }

  private refsToElements(refs: ChangeSetElementRefs): ChangeSetElement[] {
    const removed = this.buildElements('remove', refs.removed);
    const added = this.buildElements('add', refs.added);
    const updated = this.buildElements('update', refs.updated);
    return removed.concat(added).concat(updated);
  }

  private buildElements(action: string, refs: ChangeSetElementRef[]): ChangeSetElement[] {
    return refs.map((ref) => this.buildElement(action, ref));
  }

  private buildElement(action: string, ref: ChangeSetElementRef): ChangeSetElement {
    return new ChangeSetElement(action, this.elementType(), ref);
  }
}
