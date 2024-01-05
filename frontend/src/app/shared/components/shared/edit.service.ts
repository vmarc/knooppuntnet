import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { EditDialogComponent } from '@app/analysis/components/edit';
import { EditParameters } from '@app/analysis/components/edit';

@Injectable()
export class EditService {
  private readonly dialog = inject(MatDialog);

  edit(editParameters: EditParameters): void {
    this.dialog.open(EditDialogComponent, {
      data: editParameters,
      autoFocus: false,
      maxWidth: 600,
    });
  }
}
