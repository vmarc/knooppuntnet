package kpn.api.common.route

enum LinkDirection:
  /*
     The link direction is "Forward" if the first node of this way is connected to the previous way
     and/or the last node of this way is connected to the next way.
   */
  case Forward,

  /*
     The link direction is "Backward" if the first node of this way is connected to the next way
     and/or the last node of this way is connected to the previous way.
   */
  Backward,

  /*
    The direction has value "RoundaboutLeft", if the way is tagged as such and it is somehow
    connected to the previous/next member.
   */
  RoundaboutLeft, // tagged as roundabout and connected to the previous/next member

  /*
    The direction has value "RoundaboutRight", if the way is tagged as such and it is somehow
    connected to the previous/next member.
   */
  RoundaboutRight,

  /*
    If there is no connection to the previous or next member, then direction has the value "Unconnected".
   */
  Unconnected

