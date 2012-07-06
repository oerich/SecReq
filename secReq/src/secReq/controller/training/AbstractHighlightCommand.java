package secReq.controller.training;

import secReq.controller.ICommand;
import secReq.controller.ICommandManager;

public abstract class AbstractHighlightCommand implements ICommand {

	protected ICommandManager manager;

	@Override
	public void initCommand(ICommandManager manager) {
		this.manager = manager;
	}

	@Override
	public void undo() {
		// TODO Auto-generated method stub

	}

	@Override
	public ICommand getClone() {
		try {
			return getClass().newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}
