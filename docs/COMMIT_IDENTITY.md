# Commit identity

Every commit in this initial upload uses:

```text
FoxyBearGames <FoxyBearGames@users.noreply.github.com>
```

Both author and committer are checked before pushing. The repository-local pre-push hook rejects other identities. To enable it in another checkout:

```sh
git config --local user.name FoxyBearGames
git config --local user.email FoxyBearGames@users.noreply.github.com
git config --local user.useConfigOnly true
git config --local core.hooksPath .githooks
```

Local commit signatures are disabled for this upload to avoid attaching a personal signing identity. GitHub's authenticated uploader account is separate from Git commit metadata.

Before upload, 4,350 files and nested archive entries were scanned for the owner's personal email, full name and machine user paths; no matches were found. Launcher accounts, saved worlds, logs and caches are excluded.
