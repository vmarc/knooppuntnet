import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { Bounds } from '@api/common/bounds';
import { ApiService } from '@app/shared/services/api.service';
import { NzModalService } from 'ng-zorro-antd/modal';
import { EditConfiguration } from './edit-configuration';
import { EditGotoErrorDialogComponent } from './edit-goto-error-dialog.component';

@Injectable({
  providedIn: 'root',
})
export class EditGotoService {
  private readonly modalService = inject(NzModalService);
  private readonly apiService = inject(ApiService);
  private readonly configuration = new EditConfiguration();

  gotoBoundsInJosm(bounds: Bounds): void {
    const zoomUrl =
      this.configuration.josmUrl +
      `zoom?left=${bounds.minLon}&right=${bounds.maxLon}&top=${bounds.maxLat}&bottom=${bounds.minLat}`;
    this.apiService.edit(zoomUrl).subscribe({
      error: (err) => {
        this.modalService.create({
          nzContent: EditGotoErrorDialogComponent,
          nzFooter: null,
        });
      },
    });
  }
}
