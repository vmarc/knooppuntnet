import {Component, Input, OnInit} from '@angular/core';
import {ChangeSetElementRefs} from "../../../../kpn/shared/change-set-element-refs";
import {ChangeSetElementRef} from "../../../../kpn/shared/change-set-element-ref";
import {ChangeSetElement} from "./change-set-element-ref.component";
import {List} from "immutable";

@Component({
  selector: 'kpn-change-set-element-refs',
  template: `
    <div>
      <kpn-change-set-element-ref
        *ngFor="let element of elements"
        [element]="element">
      </kpn-change-set-element-ref>
    </div>
  `
})
export class ChangesSetElementRefsComponent implements OnInit {

  @Input() elementType: string;
  @Input() changeSetElementRefs: ChangeSetElementRefs;

  elements: List<ChangeSetElement>;

  ngOnInit(): void {
    this.elements = this.refsToElements(this.changeSetElementRefs);
  }

  refsToElements(refs: ChangeSetElementRefs) {
    const removed = this.buildElements("remove", refs.removed);
    const added = this.buildElements("add", refs.added);
    const updated = this.buildElements("update", refs.updated);
    return removed.concat(added).concat(updated);
  }

  private buildElements(action: string, refs: List<ChangeSetElementRef>) {
    return refs.map(ref => this.buildElement(action, ref));
  }

  private buildElement(action: string, ref: ChangeSetElementRef) {
    return new ChangeSetElement(action, this.elementType, ref);
  }

}
