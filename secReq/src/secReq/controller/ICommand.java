package secReq.controller;

public interface ICommand {

	public abstract void execute();
	
	public abstract void undo();
	
	public abstract String getName();
	
	public abstract void initCommand(ICommandManager manager);
	
	public ICommand getClone();
}
