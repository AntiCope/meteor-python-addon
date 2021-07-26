from meteordevelopment.meteorclient.utils.player import ChatUtils

def info(*args):
    msg = "".join(map(str, args))
    ChatUtils.info(msg)
    
def warning(*args):
    msg = "".join(map(str, args))
    ChatUtils.warning(msg)
    
def error(*args):
    msg = "".join(map(str, args))
    ChatUtils.error(msg)