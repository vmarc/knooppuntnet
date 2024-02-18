import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { EditLinkComponent } from '@app/analysis/components/edit';
import { ApiService } from '@app/services';
import { TimeoutComponent } from './timeout.component';

@Component({
  selector: 'kpn-josm-link',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <kpn-edit-link (edit)="edit()" /> `,
  standalone: true,
  imports: [EditLinkComponent],
})
export class JosmLinkComponent {
  kind = input.required<string>();
  elementId = input.required<number>();
  full = input(false);

  private readonly apiService = inject(ApiService);
  private readonly dialog = inject(MatDialog);

  edit(): void {
    const url = 'http://localhost:8111/import?url=https://api.openstreetmap.org/api/0.6';
    const fullUrl = `${url}/${this.kind()}/${this.elementId()}${this.full() ? '/full' : ''}`;
    this.apiService.edit(fullUrl).subscribe({
      next: (result) => {},
      error: (err) => {
        this.dialog.open(TimeoutComponent, { autoFocus: false, maxWidth: 500 });
      },
    });
  }
}
