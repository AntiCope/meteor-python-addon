from cloudburst.pythonaddon import PythonSystem

class Module(object):
    
    def __init__(self, name, description="Python module."):
        PythonSystem.INSTANCE.pymodules.append(self)
        self.name = name
        self.description = description
    
    def on_activate(self):
        pass
    
    def on_deactivate(self):
        pass