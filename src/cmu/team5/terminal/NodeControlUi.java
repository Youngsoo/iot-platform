package cmu.team5.terminal;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;


/** Table Model class for the Table **/
public class NodeControlUi extends AbstractTableModel  {

private static final long serialVersionUID = 1L;

private List<SourceModel> sourceList = new ArrayList<SourceModel>(); 
private String[] columnNamesList = {"Active", "InActive", "Acturator", "NodeId"};

public NodeControlUi() {
    this.sourceList = getSourceDOList();
}

@Override
public String getColumnName(int column) {
    return columnNamesList[column];
}

@Override
public int getRowCount() {
    return sourceList.size();
}

@Override
public int getColumnCount() {
    return columnNamesList.length;
}

@Override
public Class<?> getColumnClass(int columnIndex) {
    return ((columnIndex == 0 || columnIndex == 1) ? Boolean.class : String.class);
}

@Override
public boolean isCellEditable(int rowIndex, int columnIndex) {
    return ((columnIndex == 0 || columnIndex == 1) ? true : false);
}

/**
     **Important:** Here when ever user clicks on the column one then other column values should be made false. Similarly vice-versa is also true.
**/
@Override
public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    SourceModel model = (SourceModel) sourceList.get(rowIndex);
    switch (columnIndex) {
case 0: 
        model.setSelect(true);
        model.setInActive(false);
        fireTableRowsUpdated(0, getRowCount() - 1);
        break;
case 1:
        model.setSelect(false);
        model.setInActive(true);
        fireTableRowsUpdated(0, getRowCount() - 1);
        break;
case 2: 
    model.setFactory((String) aValue);
    break;
case 3: 
    model.setSupplier((String) aValue);
    break;
}
fireTableCellUpdated(rowIndex, columnIndex);
}

@Override
public Object getValueAt(int rowIndex, int columnIndex) {
SourceModel source = sourceList.get(rowIndex);
//SourceModel source = getSourceDOList().get(rowIndex);
switch(columnIndex){
case 0:
    return source.isSelect();
case 1:
    return source.isInActive();    
case 2:
    return source.getFactory();
case 3:
    return source.getSupplier();
default:
    return null;
}
}

/**
 * List for populating the table.
 * @return list of sourceDO's.
 */
private List<SourceModel> getSourceDOList() {
   List<SourceModel> tempSourceList = new ArrayList<SourceModel>();
   for (int index = 0; index < 5; index++) {

    SourceModel source = new SourceModel();
    source.setSelect(false);
    source.setInActive(false);
    source.setFactory("Acturator " + index);
    source.setSupplier("NodeId " + index);

    tempSourceList.add(source);
}
return tempSourceList;
}
}

/** Class that is holding the model for each row **/
class SourceModel {

private boolean active;
private boolean inActive;
private String factory;
private String supplier;

public SourceModel() {
    // No Code;
}

public SourceModel(boolean select, boolean inActive, String factory, String supplier) {
    super();
    this.active = select;
    this.inActive = inActive;
    this.factory = factory;
    this.supplier = supplier;
}

public boolean isSelect() {
    return active;
}

public void setSelect(boolean select) {
    this.active = select;
}

public String getFactory() {
    return factory;
}

public boolean isInActive() {
    return inActive;
}

public void setInActive(boolean inActive) {
    this.inActive = inActive;
}

public void setFactory(String factory) {
    this.factory = factory;
}

public String getSupplier() {
    return supplier;
}

public void setSupplier(String supplier) {
    this.supplier = supplier;
}
}

/** Renderer class for JRadioButton **/
class RadioButtonRenderer implements TableCellRenderer {

    public JRadioButton btn = new JRadioButton();
    public Component getTableCellRendererComponent(JTable table, Object value,
      boolean isSelected, boolean hasFocus, int row, int column) {

      if (value == null) 
          return null;
      btn.setSelected((Boolean) value);
      return btn;
  }
}

/** Editor class for JRadioButton **/
class RadioButtonEditor extends DefaultCellEditor implements ItemListener {

public JRadioButton btn = new JRadioButton();

public RadioButtonEditor(JCheckBox checkBox) {
    super(checkBox);
}

public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

    if (value == null) 
        return null;

    btn.addItemListener(this);

    if (((Boolean) value).booleanValue())
        btn.setSelected(true);
    else
        btn.setSelected(false);

    return btn;
}

public Object getCellEditorValue() {
    if(btn.isSelected() == true)
        return new Boolean(true);
    else 
        return new Boolean(false);
}

public void itemStateChanged(ItemEvent e) {
    super.fireEditingStopped();
}
}
