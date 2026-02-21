module com.sau.bankproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql; // Veritabanı (PostgreSQL) için gerekli
    requires org.postgresql.jdbc;

    // Bu satır çok önemli: Controller paketini fxml'e açıyoruz
    opens com.sau.bankproject.Controller to javafx.fxml;

    // DTO paketini de açmak gerekebilir (Yansıma/Reflection işlemleri için)
    opens com.sau.bankproject.DTO to javafx.fxml;

    exports com.sau.bankproject;
}