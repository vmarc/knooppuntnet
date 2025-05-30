import { output } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { NzButtonComponent } from 'ng-zorro-antd/button';

@Component({
  selector: 'ui-expand-collapse',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-button-group">
      <button nz-button (click)="expandAll.emit()" i18n="@@location.tree.expand-all">
        Expand all
      </button>
      <button nz-button (click)="collapseAll.emit()" i18n="@@location.tree.collapse-all">
        Collapse all
      </button>
    </div>
  `,
  imports: [NzButtonComponent],
})
export class ExpandCollapseComponent {
  expandAll = output<void>();
  collapseAll = output<void>();
}
