import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { EditDialogComponent } from '@app/analysis/components/edit/edit-dialog.component';
import { EditParameters } from '@app/analysis/components/edit/edit-parameters';
import { NzModalService } from 'ng-zorro-antd/modal';

@Injectable()
export class EditService {
  private readonly modalService = inject(NzModalService);

  edit(editParameters: EditParameters): void {
    this.modalService.create({
      nzTitle: $localize`:@@edit-dialog.title:Load in editor`,
      nzContent: EditDialogComponent,
      nzData: editParameters,
      nzFooter: null,
    });
  }
}
