import {danger, fail, message, warn, schedule} from 'danger'

// Add a CHANGELOG entry for app changes
const hasChangelog = danger.git.modified_files.includes("CHANGELOG.yml")

if (!hasChangelog) {
  fail("Please add a changelog entry for your changes. :crystall_ball:")
}