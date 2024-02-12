import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Bounds } from '@api/common';
import { ApiService } from '@app/services';
import { EditConfiguration } from './edit-configuration';
import { EditGotoErrorDialogComponent } from './edit-goto-error-dialog.component';

@Injectable({
  providedIn: 'root',
})
export class EditGotoService {
  private readonly dialog = inject(MatDialog);
  private readonly apiService = inject(ApiService);
  private readonly configuration = new EditConfiguration();

  gotoBoundsInJosm(bounds: Bounds): void {
    const zoomUrl =
      this.configuration.josmUrl +
      `zoom?left=${bounds.minLon}&right=${bounds.maxLon}&top=${bounds.maxLat}&bottom=${bounds.minLat}`;
    this.apiService.edit(zoomUrl).subscribe({
      error: (err) => {
        this.dialog.open(EditGotoErrorDialogComponent, {
          //autoFocus: false,
          maxWidth: 600,
        });
      },
    });
  }
}
