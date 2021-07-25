from meteordevelopment.meteorclient.utils.player import ChatUtils

def info(*args):
    msg = "".join(map(str, args))
    ChatUtils.info(msg)