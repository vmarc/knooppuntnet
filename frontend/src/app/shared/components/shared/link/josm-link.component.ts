import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ApiService } from '@app/services';
import { TimeoutComponent } from './timeout.component';

@Component({
  selector: 'kpn-josm-link',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a
      rel="nofollow"
      (click)="edit()"
      title="Open in editor (like JOSM)"
      i18n-title="@@edit.link.title"
      i18n="@@edit.link"
      >edit</a
    >
  `,
  standalone: true,
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
