import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatStepperModule } from '@angular/material/stepper';
import { ChangeDetectionStrategy } from '@angular/core';
import { MonitorRouteForm } from '@app/monitor/internal/route/components/monitor-route-form.service';
import { NzButtonComponent } from 'ng-zorro-antd/button';

@Component({
  selector: 'ui-monitor-route-properties-step-6-comment',
  changeDetection: ChangeDetectionStrategy.Default,
  template: `
    <form [formGroup]="commentForm">
      <mat-form-field appearance="fill" class="comment">
        <mat-label i18n="@@monitor.route.properties.comment.label"
          >Additional information about the route (optional):
        </mat-label>
        <textarea matInput rows="4" id="comment" [formControl]="comment"></textarea>
      </mat-form-field>
    </form>

    <div class="kpn-button-group">
      <button id="step6-back" nz-button nzType="default" i18n="@@action.back">Back</button>
    </div>
  `,
  styles: `
    .comment {
      width: 50em;
    }
  `,
  imports: [
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatStepperModule,
    ReactiveFormsModule,
    NzButtonComponent,
  ],
})
export class MonitorRoutePropertiesStep6CommentComponent {
  private readonly monitorForm = inject(MonitorRouteForm);
  protected readonly commentForm = this.monitorForm.commentForm;
  protected readonly comment = this.monitorForm.comment;
}
