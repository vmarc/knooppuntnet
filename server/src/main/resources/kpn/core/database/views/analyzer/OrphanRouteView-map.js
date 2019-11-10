if (doc && doc.route && doc.route.active === true && doc.route.orphan === true) {
    var key = [
        doc.route.summary.country,
        doc.route.summary.networkType,
        doc.route.summary.id
    ];
    emit(key, doc.route.summary);
}
