if (doc && doc.node && doc.node.active === true) {
    var n = doc.node;
    if (n.active === true && (n.orphan === true || n.ignored === true)) {
        if (n.rwnName.length > 0) {
            var key = [
                n.ignored,
                n.orphan,
                n.display,
                n.country,
                "rwn",
                n.id
            ];
            emit(key, n);
        }
        if (n.rcnName.length > 0) {
            var key = [
                n.ignored,
                n.orphan,
                n.display,
                n.country,
                "rcn",
                n.id
            ];
            emit(key, n);
        }

        if (n.rhnName.length > 0) {
            var key = [
                n.ignored,
                n.orphan,
                n.display,
                n.country,
                "rhn",
                n.id
            ];
            emit(key, n);
        }
        if (n.rmnName.length > 0) {
            var key = [
                n.ignored,
                n.orphan,
                n.display,
                n.country,
                "rmn",
                n.id
            ];
            emit(key, n);
        }
        if (n.rpnName.length > 0) {
            var key = [
                n.ignored,
                n.orphan,
                n.display,
                n.country,
                "rpn",
                n.id
            ];
            emit(key, n);
        }
        if (n.rinName.length > 0) {
            var key = [
                n.ignored,
                n.orphan,
                n.display,
                n.country,
                "rin",
                n.id
            ];
            emit(key, n);
        }
    }
}
