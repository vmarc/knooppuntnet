// TODO migrate to Angular
package kpn.client

import kpn.client.components.common.CssSettings.default._
import kpn.client.components.common.PageWidth

object UnsafeStyles extends StyleSheet.Inline {

  import dsl._

  val textColor = rgb(60, 60, 60)
  private val linkColor = rgb(0, 0, 255)

  style(
    unsafeRoot("body")(
      font := "normal small 'Roboto', sans-serif",
      fontSize(15.px),
      fontWeight.normal,
      overflowY.scroll
    )
  )

  style(
    unsafeRoot("a")(
      color(linkColor),
      textDecoration := "none",
      &.hover(
        color(linkColor)
      ),
      &.active(
        color(linkColor)
      )
    )
  )

  style(
    unsafeRoot("a.external")(
      background := "rgba(0, 0, 0, 0) url('/assets/images/external.png') no-repeat scroll right center",
      paddingRight(13.px)
    )
  )

  style(
    unsafeRoot("h1")(
      media.maxWidth(PageWidth.SmallMaxWidth.px)(
        marginTop(18.px),
        marginBottom(5.px),
        fontSize(20.px),
        fontWeight.normal,
        color(textColor)
      ),
      media.minWidth((PageWidth.SmallMaxWidth + 1).px)(
        marginTop(18.px),
        marginBottom(5.px),
        fontSize(26.px),
        fontWeight.normal,
        color(textColor)
      )
    )
  )

  style(
    unsafeRoot("h2")(
      media.maxWidth(PageWidth.SmallMaxWidth.px)(
        marginTop(0.px),
        marginBottom(14.px),
        fontSize(16.px),
        fontWeight.normal,
        color(textColor)
      ),
      media.minWidth((PageWidth.SmallMaxWidth + 1).px)(
        marginTop(0.px),
        marginBottom(8.px),
        fontSize(20.px),
        fontWeight.normal,
        color(textColor)
      )
    )
  )

  style(
    unsafeRoot("table")(
      borderCollapse.collapse,
      borderSpacing(0.px)
    )
  )

  style(
    unsafeRoot("table th, table td")(
      textAlign.left,
      padding(5.px),
      lineHeight(20.px),
      verticalAlign.top,
      borderWidth(1.px),
      borderStyle.solid,
      borderColor(c"#ccc")
    )
  )

  style(
    unsafeRoot("table th")(
      fontWeight.bold,
      color(c"#222"),
      backgroundColor(c"#eee")
    )
  )

  style(
    unsafeRoot("p")(
      marginTop(14.px),
      marginBottom(14.px),
      color(textColor),
      maxWidth(60.em)
    )
  )

  style(
    unsafeRoot("div")(
      color(textColor)
    )
  )

  style(
    unsafeRoot("ul")(
      marginLeft(30.px)
    )
  )
}
