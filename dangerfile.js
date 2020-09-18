import {danger, fail, message, warn, schedule} from 'danger'

const reporter = require("danger-plugin-lint-report")

// Scan ktlint reports
schedule(reporter.scan({
    fileMask: "**/reports/ktlint/*.xml",
    reportSeverity: true,
    requireLineModification: true,
}))

// Scan detekt reports
schedule(reporter.scan({
    fileMask: "**/reports/detekt.xml",
    reportSeverity: true,
    requireLineModification: true,
}))

// Scan Android Lint reports
schedule(reporter.scan({
    fileMask: "**/reports/lint-results-*.xml",
    reportSeverity: true,
    requireLineModification: true,
}))