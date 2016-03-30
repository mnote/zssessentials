package org.zkoss.zss.essential.advanced.customization;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zss.api.AreaRef;
import org.zkoss.zss.api.Range;
import org.zkoss.zss.api.Ranges;
import org.zkoss.zss.api.model.Book;
import org.zkoss.zss.api.model.CellData.CellType;
import org.zkoss.zss.api.model.Sheet;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 * @author Hawk
 */
@SuppressWarnings("serial")
public class FindDialogComposer extends SelectorComposer<Component> {
	@Wire
	protected Window findDialog;
	@Wire
	protected Textbox keywordBox;
	@Wire
	protected Combobox scopeBox;
	
	protected enum SearchScope {Sheet, Book};
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		ListModelList<SearchScope> scopeList = new ListModelList<SearchScope>(SearchScope.values());
		scopeBox.setModel(scopeList);
		scopeList.addToSelection(scopeList.get(0));
	}
	
	@Listen("onClick=#find; onOK=#keywordBox")
	public void find(Event event) {
		Spreadsheet ss = (Spreadsheet)findDialog.getAttribute(Spreadsheet.class.toString());
		Range targetCell = null;
		if (scopeBox.getSelectedItem().getValue().equals(SearchScope.Sheet)){
			targetCell = findNext(ss.getSelectedSheet(), ss.getSelection());
		}else{
			targetCell = findNext(ss.getBook(), ss.getSelectedSheet(), ss.getSelection());
		}
		focusToCell(ss, targetCell);
	}


	protected void focusToCell(Spreadsheet ss, Range targetCell) {
		ss.setSelectedSheet(targetCell.getSheetName());
		ss.focusTo(targetCell.getRow(), targetCell.getColumn());
	}
	
	protected Range findNext(Book book, Sheet startingSheet, AreaRef stargingSelection) {
		AreaRef selection = stargingSelection;
		Range foundCell = null;
		int index = book.getSheetIndex(startingSheet);
		while (index < book.getNumberOfSheets()){
			foundCell = findNext(book.getSheetAt(index), selection);
			if (selection.getRow() == foundCell.getRow() &&
				selection.getColumn() == foundCell.getColumn()){ //nothing matched, move to next sheet
				index++;
				selection = new AreaRef(0, 0, 0, 0);
			}else{
				return foundCell;
			}
		}
		
		return foundCell;
	}
	
	/**
	 * If nothing found, return current selected cell.
	 * @param sheet
	 * @param selection
	 * @return
	 */
	protected Range findNext(Sheet sheet, AreaRef selection) {
		int lastColumn = Ranges.range(sheet).getDataRegion().getLastColumn();
		int lastRow = Ranges.range(sheet).getDataRegion().getLastRow();
		String keyword = keywordBox.getValue().trim();
		//starting from current selection position
		int row = selection.getRow();
		int column = selection.getColumn()+1; 
		// by rows
		while (row <= lastRow){
			while (column <= lastColumn){
				Range cell = Ranges.range(sheet, row, column);
				if (cell.getCellData().getType() == CellType.STRING){
					if (cell.getCellData().getEditText().toLowerCase().contains(keyword)){
						return cell;
					}
				}
				column++;
			}
			column = 0;
			row++;
		}
		return Ranges.range(sheet, selection);
	}

	@Listen("onClick = #close")
	public void close(MouseEvent event) {
		findDialog.setVisible(false);
	}
	
	@Listen("onOpen = #findDialog")
	public void show(){
		findDialog.setVisible(true);
		keywordBox.setFocus(true);
	}
}



