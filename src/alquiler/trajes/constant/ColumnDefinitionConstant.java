package alquiler.trajes.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ColumnDefinitionConstant {

    public static final String TINYINT_DEFAULT_1_DEFINITION = "TINYINT(1) DEFAULT 1";
    public static final String USERS_TABLE_NAME = "users";
    public static final String CUSTOMERS_TABLE_NAME = "customers";
    public static final String EVENTS_TABLE_NAME = "events";
    public static final String DETAIL_EVENT_TABLE_NAME = "detail_event";
    public static final String PAYMENTS_TABLE_NAME = "payments";    
    public static final String CATALOG_TYPE_EVENT_TABLE_NAME = "catalog_type_event"; 
    public static final String ROLES_TABLE_NAME = "roles";
    public static final String USERS_ROLES_TABLE_NAME = "users_roles";
    public static final String JOIN_COLUMN_USER_ID = "user_id";
    public static final String JOIN_COLUMN_ROLE_ID = "role_id";
}
