import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {ReactiveFormsModule} from "@angular/forms";
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {MatButtonModule} from "@angular/material/button";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatIconModule} from "@angular/material/icon";
import {MatInputModule} from "@angular/material/input";
import {MatRadioModule} from "@angular/material/radio";
import {MatTreeModule} from "@angular/material/tree";
import {SharedModule} from "../../components/shared/shared.module";
import {AnalysisRoutingModule} from "./analysis-routing.module";
import {AnalysisCanoePageComponent} from "./pages/analysis-canoe-page.component";
import {AnalysisCyclingPageComponent} from "./pages/analysis-cycling-page.component";
import {AnalysisHikingPageComponent} from "./pages/analysis-hiking-page.component";
import {AnalysisHorseRidingPageComponent} from "./pages/analysis-horse-riding-page.component";
import {AnalysisInlineSkatingPageComponent} from "./pages/analysis-inline-skating-page.component";
import {AnalysisMotorboatPageComponent} from "./pages/analysis-motorboat-page.component";
import {AnalysisPageComponent} from "./pages/analysis-page.component";
import {LocationNodesPageComponent} from "./pages/location-nodes-page.component";
import {LocationSelectionPageComponent} from "./pages/location-selection-page.component";
import {LocationSelectorComponent} from "./pages/location-selector.component";
import {LocationTreeComponent} from "./pages/location-tree.component";
import {AnalysisModeComponent} from "./pages/analysis-mode.component";
import {AnalysisModeService} from "./pages/analysis-mode.service";
import { LocationModeComponent } from "./pages/location-mode.component";
import {LocationModeService} from "./pages/location-mode.service";

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    AnalysisRoutingModule,
    MatTreeModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatAutocompleteModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatRadioModule
  ],
  declarations: [
    AnalysisPageComponent,
    LocationTreeComponent,
    LocationSelectorComponent,
    AnalysisCyclingPageComponent,
    AnalysisHikingPageComponent,
    AnalysisInlineSkatingPageComponent,
    AnalysisMotorboatPageComponent,
    AnalysisHorseRidingPageComponent,
    AnalysisCanoePageComponent,
    LocationSelectionPageComponent,
    LocationNodesPageComponent,
    AnalysisModeComponent,
    LocationModeComponent
  ],
  providers: [
    AnalysisModeService,
    LocationModeService
  ]
})
export class AnalysisModule {
}
