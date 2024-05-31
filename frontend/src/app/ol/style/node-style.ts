import { proposedLarge } from './node-style-builder';
import { large } from './node-style-builder';
import { small } from './node-style-builder';
import { StyleColor } from './style-color';

export class NodeStyle {
  static readonly defaultSmall = small(StyleColor.defaultColor);
  static readonly defaultLarge = large(StyleColor.defaultColor);
  static readonly defaultProposedLarge = proposedLarge(StyleColor.defaultColor);

  static readonly surfaceLarge = large(StyleColor.surfacePaved);
  static readonly surfaceSmall = small(StyleColor.surfacePaved);
  static readonly surfaceProposedLarge = proposedLarge(StyleColor.surfacePaved);

  static readonly surveyUnknownSmall = small(StyleColor.surveyUnknownNode);
  static readonly surveyLastMonthSmall = small(StyleColor.surveyLastMonth);
  static readonly surveyLastHalfYearStartSmall = small(StyleColor.surveyLastHalfYearStart);
  static readonly surveyLastYearStartSmall = small(StyleColor.surveyLastYearStart);
  static readonly surveyLastTwoYearsStartSmall = small(StyleColor.surveyLastTwoYearsStart);
  static readonly surveyOlderSmall = small(StyleColor.surveyOlder);

  static readonly surveyUnknownLarge = large(StyleColor.surveyUnknownNode);
  static readonly surveyLastMonthLarge = large(StyleColor.surveyLastMonth);
  static readonly surveyLastHalfYearStartLarge = large(StyleColor.surveyLastHalfYearStart);
  static readonly surveyLastYearStartLarge = large(StyleColor.surveyLastYearStart);
  static readonly surveyLastTwoYearsStartLarge = large(StyleColor.surveyLastTwoYearsStart);
  static readonly surveyOlderLarge = large(StyleColor.surveyOlder);

  static readonly surveyUnknownProposedLarge = proposedLarge(StyleColor.surveyUnknownNode);
  static readonly surveyLastMonthProposedLarge = proposedLarge(StyleColor.surveyLastMonth);
  static readonly surveyLastHalfYearStartProposedLarge = proposedLarge(
    StyleColor.surveyLastHalfYearStart
  );
  static readonly surveyLastYearStartProposedLarge = proposedLarge(StyleColor.surveyLastYearStart);
  static readonly surveyLastTwoYearsStartProposedLarge = proposedLarge(
    StyleColor.surveyLastTwoYearsStart
  );
  static readonly surveyOlderProposedLarge = proposedLarge(StyleColor.surveyOlder);

  static readonly analysisOkSmall = small(StyleColor.analysisOk);
  static readonly analysisErrorSmall = small(StyleColor.analysisError);
  static readonly analysisOkLarge = large(StyleColor.analysisOk);
  static readonly analysisErrorLarge = large(StyleColor.analysisError);
  static readonly analysisOkProposedLarge = proposedLarge(StyleColor.analysisOk);
  static readonly analysisErrorProposedLarge = proposedLarge(StyleColor.analysisError);

  static readonly networkInSmall = small(StyleColor.networkIn);
  static readonly networkOutSmall = small(StyleColor.networkOut);
  static readonly networkInLarge = large(StyleColor.networkIn);
  static readonly networkInProposedLarge = proposedLarge(StyleColor.networkIn);
  static readonly networkOutLarge = large(StyleColor.networkOut);
  static readonly networkOutProposedLarge = proposedLarge(StyleColor.networkOut);

  static readonly smallGray = small(StyleColor.gray);
  static readonly largeGray = large(StyleColor.gray);
  static readonly proposedLargeGray = proposedLarge(StyleColor.gray);
}
