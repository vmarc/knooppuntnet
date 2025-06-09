import { output } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatButton } from '@angular/material/button';
import { MatAccordion } from '@angular/material/expansion';

@Component({
  selector: 'ui-old-expand-collapse',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-button-group kpn-spacer-above kpn-spacer-below">
      <button
        mat-stroked-button
        class="location-button"
        (click)="expandAll()"
        i18n="@@location.tree.expand-all"
      >
        Expand all
      </button>
      <button
        mat-stroked-button
        class="location-button"
        (click)="collapseAll()"
        i18n="@@location.tree.collapse-all"
      >
        Collapse all
      </button>
    </div>
  `,
  imports: [MatButton],
})
export class OldExpandCollapseComponent {
  readonly accordion = input.required<MatAccordion>();
  readonly active = output<boolean>();

  expandAll(): void {
    this.active.emit(true);
    this.accordion().openAll();
    this.active.emit(false);
  }

  collapseAll(): void {
    this.active.emit(true);
    this.accordion().closeAll();
    this.active.emit(false);
  }
}
