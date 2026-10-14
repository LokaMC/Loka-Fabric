"""Update the pack release version without changing dependency versions."""
from pathlib import Path
import argparse, hashlib, json, re

ROOT = Path(__file__).resolve().parents[1]

def save(path, value):
    path.write_text(json.dumps(value, ensure_ascii=False, indent=2) + '\n', encoding='utf-8', newline='')

def update(version):
    if not re.fullmatch(r'\d+\.\d+\.\d+', version):
        raise ValueError('Use a release version such as 2.0.0')
    manifest_path = ROOT / 'docs/release-manifest.json'
    manifest = json.loads(manifest_path.read_text())
    old = manifest['version']
    old_base = old.split('+', 1)[0]
    old_tag, tag = f'v{old_base}-26.3', f'v{version}-26.3'
    changelog_path = ROOT / 'docs/CHANGELOG.md'
    changelog = changelog_path.read_text(encoding='utf-8').replace(old, version)
    changelog = changelog.replace(f'26.3 — {old_base}', f'26.3 — {version}')
    changelog_path.write_text(changelog, encoding='utf-8', newline='')
    for edition, info in zip(('loka-fabric', 'loka-fabric-plus'), manifest['packs']):
        folder = ROOT / 'packs' / edition / '26.3'
        index_path = folder / 'modrinth.index.json'
        index = json.loads(index_path.read_text())
        index['versionId'] = version
        save(index_path, index)
        filename = info['filename'].replace(old_base, version)
        info['filename'] = filename
        metadata_path = folder / 'metadata/version-fields.json'
        metadata = json.loads(metadata_path.read_text())
        metadata.update(name=f'{info["name"]} 26.3 — {version}', version_number=version,
                        version_type='release', changelog=changelog,
                        file_parts=[filename], primary_file=filename)
        save(metadata_path, metadata)
        branding_path = folder / 'overrides/config/packbranding/config.json'
        branding = json.loads(branding_path.read_text())
        branding['packVersion'] = version
        save(branding_path, branding)
        overrides_path = folder / 'overrides-manifest.json'
        overrides = json.loads(overrides_path.read_text())
        overrides['overrides/config/packbranding/config.json']['sha512'] = hashlib.sha512(branding_path.read_bytes()).hexdigest()
        save(overrides_path, overrides)
        path = folder / 'README.md'
        path.write_text(path.read_text(encoding='utf-8').replace(old, version), encoding='utf-8', newline='')
    manifest['version'] = version
    save(manifest_path, manifest)
    for name in ('README.md', 'docs/VALIDATION.md', 'docs/RELEASE_CHECKLIST.md', 'assets/large-assets.json'):
        path = ROOT / name
        text = path.read_text(encoding='utf-8').replace(old_tag, tag).replace(old, version)
        path.write_text(text, encoding='utf-8', newline='')
    print(f'Pack metadata updated to {version}; dependency versions preserved.')

if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('version')
    update(parser.parse_args().version)
