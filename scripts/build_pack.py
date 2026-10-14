"""Assemble either edition from pinned index and byte-verified overrides."""
from pathlib import Path, PurePosixPath
import argparse, hashlib, json, urllib.request, zipfile

ROOT = Path(__file__).resolve().parents[1]

def safe(name):
    p = PurePosixPath(name)
    assert not p.is_absolute() and '..' not in p.parts and '\\' not in name, name
    return name

def build(edition, asset_path=None):
    folder = ROOT / 'packs' / edition / '26.3'
    index = (folder / 'modrinth.index.json').read_bytes()
    overrides = json.loads((folder / 'overrides-manifest.json').read_text())
    assets = json.loads((ROOT / 'assets/large-assets.json').read_text())
    archive = Path(asset_path) if asset_path else ROOT / '.cache' / 'Loka-Fabric-26.3-shared-assets.zip'
    if not archive.exists():
        assert not asset_path, 'Specified assets archive does not exist'
        archive.parent.mkdir(parents=True, exist_ok=True)
        temporary = archive.with_suffix('.tmp')
        urllib.request.urlretrieve(assets['url'], temporary)
        assert hashlib.sha512(temporary.read_bytes()).hexdigest() == assets['sha512'], 'Asset archive hash mismatch'
        temporary.replace(archive)
    assert hashlib.sha512(archive.read_bytes()).hexdigest() == assets['sha512'], 'Asset archive hash mismatch'
    info = json.loads((folder / 'metadata/version-fields.json').read_text())
    output = ROOT / 'out' / info['primary_file']
    output.parent.mkdir(exist_ok=True)
    with zipfile.ZipFile(archive) as blobs, zipfile.ZipFile(output, 'w', zipfile.ZIP_DEFLATED, compresslevel=6) as z:
        z.writestr('modrinth.index.json', index)
        for target, record in sorted(overrides.items()):
            assert target.startswith('overrides/')
            safe(target)
            data = blobs.read(safe(record['external'])) if 'external' in record else (ROOT / safe(record['source'])).read_bytes()
            assert hashlib.sha512(data).hexdigest() == record['sha512'], f'Override hash mismatch: {target}'
            z.writestr(target, data)
    print(output.name)
    return output

if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('edition', choices=('loka-fabric', 'loka-fabric-plus'))
    parser.add_argument('--assets')
    args = parser.parse_args()
    build(args.edition, args.assets)
