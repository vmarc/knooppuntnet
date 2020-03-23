if (doc) {
  if (doc.replication) {
    var t = doc.replication.minuteDiff.timestamp;
    var delay = doc.replication.minuteDiff.delay;
    var fileSize = doc.replication.fileSize;
    var elementCount = doc.replication.elementCount;
    var changeSetCount = doc.replication.changeSetCount;

    emit(["time", "replication-delay", t.year, t.month, t.day, t.hour, t.minute], delay);
    emit(["time", "replication-bytes", t.year, t.month, t.day, t.hour, t.minute], fileSize);
    emit(["time", "replication-elements", t.year, t.month, t.day, t.hour, t.minute], elementCount);
    emit(["time", "replication-changesets", t.year, t.month, t.day, t.hour, t.minute], changeSetCount);

    emit(["week", "replication-delay", t.weekYear, t.weekWeek, t.weekDay], delay);
    emit(["week", "replication-bytes", t.weekYear, t.weekWeek, t.weekDay], fileSize);
    emit(["week", "replication-elements", t.weekYear, t.weekWeek, t.weekDay], elementCount);
    emit(["week", "replication-changesets", t.weekYear, t.weekWeek, t.weekDay], changeSetCount);
  }
  else if (doc.update) {
    var delay = doc.update.minuteDiff.delay;
    var t = doc.update.minuteDiff.timestamp;

    emit(["time", "update-delay", t.year, t.month, t.day, t.hour, t.minute], delay);
    emit(["week", "update-delay", t.weekYear, t.weekWeek, t.weekDay], delay);
  }
  else if (doc.analysis) {
    var delay = doc.analysis.minuteDiff.delay;
    var t = doc.analysis.minuteDiff.timestamp;

    emit(["time", "analysis-delay", t.year, t.month, t.day, t.hour, t.minute], delay);
    emit(["week", "analysis-delay", t.weekYear, t.weekWeek, t.weekDay], delay);
  }
}
