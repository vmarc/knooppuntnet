if (doc) {
  if (doc.api) {
    var t = doc.api.timestamp;
    emit(["time", "all", t.year, t.month, t.day, t.hour, t.minute], 1);
    emit(["week", "all", t.weekYear, t.weekWeek, t.weekDay], 1);
    emit(["time", doc.api.name, t.year, t.month, t.day, t.hour, t.minute], 1);
    emit(["week", doc.api.name, t.weekYear, t.weekWeek, t.weekDay], 1);
  } else if (doc.log) {
    var t = doc.log.timestamp;
    for (var i = 0; i < doc.log.values.length; i++) {
      var logValue = doc.log.values[i];
      emit(["time", logValue.name, t.year, t.month, t.day, t.hour, t.minute], logValue.value);
      emit(["week", logValue.name, t.weekYear, t.weekWeek, t.weekDay], logValue.value);
    }
  }
}
