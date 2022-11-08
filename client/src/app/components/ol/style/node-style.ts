import { Color } from 'ol/color';
import { surveyUnknownNode } from './main-style-colors';
import { surveyOlder } from './main-style-colors';
import { surveyLastTwoYearsStart } from './main-style-colors';
import { surveyLastYearStart } from './main-style-colors';
import { surveyLastHalfYearStart } from './main-style-colors';
import { surveyLastMonth } from './main-style-colors';
import { surveyUnknown } from './main-style-colors';
import { darkBlue } from './main-style-colors';
import { blue } from './main-style-colors';
import { gray } from './main-style-colors';
import { darkRed } from './main-style-colors';
import { red } from './main-style-colors';
import { veryDarkGreen } from './main-style-colors';
import { darkGreen } from './main-style-colors';
import { green } from './main-style-colors';
import { lightGreen } from './main-style-colors';
import { proposedLarge } from './node-style-builder';
import { large } from './node-style-builder';
import { small } from './node-style-builder';

export class NodeStyle {
  static readonly smallLightGreen = small(lightGreen);
  static readonly smallGreen = small(green);
  static readonly smallDarkGreen = small(darkGreen);
  static readonly smallVeryDarkGreen = small(veryDarkGreen);
  static readonly smallRed = small(red);
  static readonly smallDarkRed = small(darkRed);
  static readonly smallGray = small(gray);
  static readonly smallBlue = small(blue);
  static readonly smallDarkBlue = small(darkBlue);

  static readonly largeGreen = large(green);
  static readonly largeGray = large(gray);
  static readonly largeLightGreen = large(lightGreen);
  static readonly largeDarkGreen = large(darkGreen);
  static readonly largeVeryDarkGreen = large(veryDarkGreen);
  static readonly largeDarkRed = large(darkRed);
  static readonly largeBlue = large(blue);
  static readonly largeDarkBlue = large(darkBlue);

  static readonly proposedLargeGreen = proposedLarge(green);
  static readonly proposedLargeGray = proposedLarge(gray);
  static readonly proposedLargeLightGreen = proposedLarge(lightGreen);
  static readonly proposedLargeDarkGreen = proposedLarge(darkGreen);
  static readonly proposedLargeVeryDarkGreen = proposedLarge(veryDarkGreen);
  static readonly proposedLargeDarkRed = proposedLarge(darkRed);
  static readonly proposedLargeBlue = proposedLarge(blue);
  static readonly proposedLargeDarkBlue = proposedLarge(darkBlue);

  static readonly smallSurveyUnknown = small(surveyUnknownNode);
  static readonly smallSurveyLastMonth = small(surveyLastMonth);
  static readonly smallSurveyLastHalfYearStart = small(surveyLastHalfYearStart);
  static readonly smallSurveyLastYearStart = small(surveyLastYearStart);
  static readonly smallSurveyLastTwoYearsStart = small(surveyLastTwoYearsStart);
  static readonly smallSurveyOlder = small(surveyOlder);

  static readonly largeSurveyUnknown = large(surveyUnknownNode);
  static readonly largeSurveyLastMonth = large(surveyLastMonth);
  static readonly largeSurveyLastHalfYearStart = large(surveyLastHalfYearStart);
  static readonly largeSurveyLastYearStart = large(surveyLastYearStart);
  static readonly largeSurveyLastTwoYearsStart = large(surveyLastTwoYearsStart);
  static readonly largeSurveyOlder = large(surveyOlder);
}
