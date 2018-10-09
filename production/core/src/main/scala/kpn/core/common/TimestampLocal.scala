package kpn.core.common

import java.time.ZoneId
import java.time.ZonedDateTime

import kpn.shared.Timestamp

import scala.reflect.runtime.universe._

object TimestampLocal {

  private val rm = scala.reflect.runtime.currentMirror
  private val timestampLocal = new TimestampLocal()

  private val cache = scala.collection.mutable.Map[String, Iterable[MethodSymbol]]()

  def localize(root: Any): Unit = {
    root match {
      case _: Int =>
      case _: Boolean =>
      case _: Long =>
      case _: String =>
      case None =>
      case Nil =>
      case timestamp: Timestamp => timestampLocal.makeLocal(timestamp)
      case collection: Seq[Any] => collection.foreach(localize)
      case _ => walk(root)
    }
  }

  private def walk(root: Any): Unit = {
    val instanceMirror = rm.reflect(root)
    for (accessor <- accessorsOfInstance(root)) {
      val child = instanceMirror.reflectMethod(accessor).apply()
      localize(child)
    }
  }

  private def accessorsOfInstance(root: Any): Iterable[MethodSymbol] = {
    val clazz = root.getClass
    val className = clazz.getName
    cache.get(className) match {
      case Some(accessors) => accessors
      case None =>
        val accessors = accessorsOfClass(clazz)
        cache.update(className, accessors)
        accessors
    }
  }

  private def accessorsOfClass(clazz: Class[_]): Iterable[MethodSymbol] = {
    val rootType = rm.classSymbol(clazz).toType
    rootType.members.collect {
      case m: MethodSymbol if m.isCaseAccessor => m
    }
  }

}

class TimestampLocal {

  private val rm = scala.reflect.runtime.currentMirror
  private val timestampType = rm.classSymbol(classOf[Timestamp]).toType

  private val yearAccessor = accessor("year")
  private val monthAccessor = accessor("month")
  private val dayAccessor = accessor("day")
  private val hourAccessor = accessor("hour")
  private val minuteAccessor = accessor("minute")
  private val secondAccessor = accessor("second")

  def makeLocal(timestamp: Timestamp): Unit = {

    val instanceMirror = rm.reflect(timestamp)

    val yearField = instanceMirror.reflectField(yearAccessor)
    val monthField = instanceMirror.reflectField(monthAccessor)
    val dayField = instanceMirror.reflectField(dayAccessor)
    val hourField = instanceMirror.reflectField(hourAccessor)
    val minuteField = instanceMirror.reflectField(minuteAccessor)
    val secondField = instanceMirror.reflectField(secondAccessor)

    val zoned = ZonedDateTime.of(
      yearField.get.toString.toInt,
      monthField.get.toString.toInt,
      dayField.get.toString.toInt,
      hourField.get.toString.toInt,
      minuteField.get.toString.toInt,
      secondField.get.toString.toInt,
      0,
      ZoneId.of("UTC")
    )

    val local = zoned.withZoneSameInstant(ZoneId.of("Europe/Brussels"))

    yearField.set(local.getYear)
    monthField.set(local.getMonthValue)
    dayField.set(local.getDayOfMonth)
    hourField.set(local.getHour)
    minuteField.set(local.getMinute)
    secondField.set(local.getSecond)
  }

  private def accessor(name: String): MethodSymbol = {
    val accessors = timestampType.members.collect {
      case m: MethodSymbol if m.isCaseAccessor => m
    }
    accessors.find(_.name.toString == name).get
  }

}
