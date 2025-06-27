import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ChangeDetectionStrategy } from '@angular/core';
import { MonitorRouteForm } from '@app/monitor/internal/route/components/monitor-route-form.service';
import { NzFormControlComponent } from 'ng-zorro-antd/form';
import { NzFormLabelComponent } from 'ng-zorro-antd/form';
import { NzFormItemComponent } from 'ng-zorro-antd/form';
import { NzInputDirective } from 'ng-zorro-antd/input';

@Component({
  selector: 'ui-monitor-route-properties-step-6-comment',
  changeDetection: ChangeDetectionStrategy.Default,
  template: `
    <form [formGroup]="commentForm">
      <nz-form-item>
        <nz-form-label i18n="@@monitor.route.properties.comment.label">
          Additional information about the route (optional)
        </nz-form-label>
        <nz-form-control>
          <textarea nz-input rows="4" id="comment" [formControl]="comment"></textarea>
        </nz-form-control>
      </nz-form-item>
    </form>
  `,
  styles: `
    .comment {
      width: 50em;
    }
  `,
  imports: [
    ReactiveFormsModule,
    NzFormItemComponent,
    NzFormLabelComponent,
    NzFormControlComponent,
    NzInputDirective,
  ],
})
export class MonitorRoutePropertiesStep6CommentComponent {
  private readonly monitorForm = inject(MonitorRouteForm);
  protected readonly commentForm = this.monitorForm.commentForm;
  protected readonly comment = this.monitorForm.comment;
}
