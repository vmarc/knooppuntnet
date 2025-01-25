import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { State } from '@app/state';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { NzRadioComponent } from 'ng-zorro-antd/radio';
import { NzRadioGroupComponent } from 'ng-zorro-antd/radio';

@Component({
  selector: 'kpn-toolbar-panel-toggle',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <nz-radio-group [ngModel]="activePanel()" (ngModelChange)="updateActivePanel($event)">
      <button nz-radio-button nzValue="text">
        <nz-icon nzType="text" />
      </button>
      <button nz-radio-button nzValue="map">
        <nz-icon nzType="map" />
      </button>
    </nz-radio-group>
  `,
  styles: `
    :host {
      flex-grow: 2;
      display: flex;
      justify-content: flex-end;
    }
  `,
  imports: [
    FormsModule,
    NzIconDirective,
    NzRadioComponent,
    NzRadioGroupComponent,
    ReactiveFormsModule,
  ],
})
export class ToolbarPanelToggleComponent {
  private readonly state = inject(State);
  readonly activePanel = this.state.page.activePanel;

  updateActivePanel(value: string): void {
    this.state.page.updateActivePanel(value);
  }
}
