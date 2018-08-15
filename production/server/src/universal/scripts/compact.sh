#!/usr/bin/env bash
#
#
#
curl -H "Content-Type: application/json" -X POST http://127.0.0.1:5984/master3/_compact
curl -H "Content-Type: application/json" -X POST http://127.0.0.1:5984/master4/_compact
curl -H "Content-Type: application/json" -X POST http://127.0.0.1:5984/master5/_compact
curl -H "Content-Type: application/json" -X POST http://127.0.0.1:5984/changes3/_compact
curl -H "Content-Type: application/json" -X POST http://127.0.0.1:5984/changes4/_compact
curl -H "Content-Type: application/json" -X POST http://127.0.0.1:5984/changes5/_compact
curl -H "Content-Type: application/json" -X POST http://127.0.0.1:5984/master3/_compact/AnalyzerDesign
curl -H "Content-Type: application/json" -X POST http://127.0.0.1:5984/master4/_compact/AnalyzerDesign
curl -H "Content-Type: application/json" -X POST http://127.0.0.1:5984/master5/_compact/AnalyzerDesign
curl -H "Content-Type: application/json" -X POST http://127.0.0.1:5984/changes3/_compact/ChangesDesign
curl -H "Content-Type: application/json" -X POST http://127.0.0.1:5984/changes4/_compact/ChangesDesign
curl -H "Content-Type: application/json" -X POST http://127.0.0.1:5984/changes5/_compact/ChangesDesign
