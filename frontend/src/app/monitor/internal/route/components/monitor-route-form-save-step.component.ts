import { viewChild } from '@angular/core';
import { AfterViewChecked } from '@angular/core';
import { ElementRef } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { MonitorRouteSaveStep } from '../monitor-route-save-step';
import { ChangeDetectionStrategy } from '@angular/core';

@Component({
  selector: 'ui-monitor-route-form-save-step',
  changeDetection: ChangeDetectionStrategy.Default,
  template: `
    <div #stepDiv class="kpn-line kpn-spacer-below">
      <div class="icon">
        @if (step().status === 'busy') {
          <nz-icon nzType="loading" />
        }

        @if (step().status === 'todo') {
          <nz-icon nzType="minus" class="todo" />
        }

        @if (step().status === 'done') {
          <nz-icon nzType="check" class="done" />
        }
      </div>
      <span>{{ step().description }}</span>
    </div>
  `,
  styles: `
    .icon {
      width: 2em;
      height: 1.5em;
    }

    .done {
      color: green;
    }

    .todo {
      color: grey;
      width: 0.3em;
      height: 0.3em;
    }
  `,
  imports: [NzIconDirective],
})
export class MonitorRouteFormSaveStepComponent implements AfterViewChecked {
  readonly step = input.required<MonitorRouteSaveStep>();
  private readonly stepDiv = viewChild.required<ElementRef<HTMLDivElement>>('stepDiv');

  ngAfterViewChecked() {
    if (this.step().status === 'busy') {
      this.stepDiv().nativeElement.scrollIntoView({
        behavior: 'smooth',
        block: 'center',
      });
    }
  }
}
