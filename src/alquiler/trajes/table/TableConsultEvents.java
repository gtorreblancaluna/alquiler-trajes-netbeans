package alquiler.trajes.table;

import alquiler.trajes.constant.ApplicationConstants;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import lombok.Getter;

public class TableConsultEvents extends JTable {
    
    public TableConsultEvents() {
       
        this.setFont(new Font( ApplicationConstants.ARIAL , Font.PLAIN, 11 ));
        format();
    }
    
    @Getter
    public enum Column {
        
        BOOLEAN(0,10,"",Boolean.class, true),
        ID(1,10,"Folio",String.class, false),
        DESCRIPTION(2,120,"Descripción",String.class, false),
        CUSTOMER(3,60,"Cliente",String.class, false),
        CUSTOMER_PHONES(4,60,"Teléfono cliente",String.class, false),
        DELIVERY_DATE(5,90,"Fecha entrega",String.class, false),
        RETURN_DATE(6,90,"Fecha devolución",String.class, false),
        TYPE_EVENT(7,30,"Tipo",String.class, false),
        STATUS_EVENT(8,30,"Estatus",String.class, false),
        PAYMENTS(9,20,"Abonos",String.class, false),
        SUB_TOTAL(10,20,"Subtotal",String.class, false),
        TOTAL(11,20,"Total",String.class, false);
        
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
        
        this.getColumnModel().getColumn(Column.ID.getNumber()).setCellRenderer(center);
        this.getColumnModel().getColumn(Column.TYPE_EVENT.getNumber()).setCellRenderer(center);
        this.getColumnModel().getColumn(Column.STATUS_EVENT.getNumber()).setCellRenderer(center);
        this.getColumnModel().getColumn(Column.SUB_TOTAL.getNumber()).setCellRenderer(right);
        this.getColumnModel().getColumn(Column.PAYMENTS.getNumber()).setCellRenderer(right);
        this.getColumnModel().getColumn(Column.TOTAL.getNumber()).setCellRenderer(right);
        
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
