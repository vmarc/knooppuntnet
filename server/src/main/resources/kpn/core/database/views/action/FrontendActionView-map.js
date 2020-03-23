if (doc) {
  if (doc.api) {
    var t = doc.api.timestamp;
    emit(["time", "all", t.year, t.month, t.day, t.hour, t.minute], 1);
    emit(["week", "all", t.weekYear, t.weekWeek, t.weekDay], 1);
    emit(["time", doc.api.name, t.year, t.month, t.day, t.hour, t.minute], 1);
    emit(["week", doc.api.name, t.weekYear, t.weekWeek, t.weekDay], 1);
  }
}
