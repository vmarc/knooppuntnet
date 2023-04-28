import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { FormControl } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatStepperModule } from '@angular/material/stepper';

@Component({
  selector: 'kpn-monitor-route-properties-step-6-comment',
  template: `
    <mat-form-field appearance="fill" class="comment">
      <mat-label i18n="@@monitor.route.properties.comment.label"
        >Additional information about the route (optional):
      </mat-label>
      <textarea matInput rows="4" [formControl]="comment"></textarea>
    </mat-form-field>

    <div class="kpn-button-group">
      <button mat-stroked-button matStepperPrevious i18n="@@action.back">
        Back
      </button>
    </div>
  `,
  styles: [
    `
      .comment {
        width: 50em;
      }
    `,
  ],
  standalone: true,
  imports: [
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatStepperModule,
  ],
})
export class MonitorRoutePropertiesStep6CommentComponent {
  @Input() comment: FormControl<string>;
}
