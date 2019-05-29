// TODO migrate to Angular
package kpn.client.components

import chandu0101.scalajs.react.components.materialui.Mui
import chandu0101.scalajs.react.components.materialui.MuiPalette
import chandu0101.scalajs.react.components.materialui.MuiTheme

object UiTheme {

  def custom: MuiTheme = {

    val basicTheme = Mui.Styles.LightRawTheme

    val palette = MuiPalette(
      //primary1Color = Mui.Styles.colors.indigo500,
      primary1Color = Mui.Styles.colors.fullWhite,
      primary2Color = Mui.Styles.colors.indigo700,
      primary3Color = Mui.Styles.colors.indigo100,
      //accent1Color = Mui.Styles.colors.pink400,
      accent1Color = Mui.Styles.colors.blue500,
      accent2Color = Mui.Styles.colors.pink700,
      accent3Color = Mui.Styles.colors.pink100,
      //textColor = basicTheme.palette.textColor,
      textColor = Mui.Styles.colors.black,
      //alternateTextColor = basicTheme.palette.alternateTextColor,
      alternateTextColor = Mui.Styles.colors.black,
      canvasColor = basicTheme.palette.canvasColor,
      borderColor = basicTheme.palette.borderColor,
      disabledColor = basicTheme.palette.disabledColor
    )

    val spacing = basicTheme.spacing.copy(desktopKeylineIncrement = 48)
    val muiRawTheme = basicTheme.copy(palette = palette, spacing = spacing)

    Mui.Styles.getMuiTheme(muiRawTheme)
  }
}
