# Change Log
All notable changes to this project will be documented in this file.

## [unreleased]
- Fixed travis-ci.
- Fixed a bug where speed limits were actually in b/s instead of kb/s.
- Improved caching policy by taking blocks already cached into account.

## [prealpha-0.05] - 2016-05-19
### Added
- A change log! :D
- Initial BlockSites - Static only for the minute.
- RSA Key Pairs for authoring BlockSites.
- BlockSite Initial Index (the.index).
- Added upload/download limiting.
- Added settings window.
- Added better file upload window.
- Added file tags - searching supported.
- Added auto tag generation.

### Changes
- Bug fixes.
- Fixed killing all threads on shutdown.
- Uploads now queued for background threads.

### Cleanup
- Removed Unused Imports!
