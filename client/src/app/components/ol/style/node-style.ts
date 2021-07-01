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
}
