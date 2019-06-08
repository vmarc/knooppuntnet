import {SectionInformation} from "./section-information";

export class PdfDocument {
  totalHoursParsed: number;
  totalMinutesParsed: number;
  totalSecondsParsed: number;
  totalTimeInSeconds: number;
  totalMeters: number;
  informations: SectionInformation[];
}
