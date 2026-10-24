"""Shared Loka presence defaults; images and their click-through URLs are distinct."""
import copy

LOGO = 'https://cdn.modrinth.com/data/Dm37kntU/fa72d382e606581683f407ae9e11bf97e0029375_96.webp'
DISCORD = 'https://discord.gg/loka'
MODPACK = 'https://modrinth.com/modpack/loka-fabric'
OPTIONS = {'showTownAndAlliance': True, 'refreshSeconds': 600}
# CraftPresence knows the signed-in account before a Loka connection exists.
# Use a compact UUID and an explicit PNG URL; never depend on the affiliation bridge.
PLAYER_ICON = '{getOrDefault(player.uuid.short, "") != "" ? "https://minotar.net/helm/" + player.uuid.short + "/128.png" : "grass"}'


def configure(source, pack_name='Loka Fabric'):
    config = copy.deepcopy(source)
    config['generalSettings'].update(detectATLauncherInstance=False, detectCurseManifest=False,
                                    detectMultiMCManifest=False, detectMCUpdaterInstance=False,
                                    detectTechnicPack=False, detectModrinthPack=True,
                                    detectBiomeData=False, detectDimensionData=False,
                                    detectWorldData=False, enableJoinRequests=False)
    config['advancedSettings'].update(enablePerGui=False, enablePerItem=False,
                                     enablePerEntity=False, allowEndpointIcons=False,
                                     formatWords=False)
    config['advancedSettings']['guiSettings']['guiData'] = {'default': {'textOverride': 'Forge Your Story'}}
    config['advancedSettings']['itemMessages'] = {'default': 'Forge Your Story'}
    config['advancedSettings']['entitySettings']['targetData'] = {'default': {'textOverride': 'Forge Your Story'}}
    config['advancedSettings']['entitySettings']['ridingData'] = {'default': {'textOverride': 'Forge Your Story'}}
    # Remove saved per-server/per-world overrides from the source profile.
    config['serverSettings']['serverData'] = {'default': {'textOverride': 'Forge Your Story'}}
    config['biomeSettings']['biomeData'] = {'default': {'textOverride': 'Forge Your Story'}}
    config['dimensionSettings']['dimensionData'] = {'default': {'textOverride': 'Forge Your Story'}}
    for key in config['statusMessages']:
        config['statusMessages'][key] = {'textOverride': 'Forge Your Story'}
    config['displaySettings']['presenceData'].update(
        enabled=True, useAsMain=False, activityType=0, statusDisplayType=0,
        appName='Loka Minecraft', details='{getOrDefault(loka.details, "Forge Your Story")}',
        gameState='{getOrDefault(loka.state, "' + pack_name + '")}',
        detailsUrl='', gameStateUrl='', largeImageKey=LOGO,
        largeImageText='Loka Fabric • Loka Minecraft', largeImageUrl='https://lokamc.com/',
        smallImageKey=PLAYER_ICON,
        smallImageText='{getOrDefault(player.name, "Minecraft player")} • Loka Minecraft',
        smallImageUrl='', startTimestamp='{getOrDefault(loka.started, data.general.time)}',
        endTimestamp='', buttons={
            'loka_discord': {'label': 'Loka Discord', 'url': DISCORD},
            'loka_fabric': {'label': 'Get Loka Fabric', 'url': MODPACK},
        })
    config['displaySettings']['dynamicVariables'] = {'pack': pack_name}
    config['displaySettings']['dynamicIcons'] = {}
    return config
