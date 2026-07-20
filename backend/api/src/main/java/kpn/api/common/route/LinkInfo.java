package kpn.api.common.route;

import kpn.api.common.route.Link;

public record LinkInfo(
  String name,
  String description,
  Link link
) {}

/* TODO migrate

object LinkInfo {
  def all: Seq[LinkInfo] = {
    Seq(LinkDirection.Unconnected, LinkDirection.Forward, LinkDirection.Backward, LinkDirection.RoundaboutRight) flatMap { linkDirection =>
      Seq(false, true) flatMap { isLoop =>
        Seq(false, true) flatMap { isOnewayLoopForwardPart =>
          Seq(false, true) flatMap { isOnewayLoopBackwardPart =>
            Seq(false, true) flatMap { isOnewayHead =>
              Seq(false, true) flatMap { isOnewayTail =>
                Seq(true, false) flatMap { hasPrev =>
                  Seq(true, false) map { hasNext =>
                    val link = Link(
                      0,
                      linkDirection,
                      hasPrev,
                      hasNext,
                      isLoop,
                      isOnewayLoopForwardPart,
                      isOnewayLoopBackwardPart,
                      isOnewayHead,
                      isOnewayTail
                    )
                    LinkInfo(
                      link.name,
                      link.reportString,
                      link
                    )
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}

*/
