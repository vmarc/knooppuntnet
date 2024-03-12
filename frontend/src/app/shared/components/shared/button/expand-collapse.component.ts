import { EventEmitter } from '@angular/core';
import { Output } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatButton } from '@angular/material/button';
import { MatAccordion } from '@angular/material/expansion';

@Component({
  selector: 'kpn-expand-collapse',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class=" kpn-button-group kpn-spacer-above kpn-spacer-below">
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
  standalone: true,
  imports: [MatButton],
})
export class ExpandCollapseComponent {
  accordion = input.required<MatAccordion>();
  @Output() active = new EventEmitter<boolean>();

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
