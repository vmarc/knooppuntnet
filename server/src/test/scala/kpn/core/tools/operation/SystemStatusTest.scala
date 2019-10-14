package kpn.core.tools.operation

import kpn.core.replicate.ReplicationStateRepository
import kpn.core.tools.status.StatusRepository
import kpn.shared.ReplicationId
import kpn.shared.Timestamp
import org.scalamock.scalatest.MockFactory
import org.scalatest.FunSuite
import org.scalatest.Matchers

class SystemStatusTest extends FunSuite with Matchers with MockFactory {

  private val processLines =
    """UID        PID  PPID  C STIME TTY          TIME CMD
      |root       922     1  0  2015 ?        00:00:01 dhclient -1 -v -pf /run/dhclient.p2p1.pid -lf /var/lib/dhcp/dhclient.p2p1.leases p2p1
      |root      1057     1  0  2015 ?        00:00:00 su couchdb -c /usr/bin/couchdb
      |couchdb   1176  1057  3  2015 ?        23:21:22 /usr/lib/erlang/erts-5.10.4/bin/beam.smp -Bd -K true -A 4 -- -root /usr/lib/erlang -progname erl -- -home /var/lib/couchdb -- -noshell -noinput -os_m
      |root      1216     1  0  2015 ?        00:00:01 cron
      |couchdb   1492  1176  0  2015 ?        03:36:51 /usr/bin/couchjs /usr/share/couchdb/server/main.js
      |couchdb   1494  1176  0  2015 ?        04:07:04 /usr/bin/couchjs /usr/share/couchdb/server/main.js
      |marcv     1726     1  0  2015 ?        00:00:00 /bin/bash ./replicator.sh
      |marcv     1731  1726  0  2015 ?        01:12:13 java/bin/java -cp app/lib/knooppuntnet-core.knooppuntnet-core-0.1-SNAPSHOT.jar:app/lib/* -Dlog4j.configurationFile=scripts/replicator-log4j2.xml be.k
      |marcv    17666     1  0  2015 ?        01:06:52 /media/ssd/kpn-attic/overpass/bin/dispatcher --osm-base --attic --db-dir=/media/ssd/kpn-attic/database
      |marcv    17670     1  0  2015 ?        00:13:54 /media/ssd/kpn-attic/overpass/bin/dispatcher --areas --db-dir=/media/ssd/kpn-attic/database
      |marcv    17754     1  0  2015 ?        00:00:00 /bin/bash ./updater.sh
      |marcv    17760 17754  0  2015 ?        02:13:50 java/bin/java -cp app/lib/knooppuntnet-core.knooppuntnet-core-0.1-SNAPSHOT.jar:app/lib/* -Dlog4j.configurationFile=scripts/updater-log4j2.xml -Dcom.s
      |kpn      22321     1  0 Jan08 ?        00:44:34 /kpn/wrk/soft/jdk1.8.0_45/bin/java -Xms1024m -Xss10m -Xmx4096m -XX:ReservedCodeCacheSize=128m -Dlog4j.configurationFile=/home/kpn/wrk/demo/log4j2.xml
      |kpn      22962 22321  0 Jan08 ?        00:00:00 /media/ssd/kpn-attic/overpass/bin/osm3s_query
      |kpn      25842     1  0  2015 ?        02:37:21 /kpn/wrk/soft/jdk1.8.0_45/bin/java -Xms1024m -Xss10m -Xmx4096m -XX:ReservedCodeCacheSize=128m -Dlog4j.configurationFile=/home/kpn/wrk/demo/log4j2.xml
      |kpn      27939 25842  0  2015 ?        00:00:00 /media/ssd/kpn-attic/overpass/bin/osm3s_query
      |root     31464     2  0  2015 ?        00:00:01 [kworker/6:2]""".stripMargin.split("\n").toList

  ignore("process and replication status") {

    val processReporter = stub[ProcessReporter]
    val statusRepository = stub[StatusRepository]
    val replicationStateRepository = stub[ReplicationStateRepository]

    (processReporter.processes _).when().returns(processLines)

    (statusRepository.replicatorStatus _).when().returns(Some(ReplicationId(1, 1, 4)))
    (statusRepository.updaterStatus _).when().returns(Some(ReplicationId(1, 1, 3)))
    (statusRepository.changesStatus _).when().returns(None)
    (statusRepository.analysisStatus1 _).when().returns(Some(ReplicationId(1, 1, 7)))
    (statusRepository.analysisStatus2 _).when().returns(Some(ReplicationId(1, 1, 8)))
    (statusRepository.analysisStatus3 _).when().returns(Some(ReplicationId(1, 1, 9)))

    (replicationStateRepository.read _).when(ReplicationId(1, 1, 1)).returns(Timestamp(2015, 8, 11, 1, 2, 3))
    (replicationStateRepository.read _).when(ReplicationId(1, 1, 2)).returns(Timestamp(2015, 8, 11, 2, 2, 3))
    (replicationStateRepository.read _).when(ReplicationId(1, 1, 3)).returns(Timestamp(2015, 8, 11, 3, 2, 3))
    (replicationStateRepository.read _).when(ReplicationId(1, 1, 4)).returns(Timestamp(2015, 8, 11, 4, 2, 3))
    (replicationStateRepository.read _).when(ReplicationId(1, 1, 7)).returns(Timestamp(2015, 8, 11, 7, 2, 3))
    (replicationStateRepository.read _).when(ReplicationId(1, 1, 8)).returns(Timestamp(2015, 8, 11, 8, 2, 3))
    (replicationStateRepository.read _).when(ReplicationId(1, 1, 9)).returns(Timestamp(2015, 8, 11, 9, 2, 3))

    val status = new SystemStatus(processReporter, statusRepository, replicationStateRepository)

    val expected =
      """            Name Status   PID  Start   Elapsed
        | main-dispatcher OK     17666   2015  01:06:52
        |areas-dispatcher OK     17670   2015  00:13:54
        |      replicator OK      1731   2015  01:12:13
        |         updater OK     17760   2015  02:13:50
        |       analyzer1 NOK
        |       analyzer2 NOK
        |       analyzer3 NOK
        |           query OK     22962  Jan08  00:00:00
        |           query OK     27939   2015  00:00:00
        |
        |replication 001/001/004 2015-08-11 06:02:03
        |update      001/001/003 2015-08-11 05:02:03
        |changes     ?
        |analysis1   001/001/007 2015-08-11 09:02:03
        |analysis2   001/001/008 2015-08-11 10:02:03
        |analysis3   001/001/009 2015-08-11 11:02:03""".stripMargin

    println(status.status())

    status.status() should equal(expected)
  }
}
