package org.zkoss.zss.essential.advanced.customization;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zss.ui.*;
import org.zkoss.zss.ui.impl.DefaultUserActionManagerCtrl;

/**
 * To demonstrate how to customize data validation dialog
 * @author Hawk
 *
 */
@SuppressWarnings("serial")
public class CustomDialogComposer extends SelectorComposer<Component> {
	
	@Wire
	private Spreadsheet ss;

	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		UserActionManager actionManager = ss.getUserActionManager();
		//replace the existing handlers
		actionManager.setHandler(
				DefaultUserActionManagerCtrl.Category.AUXACTION.getName(),
				AuxAction.DATA_VALIDATION.getAction(), new MyValidationHandler());
	}
}



