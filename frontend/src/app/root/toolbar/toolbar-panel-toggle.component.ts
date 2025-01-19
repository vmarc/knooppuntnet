import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { State } from '@app/state';
import { TuiIcon } from '@taiga-ui/core';
import { TuiSegmented } from '@taiga-ui/kit';

@Component({
  selector: 'kpn-toolbar-panel-toggle',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="panel-toggle">
      <tui-segmented [activeItemIndex]="activeItemIndex()">
        <button type="button" (click)="updateActivePanel('text')">
          <tui-icon icon="@tui.align-left" />
        </button>
        <button type="button" (click)="updateActivePanel('map')">
          <tui-icon icon="@tui.map" />
        </button>
      </tui-segmented>
    </div>
  `,
  styles: `
    .panel-toggle {
      padding-right: 0.5em;
    }
  `,
  imports: [TuiSegmented, TuiIcon, ReactiveFormsModule],
})
export class ToolbarPanelToggleComponent {
  private readonly state = inject(State);
  readonly activePanel = this.state.page.activePanel;
  readonly activeItemIndex = computed(() => (this.activePanel() === 'text' ? 0 : 1));

  updateActivePanel(value: string): void {
    this.state.page.updateActivePanel(value);
  }
}
