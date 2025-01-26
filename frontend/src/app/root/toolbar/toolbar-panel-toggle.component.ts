import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { State } from '@app/state';
import { NzSegmentedOptions } from 'ng-zorro-antd/segmented';
import { NzSegmentedComponent } from 'ng-zorro-antd/segmented';

@Component({
  selector: 'kpn-toolbar-panel-toggle',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <nz-segmented
      [nzOptions]="options"
      [ngModel]="activePanel()"
      (ngModelChange)="updateActivePanel($event)"
    />
  `,
  styles: `
    :host {
      flex-grow: 2;
      display: flex;
      justify-content: flex-end;
    }
  `,
  imports: [FormsModule, NzSegmentedComponent, ReactiveFormsModule],
})
export class ToolbarPanelToggleComponent {
  private readonly state = inject(State);
  readonly activePanel = this.state.page.activePanel;

  readonly options: NzSegmentedOptions = [
    { value: 'text', icon: 'text' },
    { value: 'map', icon: 'map' },
  ];

  updateActivePanel(value: string): void {
    this.state.page.updateActivePanel(value);
  }
}
