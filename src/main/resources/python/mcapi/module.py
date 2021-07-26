from cloudburst.pythonaddon import PythonSystem
from .listener import event_listener

class Module(object):
    
    def __init__(self, name, description="Python module."):
        PythonSystem.INSTANCE.pymodules.append(self)
        self.name = name
        self.description = description
        self.module = None
    
    def event_handler(self, func, type):
        # register event handler
        # func - python function, that will run
        # type - java class, event on which func should fire
        event_listener(lambda e: self.on_event(e, func), type)
    
    def on_event(self, event, func):
        if self.module.isActive():
            func(event)
    
    def on_activate(self):
        pass
    
    def on_deactivate(self):
        pass