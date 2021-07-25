import mcapi

class Module(mcapi.Module):
    
    def on_activate(self):
        mcapi.info("Activate")
    
    def on_deactivate(self):
        mcapi.info("Deactivate")
        
