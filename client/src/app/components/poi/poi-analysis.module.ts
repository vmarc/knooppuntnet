import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { PoiService } from '@app/services/poi.service';
import { SharedModule } from '../shared/shared.module';
import { PoiAnalysisComponent } from './poi-analysis.component';

@NgModule({
  imports: [CommonModule, SharedModule],
  declarations: [PoiAnalysisComponent],
  exports: [PoiAnalysisComponent],
  providers: [PoiService],
})
export class PoiAnalysisModule {}
