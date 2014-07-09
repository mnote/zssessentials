package org.zkoss.zss.essential.events;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zss.ui.event.HeaderMouseEvent;
import org.zkoss.zul.Menupopup;

/**
 * Demonstrate header events.
 * @author Hawk
 *
 */
@SuppressWarnings("serial")
public class MouseEventsComposer extends SelectorComposer<Component> {

	@Wire
	private Menupopup topHeaderMenu;
	@Wire
	private Menupopup leftHeaderMenu;
	
	@Listen("onHeaderRightClick = spreadsheet")
	public void onHeaderRightClick(HeaderMouseEvent event) {
		
		switch(event.getType()){
		case COLUMN:
			topHeaderMenu.open(event.getClientx(),  event.getClienty());
			break;
		case ROW:
			leftHeaderMenu.open(event.getClientx(),  event.getClienty());
			break;
		}
	}
}
