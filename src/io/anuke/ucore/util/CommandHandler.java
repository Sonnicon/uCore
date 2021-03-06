package io.anuke.ucore.util;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import io.anuke.ucore.function.Consumer;

import java.util.Arrays;

public class CommandHandler{
	private final ObjectMap<String, Command> commands = new ObjectMap<>();
	private final Array<Command> orderedCommands = new Array<>();
	private final String prefix;
	
	public CommandHandler(String prefix){
		this.prefix = prefix;
	}
	
	public Response handleMessage(String message){
		if(message == null || (!message.startsWith(prefix)))
			return new Response(ResponseType.noCommand, null);

		message = message.substring(prefix.length());

		String[] args = message.split(" ");

        String mergeargs = args.length > 1 ? message.substring(args[0].length() + 1) : "";

		message = args[0].toLowerCase();
		args = Arrays.copyOfRange(args, 1, args.length);
		
		Command command = commands.get(message);
		
		if(command != null){
			if(command.mergeArgs && args.length > 0){
				args = new String[]{mergeargs};
			}

			if(args.length == command.paramLength){
				command.runner.accept(args);
				return new Response(ResponseType.valid, command);
			}else{
				return new Response(ResponseType.invalidArguments, command);
			}
		}else{
			return new Response(ResponseType.unknownCommand, null);
		}
	}
	
	public Command register(String text, String description, Consumer<String[]> runner){
		Command cmd = new Command(text, "", description, runner);
		commands.put(text.toLowerCase(), cmd);
		orderedCommands.add(cmd);
		return cmd;
	}
	
	public Command register(String text, String params, String description, Consumer<String[]> runner){
		Command cmd = new Command(text, params, description, runner);
		commands.put(text.toLowerCase(), cmd);
		orderedCommands.add(cmd);
		return cmd;
	}
	
	public Iterable<Command> getCommandList(){
		return orderedCommands;
	}
	
	public static class Command{
		public final String text;
		public final String params;
		public final String description;
		public final int paramLength;
		public final Consumer<String[]> runner;
		public boolean mergeArgs;
		
		public Command(String text, String params, String description, Consumer<String[]> runner){
			this.text = text;
			this.params = params;
			this.runner = runner;
			this.description = description;
			
			paramLength = params.length() == 0 ? 0 : (params.length() - params.replaceAll(" ", "").length() + 1);
		}

		public Command mergeArgs(){
			this.mergeArgs = true;
			return this;
		}
	}
	
	public static class Response{
		public final ResponseType type;
		public final Command command;
		
		public Response(ResponseType type, Command command){
			this.type = type;
			this.command = command;
		}
	}
	
	public enum ResponseType{
		noCommand, unknownCommand, invalidArguments, valid;
	}
}
