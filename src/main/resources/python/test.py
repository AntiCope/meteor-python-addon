import mcapi
from meteordevelopment.meteorclient.events.entity.player import BreakBlockEvent

def onBreak(event):
    print(event)
    mcapi.info("On Break")
    
mcapi.event_listener(onBreak, BreakBlockEvent)