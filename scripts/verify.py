"""Check assembled contents, required dependencies, shared settings and defaults."""
from pathlib import Path
import hashlib, io, json, zipfile

ROOT = Path(__file__).resolve().parents[1]
indexes, contents = [], []
for edition in ('loka-fabric', 'loka-fabric-plus'):
    folder = ROOT / 'packs' / edition / '26.3'
    index = json.loads((folder / 'modrinth.index.json').read_text())
    indexes.append(index)
    assert index['dependencies']['minecraft'] == '26.3'
    assert all(f['env']['client'] == 'required' for f in index['files'])
    metadata = json.loads((folder / 'metadata/version-fields.json').read_text())
    with zipfile.ZipFile(ROOT / 'out' / metadata['primary_file']) as z:
        assert z.testzip() is None
        assert len(z.namelist()) == len(set(z.namelist()))
        assert json.loads(z.read('modrinth.index.json')) == index
        overrides = json.loads((folder / 'overrides-manifest.json').read_text())
        assert set(z.namelist()) == {'modrinth.index.json', *overrides}
        for name, record in overrides.items():
            assert hashlib.sha512(z.read(name)).hexdigest() == record['sha512']
        options = dict(line.split(':', 1) for line in z.read('overrides/options.txt').decode().splitlines() if ':' in line)
        assert (options['overrideWidth'], options['overrideHeight'], options['guiScale']) == ('1280', '720', '0')
        assert 'JermsyBoy' not in options['resourcePacks']
        assert not json.loads(z.read('overrides/config/minihud.json'))['Generic']['mainRenderingToggle']['enabled']
        assert "B:modpack_mode = 'true';" in z.read('overrides/config/fancymenu/options.txt').decode()
        rpc = json.loads(z.read('overrides/config/craftpresence.json'))
        assert rpc['displaySettings']['presenceData']['appName'] == 'Loka Minecraft'
        for key in ('detectBiomeData', 'detectDimensionData', 'detectWorldData', 'enableJoinRequests',
                    'detectATLauncherInstance', 'detectCurseManifest', 'detectMCUpdaterInstance'):
            assert rpc['generalSettings'][key] is False
        assert rpc['biomeSettings']['biomeData']['default']['textOverride'] == 'Forge Your Story'
        assert rpc['dimensionSettings']['dimensionData']['default']['textOverride'] == 'Forge Your Story'
        assert json.loads(z.read('overrides/config/packbranding/config.json'))['packVersion'] == '1.0.0'
        with zipfile.ZipFile(io.BytesIO(z.read('overrides/mods/loka-ui-1.0.0.jar'))) as ui:
            mixins = json.loads(ui.read('loka_ui.mixins.json'))['client']
            assert {'MenuPanoramaBackgroundMixin', 'LevelLoadingScreenMixin', 'ConnectionLoadingBackgroundMixin'} <= set(mixins)
            assert ui.read('assets/loka_ui/textures/gui/loading_deep_dark.png') == (ROOT / 'assets/loading/DeepDark.png').read_bytes()
        assert 'background_type = video' in z.read('overrides/config/fancymenu/customization/loka_singleplayer_background.txt').decode()
        contents.append({n: z.read(n) for n in z.namelist() if n.startswith('overrides/')})
        print(f'{index["name"]}: index, override hashes, integrity and defaults passed')
expanded = {f['path']: f for f in indexes[1]['files']}
assert all(expanded.get(f['path']) == f for f in indexes[0]['files'])
branded = {'overrides/config/packbranding/config.json', 'overrides/config/packbranding/icon.png', 'overrides/config/drippyloadingscreen/options.txt', 'overrides/config/craftpresence.json'}
for name, data in contents[0].items():
    assert name in contents[1]
    if name not in branded:
        assert contents[1][name] == data, f'Shared override differs: {name}'
print('Expanded edition includes every base file; shared overrides are identical except declared branding.')
