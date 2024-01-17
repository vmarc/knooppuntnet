package kpn.server.analyzer.engine.monitor.structure

import kpn.api.common.data.Member
import kpn.api.custom.Relation

import scala.collection.mutable

class MonitorRouteMemberAnalyzer {

  // see: MonitorFilter.ignoredRoles  Seq("place_of_worship", "guest_house", "outer", "inner")
  private val rolesIgnore = Seq(
    "outer",
    "inner",
    "place_of_worship",
    "guest_house"
  )

  private val roles = Seq(
    "alternative",
    "excursion",
    "approach",
    "connection",
    "variant",
    "link",
  )

  def analyzeRoute(relation: Relation): Seq[MonitorRouteMemberGroup] = {

    var currentMemberGroupOption: Option[MonitorRouteMemberGroup] = None
    val mainMembers = mutable.Buffer[Member]()
    val memberGroups = mutable.Buffer[MonitorRouteMemberGroup]()

    relation.members.foreach { member =>
      if (isRelevant(member)) {
        alternativeRole(member) match {
          case Some(role) =>
            currentMemberGroupOption match {
              case None =>
                // first member with this role
                currentMemberGroupOption = Some(
                  MonitorRouteMemberGroup(
                    Some(role),
                    Seq(member)
                  )
                )

              case Some(currentMemberGroup) =>
                if (!currentMemberGroup.role.contains(role)) {
                  memberGroups.addOne(currentMemberGroup)
                  currentMemberGroupOption = Some(MonitorRouteMemberGroup(Some(role), Seq(member)))
                }
                currentMemberGroupOption = Some(
                  currentMemberGroup.copy(
                    members = currentMemberGroup.members :+ member
                  )
                )
            }

          case None =>
            currentMemberGroupOption match {
              case None =>
              case Some(currentMemberGroup) =>
                memberGroups.addOne(currentMemberGroup)
                currentMemberGroupOption = None
            }
            mainMembers.addOne(member)
        }
      }
    }

    currentMemberGroupOption match {
      case None =>
      case Some(currentMemberGroup) =>
        memberGroups.addOne(currentMemberGroup)
        currentMemberGroupOption = None
    }
    memberGroups.addOne(MonitorRouteMemberGroup(None, mainMembers.toSeq))
    memberGroups.toSeq
  }

  private def isRelevant(member: Member): Boolean = {
    if (member.isWay || member.isRelation) {
      member.role match {
        case Some(role) => !rolesIgnore.contains(role)
        case None => true
      }
    }
    else {
      false
    }
  }

  private def alternativeRole(member: Member): Option[String] = {
    member.role match {
      case Some(role) =>
        if (roles.contains(role)) {
          Some(role)
        }
        else {
          None
        }
      case None => None
    }
  }
}
