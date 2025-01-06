package alquiler.trajes.tables;

import alquiler.trajes.constant.ApplicationConstants;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import lombok.Getter;

public class TableFormatDetail extends JTable {
    
    static class WordWrapCellRenderer extends JTextArea implements TableCellRenderer {
        WordWrapCellRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value.toString());
            setSize(table.getColumnModel().getColumn(column).getWidth(), getPreferredSize().height);
            if (table.getRowHeight(row) != getPreferredSize().height) {
                table.setRowHeight(row, getPreferredSize().height);
            }
            return this;
        }
    }
    
    public TableFormatDetail() {
       
        this.setFont(new Font( ApplicationConstants.ARIAL , Font.PLAIN, 12 ));
        format();
    }
    
    @Getter
    public enum Column {
        
        BOOLEAN(0,10,"",Boolean.class, true),
        ID(1,10,"id",String.class, true),
        NUMBER(2,10,"#",String.class, false),
        NAME(3,60,"Nombre",String.class, true),
        ITEMS(4,240,"Conceptos",String.class, true),
        ADJUTS(5,160,"Ajustes",String.class, true),
        STATUS(6,160,"Estado",String.class, true),
        IMPORT(7,20,"Importe",String.class, true),
        PAYMENT(8,20,"Anticipo",String.class, true),
        TOTAL(9,20,"Total",String.class, false);
        
        private final int number;
        private final int size;
        private final String name;
        private final Class clazzType;
        private final Boolean isEditable;

        private Column(int number, int size, String name, Class clazzType, Boolean isEditable) {
            this.number = number;
            this.size = size;
            this.name = name;
            this.clazzType = clazzType;
            this.isEditable = isEditable;
        }

        public int getNumber() {
            return number;
        }

        public int getSize() {
            return size;
        }

        public String getName() {
            return name;
        }

        public Class getClazzType() {
            return clazzType;
        }

        public Boolean getIsEditable() {
            return isEditable;
        }
        
        
        
        public static String[] getColumnNames () {
            List<String> columnNames = new ArrayList<>();
            for (Column column : Column.values()) {
                columnNames.add(column.getName());
            }
            return columnNames.toArray(new String[0]);
        }
    }
    
    public void format () {
        setModel(
            new DefaultTableModel(Column.getColumnNames(), 0){
                @Override
                public Class getColumnClass(int column) {
                    return Column.values()[column].getClazzType();
                }

                @Override
                public boolean isCellEditable (int row, int column) {
                    return Column.values()[column].getIsEditable();
                }
        });
        
        TableRowSorter<TableModel> ordenarTabla = new TableRowSorter<TableModel>(this.getModel()); 
        this.setRowSorter(ordenarTabla);
          
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        
        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        right.setHorizontalAlignment(SwingConstants.RIGHT);
        
        for (Column column : Column.values()) {
            this.getColumnModel()
                    .getColumn(column.getNumber())
                    .setPreferredWidth(column.getSize());
        }
                
        this.getColumnModel().getColumn(Column.ID.getNumber()).setMaxWidth(0);
        this.getColumnModel().getColumn(Column.ID.getNumber()).setMinWidth(0);
        this.getColumnModel().getColumn(Column.ID.getNumber()).setPreferredWidth(0);
        
        this.getColumnModel().getColumn(Column.IMPORT.getNumber()).setCellRenderer(right);
        this.getColumnModel().getColumn(Column.PAYMENT.getNumber()).setCellRenderer(right);
        this.getColumnModel().getColumn(Column.TOTAL.getNumber()).setCellRenderer(right);
        this.getColumnModel().getColumn(Column.NUMBER.getNumber()).setCellRenderer(center);
        
        this.getColumnModel().getColumn(Column.ITEMS.getNumber()).setCellRenderer(new WordWrapCellRenderer());
        
        // adding checkbox in header table
        TableColumn tc = this.getColumnModel().getColumn(Column.BOOLEAN.getNumber());
        tc.setCellEditor(this.getDefaultEditor(Boolean.class)); 
        tc.setHeaderRenderer(new CheckBoxHeader(new ItemListenerHeaderCheckbox(Column.BOOLEAN.getNumber(),this)));
        
        try {
            DefaultTableModel temp = (DefaultTableModel) this.getModel();
            temp.removeRow(temp.getRowCount() - 1);
        } catch (ArrayIndexOutOfBoundsException e) {
            ;
        }

    }
    
}
