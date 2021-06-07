package kpn.server.repository

import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.filter.ChangesFilter
import kpn.api.common.changes.filter.ChangesFilterPeriod
import kpn.api.common.changes.filter.ChangesParameters
import kpn.core.common.Time
import kpn.core.mongo.changes.MongoQueryNodeChangeCounts
import kpn.core.mongo.changes.MongoQueryNodeChanges
import kpn.core.mongo.statistics.ChangeSetCounts
import org.mongodb.scala.MongoDatabase
import org.springframework.stereotype.Component

@Component
class MongoChangeRepositoryImpl(database: MongoDatabase) extends MongoChangeRepository {

  override def nodeChanges(nodeId: Long, parameters: ChangesParameters): Seq[NodeChange] = {
    new MongoQueryNodeChanges(database).execute(nodeId, parameters)
  }

  override def nodeChangesFilter(nodeId: Long, yearOption: Option[String], monthOption: Option[String], dayOption: Option[String]): ChangesFilter = {
    val year = yearOption match {
      case None => Time.now.year
      case Some(year) => year.toInt
    }
    val changeSetCounts = new MongoQueryNodeChangeCounts(database).execute(nodeId, year, monthOption.map(_.toInt))
    toFilter(changeSetCounts, Some(year.toString), monthOption, dayOption)
  }

  private def toFilter(changeSetCounts: ChangeSetCounts, yearOption: Option[String], monthOption: Option[String], dayOption: Option[String]): ChangesFilter = {
    val periods = changeSetCounts.years.map { yearChangeSetCount =>
      val monthPeriods = changeSetCounts.months.filter(_.year == yearChangeSetCount.year).map { monthChangeSetCount =>
        val dayPeriods = changeSetCounts.days.filter(csc => csc.year == yearChangeSetCount.year && csc.month == yearChangeSetCount.month).map { dayChangeSetCount =>
          ChangesFilterPeriod(
            f"${dayChangeSetCount.day}%02d",
            dayChangeSetCount.total,
            dayChangeSetCount.impact,
            current = false,
            selected = dayOption.contains(f"${dayChangeSetCount.day}%02d"),
            Seq.empty
          )
        }
        ChangesFilterPeriod(
          f"${monthChangeSetCount.month}%02d",
          monthChangeSetCount.total,
          monthChangeSetCount.impact,
          current = false,
          selected = monthOption.contains(f"${monthChangeSetCount.month}%02d"),
          dayPeriods
        )
      }
      ChangesFilterPeriod(
        yearChangeSetCount.year.toString,
        yearChangeSetCount.total,
        yearChangeSetCount.impact,
        current = false,
        selected = yearOption.contains(yearChangeSetCount.year.toString),
        monthPeriods
      )
    }
    ChangesFilter(periods)
  }
}
