import mcapi

class HiCommand(mcapi.Command):
    def __init__(self):
        super(HiCommand, self).__init__("hi", "Hi command.")

    def command(self, name):
        mcapi.info("Hi, ", name, "!")

py = HiCommand()
