from inspect import getargspec

from cloudburst.pythonaddon import PythonSystem
from com.mojang.brigadier.arguments import StringArgumentType, IntegerArgumentType, FloatArgumentType, BoolArgumentType


class Command(object):
    
    def __init__(self, name, description="Python command."):
        PythonSystem.INSTANCE.pycommands.append(self)
        self.name = name
        self.description = description
        self.args = self.parse_command()
        
    def parse_command(self):
        args = []
        for arg in getargspec(self.command).args[1:]:
            type = arg.split("_")[0]
            name = "_".join(arg.split("_")[1:])
            if type == "str":
                args.append((name, StringArgumentType.string()))
            elif type == "int":
                args.append((name, IntegerArgumentType.integer()))
            elif type == "float":
                args.append((name, FloatArgumentType.floatArg()))
            elif type == "bool":
                args.append((name, BoolArgumentType.bool()))
        return args
    
    def execute_command(self, args):
        self.command(*args)
    
    def command(self):
        pass