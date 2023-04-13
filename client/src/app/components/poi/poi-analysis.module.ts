import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { PoiService } from '@app/services';
import { SharedModule } from '../shared';
import { PoiAnalysisComponent } from './poi-analysis.component';

@NgModule({
  imports: [CommonModule, SharedModule],
  declarations: [PoiAnalysisComponent],
  exports: [PoiAnalysisComponent],
  providers: [PoiService],
})
export class PoiAnalysisModule {}
