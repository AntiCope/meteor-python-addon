from cloudburst.pythonaddon.pyclasses import PyListener

def event_listener(func, type):
    PyListener.addListener(func, type)