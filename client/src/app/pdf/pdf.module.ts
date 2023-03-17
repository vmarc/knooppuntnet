import { NgModule } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { PlannerService } from '@app/planner/services/planner.service';
import { BitmapIconService } from './bitmap-icon.service';
import { PdfService } from './pdf.service';

@NgModule({
  imports: [MatIconModule],
  declarations: [],
  exports: [],
  providers: [PdfService, BitmapIconService, PlannerService],
})
export class PdfModule {}
