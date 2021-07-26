from cloudburst.pythonaddon.pyclasses import PyListener

def event_listener(func, type):
    # Subscribe to MeteorClient.EVENT_BUS
    # func - python function, that will run
    # type - java class, event on which func should fire
    PyListener.addListener(func, type)