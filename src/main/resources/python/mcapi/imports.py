from net.minecraft.client import MinecraftClient
from meteordevelopment.meteorclient.systems.modules import Modules
from baritone.api import BaritoneAPI

mc = MinecraftClient.getInstance()

modules = Modules.get()

baritone_api = BaritoneAPI.getProvider().getPrimaryBaritone()

def baritone(command):
    # execute baritone commands
    baritone_api.getCommandManager().execute(command)