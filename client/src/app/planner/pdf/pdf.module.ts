import { NgModule } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { PlannerService } from '../planner.service';
import { PdfService } from './pdf.service';
import { BitmapIconService } from './services/bitmap-icon.service';

@NgModule({
  imports: [MatIconModule],
  declarations: [],
  exports: [],
  providers: [PdfService, BitmapIconService, PlannerService],
})
export class PdfModule {}
