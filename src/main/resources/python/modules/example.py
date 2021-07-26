import mcapi
from meteordevelopment.meteorclient.events.entity.player import BreakBlockEvent

class ExampleModule(mcapi.Module):
    def __init__(self):
        super(ExampleModule, self).__init__("example", "Example module with onbreak event handler.")
        
        self.event_handler(self.on_break, BreakBlockEvent)
        
    def on_activate(self):
        mcapi.info("On Activate.")
        
    def on_break(self, event):
        mcapi.info(event.blockPos)
    
py = ExampleModule()