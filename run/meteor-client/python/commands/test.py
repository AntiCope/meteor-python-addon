import mcapi

class TestCommand(mcapi.Command):
    def __init__(self):
        super(TestCommand, self).__init__("test", "Test command")
        
    def command(self, str_arg):
        mcapi.warning(str_arg)
        
py = TestCommand()