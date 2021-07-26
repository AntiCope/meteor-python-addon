from inspect import getargspec

from cloudburst.pythonaddon import PythonSystem


class Command(object):
    
    def __init__(self, name, description="Python command."):
        PythonSystem.INSTANCE.pycommands.append(self)
        self.name = name
        self.description = description
        
    def parse_args(self, argstr):
        arglist = argstr.split(" ")
        args = []
        for i, arg in enumerate(getargspec(self.command).args[1:]):
            type = arg.split("_")[0]
            if type == "str":
                args.append(arglist[i])
            elif type == "int":
                args.append(int(arglist[i]))
            elif type == "bool":
                args.append(arglist[i].lower() == 'true')
            elif type == "float":
                args.append(float(arglist[i]))
        return args
    
    def execute_command(self, args=""):
        self.command(*self.parse_args(args))
    
    def command(self):
        pass