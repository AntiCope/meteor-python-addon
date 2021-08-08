import mcapi
from meteordevelopment.meteorclient.events.entity.player import BreakBlockEvent

def on_break(event):
    print(event)
    mcapi.info("On Break")
    
mcapi.event_listener(on_break, BreakBlockEvent)
