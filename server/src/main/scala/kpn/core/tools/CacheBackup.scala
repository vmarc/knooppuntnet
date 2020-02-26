package kpn.core.tools

import java.time.LocalDate

object CacheBackup {

  def main(args: Array[String]): Unit = {
    val start =  LocalDate.parse("2020-01-01")
    0 to 365 foreach { days =>
      val day = start.plusDays(days).toString.replaceAll("-", "/")
      printf("tar cf /kpn/backup/cache-backup/%s.tar /kpn/cache/%s\n", day, day)
    }

//    0 to 2 foreach { level1 =>
//      0 to 9 foreach { level2 =>
//        printf("tar cf /media/backup/replicate-backup/%03d_%d.tar %03d/%d??\n", level1, level2, level1, level2)
//      }
//    }
//
//    2012 to 2017 foreach { year =>
//      1 to 12 foreach { month =>
//        printf("tar -cvf /media/backup/cache-backup/2017-07-20/%d_%02d.tar %d/%02d > /kpn/logs/cache-backup-%d-%02d.log\n", year, month, year, month, year, month)
//      }
//    }
  }
}
