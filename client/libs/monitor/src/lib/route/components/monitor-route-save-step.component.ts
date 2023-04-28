import { Input } from '@angular/core';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { NgIf } from '@angular/common';

@Component({
  selector: 'kpn-monitor-route-save-step',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="enabled" class="kpn-line kpn-spacer-below">
      <div class="icon">
        <mat-spinner *ngIf="status === 'busy'" diameter="20"></mat-spinner>
        <mat-icon *ngIf="status === 'todo'" svgIcon="dot" class="todo" />
        <mat-icon *ngIf="status === 'done'" svgIcon="tick" class="done" />
      </div>
      <span>{{ label }}</span>
    </div>
  `,
  styles: [
    `
      .icon {
        width: 2em;
        height: 1.5em;
      }

      .done {
        color: green;
      }

      .todo {
        color: gray;
        width: 0.3em;
        height: 0.3em;
      }
    `,
  ],
  standalone: true,
  imports: [NgIf, MatProgressSpinnerModule, MatIconModule],
})
export class MonitorRouteSaveStepComponent {
  @Input() enabled: boolean;
  @Input() status: 'todo' | 'busy' | 'done';
  @Input() label: string;
}
