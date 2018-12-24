package org.zkoss.zss.essential;

import org.zkoss.poi.ss.usermodel.ZssContext;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zss.api.Ranges;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zul.*;

import java.util.*;

/**
 * Set locale of ZSS
 * @author Hawk
 *
 */
@SuppressWarnings("serial")
public class LocaleComposer extends SelectorComposer<Component> {

	@Wire
	private Spreadsheet ss;
	@Wire
	private Combobox localeBox;
	@Wire
	private Label localeLabel;

	private ListModelList localeModel;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);  //wire variables and event listeners
		Locale[] allLocales = Locale.getAvailableLocales();
		Arrays.sort(allLocales, new Comparator(){
			public int compare(Object o1, Object o2){
				return o1.toString().compareTo(o2.toString());
			}
		});
		localeModel = new ListModelList(allLocales);
		localeBox.setModel(localeModel);

		showLocale(ZssContext.getCurrent().getLocale());
	}

	@Listen("onSelect = combobox")
	public void selectLocale(){
		Locale locale = (Locale)localeModel.getSelection().iterator().next();
		ZssContext.setThreadLocal(new ZssContext(locale, -1));
		showLocale(locale);
		Ranges.range(ss.getSelectedSheet()).notifyChange();
	}

	public void showLocale(Locale l) {
		localeLabel.setValue(l.toString());
	}

}
