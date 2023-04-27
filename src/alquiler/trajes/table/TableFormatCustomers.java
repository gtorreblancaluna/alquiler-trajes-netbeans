package alquiler.trajes.table;

import alquiler.trajes.constant.ApplicationConstants;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import lombok.Getter;

public class TableFormatCustomers extends JTable {
    
    public TableFormatCustomers() {
       
        this.setFont(new Font( ApplicationConstants.ARIAL , Font.PLAIN, 11 ));
        format();
        
    }
    
    @Getter
    public enum Column {
        
        ID(0,20,"id",String.class, false),
        NAME(1,80,"Nombre",String.class, false),
        EMAIL(2,80,"Email",String.class, false),
        PHONE_NUMBER(3,40,"Teléfono",String.class, false),
        PHONE_NUMBER1(4,40,"Teléfono 2",String.class, false),
        PHONE_NUMBER2(5,40,"Teléfono 3",String.class, false),
        CREATION_DATE(6,40,"Creado",String.class, false);
        
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
        
        try {
            DefaultTableModel temp = (DefaultTableModel) this.getModel();
            temp.removeRow(temp.getRowCount() - 1);
        } catch (ArrayIndexOutOfBoundsException e) {
            ;
        }

    }
    
}
